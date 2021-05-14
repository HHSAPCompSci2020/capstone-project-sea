package Network;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import Game.Card;
import Game.Deck;

public class ClientHandler implements Runnable {
	private Socket socket;
	private String name;
	private Server server;
	private ObjectInputStream in;
	private ObjectOutputStream out;
	private boolean looping;
	
	public ClientHandler(Socket socket, String name, Server server) {
		this.socket = socket;
		this.name = name;
		this.server = server;
	}
	
	public void start() {
		try {
			out = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
			out.flush();
			in = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
			looping = true;
			new Thread(this).start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		looping = true;
		while (looping) {
			try {
				Object input = in.readObject();
				if (input instanceof DataObject) {
					DataObject data = (DataObject) input;
					synchronized (server) {
						if (data.messageType.equals(DataObject.PLAY)) {
							Card card = (Card) data.message[0];
							Deck played = server.getPlayed();
							played.addCard(card);
							int numCards = (int) data.message[1];
							if (numCards > 0) {
								AtomicInteger turn = server.getTurn();
								turn.set((turn.get()+1)%server.getHandlers().size());
								for (ClientHandler ch : server.getHandlers()) {
									DataObject next = new DataObject();
									next.messageType = DataObject.TURN;
									next.message = new Object[] {turn.get(), card, numCards};
									ch.out.writeObject(next);
									ch.out.flush();
								}
							} else {
								for (ClientHandler ch : server.getHandlers()) {
									DataObject next = new DataObject();
									next.messageType = DataObject.END;
									next.message = new Object[] {name, card};
									ch.out.writeObject(next);
									ch.out.flush();
								}
							}
						} else if (data.messageType.equals(DataObject.DRAW)) {
							Deck draw = server.getDraw();
							if (draw.getDeck().isEmpty()) {
								ArrayList<Card> cards = draw.getDeck();
								Deck played = server.getPlayed();
								ArrayList<Card> cards2 = played.getDeck();
								Card top = cards2.remove(cards2.size()-1);
								cards.addAll(played.getDeck());
								cards2.clear();
								cards2.add(top);
								draw.shuffle();
							}
							Card card = draw.removeTop();
							DataObject next = new DataObject();
							next.messageType = DataObject.DRAW;
							next.message = new Object[] {card};
							out.writeObject(next);
							out.flush();
						} else if(data.messageType.equals(DataObject.HANDSHAKE)) {
							for (ClientHandler ch : server.getHandlers()) {
								DataObject next = new DataObject();
								next.messageType = DataObject.HANDSHAKE;
								next.message = new Object[] {name, server.getHandlers().size(), server.getHost(), server.getPort()};
								ch.out.writeObject(next);
								ch.out.flush();
							}
						} else if (data.messageType.equals(DataObject.START)) {
							server.getStarted().set(true);
							int cards = 5;
							if (server.getHandlers().size() == server.getMinClients()) {
								cards = 7;
							}
							for (ClientHandler ch : server.getHandlers()) {
								Deck deck = new Deck();
								for (int i = 0; i < cards; i++) {
									deck.addCard(server.getDraw().removeTop());
								}
								DataObject next = new DataObject();
								next.messageType = DataObject.START;
								next.message = new Object[] {data.message[0], deck};
								ch.out.writeObject(next);
								ch.out.flush();
							}
							for (ClientHandler ch : server.getHandlers()) {
								DataObject next = new DataObject();
								next.messageType = DataObject.TURN;
								next.message = new Object[] {server.getTurn().get(), server.getPlayed().getTop(), -1};
								ch.out.writeObject(next);
								ch.out.flush();
							}
						} else if (data.messageType.equals(DataObject.DISCONNECT)) {
							server.getHandlers().remove(this);
							looping = false;
							for (ClientHandler ch : server.getHandlers()) {
								DataObject next = new DataObject();
								next.messageType = DataObject.DISCONNECT;
								next.message = new Object[] {name};
								ch.out.writeObject(next);
								ch.out.flush();
							}
						} else if (data.messageType.equals(DataObject.INFORMATION)) {
							server.setHost((String) data.message[0]);
							for (ClientHandler ch : server.getHandlers()) {
								DataObject next = new DataObject();
								next.messageType = DataObject.INFORMATION;
								next.message = new Object[] {data.message[0], server.getPort()};
								ch.out.writeObject(next);
								ch.out.flush();
							}
						}
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		try {
			in.close();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void stop() {
		looping = false;
	}
}

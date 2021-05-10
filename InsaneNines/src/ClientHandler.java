import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

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
			in = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
			out = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
			looping = true;
			DataObject data = new DataObject();
			data.messageType = DataObject.HANDSHAKE;
			data.message = new Object[] {name};
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		start();
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
							if (Card.isValid(card, played.getTop())) {
								played.addCard(card);
								DataObject next = new DataObject();
								next.messageType = DataObject.END_TURN;
								next.message = new Object[] {};
								out.writeObject(next);
								out.flush();
								AtomicInteger turn = server.getTurn();
								turn.set((turn.get()+1)%server.getHandlers().size());
								DataObject next2 = new DataObject();
								next2.messageType = DataObject.YOUR_TURN;
								next2.message = new Object[] {card};
								ClientHandler ch = server.getHandlers().get(turn.get());
								ch.out.writeObject(next2);
								ch.out.flush();
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
						} else if (data.messageType.equals(DataObject.START)) {
							if (server.getHandlers().size() >= server.getMinClients()) {
								int cards = 7;
								if (server.getHandlers().size() == server.getMinClients()) {
									cards = 5;
								}
								for (ClientHandler ch : server.getHandlers()) {
									Deck deck = new Deck();
									for (int i = 0; i < cards; i++) {
										deck.addCard(server.getDraw().removeTop());
									}
									DataObject next = new DataObject();
									next.messageType = DataObject.START;
									next.message = new Object[] {deck};
									ch.out.writeObject(next);
									ch.out.flush();
								}
								DataObject next = new DataObject();
								next.messageType = DataObject.YOUR_TURN;
								next.message = new Object[] {server.getPlayed().getTop()};
								out.writeObject(next);
								out.flush();
							}
						} else if (data.messageType.equals(DataObject.DISCONNECT)) {
							for (ClientHandler ch : server.getHandlers()) {
								DataObject next = new DataObject();
								next.messageType = DataObject.DISCONNECT;
								next.message = new Object[] {name};
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

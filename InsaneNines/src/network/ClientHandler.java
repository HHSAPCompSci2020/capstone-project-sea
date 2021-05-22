package network;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import game.Card;
import game.Deck;

/**
 * The part of the server that sends and receives data to a client.
 * 
 * @author Eric Chang
 */
public class ClientHandler implements Runnable {
	private Socket socket;
	private String name;
	private Server server;
	private ObjectInputStream in;
	private ObjectOutputStream out;
	private boolean looping;
	
	/**
	 * Creates a client handler with the client's socket and name.
	 * 
	 * @param socket the client's socket
	 * @param name the client's name
	 * @param server this handler's server
	 */
	public ClientHandler(Socket socket, String name, Server server) {
		this.socket = socket;
		this.name = name;
		this.server = server;
	}
	
	/**
	 * Starts both reading data from and writing data to the client.
	 */
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
							boolean won = (boolean) data.message[1];
							if (won) {
								for (ClientHandler ch : server.getHandlers()) {
									DataObject next = new DataObject();
									next.messageType = DataObject.END;
									next.message = new Object[] {name, card};
									ch.out.writeObject(next);
									ch.out.flush();
								}
							} else {
								AtomicInteger turn = server.getTurn();
								if (card.isSkip()) {
									turn.set((turn.get()+server.getDirection().get()+server.getHandlers().size())
											%server.getHandlers().size());
								} else if (card.isReverse()) {
									AtomicInteger direction = server.getDirection();
									direction.set(-direction.get());
								}
								turn.set((turn.get()+server.getDirection().get()+server.getHandlers().size())
											%server.getHandlers().size());
								for (ClientHandler ch : server.getHandlers()) {
									DataObject next = new DataObject();
									next.messageType = DataObject.TURN;
									if (card.isNine()) {
										next.message = new Object[] {turn.get(), card, data.message[2]};
									} else {
										next.message = new Object[] {turn.get(), card};
									}
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
							if (draw.getDeck().isEmpty()) {
								if (data.message.length == 0) {
									AtomicInteger turn = server.getTurn();
									turn.set((turn.get()+server.getDirection().get()+server.getHandlers().size())
											%server.getHandlers().size());
									for (ClientHandler ch : server.getHandlers()) {
										DataObject next = new DataObject();
										next.messageType = DataObject.TURN;
										next.message = new Object[] {turn.get()};
										ch.out.writeObject(next);
										ch.out.flush();
									}
								} else {
									DataObject next = new DataObject();
									next.messageType = DataObject.DRAW;
									next.message = new Object[] {};
									out.writeObject(next);
									out.flush();
								}
							} else {
								Card card = draw.removeTop();
								for (ClientHandler ch : server.getHandlers()) {
									DataObject next = new DataObject();
									next.messageType = DataObject.DRAW;
									next.message = new Object[] {card};
									ch.out.writeObject(next);
									ch.out.flush();
								}
							}
						} else if(data.messageType.equals(DataObject.HANDSHAKE)) {
							ArrayList<String> names = new ArrayList<String>();
							for (ClientHandler ch : server.getHandlers()) {
								names.add(ch.name);
							}
							for (ClientHandler ch : server.getHandlers()) {
								DataObject next = new DataObject();
								next.messageType = DataObject.HANDSHAKE;
								next.message = new Object[] {name, names, server.getHost(), server.getPort()};
								ch.out.writeObject(next);
								ch.out.flush();
							}
						} else if (data.messageType.equals(DataObject.START)) {
							server.getStarted().set(true);
							int cards = 5;
							if (server.getHandlers().size() == 2) {
								cards = 7;
							}
							for (ClientHandler ch : server.getHandlers()) {
								Deck deck = new Deck();
								for (int i = 0; i < cards; i++) {
									deck.addCard(server.getDraw().removeTop());
								}
								DataObject next = new DataObject();
								next.messageType = DataObject.START;
								next.message = new Object[] {deck, server.getTurn().get(), server.getPlayed().getTop()};
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
						} else if (data.messageType.equals(DataObject.MESSAGE)) {
							for (ClientHandler ch : server.getHandlers()) {
								ch.out.writeObject(data);
								ch.out.flush();
							}
						} else if (data.messageType.equals(DataObject.NAME_CHANGE)) {
							name = (String) data.message[1];
							for (ClientHandler ch : server.getHandlers()) {
								DataObject next = new DataObject();
								next.messageType = DataObject.NAME_CHANGE;
								next.message = new Object[] {data.message[0], data.message[1]};
								ch.out.writeObject(next);
								ch.out.flush();
							}
						}
					}
				}
			} catch (IOException e) {
				stop();
				try {
					synchronized (server) {
						if (this == server.getHandlers().get(0) && !server.getStarted().get()) {
							server.getHandlers().remove(this);
							for (ClientHandler ch : server.getHandlers()) {
								DataObject next = new DataObject();
								next.messageType = DataObject.DISCONNECT;
								next.message = new Object[] {};
								ch.out.writeObject(next);
								ch.out.flush();
							}
							server.close();
						} else {
							server.getHandlers().remove(this);
							for (ClientHandler ch : server.getHandlers()) {
								DataObject next = new DataObject();
								next.messageType = DataObject.DISCONNECT;
								next.message = new Object[] {name, server.getHandlers().size()};
								ch.out.writeObject(next);
								ch.out.flush();
							}
						}
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				}
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
	
	/**
	 * Stops both reading data from and writing data to the client.
	 */
	public void stop() {
		looping = false;
	}
}

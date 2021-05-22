package network;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import game.Card;
import game.Deck;

/**
 * A server that connects to clients.
 * 
 * @author Eric Chang
 */
public class Server implements Runnable {
	private String host;
	private int port;
	private ServerSocket server;
	private List<ClientHandler> handlers;
	private Deck draw;
	private Deck played;
	private AtomicInteger turn;
	private AtomicInteger direction;
	private AtomicBoolean started;
	
	/**
	 * Creates a local server with a random port.
	 */
	public Server() {
		try {
			server = new ServerSocket(0);
			port = server.getLocalPort();
		} catch (IOException e) {
			e.printStackTrace();
		}
		handlers = new ArrayList<ClientHandler>();
		draw = new Deck();
		played = new Deck();
		String[] ranks = {"ACE", "TWO", "THREE", "FOUR", "FIVE", "SIX", "SEVEN", "EIGHT", "NINE", "TEN", "JACK", "QUEEN", "KING"};
		String[] suits = {"CLUBS", "DIAMONDS", "HEARTS", "SPADES"};
		for (String rank : ranks) {
			for (String suit : suits) {
				draw.addCard(new Card(rank, suit));
			}
		}
		draw.shuffle();
		played.addCard(draw.removeTop());
		while (played.getTop().isNine() || played.getTop().isSkip() || played.getTop().isReverse()) {
			draw.getDeck().add(0, played.removeTop());
			played.addCard(draw.removeTop());
		}
		turn = new AtomicInteger();
		direction = new AtomicInteger(1);
		started = new AtomicBoolean();
		new Thread(new Runnable() {
			public void run() {
				for (Card card : draw.getDeck()) {
					synchronized (card) {
						card.createImage();
					}
				}
				for (Card card : played.getDeck()) {
					synchronized (card) {
						card.createImage();
					}
				}
			}
			
		}).start();
	}

	@Override
	public void run() {
		int player = 1;
		while (handlers.size() < 4 && !started.get()) {
			try {
				Socket client = server.accept();
				ClientHandler handler = new ClientHandler(client, "Player " + player, this);
				handlers.add(handler);
				handler.start();
				player++;
			} catch (IOException e) {
				break;
			}
		}
	}
	
	/**
	 * Closes this server's socket.
	 */
	public void close() {
		try {
			server.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @return this server's handlers
	 */
	public List<ClientHandler> getHandlers() {
		return handlers;
	}
	
	/**
	 * @return this server's draw deck
	 */
	public Deck getDraw() {
		return draw;
	}
	
	/**
	 * @return this server's played deck
	 */
	public Deck getPlayed() {
		return played;
	}
	
	/**
	 * @return the current player's position
	 */
	public AtomicInteger getTurn() {
		return turn;
	}
	
	/**
	 * @return the direction of play, 1 if clockwise, -1 if counterclockwise
	 */
	public AtomicInteger getDirection() {
		return direction;
	}
	
	/**
	 * @return true if this game has already started or false otherwise
	 */
	public AtomicBoolean getStarted() {
		return started;
	}
	
	/**
	 * @return this server's port
	 */
	public int getPort() {
		return port;
	}
	
	/**
	 * @param host this server's host
	 */
	public void setHost(String host) {
		this.host = host;
	}
	
	/**
	 * @return this server's host
	 */
	public String getHost() {
		return host;
	}
}

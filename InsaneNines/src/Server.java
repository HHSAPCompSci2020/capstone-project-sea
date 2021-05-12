import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class Server implements Runnable {
	private int port;
	private int minClients;
	private int maxClients;
	private ServerSocket server;
	private List<ClientHandler> handlers;
	private Deck draw;
	private Deck played;
	private AtomicInteger turn;
	private AtomicBoolean started;
	
	public Server() {
		port = 9005;
		minClients = 2;
		maxClients = 4;
		try {
			server = new ServerSocket(port);
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
		turn = new AtomicInteger();
		started = new AtomicBoolean();
	}

	@Override
	public void run() {
		while (handlers.size() < maxClients && !started.get()) {
			try {
				Socket client = server.accept();
				ClientHandler handler = new ClientHandler(client, "" + handlers.size(), this);
				handler.start();
				handlers.add(handler);
				new Thread(handler).start();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void close() {
		try {
			server.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public List<ClientHandler> getHandlers() {
		return handlers;
	}
	
	public Deck getDraw() {
		return draw;
	}
	
	public Deck getPlayed() {
		return played;
	}
	
	public AtomicInteger getTurn() {
		return turn;
	}
	
	public AtomicBoolean getStarted() {
		return started;
	}
	
	public int getMinClients() {
		return minClients;
	}
	
	protected void finalize() {
		close();
	}
}

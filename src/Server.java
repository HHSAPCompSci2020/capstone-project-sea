import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server implements Runnable {
	private int port;
	private int minClients;
	private int maxClients;
	private List<ClientHandler> handlers;
	
	public Server() {
		port = 9000;
		minClients = 2;
		maxClients = 4;
		handlers = new ArrayList<ClientHandler>();
	}

	@Override
	public void run() {
		try (ServerSocket server = new ServerSocket(port)) {
			while (handlers.size() < maxClients) {
				Socket client = server.accept();
				System.out.println("accepted");
				ClientHandler handler = new ClientHandler(client, "Client " + (handlers.size()+1), this);
				handlers.add(handler);
				new Thread(handler).start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public List<ClientHandler> getHandlers() {
		return handlers;
	}
}

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler implements Runnable {
	private Socket socket;
	private String name;
	private Server server;
	private BufferedReader in;
	private PrintWriter out;
	
	public ClientHandler(Socket socket, String name, Server server) {
		this.socket = socket;
		this.name = name;
		this.server = server;
		try {
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream(), true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		String input;
		while (true) {
			try {
				input = in.readLine();
				if (input == null) {
					break;
				}
				broadcast(input);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("closed");
	}
	
	private void broadcast(String message) {
		for (ClientHandler ch : server.getHandlers()) {
			ch.out.println(name + ": " + message);
		}
	}
}

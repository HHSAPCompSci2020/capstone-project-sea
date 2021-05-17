package network;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * The part of the client that sends messages to the server.
 * 
 * @author Eric Chang
 * @author John Shelby
 *
 */
public class ClientWriter implements Runnable {
	private Socket socket;
	private ObjectOutputStream out;
	private Queue<DataObject> queue;
	private boolean looping;
	
	/**
	 * Creates a writer with the client's socket.
	 * 
	 * @param socket the client's socket
	 */
	public ClientWriter(Socket socket) {
		this.socket = socket;
		queue = new ConcurrentLinkedQueue<DataObject>();
	}
	
	/**
	 * Sends a message to the server.
	 * 
	 * @param data the data to be sent to the server
	 */
	public void sendMessage(DataObject data) {
		queue.add(data);
	}
	
	/**
	 * Starts writing data to the server.
	 */
	public void start() {
		try {
			out = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
			out.flush();
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
				while (!queue.isEmpty()) {
						out.writeObject(queue.remove());
				}
				out.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Stops reading data to the server.
	 */
	public void stop() {
		looping = false;
	}
}

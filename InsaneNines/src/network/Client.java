package network;
import java.io.IOException;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

/**
 * A client that sends and receives data to a server.
 * 
 * @author Eric Chang
 * @author John Shelby
 */
public class Client {
	private Socket socket;
	private String host;
	private int port;
	private ClientReader reader;
	private ClientWriter writer;
	private ArrayList<NetworkListener> listeners;

	/**
	 * Creates a client with the server's host and port.
	 * 
	 * @param host the server's host
	 * @param port the server's port
	 */
	public Client(String host, int port) {
		this.host = host;
		this.port = port;
		listeners = new ArrayList<NetworkListener>();
	}
	
	/**
	 * Connects to the server.
	 * 
	 * @return true if the client successfully connected to the client or false otherwise
	 * @throws ConnectException if the server is full or does not exist
	 * @throws SocketTimeoutException if the client could not connect to the server in time
	 */
	public synchronized boolean connect() throws ConnectException, SocketTimeoutException {
		try {
			socket = new Socket();
			socket.connect(new InetSocketAddress(host, port), 1000);
			reader = new ClientReader(socket);
			writer = new ClientWriter(socket);
			reader.setListeners(listeners);
			reader.start();
			writer.start();
			sendMessage(DataObject.HANDSHAKE, new Object[] {});
		} catch (ConnectException e) {
			throw e;
		} catch (SocketTimeoutException e) {
			throw e;
		} catch (IOException e) {
			disconnect();
			return false;
		}
		return true;
	}
	
	/**
	 * Disconnects from the server.
	 */
	public synchronized void disconnect() {
		if (reader != null) {
			reader.stop();
			reader = null;
		}
		if (writer != null) {
			writer.stop();
			writer = null;
		}
	}
	
	/**
	 * Sends a message as a data object to the server.
	 * 
	 * @param messageType the type of message to send
	 * @param message the message to send
	 * @see DataObject
	 */
	public synchronized void sendMessage(String messageType, Object... message) {
		DataObject data = new DataObject();
		data.messageType = messageType;
		data.message = message;
		writer.sendMessage(data);
	}
	
	/**
	 * Adds a listener to listen when the client receives a message.
	 * 
	 * @param listener the listener to add
	 */
	public void addListener(NetworkListener listener) {
		synchronized (listeners) {
			listeners.add(listener);
		}
	}
}

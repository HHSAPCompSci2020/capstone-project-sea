package Network;
import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class Client {
	private Socket socket;
	private String host;
	private int port;
	private ClientReader reader;
	private ClientWriter writer;
	private ArrayList<NetworkListener> listeners;

	public Client(String host, int port) {
		this.host = host;
		this.port = port;
		listeners = new ArrayList<NetworkListener>();
	}
	
	public synchronized boolean connect() throws ConnectException {
		try {
			socket = new Socket(host, port);
			reader = new ClientReader(socket);
			writer = new ClientWriter(socket);
			reader.setListeners(listeners);
			reader.start();
			writer.start();
			sendMessage(DataObject.HANDSHAKE, new Object[] {});
		} catch (UnknownHostException e) {
			e.printStackTrace();
			return false;
		} catch (ConnectException e) {
			throw e;
		} catch (IOException e) {
			e.printStackTrace();
			disconnect();
			return false;
		}
		return true;
	}
	
	public synchronized void disconnect() {
		if (reader != null) {
			reader.stop();
		}
		if (writer != null) {
			writer.stop();
		}
	}
	
	public synchronized void sendMessage(String messageType, Object... message) {
		DataObject data = new DataObject();
		data.messageType = messageType;
		data.message = message;
		writer.sendMessage(data);
	}
	
	public void addListener(NetworkListener listener) {
		synchronized (listeners) {
			listeners.add(listener);
		}
	}
	
	public String getHost() {
		return host;
	}
}

package Network;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
	private Socket socket;
	private String host;
	private int port;
	private ClientReader reader;
	private ClientWriter writer;
	private NetworkListener listener;

	public Client(String host) {
		// private 192.168.0.22
		// public 98.210.100.211
		this.host = host;
		port = 9005;
	}
	
	public Client(String host, int port) {
		this.host = host;
		this.port = port;
	}
	
	public synchronized boolean connect() {
		try {
			socket = new Socket(host, port);
			reader = new ClientReader(socket);
			writer = new ClientWriter(socket);
			reader.setListener(listener);
			reader.start();
			writer.start();
			sendMessage(DataObject.HANDSHAKE, new Object[] {});
		} catch (UnknownHostException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			disconnect();
			return false;
		}
		return true;
	}
	
	public synchronized void disconnect() {
		reader.stop();
		writer.stop();
	}
	
	public synchronized void sendMessage(String messageType, Object... message) {
		DataObject data = new DataObject();
		data.messageType = messageType;
		data.message = message;
		writer.sendMessage(data);
	}
	
	public synchronized void setListener(NetworkListener listener) {
		this.listener = listener;
	}
	
	public String getHost() {
		return host;
	}
}

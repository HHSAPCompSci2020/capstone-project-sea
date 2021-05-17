package network;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.SwingUtilities;

/**
 * The part of the client that receives messages from the server.
 * 
 * @author Eric Chang
 * @author John Shelby
 */
public class ClientReader implements Runnable {
	private Socket socket;
	private ObjectInputStream in;
	private boolean looping;
	private ArrayList<NetworkListener> listeners;
	
	/**
	 * Creates a reader with the client's socket.
	 * 
	 * @param socket the client's socket
	 */
	public ClientReader(Socket socket) {
		this.socket = socket;
	}
	
	/**
	 * @param listeners the client's listeners
	 */
	public void setListeners(ArrayList<NetworkListener> listeners) {
		this.listeners = listeners;
	}
	
	/**
	 * Starts reading data from the server.
	 */
	public void start() {
		try {
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
					for (NetworkListener listener : listeners) {
						SwingUtilities.invokeLater(new Runnable() {
							@Override
							public void run() {
								listener.messageReceived(data);
							}
						});
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
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Stops reading data from the server.
	 */
	public void stop() {
		looping = false;
	}
}

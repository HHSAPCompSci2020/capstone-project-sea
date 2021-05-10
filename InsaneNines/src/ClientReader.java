import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

import javax.swing.SwingUtilities;

public class ClientReader implements Runnable {
	private Socket socket;
	private ObjectInputStream in;
	private boolean looping;
	private NetworkListener listener;
	
	public ClientReader(Socket socket) {
		this.socket = socket;
	}
	
	public void setListener(NetworkListener listener) {
		this.listener = listener;
	}
	
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
					if (listener != null) {
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
	
	public void stop() {
		looping = false;
	}
}

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ClientWriter implements Runnable {
	private Socket socket;
	private ObjectOutputStream out;
	private Queue<DataObject> queue;
	private boolean looping;
	
	public ClientWriter(Socket socket) {
		this.socket = socket;
		queue = new ConcurrentLinkedQueue<DataObject>();
	}
	
	public void sendMessage(DataObject data) {
		queue.add(data);
	}
	
	public void start() {
		try {
			out = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
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
	
	public void stop() {
		looping = false;
	}
}

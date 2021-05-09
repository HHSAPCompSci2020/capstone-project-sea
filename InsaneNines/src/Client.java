import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {

	public static void main(String[] args) throws UnknownHostException, IOException {
		// private 192.168.0.22
		// public 98.210.100.211
		String host = "localhost";
		int port = 9005;

		Socket socket = new Socket(host, port);
		PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
		BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
		Thread input = new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						String message = stdin.readLine();
						if (message != null)
							out.println(message);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

		});
		Thread output = new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						String message = in.readLine();
						System.out.println(message);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

		});
		input.start();
		output.start();
	}
}

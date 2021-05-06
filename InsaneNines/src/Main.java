import java.awt.Color;

import javax.swing.JFrame;

public class Main {

	public static void main(String[] args) {
//		Server server = new Server();
//		server.run();
		
		JFrame frame = new JFrame("Welcome Screen");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.setSize(500,400);
		frame.getContentPane().setBackground(new Color(0, 0, 0));
	}

}

package Game;
import java.awt.Container;

import javax.swing.JFrame;
import javax.swing.JTextArea;

public class Player extends JFrame{
	
	private Deck hand;
	private int width;
	private int height;
	private Container contentPane;
	private JTextArea turn;
	
	public Player(int w, int h) {
		width = w;
		height = h;
		contentPane = this.getContentPane();
		turn = new JTextArea();
	}
	
	public void setUpGui() {
		this.setSize(width, height);
		this.setTitle("GAME");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		contentPane.add(turn);
		turn.setText("YOUR TURN");
		turn.setWrapStyleWord(true);
		turn.setLineWrap(true);
		turn.setEditable(false);
		this.setVisible(true);
	}
	
	
	public static void main(String[] args) {
		Player p = new Player(500, 400);
		p.setUpGui();
	}
	
}

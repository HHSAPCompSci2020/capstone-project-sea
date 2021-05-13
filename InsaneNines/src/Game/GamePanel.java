package Game;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class GamePanel extends JFrame {
	
	private ArrayList<Player> players;
	private Deck discardPile;
	
	public GamePanel(int numOfPlayers) {
		JFrame game = new JFrame();
		game.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		game.setBackground(new Color(0, 0, 0));
	
	}
	
}

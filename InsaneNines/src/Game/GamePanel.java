package Game;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class GamePanel extends JPanel {
	private ArrayList<Player> players;
	private Deck discardPile;
	
	public GamePanel(int numOfPlayers) {
		super("GAME");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBackground(new Color(0, 0, 0));
	}
	
}

package Game;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

import Network.Server;

public class GamePanel extends JFrame {
	
	private ArrayList<Player> players;
	private Deck discardPile;
	private Server s;
	
	public GamePanel(int numOfPlayers, Server s) {
		super("GAME");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBackground(new Color(0, 0, 0));
		this.s = s;
	}
	
	
}

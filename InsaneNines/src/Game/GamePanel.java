package Game;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

import Network.DataObject;
import Network.NetworkListener;

public class GamePanel extends JFrame implements NetworkListener {
	private int pos;
	private ArrayList<Player> players;
	
	public GamePanel(String name, ArrayList<String> names, Deck deck) {
		super("GAME");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBackground(new Color(0, 0, 0));
		
		players = new ArrayList<Player>();
		for (int i = 0; i < names.size(); i++) {
			if (names.get(i).equals(name)) {
				pos = i;
				players.add(new Player(name, deck));
			} else {
				players.add(new Player(names.get(i), deck.getDeck().size()));
			}
		}
	}

	@Override
	public void messageReceived(DataObject data) {
		
	}
	
}

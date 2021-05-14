package Game;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.swing.JFrame;

import Network.DataObject;
import Network.NetworkListener;

public class GamePanel extends JFrame implements MouseListener, MouseMotionListener, NetworkListener {
	private static final Color BACKGROUND_COLOR = Color.GREEN;
	private static final int TABLE_SIZE = 400; // Pixels.
	private int pos;
	private boolean myTurn;
	private Card top;
	private ArrayList<Player> players;

	public GamePanel(String name, ArrayList<String> names, Deck deck) {
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		setSize(500, 400);
		myTurn = false;
		top = null;
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
		if (data.messageType.equals(DataObject.TURN)) {
			int turn = (int) data.message[0];
			if (turn == pos) {
				myTurn = true;
				top = (Card) data.message[1];
			} else {
				players.get(turn).setNumCards((int) data.message[2]);
			}
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}
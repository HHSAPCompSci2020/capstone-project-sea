package Game;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import Network.Client;
import Network.DataObject;
import Network.NetworkListener;

public class GamePanel extends JFrame implements NetworkListener {
	private static final long serialVersionUID = 9043093457846944651L;
	private static final Color BACKGROUND_COLOR = Color.GREEN;
	private JPanel middle, cards, area1, area2, area3, area4, area5;
	private JLabel playerTurn, name1, name2, name3, name4, num1, num2, num3, num4, draw, topLabel;
	private int pos;
	private int turn;
	private boolean myTurn;
	private Card top;
	private ArrayList<Player> players;
	private Client client;

	public GamePanel(Client client, String name, ArrayList<String> names, Deck deck) {
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
		this.client = client;
		playerTurn = new JLabel();
		middle = new JPanel(new BorderLayout());
		cards = new JPanel(new BoxLayout(cards, BoxLayout.X_AXIS));
		for (Card card : deck.getDeck()) {
			JLabel cardLabel = new JLabel(card.getImage());
			cardLabel.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					if (myTurn && card.canPlay(top)) {
						myTurn = false;
						int numCards = players.get(pos).play(card);
						cards.remove(cardLabel);
						revalidate();
						repaint();
						client.sendMessage(DataObject.PLAY, new Object[] {card, numCards});
					}
				}
			});
			cards.add(cardLabel);
		}
		
		area1 = new JPanel(new BoxLayout(area1, BoxLayout.Y_AXIS));
		area2 = new JPanel(new BoxLayout(area2, BoxLayout.Y_AXIS));
		area3 = new JPanel(new BoxLayout(area3, BoxLayout.Y_AXIS));
		area4 = new JPanel(new BoxLayout(area4, BoxLayout.Y_AXIS));
		area5 = new JPanel(new BoxLayout(area5, BoxLayout.X_AXIS));
		
		name1 = new JLabel(names.get(0));
		name2 = new JLabel(names.get(1));
		num1 = new JLabel(deck.getDeck().size() + " Cards");
		num2 = new JLabel(deck.getDeck().size() + " Cards");
		if (names.size() >= 3) {
			name3 = new JLabel(names.get(2));
			num3 = new JLabel(deck.getDeck().size() + " Cards");
		}
		if (names.size() == 4) {
			name4 = new JLabel(names.get(3));
			num4 = new JLabel(deck.getDeck().size() + " Cards");
		}
		draw = new JLabel(new ImageIcon(new ImageIcon(getClass().getResource("Images/cardback.png")).getImage()
				.getScaledInstance(75, 105, Image.SCALE_DEFAULT)));
		draw.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (myTurn) {
					client.sendMessage(DataObject.DRAW, new Object[] {});
				}
			}
		});
		topLabel = new JLabel();
		
		area1.add(name1);
		area1.add(num1);
		area2.add(name2);
		area2.add(num2);
		area3.add(name3);
		area3.add(num3);
		area4.add(name4);
		area4.add(num4);
		area5.add(draw);
		area5.add(topLabel);
		
		middle.add(area1, BorderLayout.NORTH);
		middle.add(area2, BorderLayout.EAST);
		middle.add(area3, BorderLayout.SOUTH);
		middle.add(area4, BorderLayout.WEST);
		middle.add(area5, BorderLayout.CENTER);
		
		add(playerTurn, BorderLayout.NORTH);
		add(middle, BorderLayout.CENTER);
		add(cards, BorderLayout.SOUTH);
		
		setBackground(BACKGROUND_COLOR);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		setSize(500, 400);
	}

	@Override
	public void messageReceived(DataObject data) {
		if (data.messageType.equals(DataObject.TURN)) {
			turn = (int) data.message[0];
			top = (Card) data.message[1];
			topLabel.setIcon(top.getImage());
			if (turn == pos) {
				myTurn = true;
				playerTurn.setText(players.get(pos).getName() + "'s Turn (you)");
			} else {
				playerTurn.setText(players.get(turn).getName() + "'s Turn");
			}
			int numCards = (int) data.message[2];
			if (numCards != -1) {
				int prevTurn = (turn - 1) % players.size();
				players.get(prevTurn).setNumCards(numCards);
				if (prevTurn == 0) {
					num1.setText(numCards + " Cards");
				} else if (prevTurn == 1) {
					num2.setText(numCards + " Cards");
				} else if (prevTurn == 2) {
					num3.setText(numCards + " Cards");
				} else {
					num4.setText(numCards + " Cards");
				}
			}
			revalidate();
			repaint();
		} else if (data.messageType.equals(DataObject.DRAW)) {
			Card card = (Card) data.message[0];
			players.get(pos).draw(card);
			JLabel cardLabel = new JLabel(card.getImage());
			cardLabel.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					if (myTurn && card.canPlay(top)) {
						myTurn = false;
						int numCards = players.get(pos).play(card);
						cards.remove(cardLabel);
						revalidate();
						repaint();
						client.sendMessage(DataObject.PLAY, new Object[] {card, numCards});
					}
				}
			});
			cards.add(cardLabel);
			revalidate();
			repaint();
			
		} else if (data.messageType.equals(DataObject.END)) {
			playerTurn.setText((String) data.message[0] + " wins!");
			players.get(turn).setNumCards(0);
			if (turn == 0) {
				num1.setText("0 Cards");
			} else if (turn == 1) {
				num2.setText("0 Cards");
			} else if (turn == 2) {
				num3.setText("0 Cards");
			} else {
				num4.setText("0 Cards");
			}
			topLabel.setIcon(((Card) data.message[1]).getImage());
			revalidate();
			repaint();
		}
	}
}
package game;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import network.Client;
import network.DataObject;
import network.NetworkListener;

/**
 * The frame that the game is played on.
 * 
 * @author Eric Chang
 * @author Samuel Zhou
 */
public class GamePanel extends JFrame implements NetworkListener {
	private static final long serialVersionUID = 9043093457846944651L;
	private static final Color BACKGROUND_COLOR = Color.GREEN;
	private JPanel game, middle, cards, area1, area2, area3, area4, area5, largeArea2, largeArea4, chatArea, sendArea;
	private JTextArea messages;
	private JTextField message;
	private JScrollPane pane, chat;
	private JLabel playerTurn, name1, name2, name3, name4, num1, num2, num3, num4, draw, topLabel;
	private JButton send;
	private int pos;
	private int turn;
	private boolean myTurn;
	private Card top;
	private ArrayList<Player> players;
	private Client client;
	private Main main;

	/**
	 * Creates the game panel.
	 * 
	 * @param client the client for this panel's player
	 * @param name the name of this panel's player
	 * @param names the names of every player in the game
	 * @param deck the deck of this panel's player
	 * @param turn the starting player's position
	 * @param top the starting card
	 * @param Main the main menu
	 */
	public GamePanel(Client client, String name, ArrayList<String> names, Deck deck, int turn, Card top, Main main) {
		myTurn = false;
		this.turn = turn;
		this.top = top;
		this.main = main;
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
		if (turn == pos) {
			myTurn = true;
		}
		playerTurn.setText(players.get(turn).getName() + "'s Turn");
		playerTurn.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 30));
		playerTurn.setHorizontalAlignment(SwingConstants.CENTER);
		game = new JPanel(new BorderLayout());
		middle = new JPanel(new BorderLayout());
		cards = new JPanel();
		pane = new JScrollPane(cards);
		cards.setAutoscrolls(true);
		cards.setPreferredSize(new Dimension(600, 950));
		pane.setPreferredSize(new Dimension(600, 150));
		game.setPreferredSize(new Dimension(600, 400));
		pane.getVerticalScrollBar().setUnitIncrement(10);
		for (Card card : deck.getDeck()) {
			JLabel cardLabel = new JLabel(card.getImage());
			cardLabel.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					if (myTurn && card.canPlay(GamePanel.this.top)) {
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
		largeArea2 = new JPanel();
		largeArea2.setLayout(new BoxLayout(largeArea2, BoxLayout.X_AXIS));
		largeArea4 = new JPanel();
		largeArea4.setLayout(new BoxLayout(largeArea4, BoxLayout.X_AXIS));
		
		area1 = new JPanel();
		area1.setLayout(new BoxLayout(area1, BoxLayout.Y_AXIS));
		area2 = new JPanel();
		area2.setLayout(new BoxLayout(area2, BoxLayout.Y_AXIS));
		area3 = new JPanel();
		area3.setLayout(new BoxLayout(area3, BoxLayout.Y_AXIS));
		area4 = new JPanel();
		area4.setLayout(new BoxLayout(area4, BoxLayout.Y_AXIS));
		area5 = new JPanel();
		area5.setLayout(new BoxLayout(area5, BoxLayout.X_AXIS));
		
		name1 = new JLabel(names.get(0));
		if (pos == 0) {
			name1.setText(name1.getText() + " (you)");
		}
		name1.setAlignmentX(Component.CENTER_ALIGNMENT);
		name2 = new JLabel(names.get(1));
		if (pos == 1) {
			name2.setText(name2.getText() + " (you)");
		}
		name2.setAlignmentX(Component.CENTER_ALIGNMENT);
		num1 = new JLabel(deck.getDeck().size() + " Cards");
		num1.setAlignmentX(Component.CENTER_ALIGNMENT);
		num2 = new JLabel(deck.getDeck().size() + " Cards");
		num2.setAlignmentX(Component.CENTER_ALIGNMENT);
		name3 = new JLabel("None");
		name3.setAlignmentX(Component.CENTER_ALIGNMENT);
		num3 = new JLabel("None");
		num3.setAlignmentX(Component.CENTER_ALIGNMENT);
		name4 = new JLabel("None");
		name4.setAlignmentX(Component.CENTER_ALIGNMENT);
		num4 = new JLabel("None");
		num4.setAlignmentX(Component.CENTER_ALIGNMENT);
		if (names.size() >= 3) {
			name3.setText(names.get(2));
			if (pos == 2) {
				name3.setText(name3.getText() + " (you)");
			}
			num3.setText(deck.getDeck().size() + " Cards");
		}
		if (names.size() == 4) {
			name4.setText(names.get(3));
			if (pos == 3) {
				name4.setText(name4.getText() + " (you)");
			}
			num4.setText(deck.getDeck().size() + " Cards");
		}
		draw = new JLabel(new ImageIcon(new ImageIcon(getClass().getResource("/cardback.png")).getImage()
				.getScaledInstance(75, 105, Image.SCALE_DEFAULT)));
		draw.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (myTurn) {
					client.sendMessage(DataObject.DRAW, new Object[] {});
				}
			}
		});
		draw.setAlignmentX(Component.CENTER_ALIGNMENT);
		topLabel = new JLabel(top.getImage());
		topLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		area1.add(name1);
		area1.add(num1);
		area2.add(name2);
		area2.add(num2);
		area3.add(name3);
		area3.add(num3);
		area4.add(name4);
		area4.add(num4);
		area5.add(Box.createHorizontalGlue());
		area5.add(draw);
		area5.add(topLabel);
		area5.add(Box.createHorizontalGlue());

		largeArea2.add(area2);
		largeArea2.add(Box.createRigidArea(new Dimension(100, 0)));
		largeArea4.add(Box.createRigidArea(new Dimension(100, 0)));
		largeArea4.add(area4);
		
		middle.add(area1, BorderLayout.NORTH);
		middle.add(largeArea2, BorderLayout.EAST);
		middle.add(area3, BorderLayout.SOUTH);
		middle.add(largeArea4, BorderLayout.WEST);
		middle.add(area5, BorderLayout.CENTER);
		
		game.add(playerTurn, BorderLayout.NORTH);
		game.add(middle, BorderLayout.CENTER);
		game.add(pane, BorderLayout.SOUTH);
		
		messages = new JTextArea();
		messages.setEditable(false);
		chat = new JScrollPane(messages);
		chat.setAutoscrolls(true);
		message = new JTextField();
		message.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!message.getText().trim().equals("")) {
					client.sendMessage(DataObject.MESSAGE, players.get(pos).getName(), message.getText().trim());
					message.setText("");
				}
			}
		});
		send = new JButton("Send");
		send.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!message.getText().trim().equals("")) {
					client.sendMessage(DataObject.MESSAGE, players.get(pos).getName(), message.getText().trim());
					message.setText("");
				}
			}
			
		});
		sendArea = new JPanel(new BorderLayout());
		sendArea.add(message, BorderLayout.CENTER);
		sendArea.add(send, BorderLayout.EAST);
		chatArea = new JPanel(new BorderLayout());
		chatArea.setPreferredSize(new Dimension(300, 400));
		chatArea.add(chat, BorderLayout.CENTER);
		chatArea.add(sendArea, BorderLayout.SOUTH);
		
		add(game, BorderLayout.CENTER);
		add(chatArea, BorderLayout.EAST);
		
		setBackground(BACKGROUND_COLOR);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		setSize(900, 400);
		pack();
	}

	@Override
	public void messageReceived(DataObject data) {
		if (data.messageType.equals(DataObject.TURN)) {
			turn = (int) data.message[0];
			top = (Card) data.message[1];
			topLabel.setIcon(top.getImage());
			if (turn == pos) {
				myTurn = true;
			}
			playerTurn.setText(players.get(turn).getName() + "'s Turn");
			int numCards = (int) data.message[2];
			int prevTurn = (turn - 1 + players.size()) % players.size();
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
			revalidate();
			repaint();
		} else if (data.messageType.equals(DataObject.DRAW)) {
			Card card = (Card) data.message[0];
			int numCards = players.get(pos).draw(card);
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
			if (pos == 0) {
				num1.setText(numCards + " Cards");
			} else if (pos == 1) {
				num2.setText(numCards + " Cards");
			} else if (pos == 2) {
				num3.setText(numCards + " Cards");
			} else {
				num4.setText(numCards + " Cards");
			}
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
		} else if(data.messageType.equals(DataObject.MESSAGE)) {
			String name = (String) data.message[0];
			if (!messages.getText().equals("")) {
				messages.append("\n");
			}
			if (name.equals(players.get(pos).getName())) {
				messages.append(name + " (you): " + (String) data.message[1]);
			} else {
				messages.append(name + ": " + (String) data.message[1]);
			}
		} else if (data.messageType.equals(DataObject.DISCONNECT)) {
			setVisible(false);
			main.backToMenu("A player disconnected. The server has been closed.");
		}
	}
}
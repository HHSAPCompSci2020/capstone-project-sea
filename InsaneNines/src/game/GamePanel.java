package game;

import java.awt.BorderLayout;
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
import javax.swing.JOptionPane;
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
	private JPanel game, middle, cards, area1, area2, area3, area4, area5, largeArea2, largeArea4,
	chatArea, sendArea, bottom;
	private JTextArea messages;
	private JTextField message;
	private JScrollPane pane, chat;
	private JLabel playerTurn, name1, name2, name3, name4, num1, num2, num3, num4, draw, topLabel;
	private JButton send, leave;
	private int pos;
	private int turn;
	private int prevTurn;
	private boolean myTurn;
	private String suit;
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
	 * @param main the main menu
	 */
	public GamePanel(Client client, String name, ArrayList<String> names, Deck deck, int turn,
			Card top, Main main) {
		this.turn = turn;
		this.top = top;
		this.main = main;
		players = new ArrayList<Player>();
		for (int i = 0; i < names.size(); i++) {
			if (names.get(i).equals(name)) {
				pos = i;
				deck.sort();
				players.add(new Player(name, deck));
			} else {
				players.add(new Player(names.get(i), deck.getDeck().size()));
			}
		}
		prevTurn = -1;
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
					if (myTurn && card.canPlay(GamePanel.this.top, suit)) {
						String suit = null;
						if (card.isNine() && players.get(pos).getNumCards() > 1) {
							Object[] suits = {"CLUBS", "DIAMONDS", "HEARTS", "SPADES"};
							suit = (String) JOptionPane.showInputDialog(GamePanel.this,
									"Choose the next suit:", "Suit Picker",
									JOptionPane.PLAIN_MESSAGE, null, suits, suits[0]);
							if (suit == null) {
								return;
							}
						}
						boolean won = players.get(pos).play(card) == 0;
						cards.remove(cardLabel);
						revalidate();
						repaint();
						if (suit == null) {
							GamePanel.this.client.sendMessage(DataObject.PLAY,
									new Object[] {card, won});
						} else {
							GamePanel.this.client.sendMessage(DataObject.PLAY,
									new Object[] {card, won, suit});
						}
					}
				}
			});
			cardLabel.setToolTipText("Play Card: " + card.toString());
			cards.add(cardLabel);
		}
		bottom = new JPanel();
		bottom.setLayout(new BoxLayout(bottom, BoxLayout.X_AXIS));
		leave = new JButton("Leave Game");
		leave.setPreferredSize(new Dimension(100, 20));
		leave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				main.backToMenu(null);
			}
		});
		bottom.add(leave);
		bottom.add(Box.createHorizontalGlue());
		
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
		draw = new JLabel(new ImageIcon(new ImageIcon(getClass().getResource("/cardback.png"))
				.getImage().getScaledInstance(75, 105, Image.SCALE_DEFAULT)));
		draw.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (myTurn) {
					if (players.get(pos).canPlay(GamePanel.this.top, suit)) {
						int choice = JOptionPane.showConfirmDialog(GamePanel.this,
								"You can play a card. Do you still want to draw?",
								"Draw Card", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
						if (choice == JOptionPane.YES_OPTION) {
							client.sendMessage(DataObject.DRAW, new Object[] {true});
						}
						return;
					}
					client.sendMessage(DataObject.DRAW, new Object[] {});
				}
			}
		});
		draw.setAlignmentX(Component.CENTER_ALIGNMENT);
		draw.setToolTipText("Draw Card");
		topLabel = new JLabel(top.getImage());
		topLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		topLabel.setToolTipText("Top Card: " + top.toString());
		
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
		area5.add(Box.createRigidArea(new Dimension(10, 105)));
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
		
		messages = new JTextArea("The game has started!\nThe top card is " + top.toString() + ".");
		messages.setEditable(false);
		messages.setLineWrap(true);
		messages.setWrapStyleWord(true);
		chat = new JScrollPane(messages);
		message = new JTextField();
		message.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!message.getText().trim().equals("")) {
					client.sendMessage(DataObject.MESSAGE, players.get(pos).getName(),
							message.getText().trim());
					message.setText("");
				}
			}
		});
		send = new JButton("Send");
		send.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!message.getText().trim().equals("")) {
					client.sendMessage(DataObject.MESSAGE, players.get(pos).getName(),
							message.getText().trim());
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
		add(bottom, BorderLayout.SOUTH);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		setSize(900, 400);
		setResizable(false);
		pack();
	}

	@Override
	public void messageReceived(DataObject data) {
		if (data.messageType.equals(DataObject.TURN)) {
			prevTurn = turn;
			turn = (int) data.message[0];
			if (turn == pos) {
				myTurn = true;
			} else {
				myTurn = false;
			}
			if (data.message.length > 1) {
				top = (Card) data.message[1];
				topLabel.setIcon(top.getImage());
				topLabel.setToolTipText("Top Card: " + top.toString());
				if (top.isNine()) {
					suit = (String) data.message[2];
					playerTurn.setText(players.get(turn).getName() + "'s Turn, Must Play " + suit);
					messages.append("\n" + players.get(prevTurn).getName() + " played "
							+ top.toString() + " and declared " + suit + ".");
				} else {
					suit = null;
					playerTurn.setText(players.get(turn).getName() + "'s Turn");
					messages.append("\n" + players.get(prevTurn).getName() + " played "
							+ top.toString() + ".");
					if (top.isSkip()) {
						int skipped = 0;
						if ((prevTurn + 2) % players.size() == turn) {
							skipped = (prevTurn + 1) % players.size();
						} else {
							skipped = (prevTurn - 1 + players.size()) % players.size();
						}
						messages.append("\n" + players.get(skipped).getName()
								+ "'s turn has been skipped.");
					} else if (top.isReverse()) {
						messages.append("\nThe direction of play has been reversed.");
					}
				}
				messages.setCaretPosition(messages.getDocument().getLength());
				int numCards = players.get(prevTurn).getNumCards() - 1;
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
			} else {
				if (top.isNine()) {
					playerTurn.setText(players.get(turn).getName() + "'s Turn, Must Play " + suit);
				} else {
					suit = null;
					playerTurn.setText(players.get(turn).getName() + "'s Turn");
				}
				if (prevTurn == pos) {
					JOptionPane.showMessageDialog(this, "There are no more cards left in the draw pile. "
							+ "Your turn has been skipped.", "Draw Card", JOptionPane.PLAIN_MESSAGE);
				}
				messages.append("\n" + players.get(prevTurn).getName() + " could not play a card "
						+ "and their turn has been skipped.");
				messages.setCaretPosition(messages.getDocument().getLength());
			}
			revalidate();
			repaint();
		} else if (data.messageType.equals(DataObject.DRAW)) {
			if (data.message.length == 0) {
				JOptionPane.showMessageDialog(this, "There are no more cards to be drawn. "
						+ "You must play a card.", "Draw Card", JOptionPane.PLAIN_MESSAGE);
			} else {
				Card card = (Card) data.message[0];
				if (turn == pos) {
					int cardPos = players.get(pos).draw(card);
					JLabel cardLabel = new JLabel(card.getImage());
					cardLabel.addMouseListener(new MouseAdapter() {
						public void mouseClicked(MouseEvent e) {
							if (myTurn && card.canPlay(GamePanel.this.top, suit)) {
								String suit = null;
								if (card.isNine() && players.get(pos).getNumCards() > 1) {
									Object[] suits = {"CLUBS", "DIAMONDS", "HEARTS", "SPADES"};
									suit = (String) JOptionPane.showInputDialog(GamePanel.this,
											"Choose the next suit:", "Suit Picker",
											JOptionPane.PLAIN_MESSAGE, null, suits, suits[0]);
									if (suit == null) {
										return;
									}
								}
								boolean won = players.get(pos).play(card) == 0;
								cards.remove(cardLabel);
								revalidate();
								repaint();
								if (suit == null) {
									client.sendMessage(DataObject.PLAY, new Object[] {card, won});
								} else {
									client.sendMessage(DataObject.PLAY, new Object[] {card, won,
											suit});
								}
							}
						}
					});
					cardLabel.setToolTipText("Play Card: " + card.toString());
					cards.add(cardLabel, cardPos);
				}
				int numCards = players.get(turn).getNumCards() + 1;
				players.get(turn).setNumCards(numCards);
				if (turn == 0) {
					num1.setText(numCards + " Cards");
				} else if (turn == 1) {
					num2.setText(numCards + " Cards");
				} else if (turn == 2) {
					num3.setText(numCards + " Cards");
				} else {
					num4.setText(numCards + " Cards");
				}
				messages.append("\n" + players.get(turn).getName() + " drew a card.");
				messages.setCaretPosition(messages.getDocument().getLength());
				revalidate();
				repaint();
			}
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
			messages.append("\n" + players.get(turn).getName() + " played " + top.toString() + ".\n"
					+ players.get(turn).getName() + " wins!");
			messages.setCaretPosition(messages.getDocument().getLength());
			revalidate();
			repaint();
		} else if(data.messageType.equals(DataObject.MESSAGE)) {
			String name = (String) data.message[0];
			if (name.equals(players.get(pos).getName())) {
				messages.append("\n" + name + " (you): " + (String) data.message[1]);
			} else {
				messages.append("\n" + name + ": " + (String) data.message[1]);
			}
			messages.setCaretPosition(messages.getDocument().getLength());
		} else if (data.messageType.equals(DataObject.DISCONNECT)) {
			setVisible(false);
			main.backToMenu("A player disconnected. The server has been closed.");
		}
	}
}
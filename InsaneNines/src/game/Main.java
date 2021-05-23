package game;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;

import network.Client;
import network.DataObject;
import network.NetworkListener;
import network.Server;

/**
 * The main class to be run.
 * 
 * @author Andy Ding
 */
public class Main implements NetworkListener {

	//public static final int GAME_SIZE = 750;
	private JFrame f;
	private GamePanel game;
	private JPanel menu, instructions, waitRoom;
	private JButton createServer, joinServer, viewInstructions, back, back2, start, changeName;
	private JLabel title, l, playerCount;
	private JTextArea serverInfo, i;
	private Server s;
	private Client c;
	private String name;
	private ArrayList<String> names;
	private SwingWorker<String, Void> worker;
	private SwingWorker<Boolean, Void> worker2;

	/**
	 * Displays the start menu.
	 */
	public Main() {
		f = new JFrame("Welcome Screen");
		title = new JLabel("Insane Nines");
		createServer = new JButton("Create Server");
		joinServer = new JButton("Join Server");
		viewInstructions = new JButton("Instructions");
		back = new JButton("Back");
		back2 = new JButton("Back");
		start = new JButton("Start");
		changeName = new JButton("Change Name");
		menu = new JPanel();
		instructions = new JPanel();
		waitRoom = new JPanel();
		l = new JLabel("Waiting for players...");
		playerCount = new JLabel("1/4");
		serverInfo = new JTextArea("IP Address: Loading...\nPort Number: Loading...");
		name = null;
		names = null;
		
		i = new JTextArea("Rules:\n\n"
				+ "When it is your turn, you can either play or draw.\n\n"
				+ "Play a card by clicking on the card in your deck. "
				+ "This card must be of the same rank or suit as the top card, "
				+ "or the card 9, which allows you to declare the suit of the next player's card. "
				+ "Queens skip the next player's turn and aces reverse the direction of play.\n\n"
				+ "Draw a card by clicking on the draw deck next to the top card.\n"
				+ "If there are no more cards available to draw and you cannot play a card, "
				+ "your turn is skipped.\n\n"
				+ "The first player to discard all their cards wins.");
//		BufferedImage img = ImageIO.read(new File("Images/welcomebackground.png"));
//		f.setContentPane(new JLabel(new ImageIcon(img)));
		
//		f.setLayout(null);
		
		menu.setLayout(null);
		title.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 30));
		title.setHorizontalAlignment(SwingConstants.CENTER);
		title.setBounds(50, 25, 400, 50);
		createServer.setBounds(50, 100, 400, 50);
		joinServer.setBounds(50, 175, 400, 50);
		viewInstructions.setBounds(50, 250, 400, 50);
		
		menu.add(title);
		menu.add(createServer);
		menu.add(joinServer);
		menu.add(viewInstructions);
	
		menu.setBackground(Color.WHITE);
		f.add(menu);
		
		
		waitRoom.setLayout(null);
		back.setBounds(350, 300, 100, 50);
		back2.setBounds(350, 300, 100, 50);
		start.setBounds(50, 300, 100, 50);
		changeName.setBounds(325, 200, 125, 50);
		l.setBounds(50, 100, 400, 50);
		serverInfo.setEditable(false);
		serverInfo.setBounds(50, 50, 400, 50);
		//update player label when players join
		playerCount.setBounds(245, 300, 100, 50);
		waitRoom.add(playerCount);
		waitRoom.add(back);
		waitRoom.add(l);
		waitRoom.add(serverInfo);
		waitRoom.add(changeName);
		waitRoom.setBackground(Color.WHITE);
			
		instructions.setLayout(null);
		i.setLineWrap(true);
		i.setWrapStyleWord(true);
		i.setBounds(50, 50, 400, 250);
		instructions.add(i);
		instructions.add(back2);
		instructions.setBackground(Color.WHITE);

		f.setResizable(false);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);
		f.setSize(500, 400);


		createServer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (s != null) {
					s.close();
				}
				s = new Server();
				new Thread(s).start();
				if (c != null) {
					c.disconnect();
				}
				c = new Client(InetAddress.getLoopbackAddress().getHostAddress(), s.getPort());
				c.addListener(Main.this);
				try {
					if (c.connect()) {
						worker = new SwingWorker<String, Void>() {
							protected String doInBackground() throws Exception {
								return InetAddress.getLocalHost().getHostAddress();
							}
							
							protected void done() {
								try {
									if (c != null) {
										c.sendMessage(DataObject.INFORMATION, get());
									}
								} catch (InterruptedException e) {
								} catch (ExecutionException e) {
									e.getCause().printStackTrace();
								} catch (CancellationException e) {
								}
							}
							
						};
						worker.execute();
						waitRoom.add(start);
						f.setContentPane(waitRoom);
						f.revalidate();
						f.repaint();
					}
				} catch (ConnectException e1) {
					e1.printStackTrace();
				} catch (SocketTimeoutException e1) {
					e1.printStackTrace();
				}
			}
		});

		joinServer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JTextField ip = new JTextField();
				JTextField port = new JTextField();
				Object[] message = {
						"IP Address: ", ip,
						"Port Number: ", port
				};
				int choice = JOptionPane.showConfirmDialog(f, message, "Join Server",
						JOptionPane.OK_CANCEL_OPTION);
				if (choice == JOptionPane.OK_OPTION) {
					try {
						c = new Client(ip.getText(), Integer.parseInt(port.getText()));
						c.addListener(Main.this);
						worker2 = new SwingWorker<Boolean, Void>() {
							protected Boolean doInBackground() throws Exception {
								return c.connect();
							}
							
							protected void done() {
								try {
									boolean connected = get();
									if (!connected) {
										back.getActionListeners()[0].actionPerformed(new ActionEvent(
												back, ActionEvent.ACTION_PERFORMED, ""));
										JOptionPane.showMessageDialog(f,
												"Could not connect to the server.", "Join Server",
												JOptionPane.PLAIN_MESSAGE);
									}
								} catch (InterruptedException e) {
								} catch (ExecutionException e) {
									back.getActionListeners()[0].actionPerformed(new ActionEvent(
											back, ActionEvent.ACTION_PERFORMED, ""));
									if (e.getCause() instanceof ConnectException) {
										JOptionPane.showMessageDialog(f,
												"Server is not accepting any more connections"
												+ " or does not exist.", "Join Server",
												JOptionPane.PLAIN_MESSAGE);
									} else if (e.getCause() instanceof SocketTimeoutException) {
										JOptionPane.showMessageDialog(f,
												"Server connection timed out.", "Join Server",
												JOptionPane.PLAIN_MESSAGE);
									} else if (e.getCause() instanceof IllegalArgumentException) {
										JOptionPane.showMessageDialog(f, "Server does not exist.",
												"Join Server", JOptionPane.PLAIN_MESSAGE);
									}
								}
							}
							
						};
						worker2.execute();
						f.setContentPane(waitRoom);
						f.revalidate();
						f.repaint();
					} catch (NumberFormatException nfe) {
						JOptionPane.showMessageDialog(f, "Server does not exist.", "Join Server",
								JOptionPane.PLAIN_MESSAGE);
					}
				}
			}
		});

		viewInstructions.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				f.setContentPane(instructions);
				f.revalidate();
				f.repaint();
			}
		});

		back.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (worker != null) {
					worker.cancel(true);
					worker = null;
				}
				if (worker2 != null) {
					worker2.cancel(true);
					worker2 = null;
				}
				if (s != null) {
					s = null;
				}
				if (c != null) {
					c.disconnect();
					c = null;
				}
				name = null;
				names = null;
				serverInfo.setText("IP Address: Loading...\nPort Number: Loading...");
				l.setText("Waiting for players...");
				playerCount.setText("1/4");
				if (start.getParent() == waitRoom) {
					waitRoom.remove(start);
				}
				f.setContentPane(menu);
				f.revalidate();
				f.repaint();
			}
		});

		start.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int players = playerCount.getText().charAt(0) - '0';
				if (players >= 2) {
					c.sendMessage(DataObject.START, new Object[] {names});
				} else {
					JOptionPane.showMessageDialog(f, "Must have at least 2 players to start.",
							"Start Game", JOptionPane.PLAIN_MESSAGE);
				}
			}
			
		});
		
		changeName.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String newName = (String) JOptionPane.showInputDialog(f, "Enter new name:",
						"Name Change", JOptionPane.PLAIN_MESSAGE, null, null, name);
				if (newName == null) {
					return;
				}
				newName = newName.trim();
				if (newName.equals("")) {
					JOptionPane.showMessageDialog(f, "Name cannot be blank.",
							"Name Change", JOptionPane.PLAIN_MESSAGE);
				} else if (!newName.equals(name)) {
					for (String name : names) {
						if (newName.equals(name)) {
							JOptionPane.showMessageDialog(f, "Name is already in use.",
									"Name Change", JOptionPane.PLAIN_MESSAGE);
							return;
						}
					}
					c.sendMessage(DataObject.NAME_CHANGE, new Object[] {name, newName});
				}
			}
		});
		
		back2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				f.setContentPane(menu);
				f.revalidate();
				f.repaint();
			}
		});

	}
	
	@Override
	public void messageReceived(DataObject data) {
		if (data.messageType.equals(DataObject.HANDSHAKE)) {
			if (name == null) {
				name = (String) data.message[0];
			}
			if (names == null) {
				names = (ArrayList<String>) data.message[1];
			} else {
				names.add((String) data.message[0]);
			}
			playerCount.setText(names.size() + "/4");
			if (names.size() >= 2) {
				l.setText("Ready to start!");
			}
			if (data.message[2] != null) {
				serverInfo.setText("IP Address: " + data.message[2] + "\nPort Number: "
						+ data.message[3]);
			}
		} else if (data.messageType.equals(DataObject.INFORMATION)) {
			serverInfo.setText("IP Address: " + data.message[0] + "\nPort Number: "
					+ data.message[1]);
		} else if (data.messageType.equals(DataObject.START)) {
			game = new GamePanel(c, name, names, (Deck) data.message[0],
					(int) data.message[1], (Card) data.message[2], this);
			c.addListener(game);
			f.setVisible(false);
		} else if (data.messageType.equals(DataObject.NAME_CHANGE)) {
			if (name.equals((String) data.message[0])) {
				name = (String) data.message[1];
			}
			names.set(names.indexOf((String) data.message[0]), (String) data.message[1]);
		} else if (data.messageType.equals(DataObject.DISCONNECT) && game == null) {
			if (data.message.length == 2) {
				if (names != null) {
					names.remove((String) data.message[0]);
					playerCount.setText((int) data.message[1] + "/4");
					if (names.size() == 1) {
						l.setText("Waiting for players...");
					}
				}
			} else {
				back.getActionListeners()[0].actionPerformed(new ActionEvent(back,
						ActionEvent.ACTION_PERFORMED, ""));
				JOptionPane.showMessageDialog(f, "Host disconnected. The server has been closed.",
						"Disconnection", JOptionPane.PLAIN_MESSAGE);
			}
		}
	}
	
	/**
	 * Navigates back to the main menu from a game.
	 * 
	 * @param message optional message to display
	 */
	public void backToMenu(String message) {
		back.getActionListeners()[0].actionPerformed(new ActionEvent(back,
				ActionEvent.ACTION_PERFORMED, ""));
		game = null;
		f.setVisible(true);
		if (message != null) {
			JOptionPane.showMessageDialog(f, message);
		}
	}

	/**
	 * The main method to be run.
	 * 
	 * @param args none
	 */
	public static void main(String[] args) {
		new Main();
	}



}


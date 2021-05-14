import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.ConnectException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingWorker;

import Game.Deck;
import Game.GamePanel;
import Network.Client;
import Network.DataObject;
import Network.NetworkListener;
import Network.Server;

public class Main implements ActionListener, NetworkListener {

	public static final int GAME_SIZE = 750;
//	public static String password;
	private JFrame f;
	private GamePanel game;
	private JPanel menu, instructions, waitRoom;
	private JButton createServer, joinServer, viewInstructions, back, start;
	private JLabel l, playerCount;
	private JTextArea serverInfo, i;
	private Server s;
	private Client c;
	private String name;
	private ArrayList<String> names;
	private SwingWorker<String, Void> worker;

	public Main() throws IOException {
		//Server server = new Server();
		//server.run();
		
		f = new JFrame("Welcome Screen");
		createServer = new JButton("Create Server");
		joinServer = new JButton("Join Server");
		viewInstructions = new JButton("Instructions");
		back = new JButton("Back");
		start = new JButton("Start");
		menu = new JPanel();
		instructions = new JPanel();
		waitRoom = new JPanel();
		l = new JLabel("Waiting for players... ");
		playerCount = new JLabel("1/4");
		serverInfo = new JTextArea("IP Address: Loading...\nPort Number: Loading...");
		names = new ArrayList<String>();
		
		i = new JTextArea("Insane nines is a turn based game where 2 - 4 players play and draw"
				+ "\ncards until they run out of cards. Clicking on cards selects and then"
				+ "\nclicking on the played pile in the middle plays the card. Clicking on the"
				+ "\nother card pile will draw a card. You can play cards that have matching"
				+ "\nnumbers or suits with the last played card or you can play card 9 as a wild"
				+ "\ncard and choose the next suit.");
//		BufferedImage img = ImageIO.read(new File("Images/welcomebackground.png"));
//		f.setContentPane(new JLabel(new ImageIcon(img)));
		
//		f.setLayout(null);
		
		menu.setLayout(null);
		createServer.setBounds(40, 80, 400, 40);
		joinServer.setBounds(40, 140, 400, 40);
		viewInstructions.setBounds(40, 200, 400, 40);
		
		menu.add(createServer);
		menu.add(joinServer);
		menu.add(viewInstructions);
	
		menu.setBackground(Color.WHITE);
		f.add(menu);
		
		
		waitRoom.setLayout(null);
		back.setBounds(350, 300, 100, 40);
		start.setBounds(50, 300, 100, 40);
		l.setBounds(40, 60, 400, 40);
		serverInfo.setEditable(false);
		serverInfo.setBounds(40, 20, 400, 40);
		//update player label when players join
		playerCount.setBounds(150, 100, 100, 50);
		waitRoom.add(playerCount);
		waitRoom.add(back);
		waitRoom.add(l);
		waitRoom.add(serverInfo);
		waitRoom.setBackground(Color.WHITE);
			
		instructions.setLayout(null);
		i.setBounds(40, 100, 400, 200);
		instructions.add(i);
		instructions.add(back);
		instructions.setBackground(Color.WHITE);

		f.setResizable(false);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);
		f.setSize(500,400);


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
									c.sendMessage(DataObject.INFORMATION, get());
								} catch (InterruptedException e) {
								} catch (ExecutionException e) {
									e.printStackTrace();
								}
							}
							
						};
						worker.execute();
						waitRoom.add(start);
						f.setContentPane(waitRoom);
						f.invalidate();
						f.validate();
					}
				} catch (ConnectException e1) {
					e1.printStackTrace();
				}
			}
		});

		joinServer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JTextField ip = new JTextField();
				JTextField port = new JTextField();
				Object[] message = new Object[] {
						"IP Address: ", ip,
						"Port Number: ", port
				};
				int choice = JOptionPane.showConfirmDialog(null, message, "Join Server", JOptionPane.OK_CANCEL_OPTION);
				if (choice == JOptionPane.OK_OPTION) {
					try {
						c = new Client(ip.getText(), Integer.parseInt(port.getText()));
						c.addListener(Main.this);
						if (c.connect()) {
							f.setContentPane(waitRoom);
							f.invalidate();
							f.validate();
						}
						else {
							JOptionPane.showMessageDialog(null, "Server does not exist.");
						}
					} catch (NumberFormatException nfe) {
						JOptionPane.showMessageDialog(null, "Server does not exist.");
					} catch (ConnectException ce) {
						JOptionPane.showMessageDialog(null, "Server is full or does not exist.");
					}
				}
			}
		});

		viewInstructions.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				f.setContentPane(instructions);
				f.invalidate();
				f.validate();
			}
		});

		back.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (s != null) {
					s.close();
					s = null;
				}
				if (c != null) {
					c.disconnect();
					c = null;
				}
				if (worker != null) {
					worker.cancel(true);
				}
				f.setContentPane(menu);
				f.invalidate();
				f.validate();
			}
		});

		start.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int players = playerCount.getText().charAt(0) - '0';
				if (players >= 2) {
					c.sendMessage(DataObject.START, new Object[] {names});
				} else {
					JOptionPane.showMessageDialog(null, "Must have at least 2 players to start.");
				}
			}
			
		});

	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub

	}
	
	@Override
	public void messageReceived(DataObject data) {
		if (data.messageType.equals(DataObject.HANDSHAKE)) {
			if (name == null) {
				name = (String) data.message[0];
			}
			names.add((String) data.message[0]);
			playerCount.setText((int) data.message[1] + "/4");
			if (data.message[2] != null) {
				serverInfo.setText("IP Address: " + data.message[2] + "\nPort Number: " + data.message[3]);
			}
			System.out.println("poggers");
		} else if (data.messageType.equals(DataObject.INFORMATION)) {
			serverInfo.setText("IP Address: " + data.message[0] + "\nPort Number: " + data.message[1]);
		} else if (data.messageType.equals(DataObject.START)) {
			game = new GamePanel(c, name, (ArrayList<String>) data.message[0], (Deck) data.message[1]);
			c.addListener(game);
			f.setVisible(false);
		}
	}

	public static void main(String[] args) throws IOException {
		new Main();

	}



}


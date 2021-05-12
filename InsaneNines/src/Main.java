import java.awt.Color;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.SystemMenuBar;

import Game.Player;
import Network.Client;
import Network.DataObject;
import Network.NetworkListener;
import Network.Server;

import java.awt.event.*;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;

import javax.swing.*;

public class Main implements ActionListener, NetworkListener {

	public static final int GAME_SIZE = 750;
//	public static String password;
	private JFrame f;
	private JPanel menu, waitRoom, game;
	private JButton createServer, joinServer, tutorial, back;
	private JLabel l;
	private Server s;
	private Client c;
	private Player p;
	private ArrayList<Integer> ports;

	public Main() throws IOException {
		//Server server = new Server();
		//server.run();
		
		ports = new ArrayList<Integer>();
		
		f = new JFrame("Welcome Screen");
		createServer = new JButton("Create Server");
		joinServer = new JButton("Join Server");
		tutorial = new JButton("Tutorial");
		back = new JButton("Back");
		menu = new JPanel();
		waitRoom = new JPanel();
		game = new JPanel();
		l = new JLabel("Waiting for players... ");
		
//		BufferedImage img = ImageIO.read(new File("Images/welcomebackground.png"));
//		f.setContentPane(new JLabel(new ImageIcon(img)));
		
//		f.setLayout(null);
		
		menu.setLayout(null);
		createServer.setBounds(40, 80, 400, 40);
		joinServer.setBounds(40, 140, 400, 40);
		tutorial.setBounds(40, 200, 400, 40);
		
		menu.add(createServer);
		menu.add(joinServer);
		menu.add(tutorial);
	
		menu.setBackground(Color.WHITE);
		f.add(menu);
		
		
		waitRoom.setLayout(null);
		back.setBounds(350, 300, 100, 40);
		l.setBounds(40, 40, 400, 40);
		//update player label when players join
		waitRoom.add(back);
		waitRoom.add(l);
		waitRoom.setBackground(Color.WHITE);

		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);
		f.setSize(500,400);


		createServer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (s != null) {
					s.close();
				}
				String port = JOptionPane.showInputDialog("Enter new port");
				s = new Server(Integer.valueOf(port));
				new Thread(s).start();
				if (c != null && c.getHost() != InetAddress.getLoopbackAddress().getHostName()) {
					c.disconnect();
				}
				c = new Client(InetAddress.getLoopbackAddress().getHostAddress());
				c.setListener(Main.this);
				c.connect();
				f.setContentPane(waitRoom);
				f.invalidate();
				f.validate();
			}
		});

		joinServer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String ipAddress = JOptionPane.showInputDialog("Enter ip address");
				
				int port = Integer.parseInt(ipAddress);
				c = new Client(InetAddress.getLoopbackAddress().getHostAddress(), port);
				
				if(!ports.contains(ipAddress)) {
					System.out.println("Create a new Server");
				}
				else {
					c.setListener(Main.this);
					c.connect();
				
					JLabel playerCount = new JLabel(String.valueOf(s.count));
					playerCount.setBounds(150, 100, 100, 50);
					waitRoom.add(playerCount);
				
					//join room that matches ip address
					f.setContentPane(waitRoom);
					f.invalidate();
					f.validate();
				}
			}
		});

		tutorial.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});

		back.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				f.setContentPane(menu);
				f.invalidate();
				f.validate();
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
			System.out.println("poggers");
		}
	}

	public static void main(String[] args) throws IOException {
		new Main();

	}



}


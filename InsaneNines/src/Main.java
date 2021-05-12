import java.awt.Color;
import javax.swing.JFrame;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

public class Main implements ActionListener{

	public static final int GAME_SIZE = 750;
//	public static String password;
	private JFrame f;
	private JPanel menu, waitRoom, game, instructionsTab;
	private JButton createServer, joinServer, instructions, back;
	private JLabel players;

	public Main() throws IOException {
		//Server server = new Server();
		//server.run();
		
		f = new JFrame("Welcome Screen");
		createServer = new JButton("Create Server");
		joinServer = new JButton("Join Server");
		instructions = new JButton("instructions");
		back = new JButton("Back");
		menu = new JPanel();
		waitRoom = new JPanel();
		game = new JPanel();
		instructionsTab = new JPanel();
		players = new JLabel("Waiting for players... ");
		

		menu.setLayout(null);
		createServer.setBounds(40, 80, 400, 40);
		joinServer.setBounds(40, 140, 400, 40);
		instructions.setBounds(40, 200, 400, 40);
		
		menu.add(createServer);
		menu.add(joinServer);
		menu.add(instructions);
	
		menu.setBackground(Color.WHITE);
		f.add(menu);
		
		
		waitRoom.setLayout(null);
		back.setBounds(350, 300, 100, 40);
		players.setBounds(40, 40, 400, 40);
		//update player count when players join
		waitRoom.add(back);
		waitRoom.add(players);
		waitRoom.setBackground(Color.WHITE);
		
		instructionsTab.setLayout(null);
		//add instructions text
		instructionsTab.setBackground(Color.WHITE);

		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);
		f.setSize(500,400);


		createServer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				f.setContentPane(waitRoom);
				f.invalidate();
				f.validate();
			}
		});

		joinServer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String ipAddress = JOptionPane.showInputDialog("Enter ip address");
				//join room that matches ip address
				f.setContentPane(waitRoom);
				f.invalidate();
				f.validate();
			}
		});

		instructions.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				f.setContentPane(instructionsTab);
				f.invalidate();
				f.validate();
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

	public static void main(String[] args) throws IOException {
		new Main();

	}
}


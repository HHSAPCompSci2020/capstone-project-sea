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

	private JFrame f;
	private JPanel menu, waitRoom, game;
	private JButton createServer, joinServer, tutorial, back;
	private JLabel l;

	public Main() throws IOException {
		//Server server = new Server();
		//server.run();
		
		f = new JFrame("Welcome Screen");
		createServer = new JButton("Create Server");
		joinServer = new JButton("Join Server");
		tutorial = new JButton("Tutorial");
		back = new JButton("Back");
		menu = new JPanel();
		waitRoom = new JPanel();
		game = new JPanel();
		l = new JLabel("Waiting for players... Player 1 (you)");
		
		BufferedImage img = ImageIO.read(new File("Images/welcomebackground.png"));
		f.setContentPane(new JLabel(new ImageIcon(img)));
		
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
		l.setBounds(40, 200, 400, 40);
		//update player count in the label later
		waitRoom.add(back);
		waitRoom.add(l);
		waitRoom.setBackground(Color.WHITE);

		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);
		f.setSize(800,600);


		createServer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				f.setContentPane(waitRoom);
				f.invalidate();
				f.validate();
			}
		});

		joinServer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
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

	public static void main(String[] args) throws IOException {
		new Main();

	}



}


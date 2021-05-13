package Game;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class GamePanel extends JFrame implements MouseListener, MouseMotionListener{
	
	private static final Color BACKGROUND_COLOR = Color.GREEN;
    ArrayList<Player> hands;
    
   public GamePanel(int players){
	   
	   JFrame game = new JFrame();
	   game.setResizable(false);
	   game.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	   game.setVisible(true);
	   game.setSize(500,400);
	   
	   hands = new ArrayList<Player>();
	   
//	   for(int i = 0; i < )
	   
	   
	   
	   
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
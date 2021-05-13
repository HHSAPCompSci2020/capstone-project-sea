package Game;

import java.awt.Component;
import java.awt.Graphics;

import javax.swing.ImageIcon;

public class Card {
	
	private ImageIcon image;
	private String rank;
	private String suit;
	private int x;
	private int y;
	
	
	
	public Card(ImageIcon image, String rank, String suit){
		this.rank = rank;
		this.suit = suit;
		this.image = image;
	}
	
	public Card(String rank, String suit) {
		this.rank = rank;
		this.suit = suit;
	}
	
	public void draw(Graphics g, Component c) {
		image.paintIcon(c, g, x, y);
	}
	
	public void moveTo(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public boolean isNine() {
		return rank.equals("NINE");
	}
	
	public String getRank() {
		return rank;
	}
	
	public String getSuit() {
		return suit;
	}
	
	public static boolean isValid(Card a, Card b) {
		return a.rank.equals(b.rank) || a.suit.equals(b.suit) || a.isNine();
	}
}


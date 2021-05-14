package Game;

import java.awt.Component;
import java.awt.Graphics;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class Card implements Serializable, Comparable<Card> {
	private static final long serialVersionUID = -3522929388750816698L;
	private ImageIcon image;
	private String rank;
	private String suit;
	private int x;
	private int y;
	
	public Card(String rank, String suit) {
		this.rank = rank;
		this.suit = suit;
		try {
			String[] ranks = {"ACE", "TWO", "THREE", "FOUR", "FIVE", "SIX", "SEVEN", "EIGHT", "NINE", "TEN", "JACK", "QUEEN", "KING"};
			int i = Arrays.asList(ranks).indexOf(rank);
			char c;
			if (i >= 2 && i <= 9) {
				c = (char) (i + '0');
			} else {
				c = Character.toLowerCase(ranks[i].charAt(0));
			}
			image = new ImageIcon(ImageIO.read(new File("cards" + File.separator + c + Character.toLowerCase(suit.charAt(0)) + ".gif")));
		} catch (IOException e) {
			e.printStackTrace();
		}
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
	
	public ImageIcon getImage() {
		return image;
	}

	public boolean canPlay(Card top) {
		return rank.equals(top.rank) || suit.equals(top.suit) || isNine();
	}

	@Override
	public int compareTo(Card o) {
		if (suit.equals(o.suit)) {
			String[] ranks = {"ACE", "TWO", "THREE", "FOUR", "FIVE", "SIX", "SEVEN", "EIGHT", "NINE", "TEN", "JACK", "QUEEN", "KING"};
			List<String> ranks2 = Arrays.asList(ranks);
			return Integer.compare(ranks2.indexOf(rank), ranks2.indexOf(o.rank));
		}
		return suit.compareTo(o.suit);
	}
}


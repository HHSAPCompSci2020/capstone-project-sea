package Game;

import java.awt.Image;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import javax.swing.ImageIcon;

public class Card implements Serializable, Comparable<Card> {
	private static final long serialVersionUID = -3522929388750816698L;
	private ImageIcon image;
	private String rank;
	private String suit;
	
	public Card(String rank, String suit) {
		this.rank = rank;
		this.suit = suit;
		image = null;
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
	
	public void createImage() {
		String[] ranks = {"ACE", "TWO", "THREE", "FOUR", "FIVE", "SIX", "SEVEN", "EIGHT", "NINE", "TEN", "JACK", "QUEEN", "KING"};
		int i = Arrays.asList(ranks).indexOf(rank);
		String s;
		if (i + 1 >= 2 && i + 1 <= 10) {
			s = String.valueOf(i + 1);
		} else {
			s = String.valueOf(ranks[i].charAt(0));
		}
		image = new ImageIcon(new ImageIcon(getClass().getResource("/PNG/" + s + suit.charAt(0) + ".png")).getImage()
				.getScaledInstance(75, 105, Image.SCALE_DEFAULT));
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


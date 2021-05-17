package game;

import java.awt.Image;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import javax.swing.ImageIcon;

/**
 * Represents a card with a rank and suit.
 * 
 * @author Andy Ding
 * @author Eric Chang
 * @author Samuel Zhou
 */
public class Card implements Serializable, Comparable<Card> {
	private static final long serialVersionUID = -3522929388750816698L;
	private ImageIcon image;
	private String rank;
	private String suit;
	
	/**
	 * Creates a card with a rank and suit.
	 * 
	 * @param rank this card's rank
	 * @param suit this card's suit
	 */
	public Card(String rank, String suit) {
		this.rank = rank;
		this.suit = suit;
		image = null;
	}
	
	/**
	 * @return true if this card's rank is "NINE" or false otherwise
	 */
	public boolean isNine() {
		return rank.equals("NINE");
	}
	
	/**
	 * @return this card's rank
	 */
	public String getRank() {
		return rank;
	}
	
	/**
	 * @return this card's suit
	 */
	public String getSuit() {
		return suit;
	}
	
	/**
	 * @return this card's image
	 */
	public ImageIcon getImage() {
		return image;
	}
	
	/**
	 * Retrieves and stores this card's image
	 */
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

	/**
	 * @param top the current top card of the deck
	 * @return true if this card can be played on top of the current top card or false otherwise
	 */
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


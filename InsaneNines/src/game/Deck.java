package game;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Represents a deck of cards.
 * 
 * @author Andy Ding
 * @author Eric Chang
 * @author Samuel Zhou
 */
public class Deck implements Serializable {
	private static final long serialVersionUID = -5914402136758018594L;
	private ArrayList<Card> cards;
	
	/**
	 * Creates an empty deck.
	 */
	public Deck() {
		cards = new ArrayList<Card>();
	}
	
	/**
	 * @return the underlying ArrayList containing this deck's cards
	 */
	public ArrayList<Card> getDeck(){
		return cards;
	}
	
	/**
	 * Adds a card to the top of this deck.
	 * 
	 * @param card the card to add
	 */
	public void addCard(Card card) {
		cards.add(card);
	}
	
	/**
	 * Finds a card in this deck and returns its index.
	 * 
	 * @param card the card to find in this deck
	 * @return the card's index
	 */
	public int indexOf(Card card) {
		return cards.indexOf(card);
	}
	
	/**
	 * Returns this deck's top card
	 * 
	 * @return this deck's top card
	 */
	public Card getTop() {
		return cards.get(cards.size()-1);
	}
	
	/**
	 * Removes and returns this deck's top card
	 * 
	 * @return this deck's removed top card
	 */
	public Card removeTop() {
		return cards.remove(cards.size()-1);
	}
	
	/**
	 * Randomly rearranges the order of the cards in this deck.
	 */
	public void shuffle() {
		Collections.shuffle(cards);
	}
	
	/**
	 * Sorts this deck in ascending order by suit and then rank.
	 */
	public void sort() {
		Collections.sort(cards);
	}
}

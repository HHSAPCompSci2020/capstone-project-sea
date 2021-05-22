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
	 * @param index the index of the card
	 * @return the card with the specified index
	 */
	public Card getCard(int index) {
		return cards.get(index);
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
	 * @return this deck's top card
	 */
	public Card getTop() {
		return cards.get(cards.size()-1);
	}
	
	/**
	 * @return this deck's top card after removing it
	 */
	public Card removeTop() {
		return cards.remove(cards.size()-1);
	}
	
	/**
	 * Rearranges the order of the cards in this deck.
	 */
	public void shuffle() {
		Collections.shuffle(cards);
	}
}

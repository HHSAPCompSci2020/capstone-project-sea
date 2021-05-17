package game;

/**
 * 
 * @author Andy Ding
 * @author Eric Chang
 * @author Samuel Zhou
 */
public class Player {
	private String name;
	private int numCards;
	private Deck hand;
	
	/**
	 * Creates a player with a name and starting hand.
	 * 
	 * @param name this player's name
	 * @param hand this player's starting hand
	 */
	public Player(String name, Deck hand) {
		this.name = name;
		numCards = hand.getDeck().size();
		this.hand = hand;
	}
	
	/**
	 * Creates a player with a name and the amount of cards.
	 * 
	 * @param name this player's name
	 * @param numCards this player's amount of cards
	 */
	public Player(String name, int numCards) {
		this.name = name;
		this.numCards = numCards;
		hand = null;
	}
	
	/**
	 * @return this player's name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @param name this player's name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return this player's amount of cards
	 */
	public int getNumCards() {
		return numCards;
	}

	/**
	 * @param numCards this player's amount of cards
	 */
	public void setNumCards(int numCards) {
		this.numCards = numCards;
	}
	
	/**
	 * @return this player's deck
	 */
	public Deck getHand() {
		return hand;
	}
	
	/**
	 * Plays a card by removing it from the deck.
	 * 
	 * @param card the card to be played
	 * @return the new amount of cards
	 */
	public int play(Card card) {
		hand.getDeck().remove(card);
		return hand.getDeck().size();
	}
	
	/**
	 * Draws a card by adding it to the end of the deck.
	 * @param card the card to be drawn
	 * @return the new amount of cards
	 */
	public int draw(Card card) {
		hand.addCard(card);
		return hand.getDeck().size();
	}
	
}

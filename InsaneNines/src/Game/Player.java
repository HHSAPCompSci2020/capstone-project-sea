package Game;

public class Player {
	private String name;
	private int numCards;
	private Deck hand;
	
	public Player(String name, Deck hand) {
		this.name = name;
		numCards = hand.getDeck().size();
		this.hand = hand;
	}
	
	public Player(String name, int numCards) {
		this.name = name;
		this.numCards = numCards;
		hand = null;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public int getNumCards() {
		return numCards;
	}

	public void setNumCards(int numCards) {
		this.numCards = numCards;
	}
	
	public Deck getHand() {
		return hand;
	}
	
	public int play(Card card) {
		hand.getDeck().remove(card);
		return hand.getDeck().size();
	}
	
	public void draw(Card card) {
		hand.addCard(card);
	}
	
}

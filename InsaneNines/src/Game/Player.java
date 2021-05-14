package Game;
import javax.swing.JFrame;

public class Player extends JFrame{
	private String name;
	private int numCards;
	private Deck hand;
	
	//draws your hand 
	public void setUpGuiDeck() {
		
	}
	
	public Player(String name, Deck deck) {
		this.name = name;
		numCards = deck.getDeck().size();
		hand = deck;
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
	
	public void draw(Card card) {
		hand.addCard(card);
	}
	
}

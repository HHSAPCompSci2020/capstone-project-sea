import java.util.ArrayList;

public class Deck{
	private ArrayList<Card> cards;
	
	public Deck() {
		cards = new ArrayList<Card>();
	}
	
	public ArrayList<Card> getDeck(){
		return cards;
	}
	
	public Card getCard(int index) {
		return cards.get(index);
	}
	
	public void addCard(Card card) {
		cards.add(card);
	}
	
	
	
	
}

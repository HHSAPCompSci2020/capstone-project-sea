import java.util.ArrayList;
import java.util.Collections;

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
	
	public Card getTop() {
		return cards.get(cards.size()-1);
	}
	
	public Card removeTop() {
		return cards.remove(cards.size()-1);
	}
	
	public void shuffle() {
		Collections.shuffle(cards);
	}
}

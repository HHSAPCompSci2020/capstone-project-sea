package Game;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

public class Deck implements Serializable {
	private static final long serialVersionUID = -5914402136758018594L;
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
		if(cards.size() > 1) {
			return cards.remove(cards.size()-1);
		}
		return null;
	}
	
	public void shuffle() {
		Collections.shuffle(cards);
	}
}

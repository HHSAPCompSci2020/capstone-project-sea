package Game;
import java.util.ArrayList;
import java.util.Collections;

public class Deck{
	private ArrayList<Card> cards;
	
	public Deck() {
		cards = new ArrayList<Card>();
		String[] ranks = {"ACE", "TWO", "THREE", "FOUR", "FIVE", "SIX", "SEVEN", "EIGHT", "NINE", "TEN", "JACK", "QUEEN", "KING"};
		String[] suits = {"CLUBS", "DIAMONDS", "HEARTS", "SPADES"};
		for (String rank : ranks) {
			for (String suit : suits) {
				cards.add(new Card(rank, suit));
			}
		}
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

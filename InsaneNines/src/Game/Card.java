package Game;

public class Card {
	private String rank;
	private String suit;
	
	public Card(String rank, String suit){
		this.rank = rank;
		this.suit = suit;
	}
	
	public boolean isNine() {
		return rank.equals("NINE");
	}
	
	public String getRank() {
		return rank;
	}
	
	public String getSuit() {
		return suit;
	}
	
	public static boolean isValid(Card a, Card b) {
		return a.rank.equals(b.rank) || a.suit.equals(b.suit) || a.isNine();
	}
	
}

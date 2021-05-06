
public class Card {
	private String rank;
	private String suit;
	
	Card(String rank, String suit){
		this.rank = rank;
		this.suit = suit;
	}
	
	public boolean isNine() {
		if(rank == "NINE") return true;
		return false;
	}
	
	public String getRank() {
		return rank;
	}
	
	public String getSuit() {
		return suit;
	}
	
	
	
}

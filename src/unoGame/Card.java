package unoGame;

public class Card {

	public String color;
	public String value;
	public boolean isActionCard;
	public String prevColor;
	
	public Card(String color , String value , boolean isActionCard) {
		
		this.color = color;
		this.value = value;
		this.isActionCard = isActionCard;
	}

	
	@Override
	public String toString() {
		
		return color + " " + value;
	}
	
}
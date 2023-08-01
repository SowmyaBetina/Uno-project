package uno;

public class NormalCard extends Card {
	
	private int value;

	public NormalCard(String color, int value) {
		
		super(color);
		this.value = value;
	}
	
	public int getValue() {
		
		return value;
	}
	
	@Override
	
	public String toString() {
		
		return super.toString() + " " + value;
	}

}
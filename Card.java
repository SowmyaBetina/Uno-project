package uno;

import java.util.*;

public class Card {
	
	private String color;
	
	public Card(String color) {
		
		this.color = color;
		
	}
	
	public String getColor() {
		
		return color;
	}
	
	@Override
	
	public String toString() {
		
		return color;
	}

}
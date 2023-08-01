package uno;

import java.util.*;

public class Deck {
	
	private List<Card> unoCards;
	private String[] colors = {"red" , "green" , "blue" , "yellow"};
	private String[] actions = {"skip" , "drawTwo" , "reverse"};
	private static final int TOP = 0;
	
	public Deck(List<Card> unoCards) {
		
		unoCards = new ArrayList<>();
	}
	
	public void initialize() {
		
		for(String color : colors) {
			
			for(int number=0;number<10;number++) {
				
				NormalCard normalCard = new NormalCard(color,number);
				unoCards.add(normalCard);
			}
		}
		
		for(String color : colors) {
			
			for(String action : actions) {
				
				ActionCard actionCard = new ActionCard(color,action);
				unoCards.add(actionCard);
			}
		}
		
		for(int num=0;num<4;num++) {
			
			unoCards.add(new ActionCard("wild" , "wildDrawFour"));
			unoCards.add(new ActionCard("wild" , "wildColorCard"));
		}
	}
	

	public void suffleDeck() {
		
		Collections.shuffle(unoCards);
	}
	
	public boolean isDeckEmpty() {
		
		if(unoCards.isEmpty())return true;
		
		return false;
	}
	
	public Card throwCard() {
		
		return unoCards.get(TOP);
	}
	
	public Card drawCard() {
		
		if(!unoCards.isEmpty()) {
			
			return unoCards.remove(TOP);
		}
		
		return null;
	}
}
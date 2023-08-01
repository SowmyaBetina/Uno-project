package uno;

public class ActionCard extends Card{
	
	private String action;
	
	public ActionCard(String color , String action) {
		
		super(color);
		this.action = action;
	}

	public boolean isActionCard(Card card) {
		
		ActionCard actionCard = new ActionCard("","");
		
		if(card == actionCard)return true;
		return false;
	}
	
	public String getAction() {
		
		return action;
	}
	
	public void drawTwo() {
		
		for(int i=0;i<2;i++) {
			
			Player.pickCardFromDeck();
		}
	}
	
	public void skip() {
		
		for(int i=0;i<2;i++) {
			
			Player.nextTurn();
		}
	}
	
	public void reverse() {
		
		isClockWise = false;
	}
	
	public void wildColorCard() {
		
		return Player.chooseColor();
		
	}
	
	public String wildDrawFour() {
		
		for(int i=0;i<4;i++) {
			
			Player.pickCardFromDeck();
		}
		
		return Player.chooseColor();
	}
	
	@Override
	
	public String toString() {
		
		return action;
	}

}
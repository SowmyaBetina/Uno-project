package uno;

import java.util.*;

public class Player {
    public String name;
    public int id;
    public int turn;
    public List <Card> cardsInHand;
    
    public Player(String name , int id , int turn , List<Card> cardsInHand) {
    	
    	this.name = name;
    	this.id = id;
    	this.turn = turn;
    	this.cardsInHand = cardsInHand;
    	
    }

    Deck deck = new Deck(null);
    Card prevCard;

    public String[] names = {"Pawan", "Vijay", "Pallavi", "Shilpa", "Priyanka",
            "Ram", "Deepika", "Dev"} ;

     Random rand = new Random();

    public void play()
    {
    	
    	ActionCard actionCard = new ActionCard("" , "");
        if(actionCard.isActionCard(prevCard))
        {
            switch(actionCard.getAction())
            {
                case "drawTwo": actionCard.drawTwo();
                case "skip" : actionCard.skip();
                case "wildColorCard": actionCard.wildColorCard();
                case "wildDrawFour" : actionCard.wildDrawFour();
            }
        }
        else {
            if(isCardPresent() != null)
            {
                prevCard = isCardPresent();
                cardsInHand.remove(prevCard);
            }

            else {
                pickCardFromDeck();
                Card temp = isCardPresent();
                if(temp != null)
                {
                    prevCard = temp;
                    cardsInHand.remove(temp);
                }
            }
            if(ifUno())
            {
                System.out.println("uno");
            }
            if(ifWon())
            {
                System.out.println("I won");
            }
            turn++;
            nextTurn(id);
        }
    }

    public  int nextTurn(int id) {

        if(ActionCard.isClockWise == true)
            return((id+1)%3);

        else {
            if(id==0) {
                id=3;
                return(id);
            }

            else
                return(id-1);
        }
    }

    public  void pickCardFromDeck() {
        Card c = deck.drawCard();
        cardsInHand.add(c);
    }

    public Card isCardPresent() {
        for(int i = 0; i<cardsInHand.size(); i++)
        {
            if(cardMatches((NormalCard) cardsInHand.get(i), (NormalCard) prevCard))
            {
                return(cardsInHand.get(i));
            }
        }
        return null;
    }

    public boolean cardMatches(NormalCard c1, NormalCard c2)
    {
        return c1.getColor() == c2.getColor() || c1.getValue() == c2.getValue()    ;
    }

    public boolean ifUno()
    {
        return cardsInHand.size()==1;
    }

    public boolean ifWon()
    {
        return cardsInHand.size()==0;
    }
    public int chooseColor()
    {
        return(rand.nextInt(0,4));
    }
    
    
}
package unoGame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {

    public List<Card> unoCards;
    public String[] colors = {"red", "blue", "yellow", "green"};
    public String[] actions = {"skip", "reverse", "plusTwo"};

    public static final int TOP = 0;

    public Deck() {

        unoCards = new ArrayList<>();
    }

    public void initialize() {

        for (String color : colors) {

            for (int i = 0; i < 10; i++) {

                unoCards.add(new Card(color, String.valueOf(i), false));
            }
        }

        for (String color : colors) {

            for (String action : actions) {

                unoCards.add(new Card(color, action, true));
            }
        }

        for (int i = 0; i < 4; i++) {

            unoCards.add(new Card("wild", "plusFour", true));

        }
    }

    public void shuffleDeck() {

        Collections.shuffle(unoCards);
    }

    public Card pickFromDeck() {

        return unoCards.remove(TOP);
    }

    @Override
    public String toString() {
        return "";
    }
}
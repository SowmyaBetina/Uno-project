//package demo;
package uno;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.List;

 

public class newimp extends Application {
    private List<String> cardColors = List.of("Red", "Blue", "Green", "Yellow");
    private List<String> specialCards = List.of("Skip", "Reverse", "Draw 2");
    private List<String> numberCards = List.of("0", "1", "2", "3", "4", "5", "6", "7", "8", "9");

 

    private List<String> unoCards;

 

    public static void main(String[] args) {
        launch(args);
    }

 

    @Override
    public void start(Stage primaryStage) {
        unoCards = new ArrayList<String>();
        initializeDeck();

 

        primaryStage.setTitle("Uno Deck");

 

        FlowPane deckPane = new FlowPane(10, 10);
        for (String card : unoCards) {
            deckPane.getChildren().add(createUnoCard(card));
        }

 

        Scene scene = new Scene(deckPane, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

 

    private void initializeDeck() {
        for (String color : cardColors) {
            for (String number : numberCards) {
                unoCards.add(color + " " + number);
            }
            for (String special : specialCards) {
                unoCards.add(color + " " + special);
            }
        }
        for(int i=0;i<4;i++) {
        // Adding wild cards and wild draw 4 cards
        unoCards.add("Wild");
        unoCards.add("Wild Draw 4");
        }
    }

 

    private StackPane createUnoCard(String card) {
        Rectangle cardShape = new Rectangle(100, 150);
        cardShape.setFill(Color.WHITE);
        cardShape.setStroke(Color.BLACK);

 

        Label cardLabel = new Label(card);
        cardLabel.setStyle("-fx-font-size: 16px;");

 

        StackPane cardPane = new StackPane(cardShape, cardLabel);
        return cardPane;
    }
}
package unoGame;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.sql.Statement;


public class Game {
    public int noOfPlayers;
    public List<List<Card>> totalPlayers;
    public Deck deck;
    public Card prevCard;
    public boolean isClockWise = true;
    public int currentPlayerIndex;

    public Game(int noOfPlayers, Deck deck) {
        this.noOfPlayers = noOfPlayers;
        this.totalPlayers = new ArrayList<>();
        this.deck = deck;
        this.prevCard = deck.pickFromDeck();
        this.currentPlayerIndex = 0;
    }

    public void distributeCards(int noOfPlayers) {
        for (int i = 0; i < noOfPlayers; i++) {
            List<Card> cardsInHand = new ArrayList<>();

            for (int j = 0; j < 7; j++) {
                cardsInHand.add(deck.pickFromDeck());
            }

            totalPlayers.add(cardsInHand);
        }
    }

    public boolean hasPlayerWon(int playerId) {
        return totalPlayers.get(playerId).isEmpty();
    }

    public int getNextPlayer(int currentPlayerIndex) {
        if (isClockWise) {
            return (currentPlayerIndex + 1) % noOfPlayers;
        } else {
            return (currentPlayerIndex - 1 + noOfPlayers) % noOfPlayers;
        }
    }

    
    public String askPlayerForColor() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Choose a color for the wild card (red, blue, yellow, green): ");
        String chosenColor = scanner.nextLine().toLowerCase();

        while (!isValidColor(chosenColor)) {
            System.out.println("Invalid color. Choose a color for the wild card (red, blue, yellow, green): ");
            chosenColor = scanner.nextLine().toLowerCase();
        }

        return chosenColor;
    }

    public boolean isValidColor(String color) {
        return color.equals("red") || color.equals("blue") || color.equals("yellow") || color.equals("green");
    }
    
    
    public Card matchReturn(Card prevCard, int currentPlayerIndex) {
        List<Card> player = totalPlayers.get(currentPlayerIndex);

        boolean played = false;
        List<Card> cardsToRemove = new ArrayList<>(); 

        for (int i = 0; i < player.size(); i++) {
            Card card = player.get(i);

            if (prevCard.isActionCard) {
                switch (prevCard.value) {
                
                case "skip":
                    if (card.value.equals("skip")) {
                        prevCard = card;
                        currentPlayerIndex = getNextPlayer(currentPlayerIndex);
                        player.remove(i);

                        played = true;
                    }
                    break;

                case "reverse":
                    if (card.value.equals("reverse")) {
                        prevCard = card;
                        isClockWise = !isClockWise; 
                        currentPlayerIndex = getNextPlayer(currentPlayerIndex);
                        player.remove(i);

                        played = true;
                    }
                    break;
                
                case "plusTwo":
                    if (card.value.equals("plusTwo")) {
                        prevCard = card;
                        currentPlayerIndex = getNextPlayer(currentPlayerIndex);
                        player.remove(i);

                        int nextPlayerIndex = getNextPlayer(currentPlayerIndex);
                        totalPlayers.get(nextPlayerIndex).add(deck.pickFromDeck());
                        totalPlayers.get(nextPlayerIndex).add(deck.pickFromDeck());

                        played = true;
                    }
                    break;

                    case "plusFour":
                        if (card.value.equals("plusFour")) {
                            prevCard = card;
                            currentPlayerIndex = getNextPlayer(currentPlayerIndex);
                            player.remove(i);

                            int nextPlayerIndex = getNextPlayer(currentPlayerIndex);
                            for (int j = 0; j < 4; j++) {
                                totalPlayers.get(nextPlayerIndex).add(deck.pickFromDeck());
                            }

                            played = true;
                        }
                        break;

                    default:
                        break;
                }
            } else if (card.isActionCard && (card.value.equals("wild") || card.value.equals("plusFour"))) {
                cardsToRemove.add(card);
            } else if (card.isActionCard && card.value.equals("changeColor")) {
                cardsToRemove.add(card);
                prevCard.color = chooseNewColor();
            } else if (card.color.equals(prevCard.color) || card.value.equals(prevCard.value)) {
                cardsToRemove.add(card);
            }
        }

        player.removeAll(cardsToRemove);

        if (!played) {
            player.add(deck.pickFromDeck());
            currentPlayerIndex = getNextPlayer(currentPlayerIndex);
        }

        if (prevCard.value.equals("plusFour")) {
            int nextPlayerIndex = getNextPlayer(currentPlayerIndex);
            boolean nextPlayerHasPlusFour = false;

            for (Card card : totalPlayers.get(nextPlayerIndex)) {
                if (card.value.equals("plusFour")) {
                    nextPlayerHasPlusFour = true;
                    break;
                }
            }

            if (!nextPlayerHasPlusFour) {
                for (int j = 0; j < 4; j++) {
                    player.add(deck.pickFromDeck());
                }

                prevCard.color = chooseNewColor();

                currentPlayerIndex = getNextPlayer(currentPlayerIndex);
            }
        }
        
        if (prevCard.value.equals("plusTwo")) {
            int nextPlayerIndex = getNextPlayer(currentPlayerIndex);
            boolean nextPlayerHasPlusTwo = false;

            for (Card card : totalPlayers.get(nextPlayerIndex)) {
                if (card.value.equals("plusTwo")) {
                    nextPlayerHasPlusTwo = true;
                    break;
                }
            }

            if (!nextPlayerHasPlusTwo) {
                
                for (int j = 0; j < 2; j++) {
                    player.add(deck.pickFromDeck());
                }
                
                prevCard.color = prevCard.prevColor;
                currentPlayerIndex = getNextPlayer(currentPlayerIndex);
            }
        }

        return prevCard;
    }

        private String chooseNewColor() {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Choose a color for the wild card (red, blue, yellow, green): ");
            String chosenColor = scanner.nextLine().toLowerCase();

            while (!isValidColor(chosenColor)) {
                System.out.println("Invalid color. Choose a color for the wild card (red, blue, yellow, green): ");
                chosenColor = scanner.nextLine().toLowerCase();
            }

            return chosenColor;
        }
    


    public void playComputerTurn() {
        Card computerPlay = null;

        List<Card> computerHand = totalPlayers.get(currentPlayerIndex);

        for (Card card : computerHand) {
            if (card.value.equals(prevCard.value) || card.color.equals(prevCard.color)) {
                computerPlay = card;
                computerHand.remove(card);
                break;
            }
        }

        if (computerPlay == null) {
            computerHand.add(deck.pickFromDeck());
        }else if (computerPlay.color.equals("wild")) {
         
            String[] colors = {"red", "blue", "yellow", "green"};
            computerPlay.color = colors[new Random().nextInt(colors.length)];
            prevCard = computerPlay;
            currentPlayerIndex = getNextPlayer(currentPlayerIndex);
        } else {
            prevCard = computerPlay;
            currentPlayerIndex = getNextPlayer(currentPlayerIndex);
        }
    }
    
    public boolean canPlayCard(Card card) {
        return card.color.equals(prevCard.color) || card.value.equals(prevCard.value) || card.color.equals("wild");
    }
    public void playComputerTurn1() {
        List<Card> computerHand = totalPlayers.get(currentPlayerIndex);
        Card computerPlay = null;

        for (Card card : computerHand) {
            if (canPlayCard(card)) {
                computerPlay = card;
                computerHand.remove(card);
                break;
            }
        }

        if (computerPlay == null) {
            computerHand.add(deck.pickFromDeck());
        } else if (computerPlay.color.equals("wild")) {
            computerPlay.color = chooseRandomColor();
            prevCard = computerPlay;
            currentPlayerIndex = getNextPlayer(currentPlayerIndex);
        } else {
            prevCard = computerPlay;
            currentPlayerIndex = getNextPlayer(currentPlayerIndex);
        }
    }
    public String askPlayerForColor1() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Choose a color for the wild card (red, blue, yellow, green): ");
        String chosenColor = scanner.nextLine().toLowerCase();

        while (!isValidColor(chosenColor)) {
            System.out.println("Invalid color. Choose a color for the wild card (red, blue, yellow, green): ");
            chosenColor = scanner.nextLine().toLowerCase();
        }

        return chosenColor;
    }
    private String chooseRandomColor() {
        String[] colors = {"red", "blue", "yellow", "green"};
        return colors[new Random().nextInt(colors.length)];
    }

    public void saveGameState(Connection connection) throws SQLException {
        String insertOrUpdateQuery = "INSERT INTO game_state (player_index, prev_card_color, prev_card_value, current_player_index) " +
                                     "VALUES (?, ?, ?, ?) ON DUPLICATE KEY UPDATE " +
                                     "prev_card_color = VALUES(prev_card_color), prev_card_value = VALUES(prev_card_value), " +
                                     "current_player_index = VALUES(current_player_index)";
        
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertOrUpdateQuery)) {
            preparedStatement.setInt(1, currentPlayerIndex);
            preparedStatement.setString(2, prevCard.color);
            preparedStatement.setString(3, prevCard.value);
            preparedStatement.setInt(4, currentPlayerIndex);
            
            preparedStatement.executeUpdate();
        }
    }
    public void loadGameState(Connection connection) throws SQLException {
        String selectQuery = "SELECT player_index, prev_card_color, prev_card_value, current_player_index " +
                             "FROM game_state LIMIT 1";  // Assuming only one row in the table

        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(selectQuery);

            if (resultSet.next()) {
                int loadedPlayerIndex = resultSet.getInt("player_index");
                String loadedPrevCardColor = resultSet.getString("prev_card_color");
                String loadedPrevCardValue = resultSet.getString("prev_card_value");
                int loadedCurrentPlayerIndex = resultSet.getInt("current_player_index");

                // Update the game state variables
                currentPlayerIndex = loadedCurrentPlayerIndex;
                prevCard.color = loadedPrevCardColor;
                prevCard.value = loadedPrevCardValue;

                System.out.println("Game state loaded successfully.");
            } else {
                System.out.println("No saved game state found.");
            }
        }
    }


}
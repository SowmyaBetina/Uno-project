package unoGame;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.sql.Statement;


public class PlayGame {
    public static void main(String[] args) throws SQLException {
    	Deck deck = new Deck();
        deck.initialize();
        deck.shuffleDeck();

        Game game = new Game(3, deck); 
        game.distributeCards(3);

        Card prevCard = deck.pickFromDeck();
    	
    	System.out.println("Welcome to the game: Uno");

        String jdbcUrl = "jdbc:mysql://localhost:3306/unoGame";
        String username = "srivasthavi";
        String password = "srivasthavi";

        Connection connection = null;
        try {
            connection = DriverManager.getConnection(jdbcUrl, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        Scanner scanner = new Scanner(System.in);

        while (true) {
        	
        	System.out.println("1. Save Game");
        	System.out.println("2. Load Game");
    
            int option = scanner.nextInt();
            if (option == 1) {
                try {
                    game.saveGameState(connection);
                } catch (SQLException e) {
                    e.printStackTrace();
                    // Handle save game state error
                }
            } else if (option == 2) {
                try {
                    game.loadGameState(connection);
                } catch (SQLException e) {
                    e.printStackTrace();
                    // Handle load game state error
                }
            }
            System.out.println("Player " + (game.currentPlayerIndex + 1) + "'s turn.");

            if (game.currentPlayerIndex == 0 || game.currentPlayerIndex == 1) {
                System.out.println("Hand Contains: " + game.totalPlayers.get(game.currentPlayerIndex));
                System.out.println();
                System.out.println("Top card: " + prevCard);
                System.out.println("Enter the index of the card you want to play (-1 to draw a card):");

                int selectedIndex = -1;
                try {
                    selectedIndex = scanner.nextInt();
                } catch (InputMismatchException e) {
                    scanner.nextLine();
                }

                if (selectedIndex >= 0 && selectedIndex < game.totalPlayers.get(game.currentPlayerIndex).size()) {
                    Card selectedCard = game.totalPlayers.get(game.currentPlayerIndex).get(selectedIndex);
                    if (selectedCard.color.equals(prevCard.color) || selectedCard.value.equals(prevCard.value) || selectedCard.color.equals("wild")) {
                        prevCard = selectedCard;
                        game.totalPlayers.get(game.currentPlayerIndex).remove(selectedIndex);
                    } else {
                        System.out.println("Invalid card. Try again.");
                        continue;
                    }
                } else if (selectedIndex == -1) {
                    boolean hasWildPlusFour = false;
                    boolean hasPlusTwo = false;
                    boolean hasWildChangeColor = false;
                    boolean hasSkip = false;

                    for (Card card : game.totalPlayers.get(game.currentPlayerIndex)) {
                        if (card.value.equals("plusFour")) {
                            hasWildPlusFour = true;
                        } else if (card.value.equals("plusTwo")) {
                            hasPlusTwo = true;
                        } else if (card.value.equals("wild") && card.color.equals("changeColor")) {
                            hasWildChangeColor = true;
                        } else if (card.value.equals("skip")) {
                            hasSkip = true;
                        }
                    }

                    if (prevCard.value.equals("plusFour") && !hasWildPlusFour) {
                        System.out.println("You don't have a 'wild plus four' card. Drawing 4 cards.");
                        for (int j = 0; j < 4; j++) {
                            game.totalPlayers.get(game.currentPlayerIndex).add(deck.pickFromDeck());
                        }

                        prevCard = game.matchReturn(prevCard, game.currentPlayerIndex);
                    } else if (prevCard.value.equals("plusTwo") && !hasPlusTwo) {
                        System.out.println("You don't have a 'plus two' card. Drawing 2 cards.");
                        for (int j = 0; j < 2; j++) {
                            game.totalPlayers.get(game.currentPlayerIndex).add(deck.pickFromDeck());
                        }

                        prevCard = game.matchReturn(prevCard, game.currentPlayerIndex);
                    } else if (prevCard.value.equals("wild") && hasWildChangeColor) {
                        String newColor = game.askPlayerForColor();
                        prevCard.color = newColor;

                        System.out.println("You chose the new color: " + newColor);
                        System.out.println("Top card is: " + prevCard);

                        Card matchingCard = null;
                        for (Card card : game.totalPlayers.get(game.currentPlayerIndex)) {
                            if (card.color.equals(newColor) && !card.isActionCard) {
                                matchingCard = card;
                                break;
                            }
                        }

                        if (matchingCard != null) {
                            prevCard = matchingCard;
                            game.totalPlayers.get(game.currentPlayerIndex).remove(matchingCard);
                        } else {
                            System.out.println("No valid card in the chosen color. Drawing a card.");
                            game.totalPlayers.get(game.currentPlayerIndex).add(deck.pickFromDeck());
                            prevCard = game.matchReturn(prevCard, game.currentPlayerIndex);
                        }

                        
                    } else if (hasSkip) {
                        System.out.println("You have a 'skip' card. Player " + (game.getNextPlayer(game.currentPlayerIndex) + 1) + "'s turn is skipped.");
                        game.currentPlayerIndex = game.getNextPlayer(game.getNextPlayer(game.currentPlayerIndex));
                    } else {
                        game.totalPlayers.get(game.currentPlayerIndex).add(deck.pickFromDeck());
                        prevCard = game.matchReturn(prevCard, game.currentPlayerIndex);
                    }} else {
                    System.out.println("Invalid input. Try again.");
                    System.out.println();
                    continue;
                }
            } else {
                game.playComputerTurn();
                prevCard = game.matchReturn(prevCard, game.currentPlayerIndex);
            }

            System.out.println("Top card is: " + prevCard);
            System.out.println();

            if (game.hasPlayerWon(game.currentPlayerIndex)) {
                System.out.println("Player " + (game.currentPlayerIndex + 1) + " has won!");
                break;
            }

            game.currentPlayerIndex = game.getNextPlayer(game.currentPlayerIndex);
        }
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
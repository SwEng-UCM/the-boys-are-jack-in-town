package main.controller;

import java.util.Scanner;
import main.view.View;
import main.model.Dealer;

public class GameManager {
    private final Dealer dealer;
    private final View view;
    private final Scanner scanner;
    
    public GameManager() {
        this.dealer = new Dealer();
        this.view = new View();
        this.scanner = new Scanner(System.in);
    }

    
    public void startGame() {
        boolean playing = true;
        while (playing) {
            startNewRound();
            playing = handlePlayerActions();
        }
    }
    
        public void startNewRound() {
            System.out.println("Starting a new round...");
            view.printEmptyLine(); // Added empty line
            dealer.resetGame();  // Clears hands for a new round
            dealer.startNewGame();

            if (checkBlackjack()) {
                return;
            }

            showGameState();
        }

        private boolean checkBlackjack() {
            int playerScore = dealer.getPlayer().calculateScore();
            int dealerScore = dealer.getDealer().calculateScore();
        
            boolean playerHasBlackjack = (playerScore == 21 && dealer.getPlayer().getHand().size() == 2);
            boolean dealerHasBlackjack = (dealerScore == 21 && dealer.getDealer().getHand().size() == 2);
        
            if (playerHasBlackjack && dealerHasBlackjack) {
                System.out.println("It's a tie! Both have Blackjack.");
                view.printEmptyLine(); // Added empty line
                return true;
            } else if (playerHasBlackjack) {
                System.out.println("Player wins with a Blackjack");
                view.printEmptyLine(); // Added empty line
                return true;
            } else if (dealerHasBlackjack) {
                System.out.println("Dealer wins with a Blackjack");
                view.printEmptyLine(); // Added empty line
                return true;
            }
        
            return false;
        }
    
        public void showGameState() {
            view.showPlayerHand(dealer.getPlayer());
            view.showPlayerScore(dealer.getPlayer());
            view.printEmptyLine(); // Added empty line
            view.showDealerHand(dealer.getDealer());
            view.showDealerScore(dealer.getDealer());
            view.printEmptyLine(); // Added empty line
        }

        private boolean handlePlayerActions() {
            boolean roundOver = false;
            while (!roundOver) {
                view.showMessage("\nChoose an action: (H)it, (S)tand, (Q)uit");
                String action = scanner.nextLine().trim().toUpperCase();
    
                switch (action) {
                    case "H":
                        dealer.dealCardToPlayer();
                        view.showPlayerHand(dealer.getPlayer());
                        view.showPlayerScore(dealer.getPlayer());
                        view.printEmptyLine(); // Added empty line
                        if (dealer.getPlayer().calculateScore() > 21) {
                            view.showMessage("Player busts! Dealer wins.");
                            view.printEmptyLine(); // Added empty line
                            view.pause(); // Pause to allow user to see the end of the game
                            roundOver = true;
                        }
                        break;
                    case "S":
                        dealer.revealDealerHand();
                        while (dealer.getDealer().calculateScore() < 17) {
                            dealer.dealCardToDealer();
                        }
                        showGameState();
                        // view.showDealerHand(dealer.getDealer());
                        // view.showDealerScore(dealer.getDealer());                        
                        determineWinner();
                        view.printEmptyLine(); // Added empty line
                        view.pause(); // Pause to allow user to see the end of the game
                        roundOver = true;
                        break;
                    case "Q":
                        view.showMessage("Thanks for playing!");
                        view.printEmptyLine(); // Added empty line
                        return false;
                    default:
                    view.showMessage("Invalid action. Please choose again.");
                    view.printEmptyLine(); // Added empty line
            }
        }
        return true;
    }

    private void determineWinner() {
        showGameState(); // Show the game state before determining the winner
        int playerScore = dealer.getPlayer().calculateScore();
        int dealerScore = dealer.getDealer().calculateScore();

        if (dealerScore > 21 || playerScore > dealerScore) {
            view.showMessage("Player wins!");
        } else if (playerScore < dealerScore) {
            view.showMessage("Dealer wins!");
        } else {
            view.showMessage("It's a tie!");
        }
        view.printEmptyLine(); // Added empty line
        view.pause(); // Pause to allow user to see the end of the game
    }
    
}

    



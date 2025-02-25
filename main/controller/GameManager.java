package main.controller;

import java.util.Scanner;
import main.view.View;
import main.model.Dealer;

/**
 * Manages the flow of the Blackjack game, handling player actions and game rounds.
 */
public class GameManager {
    private final Dealer dealer;
    private final View view;
    private final Scanner scanner;

    public GameManager() {
        this.dealer = new Dealer();
        this.view = new View();
        this.scanner = new Scanner(System.in);
    }

    /** Starts the game loop, allowing multiple rounds until the player quits. */
    public void startGame() {
        boolean playing = true;
        while (playing) {
            startNewRound();
            playing = handlePlayerActions();
        }
    }

    /** Begins a new round, resets the game state, and deals initial cards. */
    public void startNewRound() {
        System.out.println("Starting a new round...");
        view.printEmptyLine();
        dealer.resetGame();
        dealer.startNewGame();

        if (checkBlackjack()) {
            return;
        }

        showGameState();
    }

    /** Checks for an immediate blackjack win and ends the round if necessary. */
    private boolean checkBlackjack() {
        int playerScore = dealer.getPlayer().calculateScore();
        int dealerScore = dealer.getDealer().calculateScore();

        boolean playerHasBlackjack = (playerScore == 21 && dealer.getPlayer().getHand().size() == 2);
        boolean dealerHasBlackjack = (dealerScore == 21 && dealer.getDealer().getHand().size() == 2);

        if (playerHasBlackjack && dealerHasBlackjack) {
            System.out.println("It's a tie! Both have Blackjack.");
            view.printEmptyLine();
            return true;
        } else if (playerHasBlackjack) {
            System.out.println("Player wins with a Blackjack!");
            view.printEmptyLine();
            return true;
        } else if (dealerHasBlackjack) {
            System.out.println("Dealer wins with a Blackjack!");
            view.printEmptyLine();
            return true;
        }

        return false;
    }

    /** Displays the player's and dealer's current hands and scores. */
    public void showGameState() {
        view.showPlayerHand(dealer.getPlayer());
        view.showPlayerScore(dealer.getPlayer());
        view.printEmptyLine();

        // Show dealer's hand with hidden card if it exists
        view.showDealerHand(dealer.getDealer(), dealer.getHiddenCard());
        view.showDealerScore(dealer.getDealer(), dealer.getHiddenCard());
        view.printEmptyLine();
    }

    /** Handles player actions during their turn, allowing hit, stand, or quit. */
    private boolean handlePlayerActions() {
        boolean roundOver = false;
        while (!roundOver) {
            view.showMessage("\nChoose an action: (H)it, (S)tand, (Q)uit");
            String action = scanner.nextLine().trim().toUpperCase();

            switch (action) {
                case "H": // Player takes a card
                    dealer.dealCardToPlayer();
                    view.showPlayerHand(dealer.getPlayer());
                    view.showPlayerScore(dealer.getPlayer());
                    view.printEmptyLine();
                    if (dealer.getPlayer().calculateScore() > 21) {
                        view.showMessage("Player busts! Dealer wins.");
                        view.printEmptyLine();
                        view.pause();
                        roundOver = true;
                    }
                    break;
                case "S": // Player stands, dealer plays
                    dealer.revealDealerHand();
                    while (dealer.getDealer().calculateScore() < 17) {
                        dealer.dealCardToDealer();
                    }
                    showGameState();
                    determineWinner();
                    view.printEmptyLine();
                    view.pause();
                    roundOver = true;
                    break;
                case "Q": // Player quits the game
                    view.showMessage("Thanks for playing!");
                    view.printEmptyLine();
                    return false;
                default:
                    view.showMessage("Invalid action. Please choose again.");
                    view.printEmptyLine();
            }
        }
        return true;
    }

    /** Determines the winner based on the final scores and displays the result. */
    private void determineWinner() {
        showGameState();
        int playerScore = dealer.getPlayer().calculateScore();
        int dealerScore = dealer.getDealer().calculateScore();

        if (dealerScore > 21 || playerScore > dealerScore) {
            view.showMessage("Player wins!");
        } else if (playerScore < dealerScore) {
            view.showMessage("Dealer wins!");
        } else {
            view.showMessage("It's a tie!");
        }
        view.printEmptyLine();
        view.pause();
    }
}

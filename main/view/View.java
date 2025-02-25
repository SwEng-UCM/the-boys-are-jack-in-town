package main.view;

import main.model.Player;

import main.model.Card;
import java.util.List;

/**
 * Handles displaying game information to the console.
 */
public class View {

    /** Displays the player's hand. */
    public void showPlayerHand(Player player) {
        System.out.println("\nPlayer's Hand:");
        player.showHand();
    }

    /** Displays the dealer's hand. */
    public void showDealerHand(Player dealer) {
        System.out.println("\nDealer's Hand:");
        //dealer.showDealerHand(true);
        dealer.showHand();
    }

    public void showDealerHand(Player dealer, Card hiddenCard) {
        System.out.println("\nDealer's Hand:");
        List<Card> hand = dealer.getHand();

        if (hiddenCard != null && hand.size() > 1) {
            System.out.println(hand.get(0) + " [Hidden]");
        } else {
            dealer.showHand();
        }
    }

    public void showDealerScore(Player dealer, Card hiddenCard) {
        if (hiddenCard != null) {
            System.out.println("Dealer's score: ??? (One card hidden)");
        } else {
            System.out.println("Dealer's score: " + dealer.calculateScore());
        }
    }

    /** Displays a message to the console. */
    public void showMessage(String message) {
        System.out.println(message);
    }

    /** Displays the player's current score. */
    public void showPlayerScore(Player player) {
        System.out.println("Player's score: " + player.calculateScore());
    }

    /** Displays the dealer's current score. */
    public void showDealerScore(Player dealer) {
        // should be modified to handle hidden card.
        System.out.println("Dealer's score: " + dealer.calculateScore());
    }

    /** Prints an empty line for spacing. */
    public void printEmptyLine() {
        System.out.println();
    }

    /** Pauses execution for a short delay (1 second). */
    public void pause() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

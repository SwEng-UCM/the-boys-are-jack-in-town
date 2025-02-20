package main.view;

import main.model.Player;

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
        dealer.showHand();
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

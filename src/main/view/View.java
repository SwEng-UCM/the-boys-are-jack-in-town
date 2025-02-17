package main.view;

import main.model.Player;

public class View {

    public void showPlayerHand(Player player) {
        System.out.println("\nPlayer's Hand:");
        player.showHand();
    }

    public void showDealerHand(Player dealer) {
        System.out.println("\nDealer's Hand:");
        dealer.showHand();
    }

    public void showMessage(String message) {
        System.out.println(message);
    }

    public void showPlayerScore(Player player) {
        System.out.println("Player's score: " + player.calculateScore());
    }

    public void showDealerScore(Player dealer) {
        System.out.println("Dealer's score: " + dealer.calculateScore());
    }

    // Added method to print an empty line
    public void printEmptyLine() {
        System.out.println();
    }

    public void pause() {
        try {
          Thread.sleep(1000); // Pause for 1 second
        } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
        }
      }
}
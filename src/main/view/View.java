package main.view;

import main.model.Player;

public class View {

    public void showPlayerHand(Player player) {
        player.showHand();
    }

    public void showDealerHand(Player dealer) {
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
}
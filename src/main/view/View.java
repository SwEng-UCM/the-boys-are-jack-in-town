package main.view;

import main.model.Player;

public class View {

    public void showPlayerHand(Player player) {
        System.out.println("\nPlayer's hand:");
        player.showHand();
    }

    public void showDealerHand(Player dealer) {
        System.out.println("\nDealer's hand:");
        dealer.showHand();
    }

    public void showMessage(String message) {
        System.out.println(message);
    }

    public void showPlayerScore(Player player) {
        System.out.println("Player's score: " + player.calculateScore());
    }
}
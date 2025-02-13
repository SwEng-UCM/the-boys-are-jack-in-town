package main.view;

import main.model.Card;
import main.model.Player;

import java.util.List;

public class View {

    public void showPlayerHand(Player player) {
        System.out.println("\nPlayer's hand:");
        player.showHand();
    }

    public void showDealerHand(Player dealer) {
        System.out.println("\nDealer's hand:");
        dealer.showHand();
    }

    public void prettyPrintHand(List<Card> hand){
        for (Card card : hand) {
            String rank = card.getRank();
            String suit = card.getSuit();
            char formattedRankChar = rank.charAt(0);
            String formattedRank = String.valueOf(formattedRankChar);

            System.out.println("+--------+");
            System.out.println("|" + formattedRank + "       |");
            System.out.println("|        |");
            System.out.println("|   " + suit + "    |");
            System.out.println("|        |");
            System.out.println("|      " + formattedRank + " |");
            System.out.println("+--------+");
        }
    }

    public void showMessage(String message) {
        System.out.println(message);
    }

    public void showPlayerScore(Player player) {
        System.out.println("Player's score: " + player.calculateScore());
    }
}
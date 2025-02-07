package main.model;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private final List<Card> hand;

    public Player() {
        this.hand = new ArrayList<>();
    }

    public void receiveCard(Card card) {
        hand.add(card);
    }

    public void showHand() {
        for (Card card : hand) {
            System.out.println(card);
        }
    }

    public List<Card> getHand() {
        return hand;
    }

    public int calculateScore() {
        int score = 0;
        int aceCount = 0;

        for (Card card : hand) {
            score += card.getValue();
            if (card.getRank().equals("Ace")) {
                aceCount++;
            }
        }

        // Adjust for Aces if score is over 21
        while (score > 21 && aceCount > 0) {
            score -= 10;
            aceCount--;
        }

        return score;
    }
}
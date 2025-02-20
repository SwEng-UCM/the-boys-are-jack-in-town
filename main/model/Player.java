package main.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Blackjack player, managing their hand and score calculations.
 */
public class Player {
    private final List<Card> hand; // Stores the player's current hand

    public Player() {
        this.hand = new ArrayList<>();
    }

    /** Adds a card to the player's hand. */
    public void receiveCard(Card card) {
        hand.add(card);
    }

    /** Displays the player's hand in the console. */
    public void showHand() {
        for (Card card : hand) {
            System.out.println(card);
        }
    }

    /** Returns the list of cards in the player's hand. */
    public List<Card> getHand() {
        return hand;
    }

    /**
     * Calculates the player's total score, adjusting Aces from 11 to 1 if necessary.
     */
    public int calculateScore() {
        int score = 0;
        int aceCount = 0;

        for (Card card : hand) {
            score += card.getValue();
            if (card.getRank().equals("Ace")) {
                aceCount++;
            }
        }

        // Convert Aces from 11 to 1 if score exceeds 21
        while (score > 21 && aceCount > 0) {
            score -= 10;
            aceCount--;
        }

        return score;
    }

    /** Clears the player's hand for a new round. */
    public void reset() {
        hand.clear();
    }
}

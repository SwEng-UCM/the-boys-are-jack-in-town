package main.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Blackjack player, managing their hand and score calculations.
 */
public class Player {
    private final List<Card> hand; // Stores the player's current hand
    private double scoreMultiplier; // Multiplier for the player's score (default 1.0)

    public Player() {
        this.hand = new ArrayList<>();
        this.scoreMultiplier = 1.0;
    }

    /** Adds a card to the player's hand. */
    public void receiveCard(Card card) {
        hand.add(card);
        // When a Split Ace wildcard is drawn, set the multiplier to 0.5
        if (card.getType() == Card.CardType.SPLIT_ACE) {
            scoreMultiplier = 0.5;
        }
    }

    /** Displays the player's hand in the console. */
    public void showHand() {
        System.out.println("Hand:");
        for (Card card : hand) {
            System.out.println(card);
        }
    }

    /** Returns the list of cards in the player's hand. */
    public List<Card> getHand() {
        return hand;
    }

    /**
     * Calculates the player's total score, adjusting Aces from 11 to 1 if necessary,
     * and applying the score multiplier if a Split Ace wildcard was drawn.
     */
    public int calculateScore() {
        int score = 0;
        int aceCount = 0;

        for (Card card : hand) {
            score += card.getValue();
            // Only count standard Aces for adjustment purposes
            if (card.getRank().equals("Ace") && card.getType() == Card.CardType.STANDARD) {
                aceCount++;
            }
        }

        // Convert standard Aces from 11 to 1 if score exceeds 21
        while (score > 21 && aceCount > 0) {
            score -= 10;
            aceCount--;
        }
        // Apply the score multiplier (if a Split Ace wildcard was drawn, this will be 0.5)
        return (int)(score * scoreMultiplier);
    }

    public boolean hasBlackjack() {
        // Check if player has exactly two cards and their (possibly halved) score equals 21
        return hand.size() == 2 && calculateScore() == 21;
    }

    /** Clears the player's hand and resets the score multiplier for a new round. */
    public void reset() {
        hand.clear();
        scoreMultiplier = 1.0;
    }
}

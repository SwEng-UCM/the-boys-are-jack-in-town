package main.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Blackjack player, managing their hand and score calculations.
 */
public class Player {
    private final List<Card> hand; // Stores the player's current hand
    private List<Card> splitHand; // Stores the second hand when splitting
    private boolean hasSplit; // Indicates if the player has split their hand

    public Player() {
        this.hand = new ArrayList<>();
        this.splitHand = new ArrayList<>();
        this.hasSplit = false;
    }

    /** Adds a card to the player's hand. */
    public void receiveCard(Card card) {
        if (hasSplit && splitHand.isEmpty()) {
            splitHand.add(card);
        } else {
            hand.add(card);
            if (card.getType() == Card.CardType.SPLIT_ACE && hand.size() == 2) {
                splitHand();
            }
        }
    }

    /** Displays the player's hands in the console. */
    public void showHand() {
        System.out.println("Main Hand:");
        for (Card card : hand) {
            System.out.println(card);
        }
        if (hasSplit) {
            System.out.println("Split Hand:");
            for (Card card : splitHand) {
                System.out.println(card);
            }
        }
    }

    /** Returns the list of cards in the player's main hand. */
    public List<Card> getHand() {
        return hand;
    }

    /** Returns the list of cards in the player's split hand, if applicable. */
    public List<Card> getSplitHand() {
        return hasSplit ? splitHand : null;
    }

    /**
     * Calculates the player's total score, adjusting Aces from 11 to 1 if necessary.
     */
    public int calculateScore() {
        return calculateHandScore(hand);
    }

    /** Calculates the score of the split hand, if applicable. */
    public int calculateSplitScore() {
        return hasSplit ? calculateHandScore(splitHand) : 0;
    }

    private int calculateHandScore(List<Card> handToCalculate) {
        int score = 0;
        int aceCount = 0;

        for (Card card : handToCalculate) {
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

    public boolean hasBlackjack() {
        // Check if player has exactly two cards and their score equals 21
        return calculateScore() == 21;
    }

    /** Splits the player's hand into two separate hands when a Split Ace is drawn. */
    private void splitHand() {
        if (hand.size() == 2 && hand.get(0).getRank().equals(hand.get(1).getRank())) {
            splitHand.add(hand.remove(1));
            hasSplit = true;
        }
    }

    /** Checks if the player has split their hand. */
    public boolean hasSplit() {
        return hasSplit;
    }

    /** Clears the player's hands for a new round. */
    public void reset() {
        hand.clear();
        splitHand.clear();
        hasSplit = false;
    }
}
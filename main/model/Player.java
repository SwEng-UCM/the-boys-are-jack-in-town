package main.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Blackjack player, managing their hand, bets, and balance.
 */
public class Player {
    private final String name; // Player's name
    private final List<Card> hand; // Stores the player's current hand
    private double scoreMultiplier; // Multiplier for score calculations (default 1.0)
    private int balance; // Player's balance (chips/money)
    private int currentBet; // Player's current bet
    private int currentScore;

    @JsonCreator
    public Player(@JsonProperty("name") String name, @JsonProperty("initialBalance")int initialBalance) {
        this.name = name;
        this.hand = new ArrayList<>();
        this.scoreMultiplier = 1.0;
        this.balance = initialBalance;
        this.currentBet = 0;
        this.currentScore = 0;
    }

    /** Adds a card to the player's hand. */
    public void receiveCard(Card card) {
        hand.add(card);
        // If the card is a SPLIT_ACE, adjust the multiplier
        if (card.getType() == Card.CardType.SPLIT_ACE) {
            scoreMultiplier = 0.5;
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

        return (int) (score * scoreMultiplier);
    }

    /** Checks if the player has Blackjack (an Ace + 10-value card). */
    public boolean hasBlackjack() {
        return hand.size() == 2 && calculateScore() == 21;
    }

    /** Places a bet, subtracting from the player's balance. */
    public boolean placeBet(int amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
            currentBet = amount;
            return true;
        }
        return false;
    }

    /** Returns the bet amount. */
    public int getCurrentBet() {
        return currentBet;
    }

    /** Awards winnings to the player. */
    public void winBet(int winnings) {
        balance += currentBet + winnings;
        currentBet = 0;
    }

    /** Loses the bet, keeping the balance reduced. */
    public void loseBet() {
        currentBet = 0; // The bet is already deducted when placed
    }

    /** Refunds the player's bet in case of a tie. */
    public void tieBet() {
        balance += currentBet; // Return the bet amount
        currentBet = 0;
    }

    /** Returns the player's balance. */
    public int getBalance() {
        return balance;
    }

    /** Resets the player's hand and score multiplier for a new round. */
    public void reset() {
        hand.clear();
        scoreMultiplier = 1.0;
        currentBet = 0;
    }

    /** Returns the player's name. */
    public String getName() {
        return name;
    }

    public void setBalance(int inputBalance) {
        this.balance = inputBalance;
    }

    public void setHand(List<Card> hand) {
        this.hand.clear();
        this.hand.addAll(hand);
    }

    public void setCurrentBet(int currentBet) {
        this.currentBet = currentBet;
    }

    public void setScoreMultiplier(double scoreMultiplier) {
        this.scoreMultiplier = scoreMultiplier;
    }

    public void setCurrentScore() {
        this.currentScore = this.calculateScore();
    }
}

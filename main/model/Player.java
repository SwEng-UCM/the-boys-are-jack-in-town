package main.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Blackjack player, managing their hand, bets, and balance.
 * It also integrates with Jackson for JSON serialization/deserialization.
 */
public class Player implements Serializable{
    private static final long serialVersionUID = 1L;

    private final String name; // Player's name
    private final List<Card> hand; // Stores the player's current hand
    private double scoreMultiplier; // Multiplier for score calculations (default 1.0)
    private int balance; // Player's balance (chips/money)
    private int currentBet; // Player's current bet
    private int currentScore;
    private boolean hasStood; // Indicates if the player has stood in the current round

    /**
     * Constructs a player with a given name and initial balance.
     * 
     * @param name            the player's name
     * @param initialBalance  the player's starting balance (chips/money)
     */
    @JsonCreator
    public Player(@JsonProperty("name") String name, @JsonProperty("initialBalance")int initialBalance) {
        this.name = name;
        this.hand = new ArrayList<>();
        this.scoreMultiplier = 1.0;
        this.balance = initialBalance;
        this.currentBet = 0;
        this.currentScore = 0;
    }

    /**
     * Copy constructor for creating a deep copy of an existing player.
     *
     * @param other the player to copy
     */
    public Player(Player other) {
        this.name = other.name;
        this.hand = new ArrayList<>();
        for (Card c : other.hand) {
            this.hand.add(new Card(c));  // Ensure Card is also Serializable & has a copy constructor
        }
        this.scoreMultiplier = other.scoreMultiplier;
        this.balance = other.balance;
        this.currentBet = other.currentBet;
        this.currentScore = other.currentScore;
        this.hasStood = other.hasStood;
    }
    

    /**
     * Adds a card to the player's hand and applies any card-specific effects.
     * 
     * Specifically, if the card is a {@code SPLIT_ACE}, it adjusts the player's
     * score multiplier to 0.5, affecting the final score calculation.
     * 
     * @param card the {@link Card} to add to the player's hand
     */
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
     * Calculates the player's total score based on the current hand.
     * 
     * The score accounts for standard Blackjack rules, such as reducing 
     * Ace values from 11 to 1 if the total score exceeds 21. Additionally,
     * it applies the player's score multiplier, which may be affected by 
     * special cards (e.g., {@code SPLIT_ACE}).
     * 
     * @return the calculated score, adjusted for Aces and multipliers
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

    /**
     * Checks if the player has Blackjack (exactly two cards totaling 21).
     *
     * @return {@code true} if the player has Blackjack, {@code false} otherwise
     */
    public boolean hasBlackjack() {
        return hand.size() == 2 && calculateScore() == 21;
    }

    /**
     * Places a bet for the player, deducting the amount from their balance.
     *
     * @param amount the amount to bet
     * @return {@code true} if the bet was successful, {@code false} otherwise
     */
    public boolean placeBet(int amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
            currentBet = amount;
            return true;
        }
        return false;
    }

    /** 
     * Returns the player's current bet.
     * 
     * @return the current bet amount
     */
    public int getCurrentBet() {
        return currentBet;
    }

    /**
     * Returns the player's current balance.
     *
     * @return the player's balance
     */
    public int getBalance() {
        return balance;
    }

    /**
     * Resets the player's hand, bet, and score multiplier for a new round.
     */
    public void reset() {
        hand.clear();
        scoreMultiplier = 1.0;
        currentBet = 0;
    }

    /**
     * Checks if the player has at least one Ace in their hand.
     *
     * @return {@code true} if the player has an Ace, {@code false} otherwise
     */
    public boolean hasAce() {
        return hand.stream().anyMatch(card -> 
            card.getRank().equals("Ace") && card.getType() == Card.CardType.STANDARD
        );
    }

    /**
     * Returns the player's name.
     *
     * @return the player's name
     */
    public String getName() {
        return name;
    }

    /**
     * Adds the specified amount to the player's balance.
     *
     * @param amount The amount to add to the player's balance.
     */
    public void addToBalance(int amount) {
        this.balance += amount;
    }

    /**
     * Checks if the player has stood in the current round.
     *
     * @return {@code true} if the player has stood, {@code false} otherwise.
     */
    public boolean hasStood() {
        return hasStood;
    }

    /*
     * -------------------------------------------------------
     * Setters for internal state (used for state restoration)
     * -------------------------------------------------------
     */

    /**
     * Sets the player's current balance.
     *
     * @param inputBalance the amount to set as the player's balance
     */
    public void setBalance(int inputBalance) {
        this.balance = inputBalance;
    }

    /**
     * Replaces the player's current hand with the specified list of cards.
     *
     * @param hand the new hand to assign to the player
     */
    public void setHand(List<Card> hand) {
        this.hand.clear();
        this.hand.addAll(hand);
    }

    /**
     * Sets the player's current bet amount.
     *
     * @param currentBet the amount the player is betting
     */
    public void setCurrentBet(int currentBet) {
        this.currentBet = currentBet;
    }

    /**
     * Sets the score multiplier used when calculating the player's score.
     *
     * @param scoreMultiplier the multiplier to apply to the player's score
     */
    public void setScoreMultiplier(double scoreMultiplier) {
        this.scoreMultiplier = scoreMultiplier;
    }

    /**
     * Updates the player's current score using the {@code calculateScore()} method.
     */
    public void setCurrentScore() {
        this.currentScore = this.calculateScore();
    }

    /**
     * Manually sets the player's score to the specified value.
     *
     * @param current the value to assign as the player's score
     */
    public void setScore(int current) {
        this.currentScore = current;
    }

    /**
     * Sets whether the player has chosen to stand in the current round.
     *
     * @param stood {@code true} if the player has stood, {@code false} otherwise
     */
    public void setStood(boolean stood) {
        this.hasStood = stood;
    }


    /**
     * Returns a string representation of the player, including their name and balance.
     *
     * @return A string in the format "name balance", where "name" is the player's name
     *         and "balance" is the player's current balance.
     */
    @Override
    public String toString() {
        return this.name + " " + this.balance;
    }
}

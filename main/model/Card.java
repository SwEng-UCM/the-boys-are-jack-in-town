package main.model;

/**
 * Represents a playing card with a rank (2-10, J, Q, K, A) and a suit (Hearts, Diamonds, Clubs, Spades).
 */
public class Card {
    private final String suit; // Suit of the card
    private final String rank; // Rank of the card
    boolean hidden;

    public Card(String rank, String suit, boolean hidden) {
        this.rank = rank;
        this.suit = suit;
        this.hidden = hidden;
    }

    public String getRank() {
        return this.rank;
    }

    public String getSuit() {
        return this.suit;
    }

    /**
     * Returns the card's value in Blackjack. Face cards are worth 10, Aces default to 11.
     */
    public int getValue() {
        return switch (rank) {
            case "2" -> 2;
            case "3" -> 3;
            case "4" -> 4;
            case "5" -> 5;
            case "6" -> 6;
            case "7" -> 7;
            case "8" -> 8;
            case "9" -> 9;
            case "10", "Jack", "Queen", "King" -> 10;
            case "Ace" -> 11; // Default Ace value, game logic should adjust if needed
            default -> 0;
        };
    }

    /**
     * Returns a formatted string like "Ace of Spades" or "10 of Hearts".
     */
    @Override
    public String toString() {
        return rank + " of " + suit;
    }
}

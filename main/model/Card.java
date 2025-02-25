package main.model;

/**
 * Represents a playing card with a rank (2-10, J, Q, K, A) and a suit (Hearts, Diamonds, Clubs, Spades).
 */
public class Card {
    private final String suit; // Suit of the card
    private final String rank; // Rank of the card

    public Card(String rank, String suit) {
        this.rank = rank;
        this.suit = suit;
    }

    public String getRank() {
        return this.rank;
    }

    public String getSuit() {
        return this.suit;
    }

    /**
     * Returns the card's value in Blackjack. Face cards are worth 10, Aces default to 11.
     * This value can be adjusted later based on the hand's total value (e.g., Ace can be 1 or 11).
     */
    public int getValue() {
        switch (rank) {
            case "2":
                return 2;
            case "3":
                return 3;
            case "4":
                return 4;
            case "5":
                return 5;
            case "6":
                return 6;
            case "7":
                return 7;
            case "8":
                return 8;
            case "9":
                return 9;
            case "10":
            case "Jack":
            case "Queen":
            case "King":
                return 10;
            case "Ace":
                return 11; // Default Ace value, will adjust during hand calculation if needed
            default:
                return 0;
        }
    }

    /**
     * Returns a formatted string like "Ace of Spades" or "10 of Hearts".
     */
    @Override
    public String toString() {
        return rank + " of " + suit;
    }
}

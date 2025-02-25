package main.model;

/**
 * Represents a playing card with a rank (2-10, J, Q, K, A) and a suit (Hearts, Diamonds, Clubs, Spades).
 */
public class Card {
    private final String suit; // Suit of the card
    private final String rank; // Rank of the card
    private boolean hidden; // Whether the card is hidden (for GUI purposes)

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
     * Toggles the visibility of the card (for GUI purposes).
     */
    public void toggleVisibility() {
        this.hidden = !this.hidden;
    }

    /**
     * Returns a formatted string like "Ace of Spades" or "10 of Hearts".
     * If the card is hidden, it returns "Hidden Card" instead.
     */
    @Override
    public String toString() {
        if (hidden) {
            return "Hidden Card"; // For the GUI to display a hidden card
        }
        return rank + " of " + suit;
    }

    /**
     * Returns a simplified card name for the GUI (just rank and suit symbol).
     */
    public String getCardForDisplay() {
        if (hidden) {
            return "Hidden Card";
        }
        return rank + " " + getSuitSymbol(suit); // Ex: Ace ♥, 10 ♠, etc.
    }

    /** Returns the Unicode symbol for a given suit. */
    private String getSuitSymbol(String suit) {
        return switch (suit) {
            case "Hearts" -> "♥";
            case "Diamonds" -> "♦";
            case "Clubs" -> "♣";
            case "Spades" -> "♠";
            default -> "?";
        };
    }
}

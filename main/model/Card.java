package main.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a playing card in the Blackjack game, including both standard cards and special cards.
 * This class includes logic for determining the card's value, including support for dynamic wild cards.
 * It also integrates with JSON serialization using Jackson annotations.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Card {

    /**
     * Enum representing different types of cards in the game, including special cards.
     */
    public enum CardType {
        /** Standard playing card (e.g., Ace, 2-10, Jack, Queen, King). */
        STANDARD,
        /** A special card representing an instant win (Blackjack Bomb). */
        BLACKJACK_BOMB,
        /** A special card that represents a Split Ace. */
        SPLIT_ACE,
        /** A special Joker Wild card with a dynamic value (1-11). */
        JOKER_WILD
    }

    private final String suit;
    private final String rank;
    @SuppressWarnings("unused")
    private final boolean hidden;
    private final CardType type;
    private int wildValue;

    /**
     * Constructs a standard playing card with the given rank, suit, and hidden state.
     * Used for JSON deserialization.
     *
     * @param rank   the rank of the card (e.g., "Ace", "2", "King")
     * @param suit   the suit of the card (e.g., "Hearts", "Spades")
     * @param hidden whether the card is hidden (face-down)
     */
    @JsonCreator
    public Card(@JsonProperty("rank") String rank, @JsonProperty("suit") String suit, @JsonProperty("hidden") boolean hidden) {
        this.rank = rank;
        this.suit = suit;
        this.hidden = hidden;
        this.type = CardType.STANDARD;
    }

    /**
     * Copy constructor. Creates a new card based on an existing card.
     *
     * @param original the card to copy
     */
    public Card(Card original) {
        this.rank = original.rank;
        this.suit = original.suit;
        this.type = original.type;
        this.wildValue = original.wildValue;
        this.hidden = false;
    }
    
    /**
     * Constructs a special card based on its type (e.g., Blackjack Bomb, Split Ace, Joker Wild).
     *
     * @param type the type of special card
     * @throws IllegalArgumentException if an unknown type is provided
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public Card( CardType type) {
        this.rank = switch (type) {
            case BLACKJACK_BOMB -> "BB";
            case SPLIT_ACE -> "SA";
            case JOKER_WILD -> "JW";
            default -> throw new IllegalArgumentException("Unknown special card type");
        };
        this.suit = "Special";
        this.hidden = false;
        this.type = type;
        this.wildValue = 11;
    }

    /**
     * Returns the rank of the card.
     *
     * @return the card's rank
     */
    public String getRank() {
        return this.rank;
    }

    /**
     * Returns the suit of the card.
     *
     * @return the card's suit
     */
    public String getSuit() {
        return this.suit;
    }

    /**
     * Returns the type of the card.
     *
     * @return the card's type (STANDARD, BLACKJACK_BOMB, etc.)
     */
    public CardType getType() {
        return this.type;
    }

    /**
     * Returns the card's value based on its type.
     *
     * @return the card's numeric value (e.g., 2-11 or special values)
     */
    public int getValue() {
        return switch (type) {
            case BLACKJACK_BOMB -> 21;
            case SPLIT_ACE -> 0;
            case JOKER_WILD -> wildValue; 
            default -> standardCardValue();
        };
    }

    /**
     * Sets the dynamic value for Joker Wild cards (between 1 and 11).
     *
     * @param value the new value for the Joker Wild card
     * @throws IllegalArgumentException if the value is not between 1 and 11
     */
    public void setWildValue(int value) {
        if (type == CardType.JOKER_WILD && value >= 1 && value <= 11) {
            this.wildValue = value;
        } else {
            throw new IllegalArgumentException("Invalid value for Joker Wild");
        }
    }

    /**
     * Returns the standard value of a card (e.g., numeric cards, face cards, Ace).
     *
     * @return the standard card value
     */
    private int standardCardValue() {
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
            case "Ace" -> 11;
            default -> 0;
        };
    }

    /**
     * Checks if the card is a Joker Wild card.
     *
     * @return true if the card is a Joker Wild, false otherwise
     */
    public boolean isJokerWild() {
        return this.type == CardType.JOKER_WILD;
    }

    /**
     * Checks if the card is a Split Ace card.
     *
     * @return true if the card is a Split Ace, false otherwise
     */
    public boolean isSplitAce() {
        return this.type == CardType.SPLIT_ACE;
    }

    /**
     * Checks if the card is a Blackjack Bomb card.
     *
     * @return true if the card is a Blackjack Bomb, false otherwise
     */
    public boolean isBlackjackBomb() {
        return this.type == CardType.BLACKJACK_BOMB;
    }

    /**
     * Returns a string representation of the card.
     * Special cards are represented with emojis and descriptions.
     *
     * @return the string representation of the card
     */
    @Override
    public String toString() {
        return switch (type) {
            case BLACKJACK_BOMB -> "Blackjack Bomb";
            case SPLIT_ACE -> "Split Ace";
            case JOKER_WILD -> "Joker Wild [Value: " + wildValue + "]";
            default -> rank + " of " + suit;
        };
    }
}

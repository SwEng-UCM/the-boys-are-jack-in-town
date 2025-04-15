package main.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a playing card with a rank and a suit, including special cards that modify Blackjack rules.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Card {

    public enum CardType {
        STANDARD, BLACKJACK_BOMB, SPLIT_ACE, JOKER_WILD
    }

    private final String suit;
    private final String rank;
    @SuppressWarnings("unused")
    private final boolean hidden;
    private final CardType type;
    private int wildValue; // Dynamic value for Joker Wild
    private int value;

    @JsonCreator
    public Card(@JsonProperty("rank") String rank, @JsonProperty("suit") String suit, @JsonProperty("hidden") boolean hidden) {
        this.rank = rank;
        this.suit = suit;
        this.hidden = hidden;
        this.type = CardType.STANDARD;
    }

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
        this.wildValue = 11; // Default value for Joker Wild
    }

    public String getRank() {
        return this.rank;
    }

    public String getSuit() {
        return this.suit;
    }

    public CardType getType() {
        return this.type;
    }

    public int getValue() {
        return switch (type) {
            case BLACKJACK_BOMB -> 21; // Instant win
            case SPLIT_ACE -> 0; // Default Ace value, can be adjusted in gameplay
            case JOKER_WILD -> wildValue; // Dynamic value
            default -> standardCardValue();
        };
    }

    public void setWildValue(int value) {
        if (type == CardType.JOKER_WILD && value >= 1 && value <= 11) {
            this.wildValue = value;
        } else {
            throw new IllegalArgumentException("Invalid value for Joker Wild");
        }
    }

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

    @Override
    public String toString() {
        return switch (type) {
            case BLACKJACK_BOMB -> "Blackjack Bomb (💣)";
            case SPLIT_ACE -> "Split Ace (♠♠)";
            case JOKER_WILD -> "Joker Wild (🤡) [Value: " + wildValue + "]";
            default -> rank + " of " + suit;
        };
    }

    public boolean isJokerWild() {
        return this.type == CardType.JOKER_WILD;
    }

    public boolean isSplitAce() {
        return this.type == CardType.SPLIT_ACE;
    }

    public boolean isBlackjackBomb() {
        return this.type == CardType.BLACKJACK_BOMB;
    }

    public int getStandardCardValue() {
        System.out.println(">> "+this.rank + this.suit);
        switch (rank) {
            case "2": return 2;
            case "3": return  3;
            case "4": return 4;
            case "5": return 5;
            case "6": return 6;
            case "7": return 7;
            case "8": return 8;
            case "9": return 9;
            case "10", "Jack", "Queen", "King": return 10;
            case "Ace": return 11;
            default : return 0;
    }
    }
}

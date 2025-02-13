package main.model;

public class Card {
    private final String suit;
    private final String rank;

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
            case "10" -> 10;
            case "Jack", "Queen", "King" -> 10;
            case "Ace" -> 11; // Assuming Ace is 11 for simplicity
            default -> 0;
        };
    }

    @Override
    public String toString() {
        return rank + " of " + suit;
    }
}

package main.model;

import java.util.*;

public class Deck {
    private final List<Card> cards;

    public Deck() {
        String[] suits = {"Hearts", "Diamonds", "Clubs", "Spades"};
        String[] ranks = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King", "Ace"};

        cards = new ArrayList<>();

        for (String suit : suits) {
            for (String rank : ranks) {
                cards.add(new Card(rank, suit));
            }
        }
        Collections.shuffle(cards);
    }

    public Card dealCard() {
        if (cards.isEmpty()) {
            throw new IllegalStateException("No cards left in the deck");
        }
        return cards.remove(cards.size() - 1);
    }

    public void prettyPrint() {
        for (Card card : cards) {
            String rank = card.getRank();
            String suit = getSuitSymbol(card.getSuit());
            char formattedRankChar = rank.charAt(0);
            String formattedRank = String.valueOf(formattedRankChar);

            System.out.println("+--------+");
            System.out.println("|" + formattedRank + "       |");
            System.out.println("|        |");
            System.out.println("|   " + suit + "    |");
            System.out.println("|        |");
            System.out.println("|      " + formattedRank + " |");
            System.out.println("+--------+");
        }
    }

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

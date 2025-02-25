package main.model;

import java.util.*;

/**
 * Represents a deck of 52 playing cards, used in Blackjack.
 * The deck is shuffled upon creation.
 */
public class Deck {
    private final List<Card> cards; // List of cards in the deck
    private final Random random; // Random instance for shuffling and drawing

    public Deck() {
        String[] suits = {"Hearts", "Diamonds", "Clubs", "Spades"};
        String[] ranks = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King", "Ace"};

        cards = new ArrayList<>();
        random = new Random();

        // Generate all 52 cards and shuffle the deck
        for (String suit : suits) {
            for (String rank : ranks) {
                cards.add(new Card(rank, suit, false)); // Cards are not hidden initially
            }
        }
        Collections.shuffle(cards); // Shuffle the deck
    }

    /**
     * Deals one card from the top of the deck.
     * Throws an exception if the deck is empty.
     */
    public Card dealCard() {
        if (cards.isEmpty()) {
            throw new IllegalStateException("No cards left in the deck");
        }
        return cards.remove(cards.size() - 1);
    }

    /**
     * Draws multiple random cards (up to n), ensuring cards are not drawn if the deck is empty.
     */
    public List<Card> drawMultipleRandomCards(int n) {
        List<Card> drawnCards = new ArrayList<>();
        for (int i = 0; i < n && !cards.isEmpty(); i++) {
            drawnCards.add(dealCard());
        }
        return drawnCards;
    }

    /**
     * Re-shuffles the deck, resetting the cards.
     */
    public void reshuffle() {
        cards.clear();
        String[] suits = {"Hearts", "Diamonds", "Clubs", "Spades"};
        String[] ranks = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King", "Ace"};
        
        for (String suit : suits) {
            for (String rank : ranks) {
                cards.add(new Card(rank, suit, false)); // Create new cards, not hidden
            }
        }
        Collections.shuffle(cards); // Shuffle the deck
    }

    /** Displays a simple visual representation of the remaining cards in the deck. */
    public void prettyPrint() {
        for (Card card : cards) {
            String rank = card.getRank();
            String suit = card.getSuit();
            System.out.println("+--------+");
            System.out.println("| " + rank + "       |");
            System.out.println("|        |");
            System.out.println("|   " + suit + "    |");
            System.out.println("|        |");
            System.out.println("|      " + rank + " |");
            System.out.println("+--------+");
        }
    }

    /** Returns the number of cards remaining in the deck. */
    public int getRemainingCards() {
        return cards.size();
    }
}

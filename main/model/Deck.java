package main.model;

import java.io.Serializable;
import java.util.*;

/**
 * Represents a standard deck of playing cards, including special cards like Blackjack Bomb, Split Ace, and Joker Wild.
 * This class manages card storage, shuffling, and dealing but does not handle serialization logic.
 */
public class Deck implements Serializable {
    private static final long serialVersionUID = 1L;

    private final List<Card> cards;
    private final Random random;

    /**
     * Constructs a new standard deck of cards, including special rule cards.
     */
    public Deck() {
        cards = new ArrayList<>();
        random = new Random();

        String[] suits = {"Hearts", "Diamonds", "Clubs", "Spades"};
        String[] ranks = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King", "Ace"};

        // Add normal cards to the deck
        for (String suit : suits) {
            for (String rank : ranks) {
                cards.add(new Card(rank, suit, false));
            }
        }

        cards.add(new Card(Card.CardType.BLACKJACK_BOMB));
        cards.add(new Card(Card.CardType.SPLIT_ACE));
        cards.add(new Card(Card.CardType.JOKER_WILD));

        shuffle();
    }

    /**
     * Copy constructor for creating a new deck from an existing deck.
     *
     * @param original the original deck to copy
     */
    public Deck(Deck original) {
        this.cards = new ArrayList<>();
        for (Card card : original.cards) {
            this.cards.add(new Card(card)); // Assumes Card has a copy constructor
        }
        this.random = new Random();
    }

    /**
     * Deals (removes) the top card from the deck.
     *
     * @return the dealt card
     * @throws IllegalStateException if the deck is empty
     */
    public Card dealCard() {
        if (cards.isEmpty()) {
            throw new IllegalStateException("No cards left in the deck");
        }
        return cards.remove(cards.size() - 1);
    }
    public List<Card> getCards() {
        return cards;
    }
    
    /**
     * Draws multiple random cards from the deck.
     *
     * @param n the number of cards to draw
     * @return a list of drawn cards
     */
    public List<Card> drawMultipleRandomCards(int n) {
        List<Card> drawnCards = new ArrayList<>();
        for (int i = 0; i < n && !cards.isEmpty(); i++) {
            drawnCards.add(dealCard());
        }
        return drawnCards;
    }

    /**
     * Shuffles the deck.
     */
    public void shuffle() {
        Collections.shuffle(cards, random);
    }

    /**
     * Returns all remaining cards in the deck.
     *
     * @return the list of cards
     */
    public List<Card> getAllCards() {
        return cards;
    }

    /**
     * Clears the current deck and replaces it with a given list of cards.
     *
     * @param newCards the new set of cards to replace the current deck
     */
    public void setCards(List<Card> newCards) {
        cards.clear();
        cards.addAll(newCards);
    }
}

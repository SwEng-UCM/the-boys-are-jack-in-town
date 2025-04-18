package main.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.*;

/**
 * Represents a deck of playing cards, including special rule-changing cards.
 */
public class Deck {
    private final List<Card> cards;
    private final Random random;


    @JsonCreator
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

        // Add special cards
        cards.add(new Card(Card.CardType.BLACKJACK_BOMB));
        cards.add(new Card(Card.CardType.SPLIT_ACE));
        cards.add(new Card(Card.CardType.JOKER_WILD));

        // Shuffle the deck initially
        shuffle();
    }

    @JsonCreator
    public Deck(@JsonProperty("allCards") List<Card> cards) {
        random = new Random();

        List<Card> newCards = new ArrayList<>();
        for (Card c : cards) {
            newCards.add(new Card(c.getRank(), c.getSuit(), false));
        }
        newCards.add(new Card(Card.CardType.BLACKJACK_BOMB));
        newCards.add(new Card(Card.CardType.SPLIT_ACE));
        newCards.add(new Card(Card.CardType.JOKER_WILD));

        this.cards = newCards;
        shuffle();
    }

    // Copy constructor
    public Deck(Deck original) {
        this.cards = new ArrayList<>();
        for (Card card : original.cards) {
            this.cards.add(new Card(card)); // Assuming Card has a copy constructor
        }
        this.random = new Random();
    }
    

    public Card dealCard() {
        if (cards.isEmpty()) {
            throw new IllegalStateException("No cards left in the deck");
        }
        return cards.remove(cards.size() - 1);
    }
    public List<Card> getCards() {
        return cards;
    }
    

    public List<Card> drawMultipleRandomCards(int n) {
        List<Card> drawnCards = new ArrayList<>();
        for (int i = 0; i < n && !cards.isEmpty(); i++) {
            drawnCards.add(dealCard());
        }
        return drawnCards;
    }

    public void shuffle() {
        // Shuffle the deck using Collections.shuffle
        Collections.shuffle(cards, random);
    }

    public List<Card> getAllCards() {
        return cards;
    }

}

package main.model;

import java.io.Serializable;
import java.util.ArrayList;


/**
 * The Dealer class represents the dealer in the Blackjack game.
 * It is responsible for managing the deck of cards, dealing cards to players,
 * and controlling the game flow.
 */
public class Dealer implements Serializable {
    private static final long serialVersionUID = 1L;

    private Deck deck; // The deck of cards used in the game
    private final Player dealer; // The dealer 
    private Card hiddenCard; // Stores the dealer's hidden card

    public Dealer() {
        this.deck = new Deck();
        this.dealer = new Player("Dealer", 1000); // Assuming dealer has a name and balance
    }

    
    public void startNewGame(ArrayList<Player> players) {
        for (Player player : players) {
            dealInitialCards(player, false); // Player receives two visible cards
        }
        dealInitialCards(dealer, true); // Dealer receives one hidden card
    }

    /**
     * Deals two cards to the given player. Optionally hides the second card for the dealer.
     * 
     * @param targetPlayer The player to whom cards are dealt
     * @param hideSecondCard Whether to hide the second card
     */
    private void dealInitialCards(Player targetPlayer, boolean hideSecondCard) {
        targetPlayer.receiveCard(deck.dealCard()); // First card
        if (hideSecondCard) {
            hiddenCard = deck.dealCard(); // Store hidden card
        } else {
            targetPlayer.receiveCard(deck.dealCard()); // Second visible card
        }
    }

    /**
     * Deals one card to the specified player.
     * 
     * @param targetPlayer The player to whom the card is dealt
     */
    public void dealCardToPlayer(Player targetPlayer) {
        targetPlayer.receiveCard(deck.dealCard());
    }

    /**
     * Deals multiple random cards to the specified player.
     * 
     * @param targetPlayer The player to whom the cards are dealt
     * @param n The number of cards to deal
     */
    public void dealMultipleRandomCardsToPlayer(Player targetPlayer, int n) {
        for (Card card : deck.drawMultipleRandomCards(n)) {
            targetPlayer.receiveCard(card);
        }
    }

    /**
     * Displays the hand of the specified player. Optionally hides the second card for the dealer.
     * 
     * @param targetPlayer The player whose hand is displayed
     * @param hideSecondCard Whether to hide the second card
     */

    /**
     * Reveals the dealer's full hand and updates the view.
     */
    public void revealDealerHand() {
        if (hiddenCard != null) {
            dealer.receiveCard(hiddenCard); // Add hidden card back to the hand
            hiddenCard = null; // Remove reference to avoid duplicate addition
        }
       }

    /**
     * Returns the hidden card for the dealer.
     * 
     * @return The hidden card
     */
    public Card getHiddenCard() {
        return hiddenCard;
    }

    /**
     * Returns the dealer instance.
     * 
     * @return The dealer instance
     */
    public Player getDealer() {
        return dealer;
    }

    /**
     * Resets the game by clearing hands and reinitializing the deck.
     * 
     * @param Player The player whose hand is reset
     */
    public void resetGame(ArrayList<Player> players) {
        for (Player player : players) {
            dealInitialCards(player, false); // Player receives two visible cards
        }
        dealer.reset(); // Reset dealer’s hand
        this.deck = new Deck(); // Reinitialize the deck
    }
}

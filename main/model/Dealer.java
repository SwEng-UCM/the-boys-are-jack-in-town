package main.model;

import java.util.List;

/**
 * Manages the game flow, dealing cards and handling player and dealer actions.
 */
public class Dealer {
    private Deck deck; // The deck of cards used in the game
    private final Player player; // The human player
    private final Player dealer; // The dealer (AI opponent)

    public Dealer() {
        this.deck = new Deck();
        this.player = new Player();
        this.dealer = new Player();
    }

    /**
     * Starts a new game by dealing two initial cards to both the player and dealer.
     */
    public void startNewGame() {
        player.receiveCard(deck.dealCard());
        dealer.receiveCard(deck.dealCard());
        player.receiveCard(deck.dealCard());
        dealer.receiveCard(deck.dealCard());
    }

    /** Deals one card to the player. */
    public void dealCardToPlayer() {
        player.receiveCard(deck.dealCard());
    }

    /** Deals one card to the dealer. */
    public void dealCardToDealer() {
        dealer.receiveCard(deck.dealCard());
    }

    /** Deals multiple random cards to the player. */
    public void dealMultipleRandomCardsToPlayer(int n) {
        for (Card card : deck.drawMultipleRandomCards(n)) {
            player.receiveCard(card);
        }
    }

    /** Deals multiple random cards to the dealer. */
    public void dealMultipleRandomCardsToDealer(int n) {
        for (Card card : deck.drawMultipleRandomCards(n)) {
            dealer.receiveCard(card);
        }
    }

    /** Displays the player's hand. */
    public void showPlayerHand() {
        player.showHand();
    }

    /**
     * Displays the dealer's hand. The second card can be hidden if required.
     */
    public void showDealerHand(boolean hideSecondCard) {
        if (hideSecondCard) {
            System.out.println("\nDealer's Hand:");
            System.out.println(dealer.getHand().get(0) + " [Hidden]");
        } else {
            dealer.showHand();
        }
    }

    /** Reveals the dealer's full hand and updates the view. */
    public void revealDealerHand() {
        System.out.println("\nDealer reveals their full hand:");
        showDealerHand(false);
    }

    /** Displays the dealer's hand without hiding any cards. */
    public void showDealerHand() {
        dealer.showHand();
    }

    /** Returns the player instance. */
    public Player getPlayer() {
        return player;
    }

    /** Returns the dealer instance. */
    public Player getDealer() {
        return dealer;
    }

    /** Resets the game by clearing hands and reinitializing the deck. */
    public void resetGame() {
        player.reset();
        dealer.reset();
        this.deck = new Deck();
    }
}

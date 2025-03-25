package main.model;

/**
 * The Dealer class represents the dealer in the Blackjack game.
 * It is responsible for managing the deck of cards, dealing cards to players,
 * and controlling the game flow.
 */
public class Dealer {
    private Deck deck; // The deck of cards used in the game
    private final Player dealer; // The dealer (AI opponent)
    private Card hiddenCard; // Stores the dealer's hidden card

    public Dealer() {
        this.deck = new Deck();
        this.dealer = new Player("Dealer", 1000); // Assuming dealer has a name and balance
    }

    /**
     * Starts a new game by dealing two initial cards to both the player and dealer.
     * 
     * @param player The player to whom cards are dealt
     */
    public void startNewGame(Player player) {
        dealInitialCards(dealer, true); // Dealer receives one hidden card
        dealInitialCards(player, false); // Player receives two visible cards
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
    public void showHand(Player targetPlayer, boolean hideSecondCard) {
        if (targetPlayer == dealer && hideSecondCard) {
            System.out.println("\nDealer's Hand:");
            System.out.println(dealer.getHand().get(0) + " [Hidden]"); // Show the first card and hide the second
        } else {
            targetPlayer.showHand();
        }
    }

    /**
     * Reveals the dealer's full hand and updates the view.
     */
    public void revealDealerHand() {
        if (hiddenCard != null) {
            dealer.receiveCard(hiddenCard); // Add hidden card back to the hand
            hiddenCard = null; // Remove reference to avoid duplicate addition
        }
        System.out.println("\nDealer reveals their full hand:");
        showHand(dealer, false); // Show the full hand without hiding any cards
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
     * @param player The player whose hand is reset
     */
    public void resetGame(Player player) {
        player.reset(); // Reset player’s hand
        dealer.reset(); // Reset dealer’s hand
        this.deck = new Deck(); // Reinitialize the deck
    }
}

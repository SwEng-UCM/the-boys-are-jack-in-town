package main.model;

public class Dealer {
    private Deck deck; // The deck of cards used in the game
    private final Player player; // The human player
    private final Player dealer; // The dealer (AI opponent)
    private Card hiddenCard; // Stores the dealer's hidden card

    public Dealer() {
        this.deck = new Deck();
        this.player = new Player();
        this.dealer = new Player();
    }

    /**
     * Starts a new game by dealing two initial cards to both the player and dealer.
     */
    public void startNewGame() {
        dealInitialCards(player, false);
        dealInitialCards(dealer, true); // Dealer receives one hidden card
        player.receiveCard(deck.dealCard()); // Player gets their second card
    }

    /**
     * Deals two cards to the given player. Optionally hides the second card.
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
     */
    public void dealCardToPlayer(Player targetPlayer) {
        targetPlayer.receiveCard(deck.dealCard());
    }

    /**
     * Deals multiple random cards to the specified player.
     */
    public void dealMultipleRandomCardsToPlayer(Player targetPlayer, int n) {
        for (Card card : deck.drawMultipleRandomCards(n)) {
            targetPlayer.receiveCard(card);
        }
    }

    /**
     * Displays the hand of the specified player. Optionally hides the second card for the dealer.
     */
    public void showHand(Player targetPlayer, boolean hideSecondCard) {
        if (targetPlayer == dealer && hideSecondCard) {
            System.out.println("\nDealer's Hand:");
            System.out.println(dealer.getHand().get(2) + " [Hidden]");
        } else {
            targetPlayer.showHand();
        }
    }

    /**
     * Reveals the dealer's full hand and updates the view.
     */
    public void revealDealerHand() {
        dealer.receiveCard(hiddenCard); // Add hidden card back to the hand
        hiddenCard = null; // Remove reference to avoid duplicate addition
        System.out.println("\nDealer reveals their full hand:");
        showHand(dealer, false); // Show the full hand without hiding any cards
    }

    /**
     * Returns the hidden card for the dealer.
     */
    public Card getHiddenCard() {
        return hiddenCard;
    }

    /**
     * Returns the player instance.
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Returns the dealer instance.
     */
    public Player getDealer() {
        return dealer;
    }

    /**
     * Resets the game by clearing hands and reinitializing the deck.
     */
    public void resetGame() {
        player.reset();
        dealer.reset();
        this.deck = new Deck();
    }
}

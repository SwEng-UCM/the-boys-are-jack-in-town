package main.model;

import java.util.List;


public class Dealer {
    private  Deck deck;
    private final Player player;
    private final Player dealer;

    public Dealer() {
        this.deck = new Deck();
        this.player = new Player();
        this.dealer = new Player();
    }

    public void startNewGame() {
        player.receiveCard(deck.dealCard());
        dealer.receiveCard(deck.dealCard());
        player.receiveCard(deck.dealCard());
        dealer.receiveCard(deck.dealCard());
    }

    public void dealCardToPlayer() {
        player.receiveCard(deck.dealCard());
    }

    public void dealCardToDealer() {
        dealer.receiveCard(deck.dealCard());
    }


    public void dealMultipleRandomCardsToPlayer(int n) {
        List<Card> cards = deck.drawMultipleRandomCards(n);
        for (Card card : cards) {
            player.receiveCard(card);
        }
    }

    public void dealMultipleRandomCardsToDealer(int n) {
        List<Card> cards = deck.drawMultipleRandomCards(n);
        for (Card card : cards) {
            dealer.receiveCard(card);
        }
    }

    public void showPlayerHand() {
        player.showHand();
    }
    public void showDealerHand(boolean hideSecondCard) {
        if (hideSecondCard) {
            System.out.println("\nDealer's Hand:");
            System.out.println(dealer.getHand().get(0) + " [Hidden]");
        } else {
            dealer.showHand();
        }
    }
    public void revealDealerHand() {
        System.out.println("\nDealer reveals their full hand:");
        showDealerHand(false);
    }

    public void showDealerHand() {
        dealer.showHand();
    }

    public Player getPlayer() {
        return player;
    }

    public Player getDealer() {
        return dealer;
    }

    public void resetGame() {
        player.reset();
        dealer.reset();
        this.deck = new Deck();

        
    }
}
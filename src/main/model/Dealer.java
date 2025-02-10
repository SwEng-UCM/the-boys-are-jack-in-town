package main.model;

import java.util.List;

public class Dealer {
    private final Deck deck;
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

    public void showDealerHand() {
        dealer.showHand();
    }

    public Player getPlayer() {
        return player;
    }

    public Player getDealer() {
        return dealer;
    }
}
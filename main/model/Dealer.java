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

   public void decideWinner(int dealerTotal, int playerTotal) {
        // all face cards are 10, ace is 11 unless it gives dealer or player an excess of 21, in which case it gives 1.

       if(dealerTotal > playerTotal && dealerTotal <= 21) {
           System.out.println("Dealer wins!");
       }

       else if(playerTotal > dealerTotal && playerTotal <= 21) {
           System.out.println("Player wins!");
       }

       else{
           // A tie is a Standoff and nobody loses. After each player is satisfied with the cards he has asked for,
           // the Dealer turns up his hidden card. If he has 16 or less he must draw additional cards until he reaches
           // 17 or over. If he has 17 or more he does not take any more cards.
       }
    }


}
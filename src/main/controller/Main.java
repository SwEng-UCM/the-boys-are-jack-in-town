package main.controller;

import main.model.Dealer;
import main.model.Deck;
import main.view.View;

public class Main {

    public static void main(String[] args) {
        View view = new View();
        Dealer dealer = new Dealer();

        view.showMessage("Starting a new game...");
        dealer.startNewGame();

        view.showPlayerHand(dealer.getPlayer());
        view.showDealerHand(dealer.getDealer());

        view.showMessage("\nDealing a new card to the player...");
        dealer.dealCardToPlayer();

        view.showMessage("\nPlayer's hand after receiving a new card:");
        view.showPlayerHand(dealer.getPlayer());
        
        view.showMessage("\nPlayer's score after receiving a new card:");
        view.showPlayerScore(dealer.getPlayer());

    //    Deck standard = new Deck();
    //    standard.prettyPrint();
    }
}
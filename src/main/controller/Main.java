package main.controller;

import main.model.Dealer;
import main.model.Deck;
import main.view.View;

public class Main {
    
    public static void main(String[] args) {
        GameManager gameManager = new GameManager();
        gameManager.startGame();
    }
        // View view = new View();
        // Dealer dealer = new Dealer();

        // view.showMessage("Starting a new game...");
        // dealer.startNewGame();

        // view.showPlayerHand(dealer.getPlayer());
        // view.showDealerHand(dealer.getDealer());

        // view.showMessage("\nDealing a new card to the player...");
        // dealer.dealCardToPlayer();

        // view.showMessage("\nPlayer's hand after receiving a new card:");
        // view.showPlayerHand(dealer.getPlayer());
        
        // view.showMessage("\nPlayer's score after receiving a new card:");
        // view.showPlayerScore(dealer.getPlayer());

        // view.showMessage("\nDealing three more cards to the dealer...");
        // dealer.dealMultipleRandomCardsToDealer(3);
        // view.showDealerHand(dealer.getDealer());

       
        // view.showMessage("\nFinal hands:");
        // view.showPlayerHand(dealer.getPlayer());
        // view.showDealerHand(dealer.getDealer());

        //view.showMessage("\nStart new game or terminate?");
        

    //    Deck standard = new Deck();
    //    standard.prettyPrint();
    // }
}
package main.controller;

import main.model.Dealer;

public class GameManager {
        private final Dealer dealer;
    
        public GameManager() {
            this.dealer = new Dealer();
        }
    
        public void startNewRound() {
            System.out.println("Starting a new round...");
            dealer.resetGame();  // Clears hands for a new round
            dealer.startNewGame();
        }
    
        public void showGameState() {
            System.out.println("Player's Hand:");
            dealer.showPlayerHand();
            System.out.println("Dealer's Hand:");
            dealer.showDealerHand();
        }
}

    



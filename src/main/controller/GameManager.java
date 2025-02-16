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

            if (checkBlackjack()) {
                return;
            }

            showGameState();
        }

        private boolean checkBlackjack() {
            int playerScore = dealer.getPlayer().calculateScore();
            int dealerScore = dealer.getDealer().calculateScore();
        
            boolean playerHasBlackjack = (playerScore == 21 && dealer.getPlayer().getHand().size() == 2);
            boolean dealerHasBlackjack = (dealerScore == 21 && dealer.getDealer().getHand().size() == 2);
        
            if (playerHasBlackjack && dealerHasBlackjack) {
                System.out.println("It's a tie! Both have Blackjack.");
                return true;
            } else if (playerHasBlackjack) {
                System.out.println("Player wins with a Blackjack");
                return true;
            } else if (dealerHasBlackjack) {
                System.out.println("Dealer wins with a Blackjack");
                return true;
            }
        
            return false;
        }
    
        public void showGameState() {
            System.out.println("Player's Hand:");
            dealer.showPlayerHand();
            System.out.println("Dealer's Hand:");
            dealer.showDealerHand();
        }


    
}

    



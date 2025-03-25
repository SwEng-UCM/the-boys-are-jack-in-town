package main.controller;

public class BettingManager {
    private int playerBalance;
    private int dealerBalance;
    private int playerBet; // Player's current bet
    private int dealerBet; // Dealer's current bet

    public BettingManager(int initialPlayerBalance, int initialDealerBalance) {
        this.playerBalance = initialPlayerBalance;
        this.dealerBalance = initialDealerBalance;
        this.playerBet = 0;
        this.dealerBet = 0;
    }

    // player's bet
    public boolean placeBet(int betAmount) { 
        if (betAmount > 0 && betAmount <= playerBalance) {
            playerBalance -= betAmount; 
            playerBet = betAmount; 
            return true;
        }
        return false; 
    }

    // dealer's bet
    public void placeDealerBet(int betAmount) {
        if (betAmount > 0 && betAmount <= dealerBalance) {
            dealerBalance -= betAmount; 
            dealerBet = betAmount; 
        } // commit
    }

    public void playerWins() {
        // Player gets their bet back plus the dealer's bet
        playerBalance += playerBet + dealerBet;
        // Dealer loses their bet (already deducted when placed)
        // Reset bets
        resetBets();
    }

    // Dealer wins the round
    public void dealerWins() {
        // Dealer gets their bet back plus the player's bet
        dealerBalance += dealerBet + playerBet;
        // Player loses their bet (already deducted when placed)
        // Reset bets
        resetBets();
    }

    // Tie (push) - both player and dealer get their bets back
    public void tie() {
        playerBalance += playerBet; // Return the player's bet
        dealerBalance += dealerBet; // Return the dealer's bet
        // Reset bets
        resetBets();
    }

    // Player gets a Blackjack (typically pays 3:2)
    public void playerBlackjack() {
        int winnings = (int) (playerBet * 1.5); // 3:2 payout for Blackjack
        playerBalance += playerBet + winnings; // Original bet + winnings
        dealerBalance -= winnings; // Dealer pays the winnings
        // Reset bets
        resetBets();
    }

    // Reset bets after a round
    public void resetBets() {
        playerBet = 0;
        dealerBet = 0;
    }

    // Getters for balances and bets
    public int getPlayerBalance() {
        return playerBalance;
    }

    public int getDealerBalance() {
        return dealerBalance;
    }

    public int getPlayerBet() {
        return playerBet;
    }

    public int getDealerBet() {
        return dealerBet;
    }
}
// commit
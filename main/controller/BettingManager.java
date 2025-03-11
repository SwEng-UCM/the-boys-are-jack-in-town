package main.controller;

public class BettingManager {
    private int playerBalance;
    private int dealerBalance;
    private int currentBet; // Player's bet
    private int dealerBet; // Dealer's bet

    public BettingManager(int initialPlayerBalance, int initialDealerBalance) {
        this.playerBalance = initialPlayerBalance;
        this.dealerBalance = initialDealerBalance;
        this.currentBet = 0;
        this.dealerBet = 0;
    }

    // player's bet
    public boolean placeBet(int betAmount) {
        if (betAmount > 0 && betAmount <= playerBalance) {
            playerBalance -= betAmount;
            currentBet = betAmount;
            return true;
        }
        return false;
    }

    // dealer's bet
    public void placeDealerBet(int betAmount) {
        if (betAmount > 0 && betAmount <= dealerBalance) {
            dealerBalance -= betAmount;
            dealerBet = betAmount;
        }
    }

    public void playerWins() {
        // Player gets their bet back plus the dealer's bet
        playerBalance += currentBet + dealerBet;
        // Dealer loses their bet
        dealerBalance -= dealerBet;
        // Reset bets
        currentBet = 0;
        dealerBet = 0;
    }

    public void dealerWins() {
        // Dealer gets their bet back plus the player's bet
        dealerBalance += dealerBet + currentBet;
        // Player loses their bet
        playerBalance -= currentBet;
        // Reset bets
        currentBet = 0;
        dealerBet = 0;
    }

    public void tie() {
        playerBalance += currentBet; // Return the bet to the player
        dealerBalance += dealerBet; // Return the bet to the dealer
        currentBet = 0;
        dealerBet = 0; // Reset dealer's bet
    }

    public void resetBet() {
        currentBet = 0;
        dealerBet = 0;
    }

    public int getPlayerBalance() {
        return playerBalance;
    }

    public int getDealerBalance() {
        return dealerBalance;
    }

    public int getDealerBet() {
        return dealerBet;
    }

    public int getCurrentBet() {
        return currentBet;
    }
}
package main.controller;

public class BettingManager {
    private int playerBalance;
    private int currentBet;

    public BettingManager(int initialBalance) {
        this.playerBalance = initialBalance;
        this.currentBet = 0;
    }

    public boolean placeBet(int betAmount) {
        if (betAmount > 0 && betAmount <= playerBalance) {
            playerBalance -= betAmount;
            currentBet = betAmount;
            return true;
        }
        return false;
    }

    public void playerWins() {
        playerBalance += currentBet * 2;
        currentBet = 0;
    }

    public void tie() {
        playerBalance += currentBet;
        currentBet = 0;
    }

    public void resetBet() {
        currentBet = 0;
    }

    public int getPlayerBalance() {
        return playerBalance;
    }

    public int getCurrentBet() {
        return currentBet;
    }
}
package main.controller;

import main.model.Player;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The BettingManager class handles all betting logic for players and the dealer.
 * It tracks player balances, current bets, and manages payouts and bet resets after each round.
 */
public class BettingManager {
    private Map<String, Integer> playerBalances; // Each player's balance
    private Map<String, Integer> playerBets; // Each player's current bet
    private int dealerBalance;
    private int dealerBet;

    /**
     * Constructs a BettingManager for the given list of players, setting initial balances for players and the dealer.
     *
     * @param players the list of players participating
     * @param initialPlayerBalance the starting balance for each player
     * @param initialDealerBalance the starting balance for the dealer
     */
    public BettingManager(List<Player> players, int initialPlayerBalance, int initialDealerBalance) {
        playerBalances = new HashMap<>();
        playerBets = new HashMap<>();

        for (Player player : players) {
            playerBalances.put(player.getName(), initialPlayerBalance);
            playerBets.put(player.getName(), 0);
        }

        this.dealerBalance = initialDealerBalance;
        this.dealerBet = 0;
    }

    /**
     * Places a bet for a specific player.
     * @param playerName The name of the player placing the bet.
     * @param betAmount The amount to bet.
     * @return true if the bet is valid and placed, false otherwise.
     */
    public boolean placeBet(String playerName, int betAmount) {
        if (playerBalances.containsKey(playerName) && betAmount > 0 && betAmount <= playerBalances.get(playerName)) {
            playerBalances.put(playerName, playerBalances.get(playerName) - betAmount);
            playerBets.put(playerName, betAmount);
            return true;
        }
        return false;
    }

    /**
     * Places a bet for the dealer.
     * @param betAmount The amount the dealer bets.
     */
    public void placeDealerBet(int betAmount) {
        if (betAmount > 0 && betAmount <= dealerBalance) {
            dealerBalance -= betAmount;
            dealerBet = betAmount;
        }

    }

    /**
     * Awards winnings to a player.
     * @param playerName The winning player's name.
     */
    public void playerWins(String playerName) {
        Integer bet = playerBets.get(playerName);
        if (bet == null || bet == 0) {
            // No bet to pay out => just ignore safely
            return;
        }
        playerBalances.put(playerName, playerBalances.get(playerName) + bet + dealerBet);
        resetPlayerBet(playerName);
    }
    
    
    /**
     * Dealer wins, so the dealer collects the player's bets.
     * @param playerName The losing player's name.
     */
    public void dealerWins(String playerName) {
        int bet = playerBets.getOrDefault(playerName, 0);
        dealerBalance += bet;
        resetPlayerBet(playerName);
    }

    /**
     * Player loses — no payout.
     */
    public void playerLoses(String playerName) {
        resetPlayerBet(playerName);
    }

    /**
     * Handles a tie (push), returning bets to both the player and dealer.
     * @param playerName The player's name who tied.
     */
    public void tie(String playerName) {
        if (!playerBets.containsKey(playerName)) {
            return;
        }
    
        int bet = playerBets.get(playerName);
        if (bet <= 0) return;
    
        playerBalances.put(playerName, playerBalances.getOrDefault(playerName, 0) + bet);
        dealerBalance += dealerBet;
        resetPlayerBet(playerName);
    }
    

    /**
     * Player wins with a Blackjack (3:2 payout).
     * @param playerName The player's name who got Blackjack.
     */
    public void playerBlackjack(String playerName) {
        int bet = playerBets.getOrDefault(playerName, 0);
        int winnings = (int) (bet * 1.5); // 3:2 payout
        playerBalances.put(playerName, playerBalances.get(playerName) + bet + winnings);
        dealerBalance -= winnings;
        resetPlayerBet(playerName);
    }

    /**
     * Resets the bet for a specific player after a round.
     * @param playerName The player's name whose bet should be reset.
     */
    private void resetPlayerBet(String playerName) {
        playerBets.put(playerName, 0);
    }

    /**
     * Resets all bets after a round.
     */
    public void resetAllBets() {
        playerBets.replaceAll((key, value) -> 0);
        dealerBet = 0;
    }

    /**
     * Retrieves the current balance of a player.
     *
     * @param playerName the name of the player
     * @return the player's current balance
     */
    public int getPlayerBalance(String playerName) {
        return playerBalances.getOrDefault(playerName, 0);
    }

    /**
     * Retrieves the current balance of the dealer.
     *
     * @return the dealer's current balance
     */
    public int getDealerBalance() {
        return dealerBalance;
    }

    /**
     * Retrieves the current bet placed by a player.
     *
     * @param playerName the name of the player
     * @return the player's current bet
     */
    public int getPlayerBet(String playerName) {
        return playerBets.getOrDefault(playerName, 0);
    }

    /**
     * Retrieves the current bet placed by the dealer.
     *
     * @return the dealer's current bet
     */
    public int getDealerBet() {
        return dealerBet;
    }
}

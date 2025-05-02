package main.controller;

import java.util.ArrayList;
import java.util.List;
import main.model.Card;
import main.model.Deck;
import main.model.Player;

/**
 * Manages all players in a multiplayer Blackjack game, including tracking
 * current turn, player status, and exposing aggregated information.
 */
public class PlayerManager {
    private ArrayList<Player> players = new ArrayList<>();
    private int currentPlayerIndex = 0;

    /**
     * Returns the list of all players.
     *
     * @return the current list of players
     */
    public ArrayList<Player> getPlayers() {
        return this.players;
    }

    /**
     * Sets the list of players.
     *
     * @param players the new list of players
     */
    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }

    /**
     * Returns the current player whose turn it is.
     *
     * @return the current Player, or {@code null} if the index is invalid
     */
    public Player getCurrentPlayer() {
        if (currentPlayerIndex < players.size()) {
            return players.get(currentPlayerIndex);
        }
        return null;
    }

    /**
     * Checks if the current player is still active in the round.
     * 
     * @return {@code true} if the player hasn't stood or busted; {@code false} otherwise
     */
    public boolean isCurrentPlayerStillInRound() {
        Player currentPlayer = getCurrentPlayer();
        if (currentPlayer == null) return false;
        return !currentPlayer.hasStood() && currentPlayer.calculateScore() <= 21;
    }

    /**
     * Gets the index of the current player.
     *
     * @return the current player's index
     */
    public int getCurrentPlayerIndex() {
        return this.currentPlayerIndex;
    }

    /**
     * Sets the index of the current player.
     *
     * @param index the new index for the current player
     */
    public void setCurrentPlayerIndex(int index) {
        this.currentPlayerIndex = index;
    }

    /**
     * Checks if the given player object is the current player.
     *
     * @param player the player to check
     * @return {@code true} if the given player is the current player
     */
    public boolean isCurrentPlayer(Player player) {
        return !players.isEmpty() &&
               currentPlayerIndex >= 0 &&
               currentPlayerIndex < players.size() &&
               players.get(currentPlayerIndex).equals(player);
    }

    /**
     * Checks if the current player has the given name.
     *
     * @param playerName the name to check
     * @return {@code true} if the name matches the current player
     */
    public boolean isCurrentPlayer(String playerName) {
        if (players.isEmpty() || currentPlayerIndex < 0 || currentPlayerIndex >= players.size()) {
            return false;
        }
        Player currentPlayer = players.get(currentPlayerIndex);
        return currentPlayer != null && currentPlayer.getName().equalsIgnoreCase(playerName);
    }

    /**
     * Returns the score of the specified player.
     *
     * @param currentPlayer the player whose score to retrieve
     * @return the player's score
     */
    public int getPlayerScore(Player currentPlayer) {
        return currentPlayer.calculateScore();
    }

    /**
     * Returns the current bet of the specified player.
     *
     * @param currentPlayer the player whose bet to retrieve
     * @return the player's current bet
     */
    public int getPlayerBet(Player currentPlayer) {
        return currentPlayer.getCurrentBet();
    }

    /**
     * Advances the turn to the next player.
     */
    public void advanceToNextPlayer() {
        currentPlayerIndex++;
    }

    /**
     * Finds a player by their name.
     *
     * @param name the player's name
     * @return the Player object or {@code null} if not found
     */
    public Player getPlayerByName(String name) {
        return players.stream()
            .filter(p -> p.getName().equals(name))
            .findFirst()
            .orElse(null);
    }

    /**
     * Returns the hand of the given player as a string.
     *
     * @param player the player whose hand to display
     * @return a string representation of the hand
     */
    public String getPlayerHand(Player player) {
        return player.getHand().toString();
    }

    /**
     * Returns the balance of the given player.
     *
     * @param player the player whose balance to retrieve
     * @return the player's balance
     */
    public int getPlayerBalance(Player player) {
        return player.getBalance();
    }

    /**
     * Checks if there are more players left in the round.
     *
     * @return {@code true} if there's another player after the current one
     */
    public boolean hasNextPlayer() {
        return currentPlayerIndex < players.size() - 1;
    }

    /**
     * Returns the scores of all players.
     *
     * @return a list of scores
     */
    public List<Integer> getPlayerScores() {
        List<Integer> pScores = new ArrayList<>();
        for (Player p : players) {
            pScores.add(p.calculateScore());
        }
        return pScores;
    }

    /**
     * Returns the balances of all players.
     *
     * @return a list of balances
     */
    public List<Integer> getPlayerBalances() {
        List<Integer> pBalances = new ArrayList<>();
        for (Player p : players) {
            pBalances.add(p.getBalance());
        }
        return pBalances;
    }

    /**
     * Returns the current bets of all players.
     *
     * @return a list of bets
     */
    public List<Integer> getPlayerBets() {
        List<Integer> pBets = new ArrayList<>();
        for (Player p : players) {
            pBets.add(p.getCurrentBet());
        }
        return pBets;
    }

    /**
     * Returns the hands of all players.
     *
     * @return a list of player hands
     */
    public List<List<Card>> getPlayerHands() {
        List<List<Card>> playerHands = new ArrayList<>();
        for (Player p : players) {
            playerHands.add(p.getHand());
        }
        return playerHands;
    }

    /**
     * Adds a new player with the given name and balance.
     *
     * @param name           the player's name
     * @param initialBalance the player's starting balance
     */
    public void addPlayer(String name, int initialBalance) {
        players.add(new Player(name, initialBalance));
    }

    /**
     * Encapsulates summary information about a player.
     */
    public static class PlayerInfo {
        public final String name;
        public final int score;
        public final int bet;
        public final int balance;
        public final List<Card> hand;

        public PlayerInfo(String name, int score, int bet, int balance, List<Card> hand) {
            this.name = name;
            this.score = score;
            this.bet = bet;
            this.balance = balance;
            this.hand = hand;
        }
    }

    /**
     * Returns a summary of all player information.
     *
     * @return a list of {@code PlayerInfo} objects
     */
    public List<PlayerInfo> getAllPlayerInfo() {
        List<PlayerInfo> infoList = new ArrayList<>();
        for (Player p : players) {
            infoList.add(new PlayerInfo(
                p.getName(),
                p.calculateScore(),
                p.getCurrentBet(),
                p.getBalance(),
                new ArrayList<>(p.getHand()) // defensive copy
            ));
        }
        return infoList;
    }

    /**
     * Sets the scores for all players.
     *
     * @param scores a list of scores to assign to each player
     */
    public void setPlayerScores(List<Integer> scores) {
        for (int i = 0; i < Math.min(scores.size(), players.size()); i++) {
            players.get(i).setScore(scores.get(i));
        }
    }
}

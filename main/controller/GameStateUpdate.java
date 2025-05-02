package main.controller;

import main.model.Player;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents an immutable snapshot of the game's state used for synchronization
 * between the server and clients in a multiplayer Blackjack game.
 * <p>
 * This class includes a defensive copy of all players and the dealer to ensure
 * that the state cannot be externally modified after construction.
 */
public class GameStateUpdate implements Serializable {
    private static final long serialVersionUID = 1L;

    private final List<Player> players;
    private final Player dealer;
    private final int currentPlayerIndex;
    private final boolean gameOver;

    /**
     * Constructs a new {@code GameStateUpdate} with the given players, dealer,
     * current player index, and game over flag.
     *
     * @param players            the list of players (copied defensively)
     * @param dealer             the dealer (copied defensively)
     * @param currentPlayerIndex index of the current player
     * @param gameOver           {@code true} if the game is over, {@code false} otherwise
     */
    public GameStateUpdate(List<Player> players, Player dealer, 
                           int currentPlayerIndex, boolean gameOver) {
        this.players = new ArrayList<>(players); // Defensive copy
        this.dealer = new Player(dealer);        // Defensive copy
        this.currentPlayerIndex = currentPlayerIndex;
        this.gameOver = gameOver;
    }

    /**
     * Returns a copy of the list of players to maintain immutability.
     *
     * @return a new list containing copies of the players
     */
    public List<Player> getPlayers() {
        return new ArrayList<>(players); // Return a new list for immutability
    }

    /**
     * Returns a copy of the dealer to maintain immutability.
     *
     * @return a copy of the dealer
     */
    public Player getDealer() {
        return new Player(dealer); // Return a copy for immutability
    }

    /**
     * Returns the index of the current player.
     *
     * @return the current player index
     */
    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    /**
     * Returns whether the game is over.
     *
     * @return {@code true} if the game is over, {@code false} otherwise
     */
    public boolean isGameOver() {
        return gameOver;
    }

    /**
     * Returns a string representation of the game state update.
     *
     * @return a string containing current player index and game over status
     */
    @Override
    public String toString() {
        return "GameStateUpdate{" +
                "currentPlayerIndex=" + currentPlayerIndex +
                ", gameOver=" + gameOver +
                '}';
    }
}

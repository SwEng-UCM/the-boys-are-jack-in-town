package main.controller;

import main.model.Player;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GameStateUpdate implements Serializable {
    private final List<Player> players;
    private final Player dealer;
    private final int currentPlayerIndex;
    private final boolean gameOver;
    
    public GameStateUpdate(List<Player> players, Player dealer, 
                          int currentPlayerIndex, boolean gameOver) {
        // Create defensive copies to prevent external modification
        this.players = new ArrayList<>(players);
        this.dealer = new Player(dealer); // Assuming Player has a copy constructor
        this.currentPlayerIndex = currentPlayerIndex;
        this.gameOver = gameOver;
    }

    // Getters
    public List<Player> getPlayers() {
        return new ArrayList<>(players); // Return copy for immutability
    }

    public Player getDealer() {
        return new Player(dealer); // Return copy for immutability
    }

    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    @Override
    public String toString() {
        return "GameStateUpdate{" +
                "currentPlayerIndex=" + currentPlayerIndex +
                ", gameOver=" + gameOver +
                '}';
    }
}

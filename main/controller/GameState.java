package main.controller;

import main.model.*;
import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

/**
 * Represents the complete game state that can be saved and loaded.
 * Includes player data, dealer data, deck state, and difficulty level.
 */
public class GameState implements Serializable {
    private static final long serialVersionUID = 1L;

    // Game configuration
    private String currentDifficulty;
    
    // Game entities
    private List<Player> players;
    private Player dealer;
    private Deck deck;
    
    // Game progress
    private int currentPlayerIndex;
    private boolean gameOver;

    public GameState() {
        this.players = new ArrayList<>();
        this.dealer = new Player("Dealer", 1000);
        this.deck = new Deck();
        this.currentDifficulty = "Medium"; // Default difficulty
    }

    /**
     * Captures current state from GameManager
     */
    public GameState(GameManager manager) {
        this.currentDifficulty = manager.getDifficultyStrategy().getDifficultyName();
        this.players = new ArrayList<>(manager.getPlayers());
        this.dealer = new Player(manager.getDealer()); // Create copy of dealer
        this.deck = new Deck(manager.getDeck()); // Create copy of deck
        this.currentPlayerIndex = manager.getCurrentPlayerIndex();
        this.gameOver = manager.isGameOver();
    }

    /**
     * Restores difficulty setting to GameManager
     */
    public void restoreDifficulty(GameManager manager) {
        switch(this.currentDifficulty) {
            case "Easy":
                manager.setDifficultyStrategy(new EasyDifficulty());
                break;
            case "Medium":
                manager.setDifficultyStrategy(new MediumDifficulty());
                break;
            case "Hard":
                manager.setDifficultyStrategy(new HardDifficulty());
                break;
            default:
                manager.setDifficultyStrategy(new MediumDifficulty());
        }
    }

    /**
     * Restores complete game state to GameManager
     */
    public void restoreFullState(GameManager manager) {
        // Restore core entities
        manager.getPlayers().clear();
        manager.getPlayers().addAll(this.players);
        manager.getDealer().getHand().clear();
        manager.getDealer().getHand().addAll(this.dealer.getHand());
        
        // Restore game progress
        manager.setCurrentPlayerIndex(this.currentPlayerIndex);
        manager.setGameOver(this.gameOver);
        
        // Restore deck
        manager.getDeck().getCards().clear();
        manager.getDeck().getCards().addAll(this.deck.getCards());
        
        // Restore difficulty
        restoreDifficulty(manager);
    }

    // Getters
    public String getCurrentDifficulty() { return currentDifficulty; }
    public List<Player> getPlayers() { return players; }
    public Player getDealer() { return dealer; }
    public Deck getDeck() { return deck; }
    public int getCurrentPlayerIndex() { return currentPlayerIndex; }
    public boolean isGameOver() { return gameOver; }
}
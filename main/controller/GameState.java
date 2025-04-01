package main.controller;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

import main.model.Card;
import main.model.Deck;
import main.model.Player;

public class GameState implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<Player> players;
    private Player dealer;
    private Deck deck;

    // Default constructor
    public GameState() {
        this.players = new ArrayList<>();
        this.dealer = new Player("Dealer", 1000);
        this.deck = new Deck();
    }

    // Constructor to initialize state from GameManager
    public GameState(GameManager manager) {
        this.players = new ArrayList<>(manager.getPlayers());
        this.dealer = manager.getDealer();
        this.deck = manager.getDeck();
    }

    // Getters
    public List<Player> getPlayers() { return players; }
    public Player getDealer() { return dealer; }
    public Deck getDeck() { return deck; }
}


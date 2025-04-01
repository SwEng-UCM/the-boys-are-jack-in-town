package main.controller;

import java.io.Serializable;
import java.util.List;

import main.model.Card;
import main.model.Deck;
import main.model.Player;

public class GameState implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<Card> playerHand;
    private List<Card> dealerHand;
    private Deck deck;
    private int playerBalance;
    private int currentBet;
    private int dealerBalance;
    private int dealerBet;
    private int dealerScore;
    private int playerScore;
  
    // Default constructor 
    public GameState() {}

    // Constructor to initialize state from GameManager
    public GameState(GameManager manager) {
        this.playerHand = manager.getPlayerHandList();
        this.dealerHand = manager.getDealerHandList();
        this.deck = manager.getDeck();
        this.playerBalance = manager.getPlayerBalance();
        this.currentBet = manager.getPlayerBet();
        this.dealerBalance = manager.getDealerBalance();
        this.dealerBet = manager.getDealerBet();
        this.dealerScore = manager.getDealerScore();
        this.playerScore = manager.getPlayerScore();

    }

    // Getters for Jackson
    public List<Card> getPlayerHand() { return playerHand; }
    public List<Card> getDealerHand() { return dealerHand; }
    public Deck getDeck() { return deck; }
    public int getPlayerBalance() { return playerBalance; }
    public int getCurrentBet() { return currentBet; }
    public int getDealerBalance() { return dealerBalance; }
    public int getDealerBet() { return dealerBet; }
    public int getDealerScore() { return dealerScore; }
    public int getPlayerScore() { return playerScore; }
}


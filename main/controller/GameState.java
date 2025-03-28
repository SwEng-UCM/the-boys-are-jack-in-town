package main.controller;

import java.io.Serializable;
import java.util.List;

import main.model.Card;
import main.model.Deck;
import main.model.Player;

public class GameState implements Serializable {
    private static final long serialVersionUID = 1L;

    private GameState(GameManager manager) {
        this.manager = manager;
    };
    
    private GameManager manager;
    private List<Card> playerHand;
    private List<Card> dealerHand;
    private Deck deck;
    private int playerBalance;
    private int currentBet;

    // Getters and setters

    private int getDealerScore () {
        return manager.getDealerScore();
    }
    
    private int getDealerBalance () {
        return manager.getDealerBalance();
    }

    private int getDealerBet ( ){
        return manager.getDealerBet();
    }

    private int getPlayerScore (Player player){
        return player.calculateScore();
    }

    private int getPlayerBalance (Player player){
        return player.getBalance();
    }

    private int getPlayerBet (Player player){
        return player.getCurrentBet();
    }

    private List<Card> getPlayerHand(Player player){
        return player.getHand();
    }

    private List<Card> getDealerHand (Player dealer){
        return dealer.getHand();
    }


}

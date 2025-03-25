package main.controller;

import java.io.Serializable;
import java.util.List;

import main.model.Card;
import main.model.Deck;

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

    private int getPlayerScore (){
        return manager.getPlayerScore();
    }

    private int getPlayerBalance (){
        return manager.getPlayerBalance();
    }

    private int getPlayerBet (){
        return manager.getPlayerBet();
    }

    private String getPlayerHand(){
        return manager.getPlayerHand();
    }

    private String getDealerHand (){
        return manager.getDealerHand();
    }


}

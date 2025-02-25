package main.controller;

import main.model.Card;
import main.model.Deck;
import main.model.Player;
import main.view.BlackjackGUI;

public class GameManager {
    private Player player;
    private Player dealer;
    private Deck deck;
    private BlackjackGUI gui;
    private boolean gameOver;

    public GameManager() {
        this.player = new Player();
        this.dealer = new Player();
        this.deck = new Deck();
        this.gameOver = false;
    }

    public void setGui(BlackjackGUI gui) {
        this.gui = gui;
    }

    public void handlePlayerStand() {
        if (!gameOver) {
            dealerTurn();
            determineWinner();
            gui.updateGameState(player, dealer, gameOver);
        }
    }

    public void startNewGame() {
        player.reset();
        dealer.reset();
        deck = new Deck(); // Reset the deck for a new game
        gameOver = false;

        // Deal initial cards
        player.receiveCard(deck.dealCard());
        dealer.receiveCard(deck.dealCard());
        player.receiveCard(deck.dealCard());
        dealer.receiveCard(deck.dealCard());

        if (player.hasBlackjack()) {
            gui.updateGameMessage("Player has Blackjack! Player wins!");
            gui.updateGameState(player, dealer, true); // End the game
        } else if (dealer.hasBlackjack()) {
            gui.updateGameMessage("Dealer has Blackjack! Dealer wins!");
            gui.updateGameState(player, dealer, true); // End the game
        } else {
            gui.updateGameMessage("Game On! Your turn.");
            gui.updateGameState(player, dealer, false); // Continue the game
        }
    
        // Update game state immediately to show hands
        gui.updateGameState(player, dealer, gameOver);
        gui.updateGameMessage("Player's turn. Hit or Stand.");
    }

    public String getPlayerHand() {
        return player.getHand().toString();
    }

    public String getDealerHand() {
        if (gameOver) {
            return dealer.getHand().toString();
        }
        return dealer.getHand().get(0).toString() + " [Hidden]";
    }

    private void dealerTurn() {
        while (dealer.calculateScore() < 17) { // Dealer hits on 16 or below
            dealer.receiveCard(deck.dealCard());
        }
        checkDealerBust();
    }
    public void handlePlayerHit() {
        if (!gameOver && player.calculateScore() <= 21) {
            player.receiveCard(deck.dealCard());
            checkPlayerBust();
            gui.updateGameState(player, dealer, gameOver);
        }
    }
    
    public void checkPlayerBust() {
        if (player.calculateScore() > 21) {
            gameOver = true;
            gui.updateGameMessage("Player busts! Dealer wins.");
        }
    }

    private void checkDealerBust() {
        if (dealer.calculateScore() > 21) {
            gameOver = true;
            gui.updateGameMessage("Dealer busts! Player wins.");
        }
    }

    private void determineWinner() {
        if (!gameOver) {
            int playerScore = player.calculateScore();
            int dealerScore = dealer.calculateScore();

            if (playerScore > dealerScore) {
                gui.updateGameMessage("Player wins!");
            } else if (playerScore < dealerScore) {
                gui.updateGameMessage("Dealer wins!");
            } else {
                gui.updateGameMessage("It's a tie!");
            }
            gameOver = true;
        }
    }
}

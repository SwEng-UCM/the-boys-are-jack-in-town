package main.controller;

import main.model.Card;
import main.model.Deck;
import main.model.Player;
import main.view.BlackjackGUI;
import javax.swing.JOptionPane;

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
        player.receiveCard(handleSpecialCard(deck.dealCard(), player));
        dealer.receiveCard(handleSpecialCard(deck.dealCard(), dealer));
        player.receiveCard(handleSpecialCard(deck.dealCard(), player));
        dealer.receiveCard(handleSpecialCard(deck.dealCard(), dealer));
    
        if (player.hasBlackjack()) {
            gui.updateGameMessage("Player has Blackjack! Player wins!");
            gameOver = true;
            gui.updateGameState(player, dealer, true);
            return;
        } else if (dealer.hasBlackjack()) {
            gui.updateGameMessage("Dealer has Blackjack! Dealer wins!");
            gameOver = true;
            gui.updateGameState(player, dealer, true);
            return;
        } else {
            gui.updateGameMessage("Game On! Your turn.");
            gui.updateGameState(player, dealer, false);
        }
    
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
        while (dealer.calculateScore() < 17) {
            dealer.receiveCard(handleSpecialCard(deck.dealCard(), dealer));
        }
        checkDealerBust();
    }

    public void handlePlayerHit() {
        if (!gameOver && player.calculateScore() <= 21) {
            player.receiveCard(handleSpecialCard(deck.dealCard(), player));
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
    private Card handleSpecialCard(Card card, Player recipient) {
        switch (card.getType()) {
            case BLACKJACK_BOMB:
                gui.updateGameMessage("Blackjack Bomb! Player wins instantly! 💣");
                gameOver = true;
                break;
            case SPLIT_ACE:
                gui.updateGameMessage("Split Ace! Automatically splitting your hand! ♠♠");
                splitHand(recipient);
                break;
            case JOKER_WILD:
                int wildValue = gui.promptJokerWildValue();  // Call your GUI prompt method
                card.setWildValue(wildValue);
                gui.updateGameMessage("Joker Wild set to " + wildValue + " 🤡");
                break;
            default:
                // For standard cards, do nothing special.
                break;
        }
        return card;
    }
    

    private void splitHand(Player player) {
        // Logic to split the player's hand (assuming it’s implemented in Player class)
    }
}

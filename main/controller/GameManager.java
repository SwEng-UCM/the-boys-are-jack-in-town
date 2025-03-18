package main.controller;

import main.model.Card;
import main.model.Deck;
import main.model.Player;
import main.view.BlackjackGUI;
import main.view.Texts;

import static main.view.BlackJackMenu.language;

import javax.swing.SwingUtilities;

/*
 * Singleton class.
 */

/**
 * The GameManager class is responsible for managing the game state and logic.
 * It interacts with the Player, Deck, and BlackjackGUI classes to handle player actions,
 * dealer actions, and determine the game outcome.
 */
public class GameManager {
    private static GameManager instance;

    private Player player;
    private Player dealer;
    private Deck deck;
    private BlackjackGUI gui;
    private boolean gameOver;
    private BettingManager bettingManager;

    private GameManager() {
        this.player = new Player();
        this.dealer = new Player();
        this.deck = new Deck();
        this.gameOver = false;
        this.bettingManager = new BettingManager(1000, 1000); // Initial balance
    }

    // Public method to provide access to the singleton instance
    public static GameManager getInstance() {
        if (instance == null) {
            instance = new GameManager();
        }
        return instance;
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
    public void resetBettingManager(int playerBalance, int dealerBalance) {
        this.bettingManager = new BettingManager(playerBalance, dealerBalance);
    }
    

    public void startNewGame() {
        // 🔍 Check if player or dealer has a balance of 0 or less
        if (bettingManager.getPlayerBalance() <= 0) {
            gui.showGameOverMessage("Game Over! You ran out of money! 😢");
            return;
        }
        if (bettingManager.getDealerBalance() <= 0) {
            gui.showGameOverMessage("Congratulations! The dealer ran out of money! 🎉");
            return;
        }
    
        // Reset hands and deck for a new game
        player.reset();
        dealer.reset();
        deck = new Deck();
        gameOver = false;
    
        bettingManager.resetBets(); // Reset bets before new game
        
        // Place dealer's bet (e.g., 10% of dealer's balance)
        int dealerBet = (int) (bettingManager.getDealerBalance() * 0.1);
        bettingManager.placeDealerBet(dealerBet);
    
        // Enable betting
        gui.enableBetting();
        
        // Deal initial cards
        player.receiveCard(handleSpecialCard(deck.dealCard(), player));
        dealer.receiveCard(handleSpecialCard(deck.dealCard(), dealer));
        player.receiveCard(handleSpecialCard(deck.dealCard(), player));
        dealer.receiveCard(handleSpecialCard(deck.dealCard(), dealer));
    
        // Check for Blackjack
        if (player.hasBlackjack()) {
            gui.updateGameMessage("🎉 Player has Blackjack!");
            bettingManager.playerWins();
            gameOver = true;
            return;
        } else if (dealer.hasBlackjack()) {
            gui.updateGameMessage("🎲 Dealer has Blackjack!");
            bettingManager.dealerWins();
            gameOver = true;
            return;
        }
    
        // Update UI
        gui.updateGameState(player, dealer, false);
    }
    

    public String getPlayerHand() {
        return player.getHand().toString();
    }

    public String getDealerHand() {
        return gameOver ? dealer.getHand().toString() : dealer.getHand().get(0) + " [Hidden]";
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
            gui.updateGameMessage(Texts.playerBusts[language]);
            bettingManager.dealerWins(); // Player busts, dealer wins
        }
    }

    private void checkDealerBust() {
        if (dealer.calculateScore() > 21) {
            gameOver = true;
            gui.updateGameMessage(Texts.dealerBusts[language]);
            bettingManager.playerWins(); // Dealer busts, player wins
        }
    }
    private void checkGameOver() {
        if (bettingManager.getPlayerBalance() <= 0) {
            gui.showGameOverMessage("Game Over! You ran out of money! 😢");
        } else if (bettingManager.getDealerBalance() <= 0) {
            gui.showGameOverMessage("Congratulations! The dealer ran out of money! 🎉");
        }
    }
    

    private void determineWinner() {
        if (!gameOver) {
            int playerScore = player.calculateScore();
            int dealerScore = dealer.calculateScore();
    
            if (playerScore > 21) { 
                gui.updateGameMessage(Texts.playerBusts[language]);
                bettingManager.dealerWins();
            } else if (dealerScore > 21 || playerScore > dealerScore) { 
                gui.updateGameMessage(Texts.playerWins[language]);
                bettingManager.playerWins();
            } else if (playerScore < dealerScore) { 
                gui.updateGameMessage(Texts.dealerWins[language]);
                bettingManager.dealerWins();
            } else { 
                gui.updateGameMessage(Texts.tie[language]);
                bettingManager.tie();
            }
    
            gameOver = true;
            
            // 🔍 Check if the game is over due to balance depletion
            checkGameOver();
    
            SwingUtilities.invokeLater(() -> {
                gui.enableBetting();
                gui.updateGameState(player, dealer, true);
            });
        }
    }
    

    /*
     * Betting system.
     */
    public boolean placeBet(int betAmount) {
        return bettingManager.placeBet(betAmount);
    }

    public int getPlayerBalance() {
        return bettingManager.getPlayerBalance();
    }

    public int getDealerBalance() {
        return bettingManager.getDealerBalance();
    }

    // DAVID JIMENEZ CALDERON akrjhg

    public int getDealerBet() {
        return bettingManager.getDealerBet();
    }

    /**
     * Handles special cards when drawn.
     */
    private Card handleSpecialCard(Card card, Player recipient) {
        switch (card.getType()) {
            case BLACKJACK_BOMB:
                if (recipient == player) {
                    gui.updateGameMessage("Blackjack Bomb! Player wins instantly! 💣");
                    bettingManager.playerWins(); // Player wins instantly
                } else {
                    gui.updateGameMessage("Blackjack Bomb! Dealer wins instantly! 💣");
                    bettingManager.dealerWins(); // Dealer wins instantly
                }
                gameOver = true;
                break;
            case SPLIT_ACE:
                gui.updateGameMessage("Split Ace! Automatically splitting your hand! ♠♠");
                splitHand(recipient);
                break;
            case JOKER_WILD:
                if (recipient == player) {
                    int wildValue = gui.promptJokerWildValue();  // Only prompt the player
                    card.setWildValue(wildValue);
                    gui.updateGameMessage("Joker Wild set to " + wildValue + " 🤡");
                }
                break;
            default:
                // Standard cards, no special handling needed
                break;
        }
        return card;
    }

    private void splitHand(Player player) {
        // Logic to split the player's hand (assuming it’s implemented in Player class)
    }
}
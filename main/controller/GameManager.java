package main.controller;

import main.model.Card;
import main.model.Deck;
import main.model.Player;
import main.view.BlackjackGUI;
import main.view.Texts;

import static main.view.BlackJackMenu.language;

import javax.swing.SwingUtilities;
import java.io.File;
import java.util.List;

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
    private GameStateManager gameStateManager = new GameStateManager();
    private GameState gameState;


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
        // check if a file has been provided
        if(gameState != null){
            System.out.println(">>>>>>>>>>>>>>>>>"+gameState.toString());
        }

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

        gui.resetSpecialMessage(); // Reset special card messages
    
        bettingManager.resetBets(); // Reset bets before new game
        
        // Place dealer's bet (e.g., 10% of dealer's balance)
        int dealerBet = (int) (bettingManager.getDealerBalance() * 0.1);
        bettingManager.placeDealerBet(dealerBet);
    
        // Enable betting
        gui.enableBetting();
        
        // Deal initial cards
        player.receiveCard(handleSpecialCard(deck.dealCard(), player));
        dealer.receiveCard(deck.dealCard()); // Dealer's cards don't trigger special messages
        player.receiveCard(handleSpecialCard(deck.dealCard(), player));

        dealer.receiveCard(deck.dealCard());
        if (player.hasBlackjack()) {
            gui.updateGameMessage("🎉 Player has Blackjack!");
            bettingManager.playerWins();
            gameOver = true;
        } else if (dealer.hasBlackjack()) {
            gui.updateGameMessage("🎲 Dealer has Blackjack!");
            bettingManager.dealerWins();
            gameOver = true;
        } else {
            gui.updateGameMessage(Texts.gameManagerGameOn[language]);
        }

        gui.updateGameState(player, dealer, gameOver);

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

    public int getDealerBet() {
        return bettingManager.getDealerBet();
    }

    public int getDealerScore () {
        return dealer.calculateScore();
    }

    public int getPlayerScore () {
        return player.calculateScore();
    }

    public int getPlayerBet (){
        return bettingManager.getPlayerBet();
    }

    public List<Card> getPlayerHandList(){
        return player.getHand();
    }

    public List<Card> getDealerHandList(){
        return dealer.getHand();
    }

    public Deck getDeck() {
        return deck;
    }

    /**
     * Handles special cards when drawn.
//     */

private Card handleSpecialCard(Card card, Player recipient) {
    if (recipient == player) { // Display special messages only when the player draws them
        switch (card.getType()) {
            case BLACKJACK_BOMB:
                gui.updateSpecialMessage("Blackjack Bomb! Player wins instantly! 💣");

                gameOver = true;
                break;
            case SPLIT_ACE:
                gui.updateSpecialMessage("Split Ace! Your score will be halved. ♠");
                break;
            case JOKER_WILD:
                int wildValue = gui.promptJokerWildValue();
                card.setWildValue(wildValue);
                gui.updateSpecialMessage("Joker Wild! set to " + wildValue + " 🤡");
                break;
            default:
                break;
        }
    }
    return card;
}

    private void splitHand(Player player) {
        // Logic to split the player's hand (assuming it’s implemented in Player class)
    }

    public void startGame() {
    }
}
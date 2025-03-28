package main.controller;

import main.model.Card;
import main.model.Deck;
import main.model.Player;
import main.view.BlackjackGUI;
import main.view.Texts;
import javax.swing.Timer;

import static main.view.BlackJackMenu.language;

import java.util.ArrayList;

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

        private final int INITIAL_BET = 1000;
        private boolean isPaused = false;
        private Timer gameTimer; // If you have any timers running


        private ArrayList<Player> players;
        private Player dealer;
        private Deck deck;
        private BlackjackGUI gui;
        private boolean gameOver;
        private BettingManager bettingManager;
        private int currentPlayerIndex;
    
        private GameManager() {
            this.players = new ArrayList<>();
            this.dealer = new Player("Dealer", INITIAL_BET);
            this.deck = new Deck();
            this.gameOver = false;
            this.bettingManager = new BettingManager(players, INITIAL_BET, INITIAL_BET); // Initial balance
            this.currentPlayerIndex=0;
            players.add(new Player("Player 1", INITIAL_BET)); // At least one player
            players.add(new Player("Player 2", INITIAL_BET)); // At least one player

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
        if (!gameOver && !isPaused) {
            dealerTurn();
            determineWinner();
            gui.updateGameState(player, dealer, gameOver, false);;
        }
    }
    public void resetBettingManager(int playerBalance, int dealerBalance) {
        this.bettingManager = new BettingManager(playerBalance, dealerBalance);
    }

    public void pauseGame() {
        isPaused = true;
        if (gameTimer != null && gameTimer.isRunning()) {
            gameTimer.stop();
        }
        gui.updateGameMessage(Texts.GAME_PAUSED[language]);
        // Force UI update while keeping cards hidden
        gui.updateGameState(player, dealer, false, true); // gameOver=false, isPaused=true
    }
    public boolean isPaused() {
        return isPaused;
    }

    public void resumeGame() {
        isPaused = false;
        
        if (gameTimer != null) {
            gameTimer.start();
        }
        
        gui.updateGameMessage(Texts.gameManagerGameOn[language]);
        // Restore the actual game state (hidden card if game isn't over)
        gui.updateGameState(player, dealer, gameOver, false); // gameOver=actual state, isPaused=false;
    }

    public boolean isGamePaused() {
        return isPaused;
    }
    
        public void setGui(BlackjackGUI gui) {
            this.gui = gui;
        }
    
        public void addPlayer(String name, int initialBalance) {
            players.add(new Player(name, initialBalance));
        }
    
        public void startNextPlayerTurn() {
            if (currentPlayerIndex < players.size()) {
                gui.promptPlayerAction(players.get(currentPlayerIndex));
            } else {
                dealerTurn();
            }
        }
    
        public void handlePlayerHit() {
            if (!gameOver) {
                Player currentPlayer = players.get(currentPlayerIndex);
                currentPlayer.receiveCard(handleSpecialCard(deck.dealCard(), currentPlayer));
                checkPlayerBust();
                gui.updateGameState(players, dealer, gameOver);
            }
        }
    
    
        public void handlePlayerStand() {
            if (!gameOver) {
                currentPlayerIndex++; // Move to the next player
                startNextPlayerTurn();
            }
        }

        public void checkPlayerBust() {
            Player player = players.get(currentPlayerIndex); // Reference the current player
            if (player.calculateScore() > 21) {
                gui.updateGameMessage(player.getName() + " busts! 😢");
                player.loseBet();
                bettingManager.dealerWins(null); // Player busts, dealer wins
                currentPlayerIndex++;
                startNextPlayerTurn();
            }
        }
    
        public void dealerTurn() {
            while (dealer.calculateScore() < 17) {
                dealer.receiveCard(handleSpecialCard(deck.dealCard(), dealer));
            }
            checkDealerBust();
            determineWinners();
        }
    
        private void checkDealerBust() {
            if (dealer.calculateScore() > 21) {
                gui.updateGameMessage("Dealer busts! 🎉");
                for (Player player : players) {
                    if (player.calculateScore() <= 21) {
                        player.winBet(player.getCurrentBet() * 2);
                    }
                }
            }
        }
    
        private void determineWinners() {
            if (!gameOver) {
                int dealerScore = dealer.calculateScore();
                for (Player player : players) {
                    int playerScore = player.calculateScore();
    
                    if (playerScore > 21) {
                        gui.updateGameMessage(player.getName() + " busts! Dealer wins.");
                        player.loseBet();
                    } else if (dealerScore > 21 || playerScore > dealerScore) {
                        gui.updateGameMessage(player.getName() + " wins! 🎉");
                        player.winBet(player.getCurrentBet() * 2);
                    } else if (playerScore < dealerScore) {
                        gui.updateGameMessage(player.getName() + " loses! Dealer wins.");
                        player.loseBet();
                    } else {
                        gui.updateGameMessage(player.getName() + " ties! Bets returned.");
                        player.tieBet();
                    }
                }
    
                gameOver = true;
                checkGameOver();
                SwingUtilities.invokeLater(() -> gui.updateGameState(players, dealer, true));
            }
        }
    
        public void resetBettingManager(int playerBalance, int dealerBalance) {
            this.bettingManager = new BettingManager(players, 1000,1000);
        }
    
        private void checkGameOver() {
            for (Player player : players) {
                if (player.getBalance() <= 0) {
                    gui.showGameOverMessage("Game Over! " + player.getName() + " ran out of money! 😢");
                    return;
                }
            }
            if (dealer.getBalance() <= 0) {
                gui.showGameOverMessage("Congratulations! The dealer ran out of money! 🎉");
            }
        }
        
    
        public String getPlayerHand(Player player) {
            return player.getHand().toString();
        }
    
        public String getDealerHand() {
            return gameOver ? dealer.getHand().toString() : dealer.getHand().get(0) + " [Hidden]";
        }
    
        
    
        /*
         * Betting system.
         */
        public boolean placeBet(Player player, int betAmount) {
            return player.placeBet(betAmount);
        }
        gui.updateGameState(player, dealer, gameOver, false);

    }
    
        public int getPlayerBalance(Player player) {
            return player.getBalance();
        }
    
        public int getDealerBalance() {
            return bettingManager.getDealerBalance();
        }
    
        public int getDealerBet() {
            return bettingManager.getDealerBet();
        checkDealerBust();
    }

    public void handlePlayerHit() {
        if (!gameOver && !isPaused && player.calculateScore() <= 21) {
            player.receiveCard(handleSpecialCard(deck.dealCard(), player));
            checkPlayerBust();
            gui.updateGameState(player, dealer, gameOver, false);;
        }
    }
    public boolean canPlaceBet() {
        return isGameOver() && 
               !isPaused && 
               getPlayerBalance() > 0 && 
               getDealerBalance() > 0;
    }
    public boolean isGameRunning() {
        return !isGameOver(); // Or whatever logic determines if game is running
    }

    public void checkPlayerBust() {
        if (player.calculateScore() > 21) {
            gameOver = true;
            gui.updateGameMessage(Texts.playerBusts[language]);
            bettingManager.dealerWins(); // Player busts, dealer wins
        }
    
        /**
         * Handles special cards when drawn.
    //     */
    
    private Card handleSpecialCard(Card card, Player recipient) {
        if (recipient != dealer) { // Special cards only affect players
            switch (card.getType()) {
                case BLACKJACK_BOMB:
                    gui.updateSpecialMessage("💣 Blackjack Bomb! " + recipient.getName() + " wins instantly!");
                    gameOver = true;
                    break;
                case SPLIT_ACE:
                    gui.updateSpecialMessage("♠ Split Ace! Your score is halved.");
                    break;
                case JOKER_WILD:
                    int wildValue = gui.promptJokerWildValue();
                    card.setWildValue(wildValue);
                    gui.updateSpecialMessage("🤡 Joker Wild! set to " + wildValue);
                    break;
                default:
                    break;
            }
        }
        return card;
    }
    
        public boolean hasNextPlayer() {
            return currentPlayerIndex < players.size();
        }
    
    
        public Player getCurrentPlayer() {
            if (currentPlayerIndex < players.size()) {
                return players.get(currentPlayerIndex);
            }
            return null; // In case there are no players or we've reached the dealer turn
        }
        
    
        public Object startNewGame() {
            // Reset all players' hands and balances
            for (Player player : players) {
                player.reset();
            }
            dealer.reset(); // Reset dealer's hand
            deck.shuffle(); // Shuffle the deck for the new game
            bettingManager.resetAllBets(); // Reset the bets
        
            currentPlayerIndex = 0; // Start with the first player
            gameOver = false; // Mark that the game is no longer over
        
            // Deal new cards to players and dealer
            for (Player player : players) {
                player.receiveCard(deck.dealCard());
                player.receiveCard(deck.dealCard());
            }
            
            // Dealer receives one face-up and one face-down card
            dealer.receiveCard(deck.dealCard()); // Visible card
            gui.updateGameMessage("Dealer has a hidden card.");
            
            // Update GUI with the new game state
            gui.updateGameMessage("Starting a new game!");
            gui.updateGameState(players, dealer, gameOver);
        
            // Start the first player's turn
            startNextPlayerTurn();
        
            return null; // Placeholder return type
            // 🔍 Check if the game is over due to balance depletion
            checkGameOver();
    
            SwingUtilities.invokeLater(() -> {
                gui.enableBetting();
                gui.updateGameState(player, dealer, gameOver, false);
            });
        }
        
    
        public boolean isCurrentPlayerStillInRound() {
            Player currentPlayer = getCurrentPlayer();
            if (currentPlayer == null) {
                return false; // If no player is available
            }
            return currentPlayer.calculateScore() <= 21; // Player is still in the round if they haven't busted

    /*
     * Betting system.
     */
    public boolean placeBet(int betAmount) {
        return bettingManager.placeBet(betAmount);
    }

    public int getPlayerBalance() {
        return bettingManager.getPlayerBalance();
    }
    public boolean isGameOver() {
        return gameOver;
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
    
        public ArrayList<Player> getPlayers(){
            return this.players;
    }
    
}
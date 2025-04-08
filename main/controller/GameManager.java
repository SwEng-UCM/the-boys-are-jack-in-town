package main.controller;

import main.model.Card;
import main.model.Deck;
import main.model.Player;
import main.view.BlackjackGUI;
import main.view.Texts;
import javax.swing.Timer;

import static main.view.BlackJackMenu.language;

import java.util.ArrayList;
import java.util.Stack;

import javax.swing.SwingUtilities;

/*
 * Singleton class.
 */

/**
 * The GameManager class is responsible for managing the game state and logic.
 * It interacts with the Player, Deck, and BlackjackGUI classes to handle player
 * actions,
 * dealer actions, and determine the game outcome.
 */
public class GameManager {
    private static GameManager instance;

    private final int INITIAL_BET = 1000;
    private boolean isPaused = false;
    private Timer gameTimer; // If you have any timers running
    private Stack<GameState> gameStateHistory = new Stack<>();
    private Stack<GameState> undoStack = new Stack<>(); // For undo functionality
    private Stack<GameState> redoStack = new Stack<>(); // For redo functionality

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
        this.currentPlayerIndex = 0;
        players.add(new Player("PLAYER 1", INITIAL_BET)); // At least one player
        players.add(new Player("PLAYER 2", INITIAL_BET)); // At least one player

    }

    // Public method to provide access to the singleton instance
    public static GameManager getInstance() {
        if (instance == null) {
            instance = new GameManager();
        }
        return instance;
    }

    public BettingManager getBettingManager() {
        return bettingManager;
    }


    public void setGui(BlackjackGUI gui) {
        this.gui = gui;
    }

    public void pauseGame() {
        isPaused = true;
        if (gameTimer != null && gameTimer.isRunning()) {
            gameTimer.stop();
        }
        gui.updateGameMessage(Texts.GAME_PAUSED[language]);
        gui.updateGameState(players, dealer, false, true); // gameOver=false, isPaused=true
    }

    public boolean isPaused() {
        return isPaused;
    }

    public void resumeGame() {
        isPaused = false;
        gui.setGameButtonsEnabled(true);

        if (gameTimer != null) {
            gameTimer.start();
        }

        gui.updateGameMessage(Texts.gameManagerGameOn[language]);
        // Restore the actual game state (hidden card if game isn't over)
        gui.updateGameState(players, dealer, gameOver, false); // gameOver=actual state, isPaused=false;
    }

    public boolean isGamePaused() {
        return isPaused;
    }

    public void addPlayer(String name, int initialBalance) {
        players.add(new Player(name, initialBalance));
    }
    
    public void startNextPlayerTurn() {
        if (currentPlayerIndex < players.size()) {
            gui.promptPlayerAction(players.get(currentPlayerIndex));
            if (currentPlayerIndex == 0) {
                int dealerBet = bettingManager.getDealerBalance() / 10;
                bettingManager.placeDealerBet(dealerBet);
            }
        } else {
            dealerTurn();
        }
    }

    public void handlePlayerHit() {
        if (!gameOver && !isPaused) {
            saveGameState();
            Player currentPlayer = players.get(currentPlayerIndex);
            currentPlayer.receiveCard(handleSpecialCard(deck.dealCard(), currentPlayer));
            checkPlayerBust();
            gui.updateGameState(players, dealer, gameOver, false);
        }
    }
    

    public void handlePlayerStand() {
        if (!gameOver) {
            saveGameState();
            currentPlayerIndex++; // Move to the next player
            startNextPlayerTurn();
            gui.updateGameState(players, dealer, gameOver, false);
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
            gui.updateGameState(players, dealer, gameOver, false);
        }
    }

    public void dealerTurn() {
        saveGameState();
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
                    bettingManager.dealerWins(player.getName());
                } else {
                    gui.updateGameMessage(player.getName() + " ties! Bets returned.");
                    player.tieBet();
                }

            }
            gui.updatePlayerPanels();
            gameOver = true;
            checkGameOver();
            SwingUtilities.invokeLater(() -> gui.updateGameState(players, dealer, true, false));
        }
    }

    public void resetBettingManager(int playerBalance, int dealerBalance) {
        this.bettingManager = new BettingManager(players, 1000, 1000);
    }
    private void saveGameState() {
        GameState currentState = new GameState(players, dealer, deck, currentPlayerIndex, gameOver);
        undoStack.push(currentState); // Save the current state to the undo stack
        redoStack.clear(); // Clear the redo stack since a new action invalidates the redo history
    }
    public void undoLastAction() {
        if (!undoStack.isEmpty()) {
            GameState previousState = undoStack.pop(); // Pop the last state from the undo stack
            redoStack.push(new GameState(players, dealer, deck, currentPlayerIndex, gameOver)); // Save the current state to the redo stack
            this.players = previousState.getPlayers();
            this.dealer = previousState.getDealer();
            this.deck = previousState.getDeck();
            this.currentPlayerIndex = previousState.getCurrentPlayerIndex();
            this.gameOver = previousState.isGameOver();
            gui.updateGameState(players, dealer, gameOver,isPaused);
        } else {
            gui.updateGameMessage("No actions to undo!");
        }
    }
    public void redoLastAction() {
        if (!redoStack.isEmpty()) {
            GameState nextState = redoStack.pop(); // Pop the last state from the redo stack
            undoStack.push(new GameState(players, dealer, deck, currentPlayerIndex, gameOver)); // Save the current state to the undo stack
            this.players = nextState.getPlayers();
            this.dealer = nextState.getDealer();
            this.deck = nextState.getDeck();
            this.currentPlayerIndex = nextState.getCurrentPlayerIndex();
            this.gameOver = nextState.isGameOver();
            gui.updateGameState(players, dealer, gameOver,isPaused);
        } else {
            gui.updateGameMessage("No actions to redo!");
        }
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
        gui.updateGameState(players, dealer, gameOver, false);
        return player.placeBet(betAmount);

    }

    public int getPlayerBalance(Player player) {
        return player.getBalance();
    }

    public boolean canPlaceBet(Player currentPlayer) {
        return isGameOver() &&
                !isPaused &&
                currentPlayer.getBalance() > 0 &&
                getDealerBalance() > 0;
    }

    public boolean isGameRunning() {
        return !isGameOver(); // Or whatever logic determines if game is running
    }

    public boolean hasNextPlayer() {
        return currentPlayerIndex < players.size()-1;
    }

    public Player getCurrentPlayer() {
        if (currentPlayerIndex < players.size()) {
            return players.get(currentPlayerIndex);
        }
        return null; // In case there are no players or we've reached the dealer turn
    }

    public void startNewGame() {
        gameOver = false; // Mark that the game is no longer over
        isPaused = false;
        resumeGame();
        currentPlayerIndex = 0; // Start with the first player
        deck.shuffle(); // Shuffle the deck for the new game

        // Reset all players' hands and balances
        for (Player player : players) {
            player.reset();
            player.receiveCard(deck.dealCard());
            player.receiveCard(deck.dealCard());
        }

        dealer.reset(); // Reset dealer's hand
        bettingManager.resetAllBets(); // Reset the bets

        //resumeGame();

        // Dealer receives one face-up and one face-down card
        dealer.receiveCard(deck.dealCard()); // Visible card
        //gui.updateGameMessage("Dealer has a hidden card.");

        
        // Update GUI with the new game state
        
        gui.updateGameMessage("Starting a new game!");
        gui.updateGameState(players, dealer, false, false);


        // 🔍 Check if the game is over due to balance depletion
        //checkGameOver();

        SwingUtilities.invokeLater(() -> {
            gui.enableBetting();
            gui.setGameButtonsEnabled(true);
            startNextPlayerTurn(); // Start the first player's turn
        });
        return;
    }

    public boolean isCurrentPlayerStillInRound() {
        Player currentPlayer = getCurrentPlayer();
        if (currentPlayer == null) {
            return false; // If no player is available
        }
        return currentPlayer.calculateScore() <= 21; // Player is still in the round if they haven't busted
    }
    /*
     * Betting system.
     */

    public boolean isGameOver() {
        return gameOver;
    }

    public int getDealerBalance() {
        return bettingManager.getDealerBalance();
    }

    public int getDealerBet() {
        return bettingManager.getDealerBet();
    }

    public int getDealerScore() {
        return dealer.calculateScore();
    }

    public int getPlayerScore(Player currentPlayer) {
        return currentPlayer.calculateScore();
    }

    public int getPlayerBet(Player currentPlayer) {
        return currentPlayer.getCurrentBet();
    }



    /**
     * Handles special cards when drawn.
     * //
     */

    private Card handleSpecialCard(Card card, Player recipient) {
        if (recipient != dealer) { // Display special messages only when the player draws them
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

    public ArrayList<Player> getPlayers() {
        return this.players;
    }

    public void setGameOver(boolean b) {
        this.gameOver=b;
    }

    public Deck getDeck() {
        return this.deck;
    }

    public Player getDealer() {
        return this.dealer;
    }
}
  class GameState {
    private ArrayList<Player> players;
    private Player dealer;
    private Deck deck;
    private int currentPlayerIndex;
    private boolean gameOver;

    public GameState(ArrayList<Player> players, Player dealer, Deck deck, int currentPlayerIndex, boolean gameOver) {
        this.players = new ArrayList<>(players); // Deep copy if necessary
        this.dealer = new Player(dealer); // Ensure Player has a copy constructor
        this.deck = new Deck(deck); // Ensure Deck has a copy constructor
        this.currentPlayerIndex = currentPlayerIndex;
        this.gameOver = gameOver;
    }

    // Getter methods for private fields
    public ArrayList<Player> getPlayers() {
        return players;
    }

    public Player getDealer() {
        return dealer;
    }

    public Deck getDeck() {
        return deck;
    }

    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    public boolean isGameOver() {
        return gameOver;
    }
}
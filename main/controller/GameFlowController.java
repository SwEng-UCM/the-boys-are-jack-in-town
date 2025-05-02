package main.controller;

import static main.view.BlackJackMenu.language;
import java.util.ArrayList;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import main.view.BlackjackGUI;
import main.view.Texts;
import main.model.Player;

/**
 * Controls the flow of the Blackjack game, including starting new games,
 * managing pauses and resumes, and checking game-over conditions.
 * <p>
 * Works with {@link GameManager}, {@link PlayerManager}, and {@link DealerManager}
 * to coordinate the game logic and interface updates.
 */
public class GameFlowController {
    private boolean isPaused = false;
    private Timer gameTimer;
    private BlackjackGUI gui;
    private ArrayList<Player> players;
    private Player dealer;
    private boolean gameOver;
    private final GameManager gameManager;
    private final PlayerManager playerManager;
    private final DealerManager dealerManager;

    /**
     * Constructs a {@code GameFlowController} that coordinates the Blackjack game.
     *
     * @param gameManager the main game logic controller
     * @param playerManager manages player states
     * @param dealerManager manages the dealer
     * @param gui the Blackjack GUI to update game state
     */
    public GameFlowController(GameManager gameManager, PlayerManager playerManager, DealerManager dealerManager, BlackjackGUI gui) {
        this.gui = gui;
        this.gameTimer = gameTimer;
        this.gameManager = gameManager;
        this.playerManager = playerManager;
        this.dealerManager = dealerManager;
    }

    /**
     * Starts a new round of Blackjack, resetting players, dealing initial cards,
     * enabling GUI buttons, and preparing the game state.
     */
    public void startNewGame() {
        setGameOver(false); 
        isPaused = false;
        resumeGame();
    
        playerManager.setCurrentPlayerIndex(0); 
        gameManager.getDeck().shuffle(); 
    
        // Reset all players and deal initial cards
        for (Player player : playerManager.getPlayers()) {
            player.reset();
            player.receiveCard(gameManager.getDeck().dealCard());
            player.receiveCard(gameManager.getDeck().dealCard());
            AchievementManager.getInstance().trackFirstBlackjack(player);
        }
    
        dealerManager.getDealer().reset(); 
        gameManager.getBettingManager().resetAllBets(); 
        dealerManager.getDealer().receiveCard(gameManager.getDeck().dealCard()); 
    
        AchievementManager.getInstance().trackMultiplayerGame(playerManager.getPlayers());
    
        this.players = playerManager.getPlayers();
        this.dealer = dealerManager.getDealer();
    
        gui.updateGameMessage("Starting a new game!");
        gui.updateGameState(players, dealer, false, false);
    
        SwingUtilities.invokeLater(() -> {
            gui.setGameButtonsEnabled(true);
            gui.enableBetting();  
        });
        gameManager.setCurrentPlayerIndex(0);
        gameManager.startNextPlayerTurn();
    }

    /**
     * Pauses the game and disables the game timer and updates the GUI.
     */
    public void pauseGame() {
        isPaused = true;
        if (gameTimer != null && gameTimer.isRunning()) {
            gameTimer.stop();
        }
        gui.updateGameMessage(Texts.GAME_PAUSED[language]);
    
        GameManager gm = GameManager.getInstance();
        gui.updateGameState(
            gm.getPlayerManager().getPlayers(), 
            gm.getDealerManager().getDealer(), 
            false,
            true
        );
    }

    /**
     * Resumes the game by enabling game interaction and continuing the timer.
     */
    public void resumeGame() {
        isPaused = false;
        gui.setGameButtonsEnabled(true);
        
        if (gameTimer != null) {
            gameTimer.start();
        }
        
        gui.updateGameMessage(Texts.gameManagerGameOn[language]);
        
        GameManager gm = GameManager.getInstance();
        gui.updateGameState(
            gm.getPlayerManager().getPlayers(),
            gm.getDealerManager().getDealer(),
            isGameOver(),
            false
        );
    }

    /**
     * Returns whether the game is currently paused.
     *
     * @return {@code true} if the game is paused, {@code false} otherwise
     */
    public boolean isPaused() {
        return isPaused;
    }

    /**
     * Alias for {@link #isPaused()} for semantic clarity.
     *
     * @return {@code true} if the game is paused
     */
    public boolean isGamePaused() {
        return isPaused;
    }

    /**
     * Checks if the game is over due to any player or dealer running out of money.
     * Displays a game over message if applicable.
     */
    public void checkGameOver() {
        for (Player player : players) {
            if (player.getBalance() <= 0) {
                gui.showGameOverMessage(Texts.gameOverTitle[language] + player.getName() + " ran out of money! \uD83D\uDE22");
                return;
            }
        }
        if (dealer.getBalance() <= 0) {
            gui.showGameOverMessage("Congratulations! The dealer ran out of money! 🎉");
        }
    }

    /**
     * Checks if the game is still running.
     *
     * @return {@code true} if the game is active and not over
     */
    public boolean isGameRunning() {
        return !isGameOver();
    }

    /**
     * Returns whether the game is over.
     *
     * @return {@code true} if the game is over
     */
    public boolean isGameOver() {
        return gameOver;
    }

    /**
     * Sets the game over state.
     *
     * @param b {@code true} to mark the game as over, {@code false} to mark it active
     */
    public void setGameOver(boolean b) {
        this.gameOver = b;
    }
}

package main.controller;

import static main.view.BlackJackMenu.language;
import java.util.ArrayList;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import main.view.BlackjackGUI;
import main.view.Texts;
import main.model.Player;
import main.controller.GameManager;
import main.controller.PlayerManager;
import main.controller.DealerManager;


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


    public GameFlowController(GameManager gameManager, PlayerManager playerManager, DealerManager dealerManager, BlackjackGUI gui) {
        this.gui = gui;
        this.gameTimer = gameTimer;
        this.gameManager = gameManager;
        this.playerManager = playerManager;
        this.dealerManager = dealerManager;
    }

    public void startNewGame() {
        setGameOver(false); 
        isPaused = false;
        resumeGame();
    
        playerManager.setCurrentPlayerIndex(0); 
        gameManager.getDeck().shuffle(); 
    
        // Reset all players
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
        gui.updateGameState(playerManager.getPlayers(), dealerManager.getDealer(), false, false);
    
        SwingUtilities.invokeLater(() -> {
            gui.setGameButtonsEnabled(true);
            gui.enableBetting();  
        });
        
    }
    

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
            false, // gameOver = false
            true   // isPaused = true
        );
    }

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
            false // isPaused = false
        );
    }
    

    public boolean isPaused() {
        return isPaused;
    }

    public boolean isGamePaused() {
        return isPaused;
    }

    public void checkGameOver() {
        for (Player player : players) {
            if (player.getBalance() <= 0) {
                gui.showGameOverMessage(Texts.gameOverTitle[language]+ player.getName() + " ran out of money! \uD83D\uDE22");
                return;
            }
        }
        if (dealer.getBalance() <= 0) {
            gui.showGameOverMessage("Congratulations! The dealer ran out of money! 🎉");
        }
    }

    public boolean isGameRunning() {
        return !isGameOver(); // Or whatever logic determines if game is running
    }

    
    public boolean isGameOver() {
        return gameOver;
    }

    
    public void setGameOver(boolean b) {
        this.gameOver=b;
    }
}

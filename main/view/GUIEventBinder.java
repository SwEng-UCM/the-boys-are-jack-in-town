package main.view;

import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.*;

import main.controller.GameManager;
import main.controller.MultiplayerCommand;
import main.model.Player;

/**
 * Binds GUI components (such as buttons) to their corresponding event handlers
 * and actions in the game.
 * 
 * This class attaches event listeners to the various buttons in the 
 * `BlackjackGUI` and delegates the appropriate actions to the game logic.
 */
public class GUIEventBinder {
    private final BlackjackGUI window;

    /**
     * Constructs a `GUIEventBinder` to bind event listeners to the provided `BlackjackGUI` window.
     *
     * @param window the `BlackjackGUI` instance to attach event listeners to
     */
    public GUIEventBinder(BlackjackGUI window) {
        this.window = window;
    }

    /**
     * Attaches action listeners to the buttons in the `BlackjackGUI`.
     * These listeners delegate events to the appropriate game logic actions.
     */
    public void attachEventListeners() {
        window.hitButton.addActionListener(e -> {
            try {
                if (window.gameManager.isMultiplayerMode()) {
                    Player current = window.gameManager
                        .getPlayerManager()
                        .getCurrentPlayer();
        
                    window.gameManager.getClient().sendAction(
                        new MultiplayerCommand(
                            MultiplayerCommand.Type.HIT,
                            current.getName(),
                            null
                        )
                    );
                } else {
                    window.gameManager.handlePlayerHit(); 
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        
            // STAND
    window.standButton.addActionListener(e -> {
        try {
            if (window.gameManager.isMultiplayerMode()) {
                Player current = window.gameManager.getPlayerManager().getCurrentPlayer();
                window.gameManager.getClient().sendAction(
                    new MultiplayerCommand(MultiplayerCommand.Type.STAND, current.getName(), null)
                );
            } else {
                window.gameManager.handlePlayerStand();
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    });

    // NEW GAME
    window.newGameButton.addActionListener(e -> {
        GameManager gameManager = window.gameManager;

        if (gameManager.isMultiplayerMode()) {
            if (gameManager.isServer()) {
                MultiplayerCommand startCmd = new MultiplayerCommand(
                    MultiplayerCommand.Type.START_NEW_GAME,
                    "HOST", 
                    null
                );
                gameManager.getNetworkManager().broadcast(startCmd);
                gameManager.getGameFlowController().startNewGame();
            } else {
                JOptionPane.showMessageDialog(window,
                    "Only the host can start a new game.",
                    "Multiplayer",
                    JOptionPane.INFORMATION_MESSAGE
                );
            }
        } else {
            GameManager.resetInstance();
            gameManager = GameManager.getInstance();
            gameManager.setGui(window);
            gameManager.getGameFlowController().startNewGame();
        }
    });

    // PLACE BET
    window.placeBetButton.addActionListener(e -> {
        String betText = window.betField.getText();
        if (!betText.isEmpty()) {
            try {
                int bet = Integer.parseInt(betText);
                Player currentPlayer = window.gameManager.getPlayerManager().getCurrentPlayer();
    
                if (window.gameManager.isMultiplayerMode()) {
                    MultiplayerCommand betCommand = new MultiplayerCommand(
                        MultiplayerCommand.Type.BET,
                        currentPlayer.getName(),
                        bet
                    );
                    window.gameManager.getClient().sendAction(betCommand);
                } else {
                    window.placeBet(currentPlayer); 
                }
            } catch (NumberFormatException | IOException ex) {
                JOptionPane.showMessageDialog(window, "Invalid bet amount.");
            }
        }
    });
    

        
        // Action listener for the "Pause" button
        window.pauseButton.addActionListener(e -> new PausePanel(window, window.gameManager, window, BlackJackMenu.language).showPauseMenu());

        // Action listener for the "Undo" button
        window.undoButton.addActionListener(e -> {
            window.gameManager.getCommandManager().undo();
            window.updateUndoButtonState();
        });
    }
}

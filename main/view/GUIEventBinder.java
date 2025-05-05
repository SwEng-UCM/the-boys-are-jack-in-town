package main.view;

import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.*;

import main.controller.GameManager;
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
                window.gameManager.handlePlayerHit();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });

        // Action listener for the "Stand" button
        window.standButton.addActionListener(e -> {
            try {
                window.gameManager.handlePlayerStand();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });

        // Action listener for the "New Game" button
        window.newGameButton.addActionListener(e -> {
            GameManager.resetInstance();
            GameManager gameManager = GameManager.getInstance();
        
            BlackjackGUI gui = window;
            gameManager.setGui(gui);
            gameManager.getGameFlowController().startNewGame();
           
        });

       

        // Action listener for the "Pause" button
        window.pauseButton.addActionListener(e -> new PausePanel(window, window.gameManager, window, BlackJackMenu.language).showPauseMenu());

        // Action listener for the "Place Bet" button
        window.placeBetButton.addActionListener(e -> {
            String betText = window.betField.getText();
            if (!betText.isEmpty()) {
                try {
                    int bet = Integer.parseInt(betText);
                    Player currentPlayer = window.gameManager.getPlayerManager().getCurrentPlayer();
                    window.placeBet(currentPlayer); // Call the method in BlackjackGUI to place bet
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(window, "Invalid bet amount.");
                }
            }
        });

        // Action listener for the "Undo" button
        window.undoButton.addActionListener(e -> {
            window.gameManager.getCommandManager().undo();
            window.updateUndoButtonState();
        });
    }
}

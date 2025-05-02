package main.view;

import java.io.IOException;

import javax.swing.*;

import main.controller.BetCommand;
import main.model.Player;

public class GUIEventBinder {
    private final BlackjackGUI window;

    public GUIEventBinder(BlackjackGUI window) {
        this.window = window;
    }

    public void attachEventListeners() {
        window.hitButton.addActionListener(e -> {
            try {
                window.gameManager.handlePlayerHit();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
        window.standButton.addActionListener(e -> {
            try {
                window.gameManager.handlePlayerStand();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
        window.newGameButton.addActionListener(e -> window.gameManager.getGameFlowController().startNewGame());
        window.pauseButton.addActionListener(e -> new PausePanel(window, window.gameManager, window, BlackJackMenu.language).showPauseMenu());
        window.placeBetButton.addActionListener(e -> {
    String betText = window.betField.getText();
    if (!betText.isEmpty()) {
        try {
            int bet = Integer.parseInt(betText);
            Player currentPlayer = window.gameManager.getPlayerManager().getCurrentPlayer();
            window.placeBet(currentPlayer); // ← call the method in BlackjackGUI
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(window, "Invalid bet amount.");
        }
    }
});

    }
}

package main.view;

import javax.swing.*;

public class GUIEventBinder {
    private final BlackjackGUI window;

    public GUIEventBinder(BlackjackGUI window) {
        this.window = window;
    }

    public void attachEventListeners() {
        window.hitButton.addActionListener(e -> window.gameManager.handlePlayerHit());
        window.standButton.addActionListener(e -> window.gameManager.handlePlayerStand());
        window.newGameButton.addActionListener(e -> window.gameManager.startNewGame());
        window.pauseButton.addActionListener(e -> window.showPauseMenu());
        window.placeBetButton.addActionListener(e -> {
            String betText = window.betField.getText();
            if (!betText.isEmpty()) {
                try {
                    int bet = Integer.parseInt(betText);
                    window.gameManager.placeBet(window.gameManager.getCurrentPlayer(), bet);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(window, "Invalid bet amount.");
                }
            }
        });
    }
}

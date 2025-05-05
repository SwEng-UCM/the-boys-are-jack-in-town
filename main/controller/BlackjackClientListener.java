package main.controller;

import java.io.ObjectInputStream;

import javax.swing.SwingUtilities;

import main.view.BlackjackGUI;

/**
 * A thread that listens for incoming {@link GameState} updates from the server
 * and applies them to the GUI in a multiplayer Blackjack game.
 * <p>
 * This listener is used on the client side to receive game state changes from the host/server
 * and ensure the local game view reflects the latest game state.
 */
public class BlackjackClientListener extends Thread {
    private ObjectInputStream in;

    /**
     * Constructs a new client listener with the specified input stream.
     *
     * @param in the {@link ObjectInputStream} used to receive game state updates from the server
     */
    public BlackjackClientListener(ObjectInputStream in) {
        this.in = in;
    }

    /**
     * Continuouslys {@link GameState} objects from the input stream
     * and updates the client's GUI to reflect the latest state.
     */
    @Override
public void run() {
    try {
        while (true) {
            // Read the GameStateUpdate object from the server
            GameStateUpdate update = (GameStateUpdate) in.readObject();

            // Apply update to game logic and GUI safely
            GameManager gm = GameManager.getInstance();
            gm.applyGameStateUpdate(update);

            SwingUtilities.invokeLater(() -> {
                BlackjackGUI.getInstance(gm).updateGameState(
                    gm.getPlayerManager().getPlayers(),
                    gm.getDealerManager().getDealer(),
                    gm.getGameFlowController().isGameOver(),
                    gm.getGameFlowController().isPaused()
                );
            });
        }
    } catch (Exception e) {
        System.err.println("Disconnected from server or error reading update:");
        e.printStackTrace();
    }
}

}

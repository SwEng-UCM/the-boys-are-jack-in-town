package main.controller;

import java.io.ObjectInputStream;
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
     * Continuously reads {@link GameState} objects from the input stream
     * and updates the client's GUI to reflect the latest state.
     */
    @Override
    public void run() {
        try {
            while (true) {
                // Read the GameState object sent from the server
                GameState gameState = (GameState) in.readObject();

                // Apply the received game state to the GUI
                GameManager gm = GameManager.getInstance();
                BlackjackGUI.getInstance(gm).applyGameState(gameState);
            }
        } catch (Exception e) {
            // Print any exceptions that occur during listening
            e.printStackTrace();
        }
    }
}

package main.controller;

import java.io.ObjectInputStream;
import javax.swing.SwingUtilities;
import main.view.BlackjackGUI;

/**
 * The {@code BlackjackClientListener} class is a thread responsible for listening to
 * game state updates sent from the server in a multiplayer Blackjack game.
 * <p>
 * It continuously reads objects from the provided {@link ObjectInputStream}, expecting
 * them to be instances of {@link GameStateUpdate}. Upon receiving an update, it applies
 * the new game state to the local {@link GameManager} and refreshes the {@link BlackjackGUI}
 * using the Swing event dispatch thread to ensure thread safety.
 */
public class BlackjackClientListener extends Thread {
    /** The input stream connected to the server, used to receive game state updates. */
    private final ObjectInputStream in;

    /**
     * Constructs a new {@code BlackjackClientListener}.
     *
     * @param in the {@link ObjectInputStream} connected to the server for receiving updates
     */
    public BlackjackClientListener(ObjectInputStream in) {
        this.in = in;
    }

    /**
     * The main loop of the listener thread.
     * <p>
     * Continuously reads objects from the server, checks for valid {@link GameStateUpdate}
     * instances, and applies them to the game state. If the object is not a valid update,
     * it logs an error. GUI updates are dispatched to the Swing event thread.
     */
    @Override
    public void run() {
        try {
            while (true) {
                Object obj = in.readObject();

                if (obj instanceof GameStateUpdate update) {
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
                } else {
                    System.err.println("Unexpected object received: " + obj.getClass());
                }
            }
        } catch (Exception e) {
            System.err.println("Disconnected from server or error reading update:");
            e.printStackTrace();
        }
    }
}

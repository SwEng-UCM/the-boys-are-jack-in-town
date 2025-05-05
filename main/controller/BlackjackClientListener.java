package main.controller;

import java.io.ObjectInputStream;
import javax.swing.SwingUtilities;
import main.view.BlackjackGUI;

/**
 * A thread that listens for incoming {@link GameStateUpdate} updates from the server
 * and applies them to the GUI in a multiplayer Blackjack game.
 */
public class BlackjackClientListener extends Thread {
    private final ObjectInputStream in;

    public BlackjackClientListener(ObjectInputStream in) {
        this.in = in;
    }

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

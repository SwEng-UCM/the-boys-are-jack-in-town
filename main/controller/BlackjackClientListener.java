package main.controller;

import java.io.ObjectInputStream;
import main.view.BlackjackGUI;
import main.controller.GameState;

public class BlackjackClientListener extends Thread {
    private ObjectInputStream in;

    public BlackjackClientListener(ObjectInputStream in) {
        this.in = in;
    }

    public void run() {
        try {
            while (true) {
                // Read the GameState object sent from the server
                GameState gameState = (GameState) in.readObject();
                
                // Update the GUI with the received game state
                GameManager gm = GameManager.getInstance();
                BlackjackGUI.getInstance(gm).applyGameState(gameState);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

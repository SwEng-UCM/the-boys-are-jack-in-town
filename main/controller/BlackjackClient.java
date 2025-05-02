package main.controller;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.SwingUtilities;

import main.model.Player;

public class BlackjackClient {
    private Socket socket;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private GameManager gameManager;
    
    public BlackjackClient(GameManager gameManager) {
        this.gameManager = gameManager;
    }
    
    public void connect(String host, int port) throws IOException {
        try {
            this.socket = new Socket(host, port);
            this.output = new ObjectOutputStream(socket.getOutputStream());
            this.input = new ObjectInputStream(socket.getInputStream());
            
            new Thread(this::listenForUpdates).start();
        } catch (IOException e) {
            throw new IOException("Connection failed: " + e.getMessage());
        }
    }
    
    private void listenForUpdates() {
        try {
            while (socket.isConnected()) {
                Object received = input.readObject();
                
                if (received instanceof GameStateUpdate) {
                    handleGameStateUpdate((GameStateUpdate) received);
                } else if (received instanceof MultiplayerCommand) {
                    gameManager.handleCommand((MultiplayerCommand) received);
                }
            }
        } catch (Exception e) {
            System.err.println("Connection error: " + e.getMessage());
        }
    }

    private void handleGameStateUpdate(GameStateUpdate update) {
        SwingUtilities.invokeLater(() -> {
            gameManager.applyGameStateUpdate(update);
            if (gameManager.getGUI() != null) {
                gameManager.getGUI().updateGameState(
                    (ArrayList<Player>) update.getPlayers(),
                    update.getDealer(),
                    update.isGameOver(),
                    false
                );
            }
        });
    }

    public void sendAction(MultiplayerCommand command) throws IOException {
        try {
            output.writeObject(command);
            output.flush();
        } catch (IOException e) {
            throw new IOException("Failed to send command: " + e.getMessage());
        }
    }
    
    public void disconnect() {
        try {
            if (socket != null) socket.close();
            if (output != null) output.close();
            if (input != null) input.close();
        } catch (IOException e) {
            System.err.println("Error disconnecting: " + e.getMessage());
        }
    }
}
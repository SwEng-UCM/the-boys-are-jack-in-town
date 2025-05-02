package main.controller;

import java.io.*;
import java.net.Socket;
import main.model.Player;

public class BlackjackClientHandler implements Runnable {
    private final Socket socket;
    private final GameManager gameManager;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private boolean connected = true;
    private Player player;  // Track associated player

    public BlackjackClientHandler(Socket socket, GameManager gameManager) {
        this.socket = socket;
        this.gameManager = gameManager;
    }

    @Override
    public void run() {
        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());

            // Handle initial JOIN command
            Object firstMessage = in.readObject();
            if (firstMessage instanceof MultiplayerCommand) {
                MultiplayerCommand joinCommand = (MultiplayerCommand) firstMessage;
                if (joinCommand.getType() == MultiplayerCommand.Type.JOIN) {
                    handleJoinCommand(joinCommand);
                } else {
                    throw new IOException("First message must be JOIN command");
                }
            } else {
                throw new IOException("Invalid initial message type");
            }

            // Process subsequent commands
            while (connected) {
                Object input = in.readObject();
                if (input instanceof MultiplayerCommand) {
                    gameManager.handleCommand((MultiplayerCommand) input);
                } else {
                    System.err.println("Received invalid object type: " + input.getClass());
                }
            }

        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Client disconnected: " + e.getMessage());
        } finally {
            closeConnection();
        }
    }

    public void sendMessage(Object message) {
        try {
            if (out == null) {
                throw new IOException("Output stream not initialized");
            }
            
            synchronized(out) {  // Ensure thread-safe writes
                out.writeObject(message);
                out.flush();
                
                // Optional: Reset for repeated object sending
                out.reset();  // Clears object cache to prevent reference recycling
            }
        } catch (IOException e) {
            System.err.println("Failed to send to client " + 
                              (player != null ? player.getName() : "[unknown]") + 
                              ": " + e.getMessage());
            closeConnection();
        }
    }

    private void handleJoinCommand(MultiplayerCommand command) throws IOException {
        String playerName = command.getPlayerName();
        int initialBalance = 1000; // Or get from GameManager
        
        // Create player through PlayerManager
        gameManager.getPlayerManager().addPlayer(playerName, initialBalance);
        
        // Get reference to the created player
        this.player = gameManager.getPlayerManager().getPlayerByName(playerName);
        
        // Send initial game state
        sendMessage(gameManager.createGameStateUpdate());
        
        System.out.println("Player joined: " + playerName);
    }

    public void closeConnection() {
        try {
            connected = false;
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
            if (player != null) {
                gameManager.playerDisconnected(player.getName());
            }
            // Safe removal from NetworkManager
            if (gameManager.getNetworkManager() != null) {
                gameManager.getNetworkManager().getClientHandlers().remove(this);
            }
        } catch (IOException e) {
            System.err.println("Error closing connection: " + e.getMessage());
        }
    }
}
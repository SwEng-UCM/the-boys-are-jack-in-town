package main.controller;

import java.io.*;
import java.net.Socket;
import main.model.Player;

/**
 * Handles communication with a single client in a multiplayer Blackjack game.
 * This class implements {@link Runnable} so it can be executed in a separate thread.
 * It handles the connection setup, processes commands received from the client,
 * and sends responses back. It maintains the association with a {@link Player}
 * and communicates with the {@link GameManager}.
 */
public class BlackjackClientHandler implements Runnable {
    private final Socket socket;
    private final GameManager gameManager;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private boolean connected = true;
    private Player player;  // Track associated player

    /**
     * Constructs a new handler for a connected client.
     *
     * @param socket the client's socket connection
     * @param gameManager the game manager that controls game logic and state
     */
    public BlackjackClientHandler(Socket socket, GameManager gameManager) {
        this.socket = socket;
        this.gameManager = gameManager;
    }

    /**
     * The main loop of the client handler. Sets up the input/output streams,
     * processes the initial JOIN command, and listens for further commands
     * from the client until the connection is closed.
     */
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

    /**
     * Sends a serialized message to the client.
     *
     * @param message the message object to send
     */
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

    /**
     * Handles the initial JOIN command from a new player.
     * Creates the player, associates it with this handler, and sends
     * the initial game state back to the client.
     *
     * @param command the join command containing the player name
     * @throws IOException if player setup fails
     */
    private void handleJoinCommand(MultiplayerCommand command) throws IOException {
        String playerName = command.getPlayerName();
        int initialBalance = 1000;
    
        // Add player FIRST
        gameManager.getPlayerManager().addPlayer(playerName, initialBalance);
        this.player = gameManager.getPlayerManager().getPlayerByName(playerName);
    
        System.out.println("Player joined: " + playerName);
        System.out.println("Total players: " + gameManager.getPlayerManager().getPlayers().size());
    
        // Deal initial hand (example logic — adapt to your flow)
        player.receiveCard(gameManager.getDeck().dealCard());
        player.receiveCard(gameManager.getDeck().dealCard());

        System.out.println("Dealt " + player.getHand().size() + " cards to " + player.getName());

        // THEN create and send the update
        GameStateUpdate update = gameManager.createGameStateUpdate();
        System.out.println("Sending update with players: " + update.getPlayers().size());
    
        sendMessage(update);
    
        // Optionally broadcast to all other clients
        gameManager.broadcastGameState();
    }
    

    /**
     * Closes the client connection and cleans up associated resources.
     * Removes the player from the game and unregisters this handler.
     */
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

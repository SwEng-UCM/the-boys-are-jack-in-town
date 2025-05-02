package main.controller;

import java.io.IOException;

/**
 * Represents the server for a multiplayer Blackjack game.
 * <p>
 * This class is responsible for starting and stopping the server,
 * managing the network connections through the {@link NetworkManager},
 * and enabling multiplayer mode in the {@link GameManager}.
 */
public class BlackjackServer {
    private final int port;
    private NetworkManager networkManager;
    private GameManager gameManager;

    /**
     * Constructs a new {@code BlackjackServer} with the specified port.
     *
     * @param port the port number on which the server will listen for incoming client connections
     */
    public BlackjackServer(int port) {
        this.port = port;
        this.gameManager = GameManager.getInstance();
        this.networkManager = new NetworkManager();
    }

    /**
     * Starts the Blackjack server by enabling multiplayer mode in the game manager
     * and initializing the network manager to accept client connections.
     */
    public void start() {
        try {
            gameManager.setMultiplayerMode(true);
            networkManager.startServer(port, gameManager);

            System.out.println("Blackjack Server started on port " + port);
            System.out.println("Waiting for players...");

        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
        }
    }

    /**
     * Stops the Blackjack server by closing all network connections
     * and disabling multiplayer mode in the game manager.
     */
    public void stop() {
        try {
            networkManager.close();
            gameManager.setMultiplayerMode(false);
            System.out.println("Blackjack Server stopped.");
        } catch (Exception e) {
            System.err.println("Error shutting down server: " + e.getMessage());
        }
    }
}

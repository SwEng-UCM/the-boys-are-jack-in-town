package main.controller;

import java.io.IOException;

public class BlackjackServer {
    private final int port;
    private NetworkManager networkManager;
    private GameManager gameManager;

    public BlackjackServer(int port) {
        this.port = port;
        this.gameManager = GameManager.getInstance();
        this.networkManager = new NetworkManager();
    }

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
package main.controller;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Manages all networking functionality for a multiplayer Blackjack game.
 * <p>
 * This class handles both server-side (accepting connections, broadcasting messages)
 * and client-side (connecting to a server) responsibilities.
 */
public class NetworkManager {
    private final List<BlackjackClientHandler> clientHandlers = new CopyOnWriteArrayList<>();
    private ServerSocket serverSocket;
    private boolean isServer = false;

    /**
     * Starts the server on the specified port and listens for client connections.
     *
     * @param port         the port to listen on
     * @param gameManager  the shared GameManager instance to pass to client handlers
     * @throws IOException if an I/O error occurs when opening the socket
     */
    public void startServer(int port, GameManager gameManager) throws IOException {
        isServer = true;
        serverSocket = new ServerSocket(port);
        new Thread(() -> {
            try {
                while (!serverSocket.isClosed()) {
                    Socket clientSocket = serverSocket.accept();
                    BlackjackClientHandler handler = new BlackjackClientHandler(clientSocket, gameManager);
                    clientHandlers.add(handler);
                    new Thread(handler).start();
                }
            } catch (IOException e) {
                if (!serverSocket.isClosed()) {
                    System.err.println("Server accept error: " + e.getMessage());
                }
            }
        }).start();
    }

    /**
     * Checks if the server is currently running.
     *
     * @return {@code true} if the server is active and the socket is open, {@code false} otherwise
     */
    public boolean isServerRunning() {
        return serverSocket != null && !serverSocket.isClosed();
    }

    /**
     * Connects a client to a running server.
     *
     * @param host         the server host (e.g., "localhost")
     * @param port         the server port
     * @param gameManager  the GameManager instance to pass to the client handler
     * @throws IOException if an I/O error occurs when connecting
     */
    public void connectToServer(String host, int port, GameManager gameManager) throws IOException {
        Socket socket = new Socket(host, port);
        BlackjackClientHandler handler = new BlackjackClientHandler(socket, gameManager);
        clientHandlers.add(handler);
        new Thread(handler).start();
    }

    /**
     * Retrieves a copy of all active client handlers.
     *
     * @return a list of current {@code BlackjackClientHandler} instances
     */
    public List<BlackjackClientHandler> getClientHandlers() {
        return new ArrayList<>(clientHandlers);
    }

    /**
     * Sends a message to all connected clients.
     *
     * @param message the message object to broadcast
     */
    public void broadcastGameState(GameManager gm) {
        GameState state = new GameState(gm);
        broadcast(state);
    }
    
    public void broadcast(Object message){}


    /**
     * Shuts down the network, closing the server socket and all client connections.
     */
    public void close() {
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
            for (BlackjackClientHandler handler : clientHandlers) {
                handler.closeConnection();
            }
            clientHandlers.clear();
        } catch (IOException e) {
            System.err.println("Error closing network resources: " + e.getMessage());
        }
    }
}

package main.controller;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class NetworkManager {
    private final List<BlackjackClientHandler> clientHandlers = new CopyOnWriteArrayList<>();
    private ServerSocket serverSocket;
    private boolean isServer = false;

    // Server-side methods
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

    public boolean isServerRunning() {
        return serverSocket != null && !serverSocket.isClosed();
    }

    // Client-side methods
    public void connectToServer(String host, int port, GameManager gameManager) throws IOException {
        Socket socket = new Socket(host, port);
        BlackjackClientHandler handler = new BlackjackClientHandler(socket, gameManager);
        clientHandlers.add(handler);
        new Thread(handler).start();
    }

    public List<BlackjackClientHandler> getClientHandlers() {
        return new ArrayList<>(clientHandlers);
    }

    public void broadcast(Object message) {
        for (BlackjackClientHandler handler : clientHandlers) {
            handler.sendMessage(message);
        }
    }

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
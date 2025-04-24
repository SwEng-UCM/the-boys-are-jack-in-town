package main.controller;

import java.io.*;
import java.net.*;
import main.controller.GameState;

public class BlackjackClient {
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    public BlackjackClient(String host, int port) throws IOException {
        try {
            this.socket = new Socket(host, port);
            this.out = new ObjectOutputStream(socket.getOutputStream());
            this.in = new ObjectInputStream(socket.getInputStream());
        } catch (ConnectException e) {
            throw new IOException("Server not available. Please ensure:\n"
                + "1. The server is running\n"
                + "2. The IP and port are correct\n"
                + "3. No firewall is blocking the connection");
        } catch (IOException e) {
            throw new IOException("Connection error: " + e.getMessage());
        }
    }

    public void sendAction(Object action) {
        try {
            out.writeObject(action);
            out.flush();
        } catch (IOException e) {
            System.err.println("Failed to send action to server: " + e.getMessage());
        }
    }

    public GameState receiveGameState() {
        try {
            Object obj = in.readObject();
            if (obj instanceof GameState) {
                return (GameState) obj;
            } else {
                System.err.println("Received unexpected object type from server.");
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error receiving GameState: " + e.getMessage());
        }
        return null;
    }

    // Optional main method for testing
    public static void main(String[] args) {
        try {
            BlackjackClient client = new BlackjackClient("localhost", 12345);
            System.out.println("Connected to server.");
            // Example: manually receive one game state
            GameState gameState = client.receiveGameState();
            System.out.println("Received game state: " + gameState);
        } catch (IOException e) {
            System.err.println("Unable to connect to server: " + e.getMessage());
        }
    }
}

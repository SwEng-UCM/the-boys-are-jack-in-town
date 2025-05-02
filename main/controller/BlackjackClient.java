package main.controller;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import main.view.BlackjackGUI;

public class BlackjackClient {
    private Socket socket;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    
    public BlackjackClient() {
        // Empty constructor - no auto-connect
    }
    
    public void connect(String host, int port) throws IOException {
        try {
            this.socket = new Socket(host, port);
            this.output = new ObjectOutputStream(socket.getOutputStream());
            this.input = new ObjectInputStream(socket.getInputStream());
            
            // Start listening for server updates
            new Thread(this::listenForUpdates).start();
        } catch (IOException e) {
            throw new IOException("Failed to connect: " + e.getMessage());
        }
    }
    
    private void listenForUpdates() {
        try {
            while (socket.isConnected()) {
                GameState state = (GameState) input.readObject();
                GameManager gm = GameManager.getInstance();
                BlackjackGUI.getInstance(gm).applyGameState(state);
            }
        } catch (Exception e) {
            System.err.println("Connection lost: " + e.getMessage());
        }
    }
    
    public void sendAction(String action) throws IOException {
        output.writeObject(action);
        output.flush();
    }
    
    public void disconnect() throws IOException {
        if (socket != null) {
            socket.close();
        }
    }
}
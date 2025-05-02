package main.controller;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.SwingUtilities;

import main.model.Player;

/**
 * Handles client-side networking for a multiplayer Blackjack game.
 * Manages connection to the server, sending actions, and receiving
 * game state updates or commands.
 */
public class BlackjackClient {
    private Socket socket;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private GameManager gameManager;

    /**
     * Constructs a BlackjackClient associated with a GameManager.
     *
     * @param gameManager the game manager responsible for controlling game state and GUI updates
     */
    public BlackjackClient(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    /**
     * Connects to the game server using the provided host and port.
     * Initializes input/output streams and starts a listener thread.
     *
     * @param host the server's hostname or IP address
     * @param port the port number to connect to
     * @throws IOException if connection or stream setup fails
     */
    public void connect(String host, int port) throws IOException {
        try {
            this.socket = new Socket(host, port);
            this.output = new ObjectOutputStream(socket.getOutputStream());
            this.input = new ObjectInputStream(socket.getInputStream());

            // Start a separate thread to listen for server updates
            new Thread(this::listenForUpdates).start();
        } catch (IOException e) {
            throw new IOException("Connection failed: " + e.getMessage());
        }
    }

    /**
     * Continuously listens for incoming messages from the server.
     * Handles {@link GameStateUpdate} and {@link MultiplayerCommand} messages.
     */
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

    /**
     * Applies a game state update received from the server and refreshes the GUI.
     * GUI updates are dispatched to the EDT using {@link SwingUtilities#invokeLater(Runnable)}.
     *
     * @param update the {@link GameStateUpdate} received from the server
     */
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

    /**
     * Sends a {@link MultiplayerCommand} to the server.
     *
     * @param command the command representing a player action
     * @throws IOException if sending the command fails
     */
    public void sendAction(MultiplayerCommand command) throws IOException {
        try {
            output.writeObject(command);
            output.flush();
        } catch (IOException e) {
            throw new IOException("Failed to send command: " + e.getMessage());
        }
    }

    /**
     * Disconnects from the server and closes all open resources.
     * Handles safe shutdown of socket and streams.
     */
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

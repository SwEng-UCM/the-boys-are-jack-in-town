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
        socket = new Socket(host, port);
        output = new ObjectOutputStream(socket.getOutputStream());
        input = new ObjectInputStream(socket.getInputStream());

        String playerName = "Player" + System.currentTimeMillis(); 
        MultiplayerCommand join = new MultiplayerCommand(MultiplayerCommand.Type.JOIN, playerName, null);
        output.writeObject(join);
        output.flush();

        new Thread(new BlackjackClientListener(input)).start();

        System.out.println("Connected to server as " + playerName);
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

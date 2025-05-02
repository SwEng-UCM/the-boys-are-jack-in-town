import java.io.*;
import java.net.Socket;

public class BlackjackClientHandler implements Runnable {
    private final Socket socket;
    private final GameManager gameManager;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private boolean connected = true;

    public BlackjackClientHandler(Socket socket, GameManager gameManager) {
        this.socket = socket;
        this.gameManager = gameManager;
    }

    @Override
    public void run() {
        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());

            gameManager.addPlayer(this); // Register player

            while (connected) {
                Object input = in.readObject();
                // Handle client input (e.g., bet, hit, stand)
                gameManager.handleClientInput(this, input);
            }

        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Client disconnected: " + e.getMessage());
        } finally {
            closeConnection();
        }
    }

    public void sendMessage(Object message) {
        try {
            out.writeObject(message);
            out.flush();
        } catch (IOException e) {
            System.err.println("Failed to send to client: " + e.getMessage());
        }
    }

    public void closeConnection() {
        try {
            connected = false;
            if (socket != null && !socket.isClosed()) socket.close();
        } catch (IOException e) {
            System.err.println("Error closing client connection: " + e.getMessage());
        }
    }
}

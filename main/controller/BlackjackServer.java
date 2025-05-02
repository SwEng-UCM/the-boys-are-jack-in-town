import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class BlackjackServer {
    private final int port;
    private ServerSocket serverSocket;
    private boolean running = false;
    private final List<BlackjackClientHandler> clientHandlers = new ArrayList<>();

    public BlackjackServer(int port) {
        this.port = port;
    }

    public void start() {
        try {
            serverSocket = new ServerSocket(port);
            running = true;
            System.out.println("Blackjack Server started on port " + port);

            GameManager gameManager = GameManager.getInstance();
            gameManager.setServerMode(true);

            while (running) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress());

                BlackjackClientHandler handler = new BlackjackClientHandler(clientSocket, gameManager);
                clientHandlers.add(handler);

                new Thread(handler).start();
            }

        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
        } finally {
            stop();
        }
    }

    public void stop() {
        running = false;
        try {
            for (BlackjackClientHandler handler : clientHandlers) {
                handler.closeConnection();
            }
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
            System.out.println("Blackjack Server stopped.");
        } catch (IOException e) {
            System.err.println("Error shutting down server: " + e.getMessage());
        }
    }
}

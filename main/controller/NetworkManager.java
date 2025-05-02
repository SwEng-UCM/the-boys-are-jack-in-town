package main.controller;

import java.io.*;
import java.net.*;
import java.util.function.Consumer;

public class NetworkManager {
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private Thread listenThread;
    private Consumer<Object> messageHandler;

    public void connectToServer(String host, int port, Consumer<Object> messageHandler) throws IOException {
        this.socket = new Socket(host, port);
        this.messageHandler = messageHandler;
        setupStreams();
        startListening();
    }

    public void startServer(int port, Consumer<Object> messageHandler) throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            this.socket = serverSocket.accept(); // Blocking call until a client connects
        }
        this.messageHandler = messageHandler;
        setupStreams();
        startListening();
    }

    private void setupStreams() throws IOException {
        this.out = new ObjectOutputStream(socket.getOutputStream());
        this.in = new ObjectInputStream(socket.getInputStream());
    }

    private void startListening() {
        listenThread = new Thread(() -> {
            try {
                while (true) {
                    Object message = in.readObject();
                    if (messageHandler != null) {
                        messageHandler.accept(message);
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Connection closed or error occurred: " + e.getMessage());
            }
        });
        listenThread.start();
    }

    public void send(Object message) {
        try {
            out.writeObject(message);
            out.flush();
        } catch (IOException e) {
            System.err.println("Failed to send message: " + e.getMessage());
        }
    }

    public void close() {
        try {
            if (listenThread != null) listenThread.interrupt();
            if (in != null) in.close();
            if (out != null) out.close();
            if (socket != null) socket.close();
        } catch (IOException e) {
            System.err.println("Error closing network resources: " + e.getMessage());
        }
    }
}

package main.controller;

import java.net.*;
import java.io.*;

public class BlackjackClient {
    private Socket socket;
    private BufferedReader input;
    private PrintWriter output;

    public BlackjackClient(String host, int port) throws IOException {
        socket = new Socket(host, port);
        input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        output = new PrintWriter(socket.getOutputStream(), true);
        new Thread(this::listen).start();
        sendInputLoop();
    }

    private void listen() {
        try {
            String message;
            while ((message = input.readLine()) != null) {
                System.out.println("Server: " + message);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void sendInputLoop() throws IOException {
        BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
        String command;
        while ((command = console.readLine()) != null) {
            output.println(command);
        }
    }

    public static void main(String[] args) throws IOException {
        new BlackjackClient("localhost", 12345);
    }
}

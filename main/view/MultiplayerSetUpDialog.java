package main.view;

import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import java.awt.GridLayout;

import main.controller.BlackjackClient;
import main.controller.GameManager;
import main.controller.BlackjackServer;

/**
 * A dialog utility class to configure and launch a multiplayer Blackjack game,
 * allowing the user to choose to host (as a server) or join (as a client).
 */
public class MultiplayerSetUpDialog {

    /**
     * Displays the multiplayer setup dialog, asking the user if they want to
     * host or join a multiplayer game.
     *
     * @param parentFrame the parent JFrame to use as the dialog anchor
     */
    public static void show(JFrame parentFrame) {
        Object[] options = {"Host (Server)", "Join (Client)", "Cancel"};
        int choice = JOptionPane.showOptionDialog(
                parentFrame,
                "Do you want to host or join a game?",
                "Multiplayer Setup",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
        );

        if (choice == JOptionPane.YES_OPTION) {
            startServer(parentFrame);
        } else if (choice == JOptionPane.NO_OPTION) {
            connectAsClient(parentFrame);
        }
    }

    /**
     * Prompts the user to enter a port and starts the Blackjack server on that port.
     * Also launches the game GUI in server mode.
     *
     * @param parentFrame the parent JFrame to anchor input dialogs
     */
    private static void startServer(JFrame parentFrame) {
        String portStr = JOptionPane.showInputDialog(parentFrame, "Enter port number to host on:", "12345");
        if (portStr != null) {
            try {
                int port = Integer.parseInt(portStr);
                
                new Thread(() -> {
                    BlackjackServer server = new BlackjackServer(port);
                    server.start();
                }).start();

                JOptionPane.showMessageDialog(parentFrame, "Server started. Waiting for clients...");

                GameManager gameManager = GameManager.getInstance();
                gameManager.setMultiplayerMode(true);

                SwingUtilities.invokeLater(() -> {
                    BlackjackGUI gui = new BlackjackGUI(gameManager);
                    gui.setVisible(true);
                    parentFrame.dispose();
                });

            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(parentFrame, "Invalid port number", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Prompts the user to enter server IP and port, then connects to the server
     * as a client and launches the client-side GUI.
     *
     * @param parentFrame the parent JFrame to anchor input dialogs
     */
    private static void connectAsClient(JFrame parentFrame) {
        JPanel connectionPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        JTextField ipField = new JTextField("localhost");
        JTextField portField = new JTextField("12345");

        connectionPanel.add(new JLabel("Server IP:"));
        connectionPanel.add(ipField);
        connectionPanel.add(new JLabel("Port:"));
        connectionPanel.add(portField);

        int result = JOptionPane.showConfirmDialog(
                parentFrame,
                connectionPanel,
                "Connect to Server",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            try {
                String serverIP = ipField.getText();
                int port = Integer.parseInt(portField.getText());

                JOptionPane.showMessageDialog(parentFrame, "Attempting to connect to server...");

                new Thread(() -> {
                    try {
                        GameManager gameManager = GameManager.getInstance();
                        BlackjackClient client = new BlackjackClient(gameManager);
                        client.connect(serverIP, port);
                        gameManager.setMultiplayerMode(true);
                        gameManager.setClient(client);

                        SwingUtilities.invokeLater(() -> {
                            BlackjackGUI gui = new BlackjackGUI(gameManager);
                            gui.setVisible(true);
                            parentFrame.dispose();
                        });
                    } catch (IOException ex) {
                        SwingUtilities.invokeLater(() -> {
                            JOptionPane.showMessageDialog(
                                    parentFrame,
                                    "Connection failed: " + ex.getMessage(),
                                    "Connection Error",
                                    JOptionPane.ERROR_MESSAGE
                            );
                        });
                    }
                }).start();

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(parentFrame, "Invalid port number", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}

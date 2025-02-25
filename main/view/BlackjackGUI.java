package main.view;

import javax.swing.*;
import java.awt.*;

public class BlackjackGUI extends JFrame {

    private JPanel mainPanel;
    private JButton hitButton;
    private JButton standButton;
    private JButton newGameButton;
    private JLabel playerCardsLabel;
    private JLabel dealerCardsLabel;
    private JLabel gameMessageLabel;

    public BlackjackGUI() {
        setTitle("Blackjack Game");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initializeComponents();
        layoutComponents();
    }

    private void initializeComponents() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        hitButton = new JButton("Hit");
        standButton = new JButton("Stand");
        newGameButton = new JButton("New Game");

        playerCardsLabel = new JLabel("Player Cards: ");
        dealerCardsLabel = new JLabel("Dealer Cards: ");
        gameMessageLabel = new JLabel("Welcome to Blackjack!");

        playerCardsLabel.setFont(new Font("Arial", Font.BOLD, 16));
        dealerCardsLabel.setFont(new Font("Arial", Font.BOLD, 16));
        gameMessageLabel.setFont(new Font("Arial", Font.BOLD, 18));
    }

    private void layoutComponents() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(hitButton);
        buttonPanel.add(standButton);
        buttonPanel.add(newGameButton);

        JPanel cardsPanel = new JPanel(new GridLayout(2, 1));
        cardsPanel.add(playerCardsLabel);
        cardsPanel.add(dealerCardsLabel);

        mainPanel.add(gameMessageLabel, BorderLayout.NORTH);
        mainPanel.add(cardsPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            BlackjackGUI gui = new BlackjackGUI();
            gui.setVisible(true);
        });
    }
}


package main.view;

import main.controller.GameManager;

import javax.swing.*;
import java.awt.*;

public class BlackJackMenu extends JFrame {
    private JButton startButton, instructionsButton, exitButton;
    private JLabel imageLabel;

    public BlackJackMenu() {
        setTitle("Blackjack Menu");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        initializeComponents();
        layoutComponents();
        attachEventListeners();
    }

    private void initializeComponents() {
        startButton = createStyledButton("Start Game");
        instructionsButton = createStyledButton("Instructions");
        exitButton = createStyledButton("Exit");

        // Load and resize the image
        ImageIcon originalIcon = new ImageIcon("img/blackjack.png");
        Image originalImage = originalIcon.getImage();
        Image resizedImage = originalImage.getScaledInstance(400, 200, Image.SCALE_SMOOTH);
        ImageIcon resizedIcon = new ImageIcon(resizedImage);
        imageLabel = new JLabel(resizedIcon);
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
    }

    

    private void layoutComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel buttonPanel = new JPanel(new GridLayout(3, 1, 30, 30));
        buttonPanel.add(startButton);
        buttonPanel.add(instructionsButton);
        buttonPanel.add(exitButton);

        mainPanel.add(imageLabel, BorderLayout.NORTH);
        mainPanel.add(buttonPanel, BorderLayout.CENTER);

        add(mainPanel);
    }

    private void attachEventListeners() {
        startButton.addActionListener(e -> {
            GameManager gameManager = new GameManager();
            BlackjackGUI gui = new BlackjackGUI(gameManager);
            gui.setVisible(true);
            dispose(); // Close the menu window
        });

        instructionsButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Instructions:\n1. Click 'Hit' to draw a card.\n2. Click 'Stand' to end your turn.\n3. Try to get as close to 21 without going over.", "Instructions", JOptionPane.INFORMATION_MESSAGE);
        });

        exitButton.addActionListener(e -> System.exit(0));
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 25));
        button.setBackground(new Color(255, 215, 0)); // Gold
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(50, 10)); // Set preferred size to make buttons smaller
        return button;
    }
}

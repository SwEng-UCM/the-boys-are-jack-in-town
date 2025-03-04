package main.view;

import main.controller.GameManager;

import javax.swing.*;
import java.awt.*;

import static main.view.Languages.*;

public class BlackJackMenu extends JFrame {
    private JButton startButton, instructionsButton, exitButton;
    private JLabel imageLabel;

    public static int language = 2;

    public BlackJackMenu() {
        //setTitle("Blackjack Menu");
        setTitle(Texts.startGame[language]);
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        initializeComponents();
        layoutComponents();
        attachEventListeners();
    }

    private void initializeComponents() {
        startButton = createStyledButton(Texts.startGame[language]); // "Start Game"
        instructionsButton = createStyledButton(Texts.instructions[language]);
        exitButton = createStyledButton(Texts.exit[language]);

        // Load and resize the image
        ImageIcon originalIcon = new ImageIcon("img/blackjack.png");
        Image originalImage = originalIcon.getImage();
        Image resizedImage = originalImage.getScaledInstance(500, 300, Image.SCALE_SMOOTH);
        ImageIcon resizedIcon = new ImageIcon(resizedImage);
        imageLabel = new JLabel(resizedIcon);
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
    }

    private void layoutComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel buttonPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.insets = new Insets(5, 0, 5, 0); // Add some space between buttons
        buttonPanel.add(startButton, gbc);
        buttonPanel.add(instructionsButton, gbc);
        buttonPanel.add(exitButton, gbc);

        mainPanel.add(imageLabel, BorderLayout.NORTH);
        mainPanel.add(buttonPanel, BorderLayout.CENTER);

        add(mainPanel);
    }


    // private JPanel createCenteredPanel(JButton button) {
    //     JPanel panel = new JPanel(new GridBagLayout());
    //     panel.add(button);
    //     return panel;
    // }

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
        button.setPreferredSize(new Dimension(250, 50)); // Set preferred size to make buttons smaller
        return button;
    }
}
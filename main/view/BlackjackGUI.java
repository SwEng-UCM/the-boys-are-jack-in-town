package main.view;

import main.controller.GameManager;
import main.model.Card;
import main.model.Player;

import javax.swing.*;
import java.awt.*;

import static main.view.BlackJackMenu.language;

/**
 * The BlackjackGUI class represents the graphical user interface for the Blackjack game.
 * It displays the game state, player actions, and messages to the user.
 */
public class BlackjackGUI extends JFrame {
    private JPanel mainPanel, dealerPanel, playerPanel, buttonPanel, dealerScorePanel, playerScorePanel;
    private JButton hitButton, standButton, newGameButton;
    private JLabel gameMessageLabel, dealerScoreLabel, playerScoreLabel;
    private GameManager gameManager;

    public BlackjackGUI(GameManager gameManager) {
        this.gameManager = gameManager;
        gameManager.setGui(this);

        setTitle(Texts.guiTitle[language]);// "Blackjack Game"
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        initializeComponents();
        layoutComponents();
        attachEventListeners();
    }

    private void initializeComponents() {
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(34, 139, 34)); // Casino table green
    
        hitButton = createStyledButton(Texts.guiHit[language]);
        standButton = createStyledButton(Texts.guiStand[language]);
        newGameButton = createStyledButton(Texts.guiNewGame[language]);
    
        gameMessageLabel = new JLabel("Welcome to Blackjack!", SwingConstants.CENTER);
        gameMessageLabel.setFont(new Font("Arial", Font.BOLD, 22));
        gameMessageLabel.setForeground(Color.WHITE);
    
        dealerScoreLabel = createStyledLabel("Dealer's Score: 0");
        playerScoreLabel = createStyledLabel("Player's Score: 0");
    
        // Initialize Panels
        dealerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        dealerPanel.setOpaque(false);
    
        playerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        playerPanel.setOpaque(false);
    
        dealerScorePanel = new JPanel();
        dealerScorePanel.setOpaque(false);
        dealerScorePanel.add(dealerScoreLabel);
    
        playerScorePanel = new JPanel();
        playerScorePanel.setOpaque(false);
        playerScorePanel.add(playerScoreLabel);
    
        buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 60));
        buttonPanel.setOpaque(false);
        buttonPanel.add(hitButton);
        buttonPanel.add(standButton);
        buttonPanel.add(newGameButton);
    }
    

    private void layoutComponents() {
        mainPanel.setLayout(new BorderLayout());
    
        // Dealer Section (Top)
        JPanel dealerArea = new JPanel(new BorderLayout());
        dealerArea.setOpaque(false);
        dealerArea.add(dealerScorePanel, BorderLayout.NORTH);
        dealerArea.add(dealerPanel, BorderLayout.CENTER);
    
        // Player Section (Bottom) - Wrapped with Back Button
        JPanel playerContainer = new JPanel(new BorderLayout());
        playerContainer.setOpaque(false);
        playerContainer.add(playerPanel, BorderLayout.CENTER);
        playerContainer.add(playerScorePanel, BorderLayout.NORTH);
    
        // Back Button Panel
        JPanel backButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton backButton = createStyledButton("Back to Main Menu");
        backButtonPanel.setOpaque(false);
        backButtonPanel.add(backButton);
    
        // Add action to back button
        backButton.addActionListener(e -> {
            new BlackJackMenu().setVisible(true);
            dispose(); // Close the game window
        });
    
        // Add Back Button to playerContainer BELOW the player's panel
        playerContainer.add(backButtonPanel, BorderLayout.SOUTH);
    
        // Center Section (Buttons and Messages)
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setOpaque(false);
        centerPanel.add(gameMessageLabel, BorderLayout.NORTH);
        centerPanel.add(buttonPanel, BorderLayout.CENTER);
    
        // Add everything to the main panel
        mainPanel.add(dealerArea, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(playerContainer, BorderLayout.SOUTH); // The entire player area with back button
    
        add(mainPanel);
    
        gameManager.startNewGame();
    }
    
    private void attachEventListeners() {
        hitButton.addActionListener(e -> gameManager.handlePlayerHit());
        standButton.addActionListener(e -> gameManager.handlePlayerStand());
        newGameButton.addActionListener(e -> gameManager.startNewGame());
    }

    public void updateGameState(Player player, Player dealer, boolean gameOver) {
        playerPanel.removeAll();
        dealerPanel.removeAll();
        
        // Show player's cards and check for special cards
        for (Card card : player.getHand()) {
            playerPanel.add(createCardPanel(card));
            checkForSpecialCard(card);
        }
    
        // Show dealer's cards
        if (gameOver) {
            for (Card card : dealer.getHand()) {
                dealerPanel.add(createCardPanel(card));
                checkForSpecialCard(card);
            }
            dealerScoreLabel.setText("Dealer's Score: " + dealer.calculateScore());
        } else {
            dealerPanel.add(createCardPanel(dealer.getHand().get(0)));
            dealerPanel.add(createHiddenCardPanel());
            dealerScoreLabel.setText("Dealer's Score: ???");
        }
    
        playerScoreLabel.setText("Player's Score: " + player.calculateScore());
    
        // Refresh UI
        playerPanel.revalidate();
        playerPanel.repaint();
        dealerPanel.revalidate();
        dealerPanel.repaint();
    }
    
    
    
    
    
    private JPanel createHiddenCardPanel() {
        JPanel cardPanel = new JPanel();
        cardPanel.setPreferredSize(new Dimension(80, 120));
        cardPanel.setBackground(Color.BLACK);
        cardPanel.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
    
        JLabel hiddenLabel = new JLabel("?", SwingConstants.CENTER);
        hiddenLabel.setFont(new Font("Arial", Font.BOLD, 24));
        hiddenLabel.setForeground(Color.WHITE);
    
        cardPanel.setLayout(new BorderLayout());
        cardPanel.add(hiddenLabel, BorderLayout.CENTER);
    
        return cardPanel;
    }
    

    public void updateGameMessage(String message) {
        gameMessageLabel.setText(message);
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setBackground(new Color(255, 215, 0)); // Gold
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        return button;
    }

    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 16));
        label.setForeground(Color.WHITE);
        return label;
    }

    private JPanel createCardPanel(Card card) {
        JPanel cardPanel = new JPanel();
        cardPanel.setPreferredSize(new Dimension(80, 120));
        cardPanel.setBackground(Color.WHITE);
        cardPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

        JLabel rankLabel = new JLabel(card.getRank(), SwingConstants.CENTER);
        rankLabel.setFont(new Font("Arial", Font.BOLD, 18));

        JLabel suitLabel = new JLabel(card.getSuit(), SwingConstants.CENTER);
        suitLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        cardPanel.setLayout(new BorderLayout());
        cardPanel.add(rankLabel, BorderLayout.CENTER);
        cardPanel.add(suitLabel, BorderLayout.SOUTH);

        return cardPanel;
    }

    public int promptJokerWildValue() {
        int wildValue = 0;
        boolean valid = false;
        while (!valid) {
            String input = JOptionPane.showInputDialog(
                this, 
                "Joker Wild! Choose a value between 1 and 11: 🤡", 
                "Joker Wild", 
                JOptionPane.QUESTION_MESSAGE
            );
            try {
                wildValue = Integer.parseInt(input);
                if (wildValue >= 1 && wildValue <= 11) {
                    valid = true;
                } else {
                    JOptionPane.showMessageDialog(
                        this, 
                        "Invalid choice. Please choose a value between 1 and 11.",
                        "Invalid Input",
                        JOptionPane.WARNING_MESSAGE
                    );
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(
                    this, 
                    "Please enter a valid number.", 
                    "Invalid Input", 
                    JOptionPane.WARNING_MESSAGE
                );
            }
        }
        return wildValue;
    }

    private void checkForSpecialCard(Card card) {
        if (card.isJokerWild()) {
            updateGameMessage("Joker Wild! You can choose its value between 1 and 11.");
        } else if (card.isSplitAce()) {
            updateGameMessage("Split Ace drawn! Your score will be halved.");
        } else if (card.isBlackjackBomb()) {
            updateGameMessage("Blackjack Bomb! The game is over, and the Blackjack Bomb wins.");
        }
    }

}

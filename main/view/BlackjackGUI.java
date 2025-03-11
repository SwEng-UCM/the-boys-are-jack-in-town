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
    private JPanel mainPanel, dealerPanel, playerPanel, buttonPanel, dealerScorePanel, playerScorePanel, betPanel;
    private JButton hitButton, standButton, newGameButton, placeBetButton;
    private JLabel gameMessageLabel, dealerScoreLabel, playerScoreLabel, dealerBalanceLabel, balanceLabel, dealerBetLabel, betLabel;
    private JTextField betField;
    private GameManager gameManager;

    public BlackjackGUI(GameManager gameManager) {
        this.gameManager = gameManager;
        gameManager.setGui(this);

        setTitle(Texts.guiTitle[language]); // "Blackjack Game"
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
        placeBetButton = createStyledButton("Place Bet");

        gameMessageLabel = new JLabel("Welcome to Blackjack!", SwingConstants.CENTER);
        gameMessageLabel.setFont(new Font("Arial", Font.BOLD, 22));
        gameMessageLabel.setForeground(Color.WHITE);

        dealerScoreLabel = createStyledLabel(Texts.guiDealerScore[language]);
        playerScoreLabel = createStyledLabel(Texts.guiPlayerScore[language]);
        balanceLabel = createStyledLabel("Balance: $1000");
        betLabel = createStyledLabel("Bet: $0");

        betField = new JTextField(10);

        // Initialize Panels
        dealerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        dealerPanel.setOpaque(false);

        playerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        playerPanel.setOpaque(false);

        dealerBalanceLabel = createStyledLabel("Dealer Balance: $1000");
        dealerBetLabel = createStyledLabel("Dealer Bet: $0");
        
        // Use GridLayout for dealerScorePanel to place score on the first line and balance/bet on the second line
        dealerScorePanel = new JPanel(new GridLayout(2, 1)); // 2 rows, 1 column
        dealerScorePanel.setOpaque(false);

            // First row: Dealer score
    JPanel scoreRow = new JPanel(new FlowLayout(FlowLayout.CENTER));
    scoreRow.setOpaque(false);
    scoreRow.add(dealerScoreLabel);

    // Second row: Dealer balance and bet
    JPanel balanceBetRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0)); // 20px horizontal gap
    balanceBetRow.setOpaque(false);
    balanceBetRow.add(dealerBalanceLabel);
    balanceBetRow.add(dealerBetLabel);

    // Add rows to dealerScorePanel
    dealerScorePanel.add(scoreRow);
    dealerScorePanel.add(balanceBetRow);
    
        playerScorePanel = new JPanel();
        playerScorePanel.setOpaque(false);
        playerScorePanel.add(playerScoreLabel);

        playerScorePanel = new JPanel();
        playerScorePanel.setOpaque(false);
        playerScorePanel.add(playerScoreLabel);

        buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setOpaque(false);
        buttonPanel.add(hitButton);
        buttonPanel.add(standButton);
        buttonPanel.add(newGameButton);

        betPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        betPanel.setOpaque(false);
        betPanel.add(new JLabel("Enter Bet:"));
        betPanel.add(betField);
        betPanel.add(placeBetButton);
        betPanel.add(balanceLabel);
        betPanel.add(betLabel);
    }

    private void layoutComponents() {
        mainPanel.setLayout(new BorderLayout());

        // Dealer Section (Top)
        JPanel dealerArea = new JPanel(new BorderLayout());
        dealerArea.setOpaque(false);
        dealerArea.add(dealerScorePanel, BorderLayout.NORTH); // Dealer balance and bet at the top
        dealerArea.add(dealerPanel, BorderLayout.CENTER); // Dealer's cards below

        // Player Section (Bottom) - Wrapped with Back Button
        JPanel playerContainer = new JPanel(new BorderLayout());
        playerContainer.setOpaque(false);
        playerContainer.add(playerPanel, BorderLayout.CENTER);
        playerContainer.add(playerScorePanel, BorderLayout.NORTH);

        // Back Button Panel
        JPanel backButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton backButton = createStyledButton(Texts.guiBackToMain[language]);
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
        JPanel southPanel = new JPanel(new BorderLayout()); // New container
        southPanel.setOpaque(false);
        southPanel.add(playerContainer, BorderLayout.CENTER);
        southPanel.add(betPanel, BorderLayout.SOUTH);
        
        mainPanel.add(southPanel, BorderLayout.SOUTH); // Now added as a single section
        
        add(mainPanel);

        gameManager.startNewGame();
    }

    private void attachEventListeners() {
        hitButton.addActionListener(e -> gameManager.handlePlayerHit());
        standButton.addActionListener(e -> gameManager.handlePlayerStand());
        newGameButton.addActionListener(e -> gameManager.startNewGame());
        placeBetButton.addActionListener(e -> placeBet());
    }

    /*
     *  Betting system logic.
    */
    private void placeBet() {
        try {
            int betAmount = Integer.parseInt(betField.getText());
            if (betAmount > 0 && gameManager.placeBet(betAmount)) {
                betLabel.setText("Bet: $" + betAmount);
                balanceLabel.setText("Balance: $" + gameManager.getPlayerBalance());
    
                // Disable the bet field and button after placing a bet
                betField.setEnabled(false);
                placeBetButton.setEnabled(false);
    
                // Update UI to reflect the new bet and balance
                JOptionPane.showMessageDialog(this, "Bet placed: $" + betAmount, "Bet Confirmed", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Invalid bet amount or insufficient balance.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number.", "Invalid Input", JOptionPane.WARNING_MESSAGE);
        }
    }

    // After the game ended the user should be able to take another bet
    public void enableBetting() {
        System.out.println("enableBetting called");
        System.out.println("Player Balance: " + gameManager.getPlayerBalance());
        System.out.println("Dealer Balance: " + gameManager.getDealerBalance());
        System.out.println("Dealer Bet: " + gameManager.getDealerBet());
    
        betField.setEnabled(true);
        placeBetButton.setEnabled(true);
        betField.setText(""); // Clear the bet field
        betLabel.setText("Bet: $0"); // Reset the player's bet label
        balanceLabel.setText("Balance: $" + gameManager.getPlayerBalance()); // Update the player's balance
        dealerBalanceLabel.setText("Dealer Balance: $" + gameManager.getDealerBalance()); // Update the dealer's balance
        dealerBetLabel.setText("Dealer Bet: $" + gameManager.getDealerBet()); // Update the dealer's bet
    
        System.out.println("UI Updated - Player Balance: " + gameManager.getPlayerBalance() + 
                           ", Dealer Balance: " + gameManager.getDealerBalance() + 
                           ", Dealer Bet: " + gameManager.getDealerBet());

            // Force UI refresh
    balanceLabel.revalidate();
    balanceLabel.repaint();
    dealerBalanceLabel.revalidate();
    dealerBalanceLabel.repaint();
    dealerBetLabel.revalidate();
    dealerBetLabel.repaint();

    // Refresh the entire main panel
    mainPanel.revalidate();
    mainPanel.repaint();

    System.out.println("UI Updated - Player Balance: " + gameManager.getPlayerBalance() + 
                       ", Dealer Balance: " + gameManager.getDealerBalance() + 
                       ", Dealer Bet: " + gameManager.getDealerBet());
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
            dealerScoreLabel.setText(Texts.guiDealerScore[language] + " : " + dealer.calculateScore());
        } else {
            dealerPanel.add(createCardPanel(dealer.getHand().get(0)));
            dealerPanel.add(createHiddenCardPanel());
            dealerScoreLabel.setText(Texts.guiDealerScore[language] + " ???");
        }

        playerScoreLabel.setText(Texts.guiPlayerScore[language] + " : " + player.calculateScore());

        // Update balances and bets
        balanceLabel.setText("Balance: $" + gameManager.getPlayerBalance());
        dealerBalanceLabel.setText("Dealer Balance: $" + gameManager.getDealerBalance());
        dealerBetLabel.setText("Dealer Bet: $" + gameManager.getDealerBet());


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
        } else {
//            updateGameMessage(""); this overwrites all GameMessage text.
        }
    }
}
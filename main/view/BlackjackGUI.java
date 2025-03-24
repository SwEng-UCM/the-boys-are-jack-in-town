package main.view;

import main.controller.BettingManager;
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
    private JLabel gameMessageLabel, dealerScoreLabel, playerScoreLabel, dealerBalanceLabel, balanceLabel, dealerBetLabel, betLabel, specialMessageLabel, enterBetLabel;
    private JTextField betField;
    private GameManager gameManager;

    public BlackjackGUI(GameManager gameManager) {
        this.gameManager = gameManager;
        gameManager.setGui(this);

        setTitle(Texts.guiTitle[language]);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        //setSize(1366, 768);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Custom image icon
        ImageIcon icon = new ImageIcon("img/black.png");
        setIconImage(icon.getImage());

        initializeComponents();
        layoutComponents();
        attachEventListeners();

        specialMessageLabel.setText("...");
    }

    private void initializeComponents() {
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(34, 139, 34)); // Casino table green

        hitButton = createStyledButton(Texts.guiHit[language]);
        standButton = createStyledButton(Texts.guiStand[language]);
        newGameButton = createStyledButton(Texts.guiNewGame[language]);
        placeBetButton = createStyledButton(Texts.placeBet[language]);

        gameMessageLabel = new JLabel(Texts.welcomeMessage[language], SwingConstants.CENTER);
        gameMessageLabel.setFont(new Font("Arial", Font.BOLD, 26));
        gameMessageLabel.setForeground(Color.WHITE);

        specialMessageLabel = new JLabel("", SwingConstants.CENTER);
        specialMessageLabel.setFont(new Font("Arial", Font.BOLD, 32));
        specialMessageLabel.setForeground(Color.WHITE);

        dealerScoreLabel = createStyledLabel(Texts.guiDealerScore[language]);
        playerScoreLabel = createStyledLabel(Texts.guiPlayerScore[language]);
        balanceLabel = createStyledLabel(Texts.balance[language] + " $1000");
        betLabel = createStyledLabel(Texts.bet[language] + " $0");

        betField = new JTextField(10);
        betField.setPreferredSize(new Dimension(200, 50));

        // Initialize Panels
        dealerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        dealerPanel.setOpaque(false);

        playerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        playerPanel.setOpaque(false);

        dealerBalanceLabel = createStyledLabel(Texts.dealerBalance[language] + " $1000");
        dealerBetLabel = createStyledLabel(Texts.dealerBet[language] + " $0"); 
        
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

        enterBetLabel = new JLabel(Texts.enterBet[language]);
        enterBetLabel.setFont(new Font("Arial", Font.BOLD, 22));
        enterBetLabel.setForeground(Color.WHITE);

        betPanel.setOpaque(false);
        betPanel.add(enterBetLabel);
        betPanel.add(betField);
        betPanel.add(placeBetButton);
        betPanel.add(balanceLabel);
        betPanel.add(betLabel);

//        betPanel.setPreferredSize(new Dimension(200, 55));
    }


    private void layoutComponents() {
        mainPanel.setLayout(new BorderLayout());

        // Dealer Section (Top)
        JPanel dealerArea = new JPanel(new BorderLayout());
        dealerArea.setOpaque(false);
        dealerArea.add(dealerScorePanel, BorderLayout.NORTH); // Dealer balance and bet at the top
        dealerArea.add(dealerPanel, BorderLayout.CENTER); // Dealer's cards below

        // Back Button Panel (Top-Left)
        JPanel backButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton backButton = createStyledButton(Texts.guiBackToMain[language]);
        backButtonPanel.setOpaque(false);
        backButtonPanel.add(backButton);

        // Add action to back button
        backButton.addActionListener(e -> {
            new BlackJackMenu().setVisible(true);
            dispose(); // Close the game window
        });

        // Create a new top panel to hold back button and dealer area
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.add(backButtonPanel, BorderLayout.WEST); // Back button at the top-left
        topPanel.add(dealerArea, BorderLayout.CENTER); // Dealer area next to it

        // Player Section (Bottom)
        JPanel playerContainer = new JPanel(new BorderLayout());
        playerContainer.setOpaque(false);
        playerContainer.add(playerPanel, BorderLayout.CENTER);
        playerContainer.add(playerScorePanel, BorderLayout.NORTH);

        // Center Section (Buttons and Messages)
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setOpaque(false);
        centerPanel.add(gameMessageLabel, BorderLayout.NORTH);
        centerPanel.add(specialMessageLabel, BorderLayout.SOUTH);
        centerPanel.add(buttonPanel, BorderLayout.CENTER);

        // Bottom Section (Player and Bet Panel)
        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.setOpaque(false);
        southPanel.add(playerContainer, BorderLayout.CENTER);
        southPanel.add(betPanel, BorderLayout.SOUTH);

        // Add everything to the main panel
        mainPanel.add(topPanel, BorderLayout.NORTH); // Now using topPanel to position back button correctly
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(southPanel, BorderLayout.SOUTH);

        add(mainPanel);

        gameManager.startNewGame();
    }


    public void restartGame() {
        gameManager = GameManager.getInstance(); // Get game instance
        gameManager.setGui(this);
    
        // Reset betting manager using the public method
        gameManager.resetBettingManager(1000, 1000);
    
        enableBetting(); // Enable betting input again
        gameManager.startNewGame(); // Start a fresh game
    }

    public void showGameOverMessage(String message) {
        // Show a pop-up with "Game Over" message and Restart button
        int option = JOptionPane.showOptionDialog(
            this,
            Texts.gameOverMessage[language],
            Texts.gameOverMessage[language],
            JOptionPane.YES_NO_OPTION,
            JOptionPane.ERROR_MESSAGE,
            null,
            new String[]{Texts.restartGame[language], Texts.exitGame[language]}, // Buttons
            Texts.restartGame[language]
        );
    
        // If user clicks "Restart Game", reset everything
        if (option == JOptionPane.YES_OPTION) {
            restartGame();
        } else {
            System.exit(0); // Exit the game if "Exit" is selected
        }
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
        betLabel.setText(Texts.bet[language] + " $" + betAmount);
        balanceLabel.setText(Texts.balance[language] + " $" + gameManager.getPlayerBalance());

        betField.setEnabled(false);
        placeBetButton.setEnabled(false);

        JOptionPane.showMessageDialog(
            this, 
            Texts.placeBet[language] + ": $" + betAmount, 
            "Bet Confirmed", 
            JOptionPane.INFORMATION_MESSAGE
        );
    } else {
        JOptionPane.showMessageDialog(
            this, 
            Texts.betError[language], 
            Texts.error[language], 
            JOptionPane.ERROR_MESSAGE
        );
    }
} catch (NumberFormatException e) { 
    JOptionPane.showMessageDialog(
        this, 
        Texts.invalidInput[language], 
        Texts.invalidInputTitle[language], 
        JOptionPane.WARNING_MESSAGE
    );
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
        betLabel.setText(Texts.bet[language] + " $0");
        balanceLabel.setText(Texts.balance[language] + " $" + gameManager.getPlayerBalance());
        dealerBalanceLabel.setText(Texts.balance[language] + " $" + gameManager.getDealerBalance());
        dealerBetLabel.setText(Texts.bet[language] + " $" + gameManager.getDealerBet());
    
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

        //updateSpecialMessage("- - - ");


        // Show player's cards and check for special cards
        for (Card card : player.getHand()) {
            playerPanel.add(createCardPanel(card));
            //checkForSpecialCard(card);
        }

        // Show dealer's cards
        if (gameOver) {
            for (Card card : dealer.getHand()) {
                dealerPanel.add(createCardPanel(card));
                //checkForSpecialCard(card);
            }
            dealerScoreLabel.setText(Texts.guiDealerScore[language] + " : " + dealer.calculateScore());
        } else {
            dealerPanel.add(createCardPanel(dealer.getHand().get(0)));
            dealerPanel.add(createHiddenCardPanel());
            dealerScoreLabel.setText(Texts.guiDealerScore[language] + " ???");
        }

        playerScoreLabel.setText(Texts.guiPlayerScore[language] + " : " + player.calculateScore());

        // Update balances and bets
        balanceLabel.setText(Texts.balance[language] + " $" + gameManager.getPlayerBalance());
        dealerBalanceLabel.setText(Texts.dealerBalance[language] + " $" + gameManager.getDealerBalance());
        dealerBetLabel.setText(Texts.dealerBet[language] + " $" + gameManager.getDealerBet());



        // Refresh UI
        playerPanel.revalidate();
        playerPanel.repaint();
        dealerPanel.revalidate();
        dealerPanel.repaint();
    }

    private JPanel createHiddenCardPanel() {
        JPanel cardPanel = new JPanel();
        cardPanel.setPreferredSize(new Dimension(180, 240));
        cardPanel.setBackground(Color.BLACK);
        cardPanel.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));

        JLabel hiddenLabel = new JLabel("?", SwingConstants.CENTER);
        hiddenLabel.setFont(new Font("Arial", Font.BOLD, 24));
        hiddenLabel.setForeground(Color.WHITE);

        cardPanel.setLayout(new BorderLayout());
        cardPanel.add(hiddenLabel, BorderLayout.CENTER);

        // Location of custom background
        ImageIcon cardBackground = new ImageIcon("img/card-background2.jpeg");
        hiddenLabel.setIcon(cardBackground);

        return cardPanel;
    }

    public void updateGameMessage(String message) {
        gameMessageLabel.setText(message);
    }


    public void updateSpecialMessage(String message) {
        if (!message.equals("...")) {
            specialMessageLabel.setText(message);
            Timer timer = new Timer(2000, e -> specialMessageLabel.setText("...")); // Reset after 2 sec
            timer.setRepeats(false);
            timer.start();
        }
    }

    public void resetSpecialMessage() {
        specialMessageLabel.setText("...");
    }


    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(300, 75));
        button.setFont(new Font("Arial", Font.BOLD, 26));
        button.setBackground(new Color(255, 215, 0)); // Gold
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        return button;
    }

    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 26));
        label.setForeground(Color.WHITE);
        return label;
    }

    private JPanel createCardPanel(Card card) {
        JPanel cardPanel = new JPanel();
        cardPanel.setPreferredSize(new Dimension(180, 240));
        cardPanel.setBackground(Color.WHITE);
        cardPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 4));

        JLabel rankLabel = new JLabel(card.getRank(), SwingConstants.CENTER);
        rankLabel.setFont(new Font("Arial", Font.BOLD, 24));

        JLabel suitLabel = new JLabel(card.getSuit(), SwingConstants.CENTER);
        suitLabel.setFont(new Font("Arial", Font.PLAIN, 18));

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
                    Texts.jokerWildMessage[language],  
                    Texts.jokerWildTitle[language],   
                    JOptionPane.QUESTION_MESSAGE
            );
            try {
                wildValue = Integer.parseInt(input);
                if (wildValue >= 1 && wildValue <= 11) {
                    valid = true;
                } else {
                    JOptionPane.showMessageDialog(
                            this,
                            Texts.invalidJokerInput[language], 
                            Texts.invalidInputTitle[language], 

                            JOptionPane.WARNING_MESSAGE
                    );
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(
                        this,
                        Texts.invalidJokerInput[language], 
                        Texts.invalidInputTitle[language], 
                        JOptionPane.WARNING_MESSAGE
                );
            }
        }
        return wildValue;
    }

    private void checkForSpecialCard(Card card) {
        if (card.isJokerWild()) {
            updateGameMessage(Texts.jokerWildMessage[language]);
        } else if (card.isSplitAce()) {
            updateGameMessage(Texts.splitAceMessage[language]);
        } else if (card.isBlackjackBomb()) {
            updateGameMessage(Texts.blackjackBombMessage[language]);
        } else {
            updateSpecialMessage("...");
        }
    }
}


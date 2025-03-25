package main.view;

import main.controller.GameManager;
import main.model.Card;
import main.model.Player;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

import static main.view.BlackJackMenu.language;

public class BlackjackGUI extends JFrame {
    private int gameWidth = 800;
    private int gameHeight = 600;
    private int buttonWidth = (int) (gameWidth * 0.15);
    private int buttonHeight = (int) (gameHeight * 0.08);
    private int buttonFontSize = gameWidth / 60;
    private int cardWidth = (int) (gameWidth * 0.10);
    private int cardHeight = (int) (gameHeight * 0.22);
    private int cardFontSize = gameWidth / 60;
    private JPanel mainPanel, dealerPanel, playersPanel, buttonPanel, dealerScorePanel, playerScorePanel, betPanel;
    private JButton hitButton, standButton, newGameButton, placeBetButton;
    private JLabel gameMessageLabel, dealerScoreLabel, playerScoreLabel, dealerBalanceLabel, balanceLabel, dealerBetLabel, betLabel, specialMessageLabel, enterBetLabel;
    private JTextField betField;
    private GameManager gameManager;
    private ArrayList<JPanel> playerPanels = new ArrayList<>();
    private ArrayList<JLabel> playerScoreLabels = new ArrayList<>();
    private ArrayList<JLabel> playerBalanceLabels = new ArrayList<>();
    private ArrayList<JLabel> playerBetLabels = new ArrayList<>();

    public BlackjackGUI(GameManager gameManager) {
        this.gameManager = gameManager;
        setTitle("Blackjack Game");
        setSize(gameWidth, gameHeight);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Initialize panels and components
        initializeComponents();

        setVisible(true);
    }

    private void initializeComponents() {
        // Initialize buttons
        hitButton = createStyledButton("Hit");
        standButton = createStyledButton("Stand");
        newGameButton = createStyledButton("New Game");
        placeBetButton = createStyledButton("Place Bet");

        // Game message label
        gameMessageLabel = new JLabel("Welcome to Blackjack!", SwingConstants.CENTER);
        gameMessageLabel.setFont(new Font("Arial", Font.BOLD, 24));
        gameMessageLabel.setForeground(Color.WHITE);

        // Layout setup
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(34, 139, 34));

        // Create the panels for dealer and players
        dealerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        dealerPanel.setOpaque(false);

        playersPanel = new JPanel(new GridLayout(0, 1));
        playersPanel.setOpaque(false);

        // Initialize labels for dealer and players
        dealerScoreLabel = createStyledLabel("Dealer Score: 0");
        dealerBalanceLabel = createStyledLabel("Dealer Balance: $1000");
        dealerBetLabel = createStyledLabel("Dealer Bet: $0");
        
        // Player score and balance panels
        playerScoreLabel = createStyledLabel("Player Score: 0");
        balanceLabel = createStyledLabel("Balance: $1000");
        betLabel = createStyledLabel("Bet: $0");
        
        // Bet panel components
        betField = new JTextField(10);
        enterBetLabel = new JLabel("Enter Bet");
        enterBetLabel.setFont(new Font("Arial", Font.BOLD, 22));
        enterBetLabel.setForeground(Color.WHITE);

        // Create bet panel
        betPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        betPanel.setOpaque(false);
        betPanel.add(enterBetLabel);
        betPanel.add(betField);
        betPanel.add(placeBetButton);

        // Button panel
        buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setOpaque(false);
        buttonPanel.add(hitButton);
        buttonPanel.add(standButton);
        buttonPanel.add(newGameButton);

        // Add all components to mainPanel
        mainPanel.add(gameMessageLabel, BorderLayout.NORTH);
        mainPanel.add(dealerPanel, BorderLayout.CENTER);
        mainPanel.add(playersPanel, BorderLayout.SOUTH);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        mainPanel.add(betPanel, BorderLayout.SOUTH);

        add(mainPanel);
        
        // Attach event listeners
        attachEventListeners();
    }

   
    private void attachEventListeners() {
        hitButton.addActionListener(e -> gameManager.handlePlayerHit());
        standButton.addActionListener(e -> gameManager.handlePlayerStand());
        newGameButton.addActionListener(e -> gameManager.startNewGame());
        placeBetButton.addActionListener(e -> placeBet(gameManager.getCurrentPlayer()));
    }

    private void placeBet(Player player) {
        try {
            int betAmount = Integer.parseInt(betField.getText());
            if (betAmount > 0 && player.placeBet(betAmount)) {
                betLabel.setText("Bet: $" + betAmount);
                balanceLabel.setText("Balance: $" + player.getBalance());
                betField.setEnabled(false);
                placeBetButton.setEnabled(false);
                JOptionPane.showMessageDialog(this, "Bet Confirmed: $" + betAmount, "Bet", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Invalid Bet", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid input. Please enter a valid bet amount.", "Error", JOptionPane.WARNING_MESSAGE);
        }
    }

    public void setPlayers(ArrayList<Player> players) {
        playersPanel.removeAll();
        for (Player player : players) {
            JPanel panel = new JPanel(new BorderLayout());
            JLabel scoreLabel = new JLabel(player.getName() + ": " + "Score: 0");
            JLabel balanceLabel = new JLabel("Balance: $" + player.getBalance());
            JLabel betLabel = new JLabel("Bet: $0");
            panel.add(scoreLabel, BorderLayout.NORTH);
            panel.add(balanceLabel, BorderLayout.CENTER);
            panel.add(betLabel, BorderLayout.SOUTH);
            playersPanel.add(panel);
        }
        playersPanel.revalidate();
        playersPanel.repaint();
    }

    public void restartGame() {
        gameManager.startNewGame();
        betField.setEnabled(true);
        placeBetButton.setEnabled(true);
    }

    public void showGameOverMessage(String message) {
        int option = JOptionPane.showOptionDialog(this, message, "Game Over", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE, null, new String[]{"Restart Game", "Exit"}, "Restart Game");
        if (option == JOptionPane.YES_OPTION) {
            restartGame();
        } else {
            System.exit(0);
        }
    }


    // After the game ended the user should be able to take another bet
    public void enableBetting() {
        System.out.println("enableBetting called");
        System.out.println("Player Balance: " + gameManager.getPlayerBalance(null));
        System.out.println("Dealer Balance: " + gameManager.getDealerBalance());
        System.out.println("Dealer Bet: " + gameManager.getDealerBet());
    
        betField.setEnabled(true);
        placeBetButton.setEnabled(true);
        betField.setText(""); // Clear the bet field
        betLabel.setText(Texts.bet[language] + " $0");
        balanceLabel.setText(Texts.balance[language] + " $" + gameManager.getPlayerBalance(null));
        dealerBalanceLabel.setText(Texts.balance[language] + " $" + gameManager.getDealerBalance());
        dealerBetLabel.setText(Texts.bet[language] + " $" + gameManager.getDealerBet());
    
        System.out.println("UI Updated - Player Balance: " + gameManager.getPlayerBalance(null) + 
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

    System.out.println("UI Updated - Player Balance: " + gameManager.getPlayerBalance(null) + 
                       ", Dealer Balance: " + gameManager.getDealerBalance() + 
                       ", Dealer Bet: " + gameManager.getDealerBet());
    }

    public void updateGameState(ArrayList<Player> players, Player dealer, boolean gameOver) {
        dealerPanel.removeAll();
        dealerScoreLabel.setText(Texts.guiDealerScore[language] + " ???");
    
        // Update player panels
        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);
            playerPanels.get(i).removeAll();
    
            // Ensure the player's hand is not empty before adding cards
            if (!player.getHand().isEmpty()) {
                for (Card card : player.getHand()) {
                    playerPanels.get(i).add(createCardPanel(card));
                }
            } 
    
            playerScoreLabels.get(i).setText(player.getName() + ": " + Texts.guiPlayerScore[language] + " : " + player.calculateScore());
            playerBalanceLabels.get(i).setText(Texts.balance[language] + " $" + player.getBalance());
            playerBetLabels.get(i).setText(Texts.bet[language] + " $" +  player.getCurrentBet());
        }
    
        // Update dealer panel
        if (gameOver) {
            // Ensure the dealer's hand is not empty before adding cards
            if (!dealer.getHand().isEmpty()) {
                for (Card card : dealer.getHand()) {
                    dealerPanel.add(createCardPanel(card));
                }
                dealerScoreLabel.setText(Texts.guiDealerScore[language] + " : " + dealer.calculateScore());
            }   
         else {
            // Dealer has one card face-up and one hidden card
            if (!dealer.getHand().isEmpty()) {
                dealerPanel.add(createCardPanel(dealer.getHand().get(0)));
                dealerPanel.add(createHiddenCardPanel());
            }
        }
    
        // Revalidate and repaint panels to update the UI
        playersPanel.revalidate();
        playersPanel.repaint();
        dealerPanel.revalidate();
        dealerPanel.repaint();
    }
}

    private void nextTurn() {
        if (gameManager.hasNextPlayer()) {
            gameManager.hasNextPlayer();
            updateGameMessage(gameManager.getCurrentPlayer().getName() + "'s turn");
        } else {
            gameManager.dealerTurn();
        }
    }
    
    
    private JPanel createHiddenCardPanel() {
        JPanel cardPanel = new JPanel();
        cardPanel.setPreferredSize(new Dimension(cardWidth, cardHeight));
        cardPanel.setBackground(Color.BLACK);
        cardPanel.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));

        JLabel hiddenLabel = new JLabel("?", SwingConstants.CENTER);
        hiddenLabel.setFont(new Font("Arial", Font.BOLD, cardFontSize));
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
        button.setPreferredSize(new Dimension(buttonWidth, buttonHeight));
        button.setFont(new Font("Arial", Font.BOLD, buttonFontSize));
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
        cardPanel.setPreferredSize(new Dimension(cardWidth, cardHeight));
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

    public void promptPlayerAction(Player player) {
        // Display a prompt for the player to choose an action
        String[] options = {Texts.guiHit[language], Texts.guiStand[language], Texts.guiNewGame[language]}; // Include Double Down option if applicable
    
        int choice = JOptionPane.showOptionDialog(
            this,                        // Parent component
            "Please choose an option:",   // Message to display
            "Choose Option",              // Title of the dialog
            JOptionPane.DEFAULT_OPTION,    // Option type
            JOptionPane.INFORMATION_MESSAGE, // Message type
            null,                         // Icon (use default)
            options,                      // The array of options
            options[0]                    // Default selected option
        );
    
        System.out.println("Selected choice: " + choice);
    
        // Handle the player's choice
        if (choice == JOptionPane.CLOSED_OPTION) {
            // If the player closes the dialog, exit the method
            return;
        }
    
        switch (choice) {
            case 0: // Hit
                gameManager.handlePlayerHit();
                break;
            case 1: // Stand
                gameManager.handlePlayerStand();
                break;
            case 2: // New Game
                // Add the logic for starting a new game (if needed)
                gameManager.startNewGame();
                break;
            default: // If no valid option is chosen, nothing happens
                break;
        }
    
        // After the player's action, check if the round should continue to the next player or the dealer
        if (!gameManager.isCurrentPlayerStillInRound()) {
            nextTurn();
        }
    }
    
    
}
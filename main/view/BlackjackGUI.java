package main.view;


import main.controller.AudioManager;
import main.controller.BettingManager;
import main.controller.GameManager;
import main.model.Card;
import main.model.Player;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

import java.awt.*;
import java.util.ArrayList;

import static main.view.BlackJackMenu.language;

public class BlackjackGUI extends JFrame {
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private int gameHeight = (int) screenSize.getHeight();
    private int gameWidth = (int)screenSize.getWidth();
    private int buttonWidth = (int) (gameWidth * 0.15);
    private int buttonHeight = (int) (gameHeight * 0.08);
    private int buttonFontSize = gameWidth / 60;
    private int cardWidth = (int) (gameWidth * 0.10);
    private int cardHeight = (int) (gameHeight * 0.22);
    private int cardFontSize = gameWidth / 60;
    private JPanel mainPanel, dealerPanel, buttonPanel, betPanel;
    private JButton hitButton, standButton, newGameButton, placeBetButton;
    private JLabel gameMessageLabel, dealerScoreLabel, dealerBalanceLabel, balanceLabel, dealerBetLabel, betLabel, specialMessageLabel, enterBetLabel;
    private JTextField betField;
    private GameManager gameManager;
    private PlayersPanel playersPanel;
    private JButton pauseButton;
    private JPopupMenu pauseMenu;

    private int buttonHeight, buttonWidth, buttonFontSize, cardHeight, cardWidth, cardFontSize;

    public BlackjackGUI(GameManager gameManager) {
        this.gameManager = gameManager;
        setTitle("Blackjack Game");
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        //setSize(gameWidth, gameHeight);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        
        gameManager.setGui(this);

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

        playersPanel = new PlayersPanel(); 

        // Initialize labels for dealer and players
        dealerScoreLabel = createStyledLabel("Dealer Score: 0");
        dealerBalanceLabel = createStyledLabel("Dealer Balance: $1000");
        dealerBetLabel = createStyledLabel("Dealer Bet: $0");
        
        // Player score and balance panels
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
        betPanel.setBackground(new Color(34, 139, 34));
        betPanel.add(enterBetLabel);
        betPanel.add(betField);
        betPanel.add(placeBetButton);


        // Button panel
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;
    
        buttonWidth = (int) (screenWidth * 0.15);
        buttonHeight = (int) (screenHeight * 0.08);
        buttonFontSize = screenWidth / 60;
    
        cardWidth = (int) (screenWidth * 0.10);
        cardHeight = (int) (screenHeight * 0.22);
        cardFontSize = screenWidth / 60;
    
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
    
        // Use GridLayout for dealerScorePanel
        dealerScorePanel = new JPanel(new GridLayout(2, 1));
        dealerScorePanel.setOpaque(false);
    
        // First row: Dealer score
        JPanel scoreRow = new JPanel(new FlowLayout(FlowLayout.CENTER));
        scoreRow.setOpaque(false);
        scoreRow.add(dealerScoreLabel);
    
        // Second row: Dealer balance and bet
        JPanel balanceBetRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        balanceBetRow.setOpaque(false);
        balanceBetRow.add(dealerBalanceLabel);
        balanceBetRow.add(dealerBetLabel);
    
        // Create pause button
        pauseButton = new JButton("☰");
        pauseButton.setFont(new Font("Arial", Font.BOLD, 36));
        pauseButton.setForeground(Color.WHITE);
        pauseButton.setBackground(new Color(255, 165, 0));
        pauseButton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        pauseButton.setContentAreaFilled(false);
        pauseButton.setOpaque(true);
        pauseButton.setFocusPainted(false);
    
        // Hover effects
        pauseButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                pauseButton.setBackground(new Color(255, 140, 0));
            }
    
            @Override
            public void mouseExited(MouseEvent e) {
                pauseButton.setBackground(new Color(255, 165, 0));
            }
        });
    
        // Create pause menu with volume at bottom
        pauseMenu = new JPopupMenu();
        pauseMenu.setBackground(new Color(50, 50, 50));
        pauseMenu.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100), 2));
    
        // Menu items
        JMenuItem resumeItem = new JMenuItem(Texts.RESUME[language]);
        JMenuItem mainMenuItem = new JMenuItem(Texts.guiBackToMain[language]);
        JMenuItem exitItem = new JMenuItem(Texts.exitGame[language]);
    
        // Style menu items
        Font menuFont = new Font("Arial", Font.BOLD, 18);
        resumeItem.setFont(menuFont);
        mainMenuItem.setFont(menuFont);
        exitItem.setFont(menuFont);
    
        // Volume control panel (will be added last)
        JPanel volumePanel = new JPanel(new BorderLayout(5, 5));
        volumePanel.setBackground(new Color(50, 50, 50));
        volumePanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
    
        JLabel volumeLabel = new JLabel(Texts.VOLUME[language]);
        volumeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        volumeLabel.setForeground(Color.WHITE);
    
        JSlider volumeSlider = new JSlider(0, 100, 50);
        volumeSlider.setMajorTickSpacing(25);
        volumeSlider.setMinorTickSpacing(5);
        volumeSlider.setPaintTicks(true);
        volumeSlider.setPaintLabels(true);
        volumeSlider.setForeground(Color.WHITE);
        volumeSlider.setBackground(new Color(50, 50, 50));
    
        volumeSlider.addChangeListener(e -> {
            int volume = volumeSlider.getValue();
            float volumeValue = volume / 100f;
            AudioManager.getInstance().setVolume(volumeValue);
        });
    
        volumePanel.add(volumeLabel, BorderLayout.NORTH);
        volumePanel.add(volumeSlider, BorderLayout.CENTER);
    
        // Add menu items (volume will be added last)
        pauseMenu.add(resumeItem);
        pauseMenu.addSeparator();
        pauseMenu.add(mainMenuItem);
        pauseMenu.addSeparator();
        pauseMenu.add(exitItem);
        pauseMenu.addSeparator(); // Separator before volume control
        pauseMenu.add(volumePanel); // Volume at bottom
    
        // Add rows to dealerScorePanel
        dealerScorePanel.add(scoreRow);
        dealerScorePanel.add(balanceBetRow);
    
        playerScorePanel = new JPanel();
        playerScorePanel.setOpaque(false);
        playerScorePanel.add(playerScoreLabel);
    
        buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setOpaque(false);
        buttonPanel.setBackground(new Color(34, 139, 34));

        buttonPanel.add(hitButton);
        buttonPanel.add(standButton);
        buttonPanel.add(newGameButton);



        // Add all components to mainPanel
        mainPanel.add(gameMessageLabel, BorderLayout.NORTH);
        mainPanel.add(dealerPanel, BorderLayout.CENTER);
    
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
    }

        JPanel southContainer = new JPanel(new BorderLayout());
        southContainer.setBackground(new Color(34, 139, 34)); // ADD THIS LINE
        southContainer.add(playersPanel, BorderLayout.NORTH);
        southContainer.add(buttonPanel, BorderLayout.CENTER);
        southContainer.add(betPanel, BorderLayout.SOUTH);
        mainPanel.add(southContainer, BorderLayout.SOUTH);
        


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
    private void layoutComponents() {
        // Declare topPanel first (keep this one)
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        
        // Rest of your layout code...
        mainPanel.setLayout(new BorderLayout());
    
        // Dealer Section (Top)
        JPanel dealerArea = new JPanel(new BorderLayout());
        dealerArea.setOpaque(false);
        dealerArea.add(dealerScorePanel, BorderLayout.NORTH);
        dealerArea.add(dealerPanel, BorderLayout.CENTER);
    
        
        // Add pause button to the top-right corner
        JPanel topRightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topRightPanel.setOpaque(false);
        topRightPanel.add(pauseButton);
        
        // Now you can add to topPanel
        topPanel.add(dealerArea, BorderLayout.CENTER);
        topPanel.add(topRightPanel, BorderLayout.EAST);
    
        // Remove this duplicate declaration:
        // JPanel topPanel = new JPanel(new BorderLayout());
        // topPanel.setOpaque(false);
        // topPanel.add(backButtonPanel, BorderLayout.WEST);
        // topPanel.add(dealerArea, BorderLayout.CENTER);
    
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
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(southPanel, BorderLayout.SOUTH);
    
        add(mainPanel);
    
        gameManager.startNewGame();
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

    private void showPauseMenu() {
        gameManager.pauseGame();
        setGameButtonsEnabled(false);
        
        pauseMenu.setFocusable(true);
        pauseMenu.addPopupMenuListener(new PopupMenuListener() {
            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {}
            
            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                // Only resume if not selecting an option
                if (!e.getSource().equals(pauseMenu)) {
                    resumeGame();
                }
            }
            
            @Override
            public void popupMenuCanceled(PopupMenuEvent e) {
                popupMenuWillBecomeInvisible(e);
            }
        });
        
        pauseMenu.show(pauseButton, 0, pauseButton.getHeight());
    }
    
    private void resumeGame() {
        gameManager.resumeGame();
        setGameButtonsEnabled(true);
    }
    
    private void returnToMainMenu() {
        new BlackJackMenu().setVisible(true);
        dispose();
    }
    
    private void setGameButtonsEnabled(boolean enabled) {
        // Disable all game buttons when paused
        boolean buttonsEnabled = enabled && !gameManager.isPaused();
        
        hitButton.setEnabled(buttonsEnabled && !gameManager.isGameOver());
        standButton.setEnabled(buttonsEnabled && !gameManager.isGameOver());
        newGameButton.setEnabled(buttonsEnabled); // Disable when paused
        pauseButton.setEnabled(true); // Always enabled
        
        // Enable betting only when game is over or hasn't started AND not paused
        boolean bettingEnabled = (gameManager.isGameOver() || 
                               (gameManager.getPlayerBalance() > 0 && 
                                gameManager.getDealerBalance() > 0)) && 
                                !gameManager.isPaused();
        
        betField.setEnabled(bettingEnabled);
        placeBetButton.setEnabled(bettingEnabled);
    }
    

    private void attachEventListeners() {
        hitButton.addActionListener(e -> gameManager.handlePlayerHit());
        standButton.addActionListener(e -> gameManager.handlePlayerStand());
        newGameButton.addActionListener(e -> gameManager.startNewGame());
        placeBetButton.addActionListener(e -> placeBet());
        
        // Pause button listener
        pauseButton.addActionListener(e -> showPauseMenu());
        
        // Pause menu listeners
        for (Component component : pauseMenu.getComponents()) {
            if (component instanceof JMenuItem) {
                JMenuItem menuItem = (JMenuItem) component;
                if (menuItem.getText().equals(Texts.RESUME[language])) {
                    menuItem.addActionListener(e -> {
                        resumeGame();
                        pauseMenu.setVisible(false);
                    });
                } else if (menuItem.getText().equals(Texts.guiBackToMain[language])) {
                    menuItem.addActionListener(e -> {
                        returnToMainMenu();
                        pauseMenu.setVisible(false);
                    });
                } else if (menuItem.getText().equals(Texts.exitGame[language])) {
                    menuItem.addActionListener(e -> {
                        pauseMenu.setVisible(false);
                        System.exit(0);
                    });
                }
            }
        }
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
    }

    public void updateGameState(ArrayList<Player> players, Player dealer, boolean gameOver) {
        dealerPanel.removeAll();
        dealerScoreLabel.setText(Texts.guiDealerScore[language] + " ???");
    
        playersPanel.updatePanel(players); 
    
        // Update dealer panel
        if (gameOver) {
            // Show all dealer's cards
            for (Card card : dealer.getHand()) {
                dealerPanel.add(createCardPanel(card));
            }
            dealerScoreLabel.setText("Dealer Score: " + dealer.calculateScore());
        } else {
            // Show one card face-up and a hidden card
            if (!dealer.getHand().isEmpty()) {
                dealerPanel.add(createCardPanel(dealer.getHand().get(0)));
                dealerPanel.add(createHiddenCardPanel());
            }
        }
    
        // Revalidate and repaint panels to update the UI
        playersPanel.revalidate();
        playersPanel.repaint();
            dealerPanel.add(createCardPanel(dealer.getHand().get(0)));
            dealerPanel.add(createHiddenCardPanel(isPaused));
            dealerScoreLabel.setText(Texts.guiDealerScore[language] + " ???");
        }
    
        // Rest of your method remains the same...
        playerScoreLabel.setText(Texts.guiPlayerScore[language] + " : " + player.calculateScore());
        balanceLabel.setText(Texts.balance[language] + " $" + gameManager.getPlayerBalance());
        dealerBalanceLabel.setText(Texts.dealerBalance[language] + " $" + gameManager.getDealerBalance());
        dealerBetLabel.setText(Texts.dealerBet[language] + " $" + gameManager.getDealerBet());
    
        playerPanel.revalidate();
        playerPanel.repaint();

    }
    private void nextTurn() {

        if (gameManager.hasNextPlayer()) {
            gameManager.startNextPlayerTurn();
            updateGameMessage(gameManager.getCurrentPlayer().getName() + "'s turn");
        } else {
            gameManager.dealerTurn();
        }
    }    
    
    private JPanel createHiddenCardPanel(boolean isPaused) {
        JPanel cardPanel = new JPanel();
        cardPanel.setPreferredSize(new Dimension(cardWidth, cardHeight));
        cardPanel.setBackground(Color.BLACK);
        cardPanel.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
    
        JLabel hiddenLabel = new JLabel(isPaused ? "⏸" : "?", SwingConstants.CENTER);
        hiddenLabel.setFont(new Font("Arial", Font.BOLD, cardFontSize));
        hiddenLabel.setForeground(Color.WHITE);
    
        cardPanel.setLayout(new BorderLayout());
        cardPanel.add(hiddenLabel, BorderLayout.CENTER);
    
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
/*
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
 */

 
    public void promptPlayerAction(Player player) {
        // Display a prompt for the player to choose an action
        //String[] options = {Texts.guiHit[language], Texts.guiStand[language], Texts.guiNewGame[language]}; // Include Double Down option if applicable
    
        /*int choice = JOptionPane.showOptionDialog(
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
                */
        
    
        // After the player's action, check if the round should continue to the next player or the dealer
        if (!gameManager.isCurrentPlayerStillInRound()) {
            nextTurn();
        }
    }
    
    
}
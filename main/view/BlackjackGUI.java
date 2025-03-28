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
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

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
    private JButton pauseButton;
    private JPopupMenu pauseMenu;
    private BufferedImage backgroundImage;
    private boolean backgroundLoaded = false;

    private int buttonHeight, buttonWidth, buttonFontSize, cardHeight, cardWidth, cardFontSize;

    public BlackjackGUI(GameManager gameManager) {
        this.gameManager = gameManager;
        gameManager.setGui(this);

        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        
        try {
            InputStream is = getClass().getResourceAsStream("/img/background.jpg");
            if (is != null) {
                backgroundImage = ImageIO.read(is);
                backgroundLoaded = true;
                is.close();
            }
        } catch (IOException e) {
            System.err.println("Error loading background image: " + e.getMessage());
            backgroundLoaded = false;
        }
    
        setTitle(Texts.guiTitle[language]);
        // Custom image icon
        ImageIcon icon = new ImageIcon("img/black.png");
        setIconImage(icon.getImage());

        initializeComponents();
        layoutComponents();
        attachEventListeners();

        specialMessageLabel.setText("...");
    }

    private void initializeComponents() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;
    
        buttonWidth = (int) (screenWidth * 0.15);
        buttonHeight = (int) (screenHeight * 0.08);
        buttonFontSize = screenWidth / 60;
    
        cardWidth = (int) (screenWidth * 0.10);
        cardHeight = (int) (screenHeight * 0.22);
        cardFontSize = screenWidth / 60;
    
        mainPanel = new BackgroundPanel(); // Uses your custom background-drawing panel
        mainPanel.setLayout(new BorderLayout()); // Still need BorderLayout for component placement
        ((BackgroundPanel)mainPanel).setOpaque(false); // Add this line
    
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
    }


    private void layoutComponents() {
        // Declare topPanel first (keep this one)
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
            
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

    public void updateGameState(Player player, Player dealer, boolean gameOver, boolean isPaused) {
        playerPanel.removeAll();
        dealerPanel.removeAll();
    
        // Show player's cards
        for (Card card : player.getHand()) {
            playerPanel.add(createCardPanel(card));
        }
    
        // Show dealer's cards - only show first card unless game is over or paused
         if (gameOver) {  // Changed this condition
            for (Card card : dealer.getHand()) {
                dealerPanel.add(createCardPanel(card));
            }
            dealerScoreLabel.setText(Texts.guiDealerScore[language] + " : " + dealer.calculateScore());
        } else {
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
        dealerPanel.revalidate();
        dealerPanel.repaint(); 
    }

    private JPanel createHiddenCardPanel(boolean isPaused) {
        // Create a panel with custom painting
        JPanel cardPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                // Create semi-transparent dark background
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Draw card background with subtle pattern
                g2d.setColor(new Color(20, 20, 20, 220));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                
                // Add decorative border
                g2d.setColor(new Color(100, 100, 100, 150));
                g2d.setStroke(new BasicStroke(3));
                g2d.drawRoundRect(5, 5, getWidth()-10, getHeight()-10, 15, 15);
                
                super.paintComponent(g);
            }
        };
    
        // Card styling
        cardPanel.setPreferredSize(new Dimension(cardWidth, cardHeight));
        cardPanel.setOpaque(false);
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(150, 150, 150), 2),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
    
        // Create center label with icon or pause symbol
        JLabel hiddenLabel = new JLabel(isPaused ? "⏸" : "?", SwingConstants.CENTER);
        hiddenLabel.setFont(new Font("Arial", Font.BOLD, cardFontSize * 2));
        hiddenLabel.setForeground(new Color(200, 200, 200, 200));
        
        // Try to load card back image, fallback to pattern if missing
        try {
            InputStream is = getClass().getResourceAsStream("/images/card-back.png");
            if (is != null) {
                BufferedImage backImage = ImageIO.read(is);
                Image scaledImage = backImage.getScaledInstance(cardWidth-10, cardHeight-10, Image.SCALE_SMOOTH);
                hiddenLabel.setIcon(new ImageIcon(scaledImage));
                is.close();
            } else {
                // Fallback pattern
                hiddenLabel.setText(isPaused ? "⏸ PAUSED" : "•••");
            }
        } catch (IOException e) {
            hiddenLabel.setText(isPaused ? "⏸" : "?");
        }
    
        // Layout
        cardPanel.setLayout(new BorderLayout());
        cardPanel.add(hiddenLabel, BorderLayout.CENTER);
        
        // Add hover effect
        cardPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                cardPanel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(200, 200, 200), 2),
                    BorderFactory.createEmptyBorder(5, 5, 5, 5)
                ));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                cardPanel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(150, 150, 150), 2),
                    BorderFactory.createEmptyBorder(5, 5, 5, 5)
                ));
            }
        });
    
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
        // Create a transparent panel with custom painting
        JPanel cardPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                // Create semi-transparent white background for the card
                g.setColor(new Color(255, 255, 255, 220)); // 220 = alpha (transparency)
                g.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20); // Rounded corners
                
                // Add subtle shadow effect
                g.setColor(new Color(0, 0, 0, 30));
                g.drawRoundRect(1, 1, getWidth()-2, getHeight()-2, 20, 20);
                
                super.paintComponent(g);
            }
        };
        
        // Card styling
        cardPanel.setPreferredSize(new Dimension(cardWidth, cardHeight));
        cardPanel.setOpaque(false); // Make panel transparent
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(70, 70, 70), 2),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        
        // Rank label (top)
        JLabel rankLabel = new JLabel(card.getRank(), SwingConstants.CENTER);
        rankLabel.setFont(new Font("Arial", Font.BOLD, cardFontSize));
        rankLabel.setForeground(getCardColor(card.getSuit())); // Color based on suit
        
        // Suit label (center)
        JLabel suitLabel = new JLabel(getSuitSymbol(card.getSuit()), SwingConstants.CENTER);
        suitLabel.setFont(new Font("Arial", Font.PLAIN, cardFontSize * 2));
        suitLabel.setForeground(getCardColor(card.getSuit()));
        
        // Layout
        cardPanel.setLayout(new BorderLayout(5, 5));
        cardPanel.add(rankLabel, BorderLayout.NORTH);
        cardPanel.add(suitLabel, BorderLayout.CENTER);
        
        return cardPanel;
    }
    
    // Helper method to get suit color
    private Color getCardColor(String suit) {
        return switch (suit.toLowerCase()) {
            case "hearts", "diamonds" -> Color.RED;
            case "clubs", "spades" -> Color.BLACK;
            default -> new Color(50, 50, 50); // Default dark gray
        };
    }
    
    // Helper method to get Unicode suit symbols
    private String getSuitSymbol(String suit) {
        return switch (suit.toLowerCase()) {
            case "hearts" -> "♥";
            case "diamonds" -> "♦";
            case "clubs" -> "♣";
            case "spades" -> "♠";
            default -> suit; // Fallback
        };
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
    
    private class BackgroundPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (backgroundLoaded) {
                // Scale image to fit panel while maintaining aspect ratio
                int width = getWidth();
                int height = getHeight();
                g.drawImage(backgroundImage, 0, 0, width, height, this);
            } else {
                g.setColor(new Color(34, 139, 34));
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        }
    }
    
}


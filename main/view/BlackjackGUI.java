package main.view;

import main.controller.GameManager;
import main.model.Card;
import main.model.Player;
import main.controller.AudioManager;
import main.controller.BettingManager;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;


import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
 import javax.swing.event.PopupMenuListener;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

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
    private JPanel mainPanel, dealerPanel, dealerScorePanel, buttonPanel, betPanel;
    private JButton hitButton, standButton, newGameButton, placeBetButton;
    private JLabel gameMessageLabel, dealerScoreLabel, dealerBalanceLabel, balanceLabel, dealerBetLabel, betLabel, specialMessageLabel, enterBetLabel;
    private JTextField betField;
    private GameManager gameManager;
    private BettingManager bettingManager;
    private JButton pauseButton;
    private JPopupMenu pauseMenu;
    private PlayersPanel playersPanel;
    private BufferedImage backgroundImage;
    private boolean backgroundLoaded = false;

    private final HashMap<Player, JLabel> playerBalanceLabels = new HashMap<>();
    private final HashMap<Player, JLabel> playerBetLabels = new HashMap<>();


    public BlackjackGUI(GameManager gameManager) {
        this.gameManager = gameManager;
        this.bettingManager = gameManager.getBettingManager();
        setTitle(Texts.guiTitle[language]);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());


        try {
            backgroundImage = ImageIO.read(getClass().getResource("/img/backgroundimage.png"));
            backgroundLoaded = true;
        } catch (IOException e) {
            System.err.println("Error loading background image: " + e.getMessage());
            backgroundLoaded = false;
        }
        
        gameManager.setGui(this);
    
        ImageIcon icon = new ImageIcon("img/black.png");
        setIconImage(icon.getImage());
    
        // Correct initialization sequence
        initializeComponents();  // Create ALL components first
        layoutComponents();      // THEN arrange components
        attachEventListeners();  // FINALLY setup interactions
        
        setVisible(true);        // Make visible LAST
        AudioManager.getInstance().playBackgroundMusic();
    }

    private void layoutComponents() {
        mainPanel = new BackgroundPanel();
        mainPanel.setLayout(new BorderLayout());

        // Top section (dealer area + pause button)
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);

        JPanel dealerArea = new JPanel(new BorderLayout());
        dealerArea.setOpaque(false);
        dealerArea.add(dealerScorePanel, BorderLayout.NORTH);
        dealerArea.add(dealerPanel, BorderLayout.CENTER);

        JPanel topRightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topRightPanel.setOpaque(false);
        topRightPanel.add(pauseButton);

        topPanel.add(dealerArea, BorderLayout.CENTER);
        topPanel.add(topRightPanel, BorderLayout.EAST);

        // Center section (messages + buttons)
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setOpaque(false);
        centerPanel.add(gameMessageLabel, BorderLayout.NORTH);
        centerPanel.add(specialMessageLabel, BorderLayout.SOUTH);
        centerPanel.add(buttonPanel, BorderLayout.CENTER);

        // Bottom section (players + bet panel)
        JPanel southContainer = new JPanel(new BorderLayout());
        southContainer.setPreferredSize(new Dimension(gameWidth, 300)); // Limit height
        southContainer.setBackground(new Color(34, 139, 34));
        southContainer.setOpaque(false);


        JPanel playersContainer = new JPanel(new BorderLayout());
        playersContainer.setPreferredSize(new Dimension(
            Toolkit.getDefaultToolkit().getScreenSize().width,
            300
        ));
        playersContainer.setOpaque(false);
        playersContainer.add(playersPanel, BorderLayout.CENTER);
        betPanel.setOpaque(false);

        southContainer.add(playersContainer, BorderLayout.CENTER);
        southContainer.add(betPanel, BorderLayout.SOUTH);

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(southContainer, BorderLayout.SOUTH);

        add(mainPanel);
        gameManager.startNewGame();
    }

    private void initializeComponents() {
        // Initialize CORE containers first
        mainPanel = new BackgroundPanel();
        mainPanel.setLayout(new BorderLayout());

        playersPanel = new PlayersPanel();
        dealerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        betPanel = new JPanel();
        buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        betLabel = createStyledLabel(Texts.bet[language] + " $0");
        balanceLabel = createStyledLabel(Texts.balance[language] + " $1000");
        

        // Set properties for main components
        mainPanel.setBackground(new Color(34, 139, 34));
        playersPanel.setBackground(new Color(34, 139, 34));
        dealerPanel.setBackground(new Color(34, 139, 34));
        betPanel.setBackground(new Color(34, 139, 34));
        buttonPanel.setBackground(new Color(34, 139, 34));

        playersPanel.setOpaque(false);
        dealerPanel.setOpaque(false);
        buttonPanel.setOpaque(false);
    
        // Get screen dimensions
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;
    
        // Calculate sizes
        buttonWidth = (int) (screenWidth * 0.15);
        buttonHeight = (int) (screenHeight * 0.08);
        buttonFontSize = screenWidth / 60;
        cardWidth = (int) (screenWidth * 0.09);
        cardHeight = (int) (screenHeight * 0.19);
        cardFontSize = screenWidth / 60;
    
        // Initialize buttons
        hitButton = createStyledButton(Texts.guiHit[language]);
        standButton = createStyledButton(Texts.guiStand[language]);
        newGameButton = createStyledButton(Texts.guiNewGame[language]);
        placeBetButton = createStyledButton(Texts.placeBet[language]);
    
        // Initialize labels
        gameMessageLabel = new JLabel(Texts.welcomeMessage[language], SwingConstants.CENTER);
        gameMessageLabel.setFont(new Font("Arial", Font.BOLD, 26));
        gameMessageLabel.setForeground(Color.WHITE);
    
        specialMessageLabel = new JLabel("", SwingConstants.CENTER);
        specialMessageLabel.setFont(new Font("Arial", Font.BOLD, 32));
        specialMessageLabel.setForeground(Color.WHITE);
    
        // Dealer score panel
        dealerScorePanel = new JPanel(new GridLayout(2, 1));
        dealerScorePanel.setOpaque(false);
        dealerScorePanel.setBackground(new Color(34, 139, 34));

        
        // Dealer labels
        dealerScoreLabel = createStyledLabel(Texts.guiDealerScore[language]);
        dealerBalanceLabel = createStyledLabel(Texts.dealerBalance[language] + " $1000");
        dealerBetLabel = createStyledLabel(Texts.dealerBet[language] + " $0");
    
        // Assemble dealer score panel
        JPanel scoreRow = new JPanel(new FlowLayout(FlowLayout.CENTER));
        scoreRow.add(dealerScoreLabel);
        scoreRow.setBackground(new Color(34, 139, 34));

        JPanel balanceBetRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        balanceBetRow.add(dealerBalanceLabel);
        balanceBetRow.add(dealerBetLabel);
        balanceBetRow.setBackground(new Color(34, 139, 34));

        dealerScorePanel.add(scoreRow);
        dealerScorePanel.add(balanceBetRow);
    
        // Pause button setup
        pauseButton = new JButton();
        pauseButton.setPreferredSize(new Dimension(50, 50));
        ImageIcon pauseIcon = new ImageIcon("img/icons/pause.png"); // Ensure the file path is correct
        Image scaledPauseIcon = pauseIcon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH); // Scale the image
        pauseButton.setIcon(new ImageIcon(scaledPauseIcon)); // Set the scaled icon
        pauseButton.setBackground(new Color(255, 165, 0));
        pauseButton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        pauseButton.setContentAreaFilled(false);
        pauseButton.setOpaque(true);
        pauseButton.setFocusPainted(false);
    
        // Pause button hover effects
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
    
        // Pause menu setup
        pauseMenu = new JPopupMenu();
        pauseMenu.setBackground(new Color(50, 50, 50));
        pauseMenu.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100), 2));
    
        // Menu items
        JMenuItem resumeItem = new JMenuItem(Texts.RESUME[language]);
        JMenuItem mainMenuItem = new JMenuItem(Texts.guiBackToMain[language]);
        JMenuItem exitItem = new JMenuItem(Texts.exitGame[language]);
        
        Font menuFont = new Font("Arial", Font.BOLD, 18);
        resumeItem.setFont(menuFont);
        mainMenuItem.setFont(menuFont);
        exitItem.setFont(menuFont);
    
        // Volume control
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
            JSlider source = (JSlider) e.getSource();
            float volume = source.getValue() / 100f;
            AudioManager.getInstance().setVolume(volume);
        });
        
        volumePanel.add(volumeLabel, BorderLayout.NORTH);
        volumePanel.add(volumeSlider, BorderLayout.CENTER);
    
        // Assemble pause menu
        pauseMenu.add(resumeItem);
        pauseMenu.addSeparator();
        pauseMenu.add(mainMenuItem);
        pauseMenu.addSeparator();
        pauseMenu.add(exitItem);
        pauseMenu.addSeparator();
        pauseMenu.add(volumePanel);
    
        // Bet panel components
        betField = new JTextField(5);
        betField.setPreferredSize(new Dimension(350, 40));
        betField.setMaximumSize(new Dimension(300, 30));
        betField.setFont(new Font("Arial", Font.PLAIN, 24));
        enterBetLabel = new JLabel("Enter Bet");
        enterBetLabel.setFont(new Font("Arial", Font.BOLD, 28));
        enterBetLabel.setForeground(Color.WHITE);
        betField.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
    
        // Assemble bet panel
        betPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 10)); 
        //betPanel.add(Box.createHorizontalGlue());
        betPanel.add(enterBetLabel);
        betPanel.add(betField);
        betPanel.add(placeBetButton);
        betPanel.add(Box.createHorizontalGlue());
        betPanel.add(betLabel);
        betPanel.add(balanceLabel);
    
        // Assemble button panel
        buttonPanel.add(hitButton);
        buttonPanel.add(standButton);
        buttonPanel.add(newGameButton);
    }
   
    private void attachEventListeners() {
        hitButton.addActionListener(e -> gameManager.handlePlayerHit());
        standButton.addActionListener(e -> gameManager.handlePlayerStand());
        newGameButton.addActionListener(e -> gameManager.startNewGame());
        placeBetButton.addActionListener(e -> placeBet(gameManager.getCurrentPlayer()));
    
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

    public void updatePlayerBalanceAndBet(Player player) {
        JLabel balanceLabel = playerBalanceLabels.get(player);
        JLabel betLabel = playerBetLabels.get(player);
    
        if (balanceLabel != null) {
            balanceLabel.setText("Balance: $" + player.getBalance());
            balanceLabel.revalidate();
            balanceLabel.repaint();
        }
    
        if (betLabel != null) {
            betLabel.setText("Bet: $" + player.getCurrentBet());
            betLabel.revalidate();
            betLabel.repaint();
        }
    }
    
    

    private void placeBet(Player player) {
        try {
            int betAmount = Integer.parseInt(betField.getText());
            if (betAmount > 0 && player.placeBet(betAmount)) {
                gameManager.getBettingManager().getPlayerBalance(player.getName());
                setGameButtonsEnabled(true);
                betLabel.setText("Bet: $" + betAmount);
                balanceLabel.setText("Balance: $" + player.getBalance());
                betField.setEnabled(false);
                placeBetButton.setEnabled(false);
                JOptionPane.showMessageDialog(this, "Bet Confirmed: $" + betAmount, "Bet", JOptionPane.INFORMATION_MESSAGE);
                placeBetButton.setEnabled(false);
                dealerBalanceLabel.setText("Balance: $" + gameManager.getDealerBalance());
                dealerBetLabel.setText("Bet: $" + gameManager.getDealerBet());
                playersPanel.updatePanel(gameManager.getPlayers());            
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
            panel.setOpaque(false);
            JLabel scoreLabel = new JLabel(player.getName() + ": " + "Score: 0");
            JLabel balanceLabel = new JLabel("Balance: $" + player.getBalance());
            JLabel betLabel = new JLabel("Bet: $0");
            playerBalanceLabels.put(player, balanceLabel);
playerBetLabels.put(player, betLabel);
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
        setGameButtonsEnabled(true);
        gameManager.setGameOver(false);  // Add this line
        gameManager.resumeGame();

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

    public void setGameButtonsEnabled(boolean enabled) {
        // Disable all game buttons when paused
        boolean buttonsEnabled = enabled && !gameManager.isPaused();

        hitButton.setEnabled(buttonsEnabled && !gameManager.isGameOver());
        standButton.setEnabled(buttonsEnabled && !gameManager.isGameOver());
        newGameButton.setEnabled(buttonsEnabled); // Disable when paused
        pauseButton.setEnabled(true); // Always enabled
        betField.setEnabled(buttonsEnabled && !gameManager.isGameOver());
        placeBetButton.setEnabled(buttonsEnabled && !gameManager.isGameOver());

        pauseButton.setEnabled(true);
    }


    // After the game ended the user should be able to take another bet
    public void enableBetting() {
        betField.setEnabled(true);
        placeBetButton.setEnabled(true);
        betField.setText(""); // Clear the bet field
        betLabel.setText(Texts.bet[language] + " $0");
        balanceLabel.setText(Texts.balance[language] + " $" + gameManager.getPlayerBalance(gameManager.getCurrentPlayer()));
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

    public void updateGameState(ArrayList<Player> players, Player dealer, boolean gameOver, boolean isPaused) {
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
        dealerPanel.revalidate();
        dealerPanel.repaint();
    }


    private void nextTurn() {
        if (gameManager.hasNextPlayer()) {
            gameManager.startNextPlayerTurn();
            updateGameMessage(gameManager.getCurrentPlayer().getName() + "'s turn");
            enableBetting();
        } else {
            gameManager.dealerTurn();
        }
    }    
    
    private JPanel createHiddenCardPanel() {
        JPanel cardPanel = new JPanel();
        cardPanel.setPreferredSize(new Dimension(cardWidth, cardHeight));
        cardPanel.setBackground(Color.BLACK);
        cardPanel.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));

        JLabel hiddenLabel = new JLabel(gameManager.isPaused() ? "⏸" : "?", SwingConstants.CENTER);
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
        JPanel cardPanel = new JPanel(new BorderLayout());

        cardPanel.setPreferredSize(new Dimension(cardWidth, cardHeight));
        cardPanel.setMinimumSize(new Dimension(cardWidth, cardHeight));
        cardPanel.setMaximumSize(new Dimension(cardWidth, cardHeight));
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
        if (!gameManager.isCurrentPlayerStillInRound()) {
            if (gameManager.hasNextPlayer()) {
                nextTurn();  // Triggers betting for next player
            } else {
                gameManager.dealerTurn();  // Only run dealer turn if no players left
            }
        }
        else {
            setGameButtonsEnabled(true); // Force refresh
            updateGameMessage(player.getName() + "'s turn");
            enableBetting();
        }
    }

    

    public void updatePlayerPanels() {
        playersPanel.removeAll();
        for (Player player : gameManager.getPlayers()) {
            JPanel panel = new JPanel(new BorderLayout());
            JLabel scoreLabel = new JLabel(player.getName() + ": Score: " + player.calculateScore());
            JLabel balanceLabel = new JLabel("Balance: $" + player.getBalance());
            JLabel betLabel = new JLabel("Current Bet: $" + player.getCurrentBet()); // Directly use current bet
            
            panel.add(scoreLabel, BorderLayout.NORTH);
            panel.add(balanceLabel, BorderLayout.CENTER);
            panel.add(betLabel, BorderLayout.SOUTH);
            playersPanel.add(panel);
        }
        playersPanel.revalidate();
        playersPanel.repaint();
    }

    private class BackgroundPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (backgroundLoaded && backgroundImage != null) {
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            } else {
                g.setColor(new Color(34, 139, 34)); // fallback color
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        }
    }
    
    
}
package main.view;

import main.controller.GameManager;
import main.model.Card;
import main.model.Player;
import main.controller.AudioManager;
import main.controller.BettingManager;
import main.controller.AchievementManager;

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
    private JScrollPane scrollPane;
    private JPanel topRightPanel;
    private JButton undoButton;
    private JButton redoButton;


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
        initializeComponents();  // Create ALL components first
        layoutComponents();      // THEN arrange components
        attachEventListeners();  // FINALLY setup interactions
       


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

        
        setVisible(true);        // Make visible LAST
        AudioManager.getInstance().playBackgroundMusic();
    }

    private void layoutComponents() {
        mainPanel = new BackgroundPanel();
        mainPanel.setLayout(new BorderLayout());

        // Top section (dealer area + pause button)
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);

        // Achievement panel on the LEFT
        JPanel topLeftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topLeftPanel.setOpaque(false);

        JButton achievementButton = new JButton();
        achievementButton.setPreferredSize(new Dimension(50, 50));
        achievementButton.setToolTipText("View Achievements");
        achievementButton.setFocusPainted(false);
        achievementButton.setContentAreaFilled(false);
        achievementButton.setBorderPainted(false);
        achievementButton.setOpaque(false);
        buttonPanel.add(undoButton); // Add Undo button
        buttonPanel.add(redoButton); // Add Redo button

        // Load your icon
        ImageIcon achievementIcon = new ImageIcon("img/icons/achievement.png");
        Image scaledIcon = achievementIcon.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
        achievementButton.setIcon(new ImageIcon(scaledIcon));

        achievementButton.addActionListener(e -> {
            new AchievementsWindow().setVisible(true);
        });

        topLeftPanel.add(achievementButton);
        topPanel.add(topLeftPanel, BorderLayout.WEST);

        // Dealer area in the CENTER
        JPanel dealerArea = new JPanel(new BorderLayout());
        dealerArea.setOpaque(false);
        dealerArea.add(dealerScorePanel, BorderLayout.NORTH);
        dealerArea.add(dealerPanel, BorderLayout.CENTER);

        topRightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topRightPanel.setOpaque(false);
        topRightPanel.add(pauseButton);

        topPanel.add(topLeftPanel, BorderLayout.WEST);
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
        southContainer.setPreferredSize(new Dimension(gameWidth, (int) (gameHeight/2.4))); // Limit height
        southContainer.setBackground(new Color(0, 0, 0, 0)); // Transparent

        southContainer.setOpaque(false);


        JPanel playersContainer = new JPanel(new BorderLayout());
        playersContainer.setPreferredSize(new Dimension(
            Toolkit.getDefaultToolkit().getScreenSize().width,
            300
        ));
        playersContainer.setOpaque(false);
        playersContainer.add(scrollPane, BorderLayout.CENTER);

        betPanel.setOpaque(false);

        southContainer.add(playersContainer, BorderLayout.CENTER);
        southContainer.add(betPanel, BorderLayout.SOUTH);

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(southContainer, BorderLayout.SOUTH);

        add(mainPanel);
       // gameManager.startNewGame();
        // this is where the game is being started //
    }

    private void initializeComponents() {
        // Initialize CORE containers first
        mainPanel = new BackgroundPanel();
        mainPanel.setLayout(new BorderLayout());
        buttonPanel.add(newGameButton);

        playersPanel = new PlayersPanel();
        scrollPane = new JScrollPane(playersPanel);

        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null); // optional, removes white border
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        scrollPane.getViewport().setBackground(new Color(0, 0, 0, 0)); // ✅ add this
        scrollPane.setBackground(new Color(0, 0, 0, 0));

        dealerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        betPanel = new JPanel();
        buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        betLabel = createStyledLabel(Texts.bet[language] + " $0");
        balanceLabel = createStyledLabel(Texts.balance[language] + " $1000");
        

        // Set properties for main components
        mainPanel.setBackground(new Color(0, 0, 0, 0));
        mainPanel.setOpaque(false);

        playersPanel.setBackground(new Color(0, 0, 0, 0)); // ✅ Fully transparent

        dealerPanel.setBackground(new Color(0, 0, 0, 0));
        betPanel.setBackground(new Color(0, 0, 0, 0));
        buttonPanel.setBackground(new Color(0, 0, 0, 0));


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
        playersPanel.setBackground(new Color(0, 0, 0, 0)); // Fully transparent


        
        // Dealer labels
        dealerScoreLabel = createStyledLabel(Texts.guiDealerScore[language]);
        dealerBalanceLabel = createStyledLabel(Texts.dealerBalance[language] + " $1000");
        dealerBetLabel = createStyledLabel(Texts.dealerBet[language] + " $0");
    
        // Assemble dealer score panel
        JPanel scoreRow = new JPanel(new FlowLayout(FlowLayout.CENTER));
        scoreRow.add(dealerScoreLabel);
        scoreRow.setBackground(new Color(0, 0, 0, 0));

        JPanel balanceBetRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        balanceBetRow.add(dealerBalanceLabel);
        balanceBetRow.add(dealerBetLabel);
        balanceBetRow.setOpaque(false);

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
        JMenuItem saveItem = new JMenuItem(Texts.saveGame[language]);
        
        Font menuFont = new Font("Arial", Font.BOLD, 18);
        resumeItem.setFont(menuFont);
        mainMenuItem.setFont(menuFont);
        exitItem.setFont(menuFont);
        saveItem.setFont(menuFont);

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

        saveItem.addActionListener(e -> {
            try {
                //gameManager.save();
                GameManager.getInstance().save();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
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
        pauseMenu.add(saveItem);
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
        undoButton = new JButton();
        undoButton.setPreferredSize(new Dimension(50, 50));
        ImageIcon undoIcon = new ImageIcon("resources/images/undo.png"); // Ensure the file path is correct
        Image scaledUndoIcon = undoIcon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH); // Scale the image
        undoButton.setIcon(new ImageIcon(scaledUndoIcon)); // Set the scaled icon
        undoButton.setToolTipText("Undo the last action"); // Add a tooltip
        undoButton.setBackground(new Color(255, 215, 0)); // Gold background
        undoButton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        undoButton.setContentAreaFilled(false);
        undoButton.setOpaque(true);
        undoButton.setFocusPainted(false);
    
        // Initialize Redo button with an image
        redoButton = new JButton();
        redoButton.setPreferredSize(new Dimension(50, 50));
        ImageIcon redoIcon = new ImageIcon("resources/images/redo.png"); // Ensure the file path is correct
        Image scaledRedoIcon = redoIcon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH); // Scale the image
        redoButton.setIcon(new ImageIcon(scaledRedoIcon)); // Set the scaled icon
        redoButton.setToolTipText("Redo the last undone action"); // Add a tooltip
        redoButton.setBackground(new Color(255, 215, 0)); // Gold background
        redoButton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        redoButton.setContentAreaFilled(false);
        redoButton.setOpaque(true);
        redoButton.setFocusPainted(false);
    
        // Add hover effects for Undo button
        undoButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                undoButton.setBackground(new Color(255, 200, 0)); // Darker gold on hover
            }
    
            @Override
            public void mouseExited(MouseEvent e) {
                undoButton.setBackground(new Color(255, 215, 0)); // Original gold
            }
        });
    
        // Add hover effects for Redo button
        redoButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                redoButton.setBackground(new Color(255, 200, 0)); // Darker gold on hover
            }
    
            @Override
            public void mouseExited(MouseEvent e) {
                redoButton.setBackground(new Color(255, 215, 0)); // Original gold
            }
        });

    }
   
    private void attachEventListeners() {
        hitButton.addActionListener(e -> gameManager.handlePlayerHit());
        standButton.addActionListener(e -> gameManager.handlePlayerStand());
        newGameButton.addActionListener(e -> gameManager.startNewGame());
        
        placeBetButton.addActionListener(e -> placeBet(gameManager.getCurrentPlayer()));
        undoButton.addActionListener(e -> gameManager.undoLastAction());
        redoButton.addActionListener(e -> gameManager.redoLastAction());
    
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
                AchievementManager.getInstance().trackFirstBet(player);
                AudioManager.getInstance().playSoundEffect("/sounds/bet.wav");
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
        undoButton = new JButton("Undo");
        redoButton = new JButton("Redo");
        
        undoButton.addActionListener(e -> gameManager.undoLastAction());
        redoButton.addActionListener(e -> gameManager.redoLastAction());
        
        buttonPanel.add(undoButton);
        buttonPanel.add(redoButton);
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
        undoButton.setEnabled(buttonsEnabled && !gameManager.isGameOver());
        redoButton.setEnabled(buttonsEnabled && !gameManager.isGameOver());

        pauseButton.setEnabled(true);
    }


    // After the game ended the user should be able to take another bet
    public void enableBetting() {
        betField.setEnabled(true);
        placeBetButton.setEnabled(true);
        betField.setText(""); // Clear the bet field
        betLabel.setText(Texts.bet[language] + " $"+gameManager.getPlayerBet(gameManager.getCurrentPlayer()));
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
        System.out.println("img/cards/");
        ImageIcon undoIcon = new ImageIcon("img/cards/undo.png"); // Ensure the file path is correct

        // Refresh the entire main panel
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    public void updateGameState(ArrayList<Player> players, Player dealer, boolean gameOver, boolean isPaused) {
        dealerPanel.removeAll();
        playersPanel.removeAll();
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
            dealerScoreLabel.setText("Dealer Score: ???");
            System.out.println("img/cards/");
            
        }
        for (Player player : players) {
            JPanel playerPanel = new JPanel(new BorderLayout());
            playerPanel.add(new JLabel(player.getName() + ": " + player.calculateScore()), BorderLayout.NORTH);
    
            JPanel cardsPanel = new JPanel(new FlowLayout());
            for (Card card : player.getHand()) {
                cardsPanel.add(createCardPanel(card));
            }
            playerPanel.add(cardsPanel, BorderLayout.CENTER);
    
            playersPanel.add(playerPanel);
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

    private JPanel createCardPanel(Card card) {
        JPanel cardPanel = new JPanel(new BorderLayout());
        cardPanel.setPreferredSize(new Dimension(cardWidth, cardHeight));
        cardPanel.setBackground(Color.WHITE);
        cardPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 4));
    
        String imagePath = ("img/cards/") + card.getImageFileName();
        System.out.println("Loading image: " + imagePath);
    
        try {
            ImageIcon cardIcon = new ImageIcon(imagePath);  // ✅ no leading slash
            Image scaledImage = cardIcon.getImage().getScaledInstance(cardWidth, cardHeight, Image.SCALE_SMOOTH);
            JLabel cardLabel = new JLabel(new ImageIcon(scaledImage));
            cardPanel.add(cardLabel, BorderLayout.CENTER);
        } catch (Exception e) {
            // Fallback if the image is not found
            JLabel fallbackLabel = new JLabel(card.getRank() + " of " + card.getSuit(), SwingConstants.CENTER);
            fallbackLabel.setFont(new Font("Arial", Font.BOLD, 14));
            cardPanel.add(fallbackLabel, BorderLayout.CENTER);
        }
    
        return cardPanel;
    }
    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 26));
        label.setForeground(Color.WHITE);
        return label;
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
 
 public void promptPlayerAction(Player player) {
    if (!gameManager.isCurrentPlayerStillInRound()) {
        if (gameManager.hasNextPlayer()) {
            nextTurn();
        } else {
            gameManager.dealerTurn();
        }
    } else {
        setGameButtonsEnabled(true);
        updateGameMessage(player.getName() + "'s turn");

        // ✅ Only allow betting if game is not running
        if (!gameManager.isGameRunning()) {
            enableBetting();
        }
    }
}


    public void updatePlayerPanels() {
        playersPanel.removeAll();
        for (Player player : gameManager.getPlayers()) {
            JPanel panel = new JPanel(new BorderLayout());
            panel.setOpaque(false);  // ✅ Transparent player panel
            panel.setBackground(new Color(0, 0, 0, 0));  // ✅ Explicitly set transparent bg

            JLabel scoreLabel = new JLabel(player.getName() + ": Score: " + player.calculateScore());
            JLabel balanceLabel = new JLabel("Balance: $" + player.getBalance());
            JLabel betLabel = new JLabel("Current Bet: $" + player.getCurrentBet());
    
            // ✅ White text for visibility
            scoreLabel.setForeground(Color.WHITE);
            balanceLabel.setForeground(Color.WHITE);
            betLabel.setForeground(Color.WHITE);
            JPanel cardPanel = new JPanel(new FlowLayout());
            cardPanel.setOpaque(false);
            for (Card card : player.getHand()) {
                cardPanel.add(createCardPanel(card)); // ✅ use your card renderer
            }
    
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
                g.setColor(new Color(20, 20, 20)); // Subtle dark fallback

                g.fillRect(0, 0, getWidth(), getHeight());
            }
        }
    }
    
    
    
}
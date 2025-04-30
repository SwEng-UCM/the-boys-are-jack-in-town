package main.view;

import main.controller.GameManager;
import main.controller.GameState;
import main.controller.PlayerManager;
import main.model.Card;
import main.model.Player;
import main.controller.AudioManager;
import main.controller.BetCommand;
import main.controller.BettingManager;
import main.controller.DealerManager;
import main.controller.AchievementManager;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static main.view.BlackJackMenu.language;

public class BlackjackGUI extends JFrame {
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private final int gameHeight = (int) screenSize.getHeight();
    private final int gameWidth = (int) screenSize.getWidth();
    private int buttonWidth = (int) (gameWidth * 0.15);
    private int buttonHeight = (int) (gameHeight * 0.08);
    private int buttonFontSize = gameWidth / 60;
    private int cardWidth = (int) (gameWidth * 0.10);
    private int cardHeight = (int) (gameHeight * 0.22);
    private int cardFontSize = gameWidth / 60;
    JPanel mainPanel;
    JPanel dealerPanel;
    JPanel dealerScorePanel;
    JPanel buttonPanel;
    JPanel betPanel;
    JButton hitButton;
    JButton standButton;
    JButton newGameButton;
    JButton placeBetButton;
    JLabel gameMessageLabel;
    JLabel dealerScoreLabel;
    JLabel dealerBalanceLabel;
    JLabel balanceLabel;
    JLabel dealerBetLabel;
    JLabel betLabel;
    JLabel specialMessageLabel;
    JLabel enterBetLabel;
    JTextField betField;
    final GameManager gameManager;
    private final BettingManager bettingManager;
    JButton pauseButton;
    private JPopupMenu pauseMenu;
    PlayersPanel playersPanel;
    private static BufferedImage backgroundImage;
    private static boolean backgroundLoaded = false;
    JScrollPane scrollPane;
    JPanel topRightPanel;
    JButton undoButton;
    private JLabel connectionStatusLabel;
    private boolean isConnected = false;
    private final Map<String, JLabel> playerBalanceLabels = new HashMap<>();
    private final Map<String, JLabel> playerBetLabels = new HashMap<>();
    private static BlackjackGUI instance;

    public BlackjackGUI(GameManager gameManager) {
        this.gameManager = gameManager;
        this.bettingManager = gameManager.getBettingManager();
        setTitle(Texts.guiTitle[language]);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        try {
            backgroundImage = ImageIO.read(getClass().getResource("/resources/img/backgroundimage.png"));
            backgroundLoaded = true;
        } catch (IOException e) {
            System.err.println("Error loading background image: " + e.getMessage());
            backgroundLoaded = false;
        }
        gameManager.setGui(this);

        ImageIcon icon = new ImageIcon("resources/img/black.png");
        setIconImage(icon.getImage());

        new GUIComponentInitializer(this).initializeComponents();
        new GUILayoutBuilder(this).layoutComponents();
        new GUIEventBinder(this).attachEventListeners();

        setVisible(true);
        AudioManager.getInstance().playBackgroundMusic();
    }

    public static BlackjackGUI getInstance(GameManager gm) {
        if (instance == null) {
            instance = new BlackjackGUI(gm);
        }
        return instance;
    }

    public void applyGameState(GameState gameState) {
        updatePlayerHands(gameState.getPlayerHands());
        updateDealerHand(gameManager.getDealerManager().getVisibleDealerCards(true));

        updatePlayerScores(gameState.getPlayerScores());
        updateDealerScore(gameState.getDealerScore());
        updatePlayerBalances();
        updateDealerBalance(gameState.getDealerBalance());
        updateCurrentBets();
        updateGameStatus(gameState.isGameOver());
    }

    private void updatePlayerHands(List<List<Card>> playerHands) {
        playersPanel.removeAll();

        List<PlayerManager.PlayerInfo> playerInfos = gameManager.getPlayerManager().getAllPlayerInfo();

        for (PlayerManager.PlayerInfo info : playerInfos) {
            JPanel playerPanel = new JPanel(new BorderLayout());
            playerPanel.setOpaque(false);

            // Card display
            JPanel cardsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
            cardsPanel.setOpaque(false);
            for (Card card : info.hand) {
                cardsPanel.add(createCardPanel(card));
            }

            // Info display
            JLabel infoLabel = new JLabel(
                String.format("%s: %d | Bet: $%d | Balance: $%d",
                    info.name, info.score, info.bet, info.balance)
            );
            infoLabel.setForeground(Color.WHITE);
            infoLabel.setFont(new Font("Arial", Font.BOLD, 16));

            playerPanel.add(infoLabel, BorderLayout.NORTH);
            playerPanel.add(cardsPanel, BorderLayout.CENTER);

            playersPanel.add(playerPanel);
        }

        playersPanel.revalidate();
        playersPanel.repaint();
    }

    private void updateDealerHand(DealerManager.DealerCardInfo cardInfo) {
        dealerPanel.removeAll();

        boolean gameOver = gameManager.getGameFlowController().isGameOver();
        DealerManager.DealerCardInfo dealerInfo = gameManager.getDealerManager().getVisibleDealerCards(gameOver);

        for (Card card : dealerInfo.visibleCards) {
            dealerPanel.add(createCardPanel(card));
        }

        for (int i = 0; i < dealerInfo.hiddenCardCount; i++) {
            dealerPanel.add(createHiddenCardPanel());
        }

        dealerPanel.revalidate();
        dealerPanel.repaint();
    }

    private void updatePlayerScores(List<Integer> playerScores) {
        gameManager.getPlayerManager().setPlayerScores(playerScores);
        updatePlayerPanels(); // View updates only
    }


    private void updateDealerScore(int dealerScore) {
        String scoreText = gameManager.getDealerManager().getFormattedDealerScore();
        dealerScoreLabel.setText(scoreText);
    }
    
    
    private void updatePlayerBalances() {
        List<PlayerManager.PlayerInfo> playerInfos = gameManager.getPlayerManager().getAllPlayerInfo();
        for (PlayerManager.PlayerInfo info : playerInfos) {
            for (Player player : gameManager.getPlayerManager().getPlayers()) {
                if (player.getName().equals(info.name)) {
                    JLabel balanceLabel = playerBalanceLabels.get(info.name);
                    if (balanceLabel != null) {
                        balanceLabel.setText("Balance: $" + info.balance);
                    }
                    break;
                }
            }
        }
    }
    
    
    private void updateDealerBalance(int dealerBalance) {
        dealerBalanceLabel.setText(Texts.dealerBalance[language] + " $" + dealerBalance);
    }
    
    private void updateCurrentBets() {
        List<PlayerManager.PlayerInfo> playerInfos = gameManager.getPlayerManager().getAllPlayerInfo();
        for (PlayerManager.PlayerInfo info : playerInfos) {
            JLabel betLabel = playerBetLabels.get(info.name); // Use name as key
            if (betLabel != null) {
                betLabel.setText("Bet: $" + info.bet);
            }
        }
    }
    
    
    
    private void updateGameStatus(boolean isGameOver) {
        setGameButtonsEnabled(!isGameOver);
    
        if (isGameOver) {
            // Use DealerManager to get visible cards and score
            DealerManager.DealerCardInfo cardInfo = gameManager.getDealerManager().getVisibleDealerCards(true);
            updateDealerHand(cardInfo); // <- This version expects DealerCardInfo, not List<Card>
    
            int dealerScore = gameManager.getDealerManager().getDealerScore();
            dealerScoreLabel.setText(Texts.guiDealerScore[language] + ": " + dealerScore);
        }
    }
    

    public void setConnected(boolean connected) {
        isConnected = connected;
        SwingUtilities.invokeLater(() -> {
            if (connected) {
                connectionStatusLabel.setText("Online");
                connectionStatusLabel.setForeground(Color.GREEN);
            } else {
                connectionStatusLabel.setText("Offline");
                connectionStatusLabel.setForeground(Color.RED);
            }
        });
    }

    public void showConnectionError(String message) {
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(
                    this,
                    "Connection error: " + message,
                    "Network Error",
                    JOptionPane.ERROR_MESSAGE
            );
        });
    }



    public void updatePlayerBalanceAndBet(Player player) {
        JLabel balanceLabel = playerBalanceLabels.get(player.getName());
        JLabel betLabel = playerBetLabels.get(player.getName());
        
    
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
    
    public void placeBet(Player player) {
        try {
            int betAmount = Integer.parseInt(betField.getText());
    
            if (betAmount > 0) {
                // Use Command pattern
                BetCommand betCommand = new BetCommand(player, betAmount, gameManager);
                gameManager.getCommandManager().executeCommand(betCommand);
    
                // ✅ Update UI via controller/model access only
                int updatedPlayerBalance = gameManager.getBettingManager().getPlayerBalance(player.getName());
                int updatedPlayerBet = gameManager.getBettingManager().getPlayerBet(player.getName());
                int dealerBalance = gameManager.getDealerManager().getDealerBalance();
                int dealerBet = gameManager.getDealerManager().getDealerBet();
    
                // ✅ Update GUI labels
                setGameButtonsEnabled(true);
                betLabel.setText("Bet: $" + updatedPlayerBet);
                balanceLabel.setText("Balance: $" + updatedPlayerBalance);
                dealerBalanceLabel.setText(Texts.balance[language] + " $" + dealerBalance);
                dealerBetLabel.setText("Bet: $" + dealerBet);
    
                // ✅ Lock controls after bet
                betField.setEnabled(false);
                placeBetButton.setEnabled(false);
    
                JOptionPane.showMessageDialog(this,
                    Texts.betConfirmed[language] + " $" + betAmount,
                    Texts.bet[language],
                    JOptionPane.INFORMATION_MESSAGE
                );
    
                playersPanel.updatePanel(gameManager.getPlayerManager().getPlayers());      
                AchievementManager.getInstance().trackFirstBet(player);
    
            } else {
                JOptionPane.showMessageDialog(this, "Invalid Bet", "Error", JOptionPane.ERROR_MESSAGE);
            }
    
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                "Invalid input. Please enter a valid bet amount.",
                "Error",
                JOptionPane.WARNING_MESSAGE
            );
        }
    }
    

    private void setPlayers(List<PlayerManager.PlayerInfo> playerInfos) {
        playersPanel.removeAll();
        playerBalanceLabels.clear();
        playerBetLabels.clear();
    
        for (PlayerManager.PlayerInfo info : playerInfos) {
            JPanel panel = new JPanel(new BorderLayout());
            panel.setOpaque(false);
    
            JLabel scoreLabel = new JLabel(info.name + ": Score: " + info.score);
            JLabel balanceLabel = new JLabel("Balance: $" + info.balance);
            JLabel betLabel = new JLabel("Bet: $" + info.bet);
    
            // Use player name as key (instead of Player object, which may change)
            playerBalanceLabels.put(info.name, balanceLabel);
            playerBetLabels.put(info.name, betLabel);
    
            panel.add(scoreLabel, BorderLayout.NORTH);
            panel.add(balanceLabel, BorderLayout.CENTER);
            panel.add(betLabel, BorderLayout.SOUTH);
            playersPanel.add(panel);
        }
    
        playersPanel.revalidate();
        playersPanel.repaint();
    }
    

    public void restartGame() {
        gameManager.getGameFlowController().startNewGame();
        betField.setEnabled(true);
        placeBetButton.setEnabled(true);
        setGameButtonsEnabled(true);
        gameManager.getGameFlowController().setGameOver(false);
        gameManager.getGameFlowController().resumeGame();
    }

    public void showGameOverMessage(String message) {
        int option = JOptionPane.showOptionDialog(this, message, Texts.gameOverTitle[language], JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE, null, new String[]{"Restart Game", "Exit"}, "Restart Game");
        if (option == JOptionPane.YES_OPTION) {
            restartGame();
        } else {
            System.exit(0);
        }
    }
    
    private JButton createPauseButton(String text, Font font, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(font);
        button.setFocusPainted(false);
        button.setForeground(Color.BLACK);
        button.setBackground(bgColor);
        button.setOpaque(true);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent evt) {
                button.setBackground(bgColor.darker());
            }

            @Override
            public void mouseExited(MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });

        return button;
    }

    private void returnToMainMenu() {
        new BlackJackMenu().setVisible(true);
        dispose();
    }

    public void setGameButtonsEnabled(boolean enabled) {
        // Disable all game buttons when paused
        boolean buttonsEnabled = enabled && !gameManager.getGameFlowController().isPaused();

        hitButton.setEnabled(buttonsEnabled && !gameManager.getGameFlowController().isGameOver());
        standButton.setEnabled(buttonsEnabled && !gameManager.getGameFlowController().isGameOver());
        newGameButton.setEnabled(buttonsEnabled); // Disable when paused
        pauseButton.setEnabled(true); // Always enabled
        betField.setEnabled(buttonsEnabled && !gameManager.getGameFlowController().isGameOver());
        placeBetButton.setEnabled(buttonsEnabled && !gameManager.getGameFlowController().isGameOver());

        pauseButton.setEnabled(true);
    }

    // After the game ended the user should be able to take another bet
    public void enableBetting() {
        betField.setEnabled(true);
        placeBetButton.setEnabled(true);
        betField.setText(""); // Clear the bet field
        betLabel.setText(Texts.bet[language] + " $"+ gameManager.getPlayerManager().getPlayerBet(gameManager.getPlayerManager().getCurrentPlayer()));
        balanceLabel.setText(Texts.balance[language] + " $" + gameManager.getPlayerManager().getPlayerBalance(gameManager.getPlayerManager().getCurrentPlayer()));
        dealerBalanceLabel.setText(Texts.balance[language] + " $" + gameManager.getDealerManager().getDealerBalance());
        dealerBetLabel.setText(Texts.bet[language] + " $" + gameManager.getDealerManager().getDealerBet());

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
            dealerScoreLabel.setText(Texts.guiDealerScore[language] + ": " + dealer.calculateScore());
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
    
    private JPanel createHiddenCardPanel() {
        JPanel cardPanel = new JPanel();
        cardPanel.setPreferredSize(new Dimension(cardWidth, cardHeight));
        cardPanel.setBackground(Color.BLACK);
        cardPanel.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));

        JLabel hiddenLabel = new JLabel(gameManager.getGameFlowController().isPaused() ? "⏸" : "?", SwingConstants.CENTER);
        hiddenLabel.setFont(new Font("Arial", Font.BOLD, cardFontSize));
        hiddenLabel.setForeground(Color.WHITE);

        cardPanel.setLayout(new BorderLayout());
        cardPanel.add(hiddenLabel, BorderLayout.CENTER);

        // Location of custom background
        ImageIcon cardBackground = new ImageIcon("resources/img/card-background2.jpeg");
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

    JButton createStyledButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                Color gradientStart = getModel().isRollover() ? new Color(255, 240, 100) : new Color(255, 215, 0);
                Color gradientEnd = getModel().isRollover() ? new Color(255, 210, 0) : new Color(240, 180, 0);
                GradientPaint gp = new GradientPaint(0, 0, gradientStart, 0, getHeight(), gradientEnd);

                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 40, 40);

                // Optional: shadow effect
                g2.setColor(new Color(0, 0, 0, 40));
                g2.fillRoundRect(4, 4, getWidth() - 8, getHeight() - 8, 40, 40);

                super.paintComponent(g);
                g2.dispose();
            }
        };

        button.setFont(new Font("Segoe UI", Font.BOLD, buttonFontSize));
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setPreferredSize(new Dimension(buttonWidth, buttonHeight));

        return button;
    }

    JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(new Font("Segoe UI", Font.BOLD, 26));
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
 
    public void promptPlayerAction(Player player) {
            if (!gameManager.getPlayerManager().isCurrentPlayerStillInRound()) {
                gameManager.getPlayerManager().advanceToNextPlayer(); // ✅ Advance the index here
                if (gameManager.getPlayerManager().hasNextPlayer()) {
                    gameManager.startNextPlayerTurn(); // This will call promptPlayerAction again with a new player
                } else {
                    gameManager.getDealerManager().dealerTurn(); // No players left
                }
                return; // 🔒 Important: prevent further code from running
            }
        
            setGameButtonsEnabled(true);
            updateGameMessage(player.getName() + "'s turn");
        
            if (!gameManager.getGameFlowController().isGameRunning()) {
                enableBetting();
            }
    }
    
    public void updatePlayerPanels() {
        playersPanel.removeAll();
        for (Player player : gameManager.getPlayerManager().getPlayers()) {
            JPanel panel = new JPanel(new BorderLayout());
            panel.setOpaque(false);  // ✅ Transparent player panel
            panel.setBackground(new Color(0, 0, 0, 0));  // ✅ Explicitly set transparent bg

            JLabel scoreLabel = new JLabel(player.getName() + ": Score: " + player.calculateScore());
            JLabel balanceLabel = new JLabel("Balance: $" + player.getBalance());
            JLabel betLabel = new JLabel(Texts.bet[language] + " $" + player.getCurrentBet());

            // ✅ White text for visibility
            scoreLabel.setForeground(Color.WHITE);
            balanceLabel.setForeground(Color.WHITE);
            betLabel.setForeground(Color.WHITE);

            panel.add(scoreLabel, BorderLayout.NORTH);
            panel.add(balanceLabel, BorderLayout.CENTER);
            panel.add(betLabel, BorderLayout.SOUTH);
            playersPanel.add(panel);
        }
        playersPanel.revalidate();
        playersPanel.repaint();
    }

    static class BackgroundPanel extends JPanel {
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

    public int getGameWidth(){
        return gameWidth;
    }

    public int getGameHeight(){
        return gameHeight;
    }


}
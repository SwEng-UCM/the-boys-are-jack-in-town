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


/**
 * The main graphical user interface for the Blackjack game. Handles all visual components,
 * user interactions, and game state updates. Manages player displays, dealer information,
 * and game controls while coordinating with the GameManager for game logic.
 */
public class BlackjackGUI extends JFrame {
    // Screen size and layout configuration
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private final int gameHeight = (int) screenSize.getHeight();
    private final int gameWidth = (int) screenSize.getWidth();
    private int buttonWidth = (int) (gameWidth * 0.15);
    private int buttonHeight = (int) (gameHeight * 0.08);
    private int buttonFontSize = gameWidth / 60;
    private int cardWidth = (int) (gameWidth * 0.10);
    private int cardHeight = (int) (gameHeight * 0.22);
    private int cardFontSize = gameWidth / 60;

    // UI components
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

    /**
     * Constructor for the BlackjackGUI.
     * Initializes the graphical components, background, and sets up the game environment.
     *
     * @param gameManager The GameManager that controls the game logic.
     */
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

    /**
     * Returns the singleton instance of the BlackjackGUI.
     *
     * @param gm The GameManager to associate with the instance.
     * @return The singleton instance of BlackjackGUI.
     */
    public static BlackjackGUI getInstance(GameManager gm) {
        if (instance == null) {
            instance = new BlackjackGUI(gm);
        }
        return instance;
    }

    /**
     * Applies the current game state to update the GUI elements.
     *
     * @param gameState The current game state to reflect in the GUI.
     */
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

    /**
     * Updates the hands of all players in the GUI.
     *
     * @param playerHands The hands of all players to display.
     */
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

    /**
     * Updates the dealer's hand in the GUI.
     *
     * @param cardInfo The dealer's visible card information to display.
     */
    private void updateDealerHand(DealerManager.DealerCardInfo cardInfo) {
        dealerPanel.removeAll();

        boolean gameOver = gameManager.getGameFlowController().isGameOver();
        DealerManager.DealerCardInfo dealerInfo = gameManager.getDealerManager().getVisibleDealerCards(gameOver);

        for (Card card : dealerInfo.visibleCards) {
            String imagePath = String.format("resources/img/%s_of_%s.png", card.getRank().toLowerCase(), card.getSuit().toLowerCase());

        }

        for (int i = 0; i < dealerInfo.hiddenCardCount; i++) {
            dealerPanel.add(createHiddenCardPanel());
        }

        dealerPanel.revalidate();
        dealerPanel.repaint();
    }

    /**
     * Updates the scores of all players in the GUI.
     *
     * @param playerScores The scores of all players to display.
     */
    private void updatePlayerScores(List<Integer> playerScores) {
        gameManager.getPlayerManager().setPlayerScores(playerScores);
        updatePlayerPanels(); // View updates only
    }

     /**
     * Updates the dealer's score in the GUI.
     *
     * @param dealerScore The dealer's score to display.
     */
    private void updateDealerScore(int dealerScore) {
        String scoreText = gameManager.getDealerManager().getFormattedDealerScore();
        dealerScoreLabel.setText(scoreText);
    }
    
    /**
     * Updates the balances of all players in the GUI.
     */
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
    
    /**
     * Updates the dealer's balance in the GUI.
     *
     * @param dealerBalance The dealer's balance to display.
     */
    private void updateDealerBalance(int dealerBalance) {
        dealerBalanceLabel.setText(Texts.dealerBalance[language] + " $" + dealerBalance);
    }
    
    /**
     * Updates the current bets of all players in the GUI.
     */
    private void updateCurrentBets() {
        List<PlayerManager.PlayerInfo> playerInfos = gameManager.getPlayerManager().getAllPlayerInfo();
        for (PlayerManager.PlayerInfo info : playerInfos) {
            JLabel betLabel = playerBetLabels.get(info.name); // Use name as key
            if (betLabel != null) {
                betLabel.setText("Bet: $" + info.bet);
            }
        }
    }
    
    /**
     * Updates the game status and dealer's information when the game is over.
     * 
     * @param isGameOver Indicates if the game has ended or not.
     */
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
    
    /**
     * Updates the connection status in the GUI.
     * 
     * @param connected A boolean value indicating whether the connection is established or not.
     */
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

    /**
     * Displays an error message when a connection error occurs.
     * 
     * @param message The error message to display.
     */
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

    /**
     * Updates the player's balance and current bet in the GUI.
     * 
     * @param player The player whose balance and bet need to be updated.
     */
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
    
    /**
     * Places a bet for the player after validating the bet amount.
     * 
     * @param player The player placing the bet.
     */
    public void placeBet(Player player) {
        try {
            int betAmount = Integer.parseInt(betField.getText());
    
            if (betAmount > 0) {
                // Use Command pattern
                Player actualPlayer = gameManager.getPlayerManager().getPlayerByName(player.getName());
                BetCommand betCommand = new BetCommand(actualPlayer, betAmount, gameManager);
                
                gameManager.getCommandManager().executeCommand(betCommand);
                updateUndoButtonState();

                // Update UI via controller/model access only
                int updatedPlayerBalance = gameManager.getBettingManager().getPlayerBalance(player.getName());
                int updatedPlayerBet = gameManager.getBettingManager().getPlayerBet(player.getName());
                int dealerBalance = gameManager.getDealerManager().getDealerBalance();
                int dealerBet = gameManager.getDealerManager().getDealerBet();
    
                // Update GUI labels
                setGameButtonsEnabled(true);
                betLabel.setText("Bet: $" + updatedPlayerBet);
                balanceLabel.setText("Balance: $" + updatedPlayerBalance);
                dealerBalanceLabel.setText(Texts.balance[language] + " $" + dealerBalance);
                dealerBetLabel.setText("Bet: $" + dealerBet);
    
                JOptionPane.showMessageDialog(this,
                    Texts.betConfirmed[language] + " $" + betAmount,
                    Texts.bet[language],
                    JOptionPane.INFORMATION_MESSAGE
                );
    
                playersPanel.updatePanel(gameManager.getPlayerManager().getPlayers());      
                AchievementManager.getInstance().trackFirstBet(player);

                gameManager.startNextPlayerTurn();
                updateGameState(gameManager.getPlayerManager().getPlayers(), 
                gameManager.getDealerManager().getDealer(), 
                false, 
                gameManager.getGameFlowController().isPaused());

                updatePlayerBalanceAndBet(player);

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
    

    /**
     * Sets the player panels with updated information.
     * 
     * @param playerInfos The list of player information to display on the panel.
     */
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
    

    /**
     * Restarts the game by resetting necessary parameters and enabling relevant buttons.
     */
    public void restartGame() {
        gameManager.getGameFlowController().startNewGame();
        betField.setEnabled(true);
        placeBetButton.setEnabled(true);
        setGameButtonsEnabled(true);
        gameManager.getGameFlowController().setGameOver(false);
        gameManager.getGameFlowController().resumeGame();
    }


    /**
     * Shows a game over message with options to restart or exit the game.
     * 
     * @param message The game over message to display.
     */
    public void showGameOverMessage(String message) {
        int option = JOptionPane.showOptionDialog(this, message, Texts.gameOverTitle[language], JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE, null, new String[]{"Restart Game", "Exit"}, "Restart Game");
        if (option == JOptionPane.YES_OPTION) {
            restartGame();
        } else {
            System.exit(0);
        }
    }

    /**
     * Enables or disables all game-related buttons based on the provided condition.
     * 
     * @param enabled Indicates whether to enable or disable the buttons.
     */
    public void setGameButtonsEnabled(boolean enabled) {
        // Disable all game buttons when paused
        boolean buttonsEnabled = enabled && !gameManager.getGameFlowController().isPaused();

        hitButton.setEnabled(buttonsEnabled && !gameManager.getGameFlowController().isGameOver());
        standButton.setEnabled(buttonsEnabled && !gameManager.getGameFlowController().isGameOver());
        newGameButton.setEnabled(buttonsEnabled); // Disable when paused
        pauseButton.setEnabled(true); // Always enabled

        pauseButton.setEnabled(true);
    }

    /**
     * Enables betting after the game ends and updates the player and dealer information in the GUI.
     */
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

    /**
    * Refreshes the language of the GUI to reflect the selected language.
    */
    public void refreshLanguage() {
        setTitle(Texts.guiTitle[BlackJackMenu.language]);
    }
    
    /**
     * Updates the game state with the provided list of players and dealer information.
     * 
     * @param players The list of players.
     * @param dealer The dealer player.
     * @param gameOver Indicates whether the game is over.
     * @param isPaused Indicates whether the game is paused.
     */
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
    
    /**
     * Creates a hidden card panel to represent the dealer's hidden card.
     * 
     * @return The created panel representing the hidden card.
     */
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

    /**
     * Updates the game message displayed on the screen.
     * 
     * @param message The message to display in the game.
     */
    public void updateGameMessage(String message) {
        gameMessageLabel.setText(message);
    }

    /**
     * Updates the special message displayed on the screen, hiding it after 2 seconds.
     * 
     * @param message The special message to display.
     */
    public void updateSpecialMessage(String message) {
            Timer timer = new Timer(2000, e -> specialMessageLabel.setText("...")); // Reset after 2 sec
            timer.setRepeats(false);
            timer.start();
    }
    

    /**
     * Creates a styled button with gradient effects and shadow.
     * 
     * @param text The text of the button.
     * @return The styled button.
     */
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

        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        return button;
    }

    /**
     * Creates a styled label with the given text.
     * The label is centered and uses a bold font with white text color.
     *
     * @param text The text to display on the label.
     * @return A JLabel with the given text and styling.
     */
    JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(new Font("Segoe UI", Font.BOLD, 26));
        label.setForeground(Color.WHITE);
        return label;
    }

    /**
     * Creates a panel to display a card with its rank and suit.
     * The panel uses a border and displays the card's rank in bold and suit in normal font.
     *
     * @param card The card to display in the panel.
     * @return A JPanel representing the card, with its rank and suit displayed.
     */
    private JPanel createCardPanel(Card card) {
        JPanel cardPanel = new JPanel(new BorderLayout());

        cardPanel.setPreferredSize(new Dimension(cardWidth, cardHeight));
        cardPanel.setMinimumSize(new Dimension(cardWidth, cardHeight));
        cardPanel.setMaximumSize(new Dimension(cardWidth, cardHeight));
        cardPanel.setBackground(Color.WHITE);
        cardPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 4));

        String imagePath = String.format("resources/img/%s_of_%s.png", card.getRank().toLowerCase(), card.getSuit().toLowerCase());


        ImageIcon cardImage = new ImageIcon(imagePath);
        Image scaledImage = cardImage.getImage().getScaledInstance(cardWidth, cardHeight, Image.SCALE_SMOOTH);
        JLabel imageLabel = new JLabel(new ImageIcon(scaledImage));
    
        // Add the image to the card panel
        cardPanel.add(imageLabel, BorderLayout.CENTER);




        return cardPanel;
    }

    /**
     * Updates the state of the undo button based on whether an undo action is possible.
     */
    public void updateUndoButtonState() {
        undoButton.setEnabled(gameManager.getCommandManager().canUndo());
    }
    
    /**
     * Prompts the user to input a value for the Joker Wild card.
     * The input is validated to ensure it is an integer between 1 and 11.
     * 
     * @return The selected value for the Joker Wild card.
     */
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
 
    /**
     * Prompts the user for the player's action based on whether they are in a multiplayer game.
     * It enables/disables game buttons based on the current player's turn.
     * Also, if the player has already made a bet, it handles the state of the betting system.
     *
     * @param player The player whose action is being prompted.
     */
    public void promptPlayerAction(Player player) {
        if(gameManager.isMultiplayerMode()){
            boolean isCurrentPlayer = player.getName().equals(getCurrentPlayerName());
            hitButton.setEnabled(isCurrentPlayer);
            standButton.setEnabled(isCurrentPlayer);
            
            if (isCurrentPlayer) {
                updateGameMessage(player.getName() + ", it's your turn!");
            }
        }
        if (!gameManager.getPlayerManager().isCurrentPlayerStillInRound()) {
                gameManager.getPlayerManager().advanceToNextPlayer();
                if (gameManager.getPlayerManager().hasNextPlayer()) {
                    gameManager.startNextPlayerTurn();
                    gameManager.getDealerManager().dealerTurn();
                }
                return;
            }
        
            setGameButtonsEnabled(true);
            updateGameMessage(player.getName() + "'s turn");
        
            if (!gameManager.getBettingManager().hasPlayerBet(player.getName()) && !gameManager.getGameFlowController().isGameOver()) {
                enableBetting();
            } else {
            }
    }

    /**
     * Returns the name of the current player in a multiplayer game.
     * If the game is in multiplayer mode, it retrieves the name of the player
     * based on the current player's index in the game.
     *
     * @return The name of the current player, or an empty string if the game is not in multiplayer mode.
     */
    private String getCurrentPlayerName() {
        if (gameManager.isMultiplayerMode()) {
            List<Player> players = gameManager.getPlayerManager().getPlayers();
            int index = gameManager.getPlayerManager().getCurrentPlayerIndex();
            if (index >= 0 && index < players.size()) {
                return players.get(index).getName();
            }
        }
        return "";
    }
    
     /**
     * Updates the player panels on the GUI, displaying each player's name, score, balance, and current bet.
     * This method clears and revalidates the `playersPanel` to reflect the updated player information.
     */
    public void updatePlayerPanels() {
        playersPanel.removeAll();
        for (Player player : gameManager.getPlayerManager().getPlayers()) {
            JPanel panel = new JPanel(new BorderLayout());
            panel.setOpaque(false);  // Transparent player panel
            panel.setBackground(new Color(0, 0, 0, 0));  // Explicitly set transparent bg

            JLabel scoreLabel = new JLabel(player.getName() + ": Score: " + player.calculateScore());
            JLabel balanceLabel = new JLabel("Balance: $" + player.getBalance());
            JLabel betLabel = new JLabel(Texts.bet[language] + " $" + player.getCurrentBet());

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
        if (playerBalanceLabels.isEmpty() || playerBetLabels.isEmpty()) {
            setPlayers(gameManager.getPlayerManager().getAllPlayerInfo());
        }
    }

    /**
     * Custom JPanel that draws a background image or a fallback color.
     * The background image is drawn if it is loaded successfully; otherwise, 
     * a dark color is used as a fallback.
     */
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

    /**
     * Returns the width of the game window.
     *
     * @return The width of the game window.
     */
    public int getGameWidth(){
        return gameWidth;
    }

    /**
     * Returns the height of the game window.
     *
     * @return The height of the game window.
     */
    public int getGameHeight(){
        return gameHeight;
    }

    
}
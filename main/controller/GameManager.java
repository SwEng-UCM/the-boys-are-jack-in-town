package main.controller;

import main.model.Badge;
import main.model.Card;
import main.model.Deck;
import main.model.DifficultyStrategy;
import main.model.MediumDifficulty;
import main.model.Player;
import main.view.BlackjackGUI;
import main.view.Texts;
import javax.swing.Timer;
import static main.view.BlackJackMenu.language;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.SwingUtilities;

/*
 * Singleton class.
 */

/**
 * The GameManager class is responsible for managing the game state and logic.
 * It interacts with the Player, Deck, and BlackjackGUI classes to handle player
 * actions,
 * dealer actions, and determine the game outcome.
 */

// ORIGINATOR Class
public class GameManager {
    private static GameManager instance;
    private final int INITIAL_BET = 1000;
    private boolean isPaused = false;
    private Timer gameTimer; // If you have any timers running
    private ArrayList<Player> players;
    private Player dealer;
    private Deck deck;
    private BlackjackGUI gui;
    private boolean gameOver;
    private BettingManager bettingManager;
    private int currentPlayerIndex;
    private DifficultyStrategy difficultyStrategy = new MediumDifficulty();
    private BlackjackClient client;
    private GameFlowController gameFlowController;
    private CommandManager commandManager = new CommandManager();
    private PlayerManager playerManager;
    private DealerManager dealerManager;
    

    private GameManager() {
        this.players = new ArrayList<>();
        this.dealer = new Player("Dealer", INITIAL_BET);
        this.deck = new Deck();
        this.gameOver = false;
        this.bettingManager = new BettingManager(players, INITIAL_BET, INITIAL_BET); // Initial balance
        this.currentPlayerIndex = 0;
        players.add(new Player("PLAYER 1", INITIAL_BET)); // At least one player
        players.add(new Player("PLAYER 2", INITIAL_BET)); // At least one player
        
        this.playerManager = new PlayerManager();
        playerManager.setPlayers(players); 
    }


    // Public method to provide access to the singleton instance
    public static GameManager getInstance() {
        if (instance == null) {
            instance = new GameManager();
        }
        return instance;
    }

    public PlayerManager getPlayerManager() {
        return this.playerManager;
    }
    
    public DealerManager getDealerManager() {
        return this.dealerManager;
    }

    
    public void setDifficultyStrategy(DifficultyStrategy strategy) {
        this.difficultyStrategy = strategy;
    }

    public BettingManager getBettingManager() {
        return bettingManager;
    }


    public void setGui(BlackjackGUI gui) {
        this.gui = gui;
    
        if (this.dealerManager == null) {
            this.dealerManager = new DealerManager(dealer, players, deck, difficultyStrategy, gui, bettingManager, this);
        }
    
        if (this.gameFlowController == null) {
            this.gameFlowController = new GameFlowController(this, playerManager, dealerManager, gui);
        }
    }
    
    public void setGameFlowController(GameFlowController controller) {
        this.gameFlowController = controller;
    }
    
    public GameFlowController getGameFlowController() {
        return gameFlowController;
    }

    public void startNextPlayerTurn() {
        // Do NOT increment immediately — just prompt current player
        if (currentPlayerIndex == 0) {
            int dealerBet = bettingManager.getDealerBalance() / 10;
            bettingManager.placeDealerBet(dealerBet);
        }
    
        if (currentPlayerIndex < players.size()) {
            gui.promptPlayerAction(players.get(currentPlayerIndex));
        } else {
            gui.updateGameMessage("Dealer's turn!");
            dealerManager.dealerTurn();
        }
    }

    public void handlePlayerHit() {
        if (!gameOver && !isPaused) {
            Player currentPlayer = players.get(currentPlayerIndex);
            
            // NEW: Use Command pattern
            HitCommand hitCommand = new HitCommand(currentPlayer, this);
            commandManager.executeCommand(hitCommand);
    
            gui.updateGameState(players, dealer, gameOver, false);
    
            if (currentPlayer.calculateScore() > 21) {
                checkPlayerBust();
            } else {
                gui.promptPlayerAction(currentPlayer);
            }
    
            AudioManager.getInstance().playSoundEffect("/resources/sounds/card-sounds.wav");
        }
    }
    
    

    public void handlePlayerStand() {
        if (!gameOver) {
            currentPlayerIndex++; // move to next player
    
            if (currentPlayerIndex < players.size()) {
                // prompt next player's turn
                gui.promptPlayerAction(players.get(currentPlayerIndex));
            } else {
                // all players done, now dealer plays
                dealerManager.dealerTurn();
            }
        }
    }
    
    

    public void checkPlayerBust() {
        Player player = players.get(currentPlayerIndex);
        if (player.calculateScore() > 21) {
            gui.updateGameMessage(player.getName() + " busts! 😢");
            bettingManager.playerLoses(player.getName());
            bettingManager.dealerWins(null);
            currentPlayerIndex++;
    
            if (currentPlayerIndex < players.size()) {
                gui.promptPlayerAction(players.get(currentPlayerIndex));
            } else {
                dealerManager.dealerTurn();
            }
        }
    }
    
    public DifficultyStrategy getDifficultyStrategy() {
        return this.difficultyStrategy;
    }

    public void determineWinners() {
        if (!gameOver) {
            int dealerScore = dealer.calculateScore();
            for (Player player : players) {
                int playerScore = player.calculateScore();
                int basePayout = player.getCurrentBet();
                int payout = (int) (basePayout * difficultyStrategy.getPayoutMultiplier());
    
                if (playerScore > 21) {
                    gui.updateGameMessage(player.getName() + " busts! " + Texts.dealerWins[language]);
                    bettingManager.playerLoses(player.getName());
                    AchievementManager.getInstance().unlock(Badge.FIRST_LOSS);
                    AudioManager.getInstance().playSoundEffect("/resources/sounds/lose.wav");
                } else if (dealerScore > 21 || playerScore > dealerScore) {
                    gui.updateGameMessage(player.getName() + " wins! (Payout: " + payout + ")");
                    bettingManager.playerWins(player.getName()); // bettingManager handles all balance changes
                    AchievementManager.getInstance().resetDealerWinStreak();
                    AchievementManager.getInstance().unlock(Badge.FIRST_WIN);
                    if (basePayout * 2 >= 1000) {
                        AchievementManager.getInstance().unlock(Badge.BIG_WIN);
                    }
                    AudioManager.getInstance().playSoundEffect("/resources/sounds/win.wav");
                } else if (playerScore < dealerScore) {
                    gui.updateGameMessage(player.getName() + " loses! " + Texts.dealerWins[language]);
                    bettingManager.playerLoses(player.getName());
                    AchievementManager.getInstance().unlock(Badge.FIRST_LOSS);
                    AchievementManager.getInstance().trackDealerWin();
                    AudioManager.getInstance().playSoundEffect("/resources/sounds/lose.wav");
                } else {
                    gui.updateGameMessage(player.getName() + " ties! Bets returned.");
                    bettingManager.tie(player.getName());
                }
            }
            gui.updatePlayerPanels();
            gameOver = true;
            gameFlowController.checkGameOver();
            SwingUtilities.invokeLater(() -> gui.updateGameState(players, dealer, true, false));
        }
    }
    
    

    public void resetBettingManager(int playerBalance, int dealerBalance) {
        this.bettingManager = new BettingManager(players, 1000, 1000);
    }

    public boolean placeBet(Player player, int betAmount) {
        boolean placed = bettingManager.placeBet(player.getName(), betAmount);
        if (placed) {
            AchievementManager.getInstance().unlock(Badge.FIRST_BET);
            AudioManager.getInstance().playSoundEffect("/resources/sounds/bet.wav");
        }
        return placed;
    }
    
    

    public boolean canPlaceBet(Player currentPlayer) {
        return gameFlowController.isGameOver() &&
                !isPaused &&
                currentPlayer.getBalance() > 0 &&
                dealerManager.getDealerBalance() > 0;
    }

    private Card handleSpecialCard(Card card, Player recipient) {
        if (recipient != dealer) { // Display special messages only when the player draws them
            switch (card.getType()) {
                case BLACKJACK_BOMB:
                    gui.updateSpecialMessage("Blackjack Bomb! Player wins instantly! 💣");
                    System.out.println("BB");
                    gameOver = true;
                    break;
                case SPLIT_ACE:
                    gui.updateSpecialMessage("Split Ace! Your score will be halved. ♠");
                    System.out.println("SA");
                    break;
                case JOKER_WILD:
                    int wildValue = gui.promptJokerWildValue();
                    card.setWildValue(wildValue);
                    gui.updateSpecialMessage("Joker Wild! set to " + wildValue + " 🤡");
                    System.out.println("JW");
                    break;
                default:
                    break;
            }
        }
        return card;
    }

    public List<Card> getFilteredDeck(){
        List<Card> deck = new ArrayList<>();
        List<Card> usedCards = new ArrayList<>();

        for(Player p : players) {
            usedCards.addAll(p.getHand());
        }
        usedCards.addAll(dealer.getHand());

        for(Card c : this.deck.getAllCards()) {
            if(!usedCards.contains(c)) {
                deck.add(c);
            }
        }
        return deck;
    }

    public Deck getDeck() {
        return this.deck;
    }
    
    public void setDeck(Deck deck) {
        this.deck = deck;
    }
    
    public void setBettingManager(BettingManager bettingManager) {
        this.bettingManager = bettingManager;
    }
    

    public void loadGame(GameState memento) {
        this.players = new ArrayList<>(memento.getPlayers());
        this.dealer = memento.getDealer();
        this.deck = memento.getDeck();
        this.currentPlayerIndex = memento.getCurrentPlayerIndex();
        this.gameOver = memento.isGameOver();

        SwingUtilities.invokeLater(() -> {
            gui.updateGameMessage("Game loaded successfully!");
            gui.updateGameState(players, dealer, gameOver, isPaused);
            gui.setGameButtonsEnabled(true);
            gui.enableBetting();
            startNextPlayerTurn();
        });
    }

        // Save (Originator -> Memento)
        public void save() {
            // Create a GameState object from the current game data
            GameState gameState = new GameState(this); // Assuming the GameState constructor takes the GameManager
        
            // Save the GameState using the GameStateManager
            GameStateManager gameStateManager = new GameStateManager();
            try {
                gameStateManager.saveGame(gameState);  // Save the game state to the file
                System.out.println("Game saved successfully!");
            } catch (IOException e) {
                System.err.println("Error saving game: " + e.getMessage());
            }
        }
        

        // Restore (Memento -> Originator)
        public void applyGameState(GameState state) {
            System.out.println("Applying game state");
            
            state.restore(this);

            this.gui = new BlackjackGUI(this);

            SwingUtilities.invokeLater(() -> {
                gui.updateGameMessage("Game loaded successfully!");
                gui.updateGameState(players, dealer, gameOver, isPaused);
                gui.setGameButtonsEnabled(true);
                gui.enableBetting();
                startNextPlayerTurn();
            });

            System.out.println("Game loaded successfully!");
        }

        public void setClient(BlackjackClient client) {
            this.client = client;
        }


        public void undoHit(Player player, Card card) {
            player.getHand().remove(card);
            gui.updateGameState(players, dealer, gameOver, false); // refresh GUI
        }
        
        public void undoBet(Player player, int amount) {
            player.addToBalance(amount); // use new method
            player.setCurrentBet(player.getCurrentBet() - amount);
            gui.updateGameState(players, dealer, gameOver, false); // refresh GUI
        }

        public Card hit(Player player) {
            Card drawnCard = deck.dealCard();
            Card processedCard = handleSpecialCard(drawnCard, player);
            player.receiveCard(processedCard);
            return processedCard;
        }

        public CommandManager getCommandManager() {
            return commandManager;
        }

        public void setCurrentPlayerIndex(int index) {
            this.currentPlayerIndex = index;
        }
        
}
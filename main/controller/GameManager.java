package main.controller;

import main.model.Badge;
import main.model.Card;
import main.model.Deck;
import main.model.DifficultyStrategy;
import main.model.MediumDifficulty;
import main.model.Player;
import main.view.BlackjackGUI;
import main.view.Texts;
import static main.view.BlackJackMenu.language;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.SwingUtilities;

/**
 * The GameManager class is responsible for managing the game state and logic.
 * It acts as the central controller for the game, handling player actions, dealer actions,
 * and determining the game outcome. It interacts with various components such as the Player,
 * Deck, BlackjackGUI, and other managers to coordinate the game's flow.
 *
 * <p>This class implements the Singleton design pattern to ensure only one instance
 * of the GameManager exists during the application's lifecycle.</p>
 *
 * <p>It also serves as the Originator in the Memento design pattern, allowing the game
 * state to be saved and restored.</p>
 */
public class GameManager {
    private static GameManager instance;
    private final int INITIAL_BET = 1000;
    private boolean isPaused = false;
    private ArrayList<Player> players;
    private Player dealer;
    private Deck deck;
    private BlackjackGUI gui;
    private boolean gameOver;
    private BettingManager bettingManager;
    private DifficultyStrategy difficultyStrategy = new MediumDifficulty();
    private BlackjackClient client;
    private GameFlowController gameFlowController;
    private CommandManager commandManager = new CommandManager();
    private PlayerManager playerManager;
    private DealerManager dealerManager;
    private NetworkManager networkManager;
    private boolean multiplayerMode;

    private GameManager() {
        this.players = new ArrayList<>();
        this.dealer = new Player("Dealer", INITIAL_BET);
        this.deck = new Deck();
        
        Player p1 = new Player("PLAYER 1", INITIAL_BET);
        Player p2 = new Player("PLAYER 2", INITIAL_BET);
        this.players.add(p1);
        this.players.add(p2);
        
        this.bettingManager = new BettingManager(players, INITIAL_BET, INITIAL_BET); 
        this.playerManager = new PlayerManager();
        playerManager.setPlayers(players);
        
    }

    /**
     * Retrieves the singleton instance of the GameManager.
     *
     * @return The singleton instance of GameManager.
     */
    public static GameManager getInstance() {
        if (instance == null) {
            instance = new GameManager();
        }
        return instance;
    }

    /**
     * Sets the BlackjackGUI instance for the game.
     * Initializes the DealerManager and GameFlowController if they are not already set.
     *
     * @param gui The BlackjackGUI instance to set.
     */
    public void setGui(BlackjackGUI gui) {
        this.gui = gui;
    
        if (this.dealerManager == null) {
            this.dealerManager = new DealerManager(dealer, players, deck, difficultyStrategy, gui, bettingManager, this);
        }
    
        if (this.gameFlowController == null) {
            this.gameFlowController = new GameFlowController(this, playerManager, dealerManager, gui);
        }
    }

    /**
     * Retrieves the BlackjackGUI instance associated with the game.
     *
     * @return The BlackjackGUI instance.
     */
    public BlackjackGUI getGUI(){
        return this.gui;
    }

    /**
     * Sets the GameFlowController for the game.
     *
     * @param controller The GameFlowController instance to set.
     */
    public void setGameFlowController(GameFlowController controller) {
        this.gameFlowController = controller;
    }
    
    /**
     * Retrieves the GameFlowController instance.
     *
     * @return The GameFlowController instance.
     */
    public GameFlowController getGameFlowController() {
        return gameFlowController;
    }

    /**
     * Retrieves the PlayerManager instance associated with the game.
     * The PlayerManager is responsible for managing player-related operations,
     * such as tracking player actions and states.
     *
     * @return The PlayerManager instance.
     */
    public PlayerManager getPlayerManager() {
        return this.playerManager;
    }
    
    /**
     * Retrieves the DealerManager instance associated with the game.
     * The DealerManager is responsible for managing the dealer's actions and interactions
     * during the game.
     *
     * @return The DealerManager instance.
     */
    public DealerManager getDealerManager() {
        return this.dealerManager;
    }

    /**
     * Sets the NetworkManager instance for the game.
     * The NetworkManager handles multiplayer communication, including sending and receiving
     * messages between clients.
     *
     * @param networkManager The NetworkManager instance to set.
     */
    public void setNetworkManager(NetworkManager networkManager) {
        this.networkManager = networkManager;
    }
    
    /**
     * Sets the DifficultyStrategy for the game.
     * The DifficultyStrategy determines the behavior of the dealer and other game mechanics
     * based on the selected difficulty level.
     *
     * @param strategy The DifficultyStrategy instance to set.
     */
    public void setDifficultyStrategy(DifficultyStrategy strategy) {
        this.difficultyStrategy = strategy;
    }

    /**
     * Retrieves the BettingManager instance associated with the game.
     * The BettingManager is responsible for managing bets, payouts, and player balances.
     *
     * @return The BettingManager instance.
    */
    public BettingManager getBettingManager() {
        return bettingManager;
    }

    /**
     * Retrieves the DifficultyStrategy currently used in the game.
     * The DifficultyStrategy determines the behavior of the dealer and other game mechanics
     * based on the selected difficulty level.
     *
     * @return The DifficultyStrategy instance.
     */
    public DifficultyStrategy getDifficultyStrategy() {
        return this.difficultyStrategy;
    }

    /**
     * Starts the next player's turn. If all players have taken their turn,
     * the dealer's turn begins.
     */
    public void startNextPlayerTurn() {
        if (playerManager.getCurrentPlayerIndex() == 0) {
            int dealerBet = bettingManager.getDealerBalance() / 10;
            bettingManager.placeDealerBet(dealerBet);
        }
    
        if (playerManager.getCurrentPlayerIndex() < players.size()) {
            gui.promptPlayerAction(players.get(playerManager.getCurrentPlayerIndex()));
        } else {
            gui.updateGameMessage("Dealer's turn!");
            dealerManager.dealerTurn();
        }
    }

    /**
     * Checks if the game is in multiplayer mode.
     *
     * @return True if the game is in multiplayer mode, false otherwise.
     */
    public boolean isMultiplayerMode() {
        return multiplayerMode;
    }

    /**
     * Sets the multiplayer mode for the game. Clears default players in multiplayer mode
     * and initializes default players for single-player mode.
     *
     * @param multiplayerMode True to enable multiplayer mode, false to disable it.
     */
    public void setMultiplayerMode(boolean multiplayerMode) {
        this.multiplayerMode = multiplayerMode;
        if (multiplayerMode) {
            this.networkManager = new NetworkManager();
            // Clear default players in multiplayer mode - they'll join via network
            this.players.clear();
        } else {
            // Initialize with default players for single-player
            if (players.isEmpty()) {
                players.add(new Player("PLAYER 1", INITIAL_BET));
                players.add(new Player("PLAYER 2", INITIAL_BET));
            }
        }
    }

    /**
     * Creates a GameStateUpdate object representing the current game state.
     *
     * @return A GameStateUpdate object containing the current game state.
     */
    public GameStateUpdate createGameStateUpdate() {
        return new GameStateUpdate(
            players, 
            dealer, 
            playerManager.getCurrentPlayerIndex(), 
            gameOver
        );
    }

    /**
     * Handles a player disconnecting from the game. Removes the player from the game
     * and broadcasts the updated game state to other players.
     *
     * @param playerName The name of the player who disconnected.
     */
    public void playerDisconnected(String playerName) {
        players.removeIf(p -> p.getName().equals(playerName));
        broadcastGameState();
        System.out.println("Player disconnected: " + playerName);
    }

    /**
     * Broadcasts the current game state to all connected clients in multiplayer mode.
     */
    public void broadcastGameState() {
        if (!multiplayerMode) return;
        
        GameStateUpdate update = createGameStateUpdate();
        System.out.println("Sending update with players: " + update.getPlayers().size());
        for (Player p : playerManager.getPlayers()) {
            System.out.println("Player " + p.getName() + " has " + p.getHand().size() + " cards");
        }
        networkManager.getClientHandlers().forEach(handler -> 
            handler.sendMessage(update)
        );
        if (gui != null) {
            SwingUtilities.invokeLater(() -> 
                gui.updateGameState(players, dealer, gameOver, false)
            );
        }
    }
    
    public boolean isServer() {
        return multiplayerMode && client == null;
    }
    
    /**
     * Handles a multiplayer command sent by a client.
     *
     * @param command The MultiplayerCommand to handle.
     */
    public void handleCommand(MultiplayerCommand command) {
        if (!multiplayerMode || command == null || command.getType() == null) {
            return;
        }
    
        String playerName = command.getPlayerName();
        if (playerName == null && command.getType() != MultiplayerCommand.Type.START_NEW_GAME) {
            System.err.println("Command missing player name: " + command.getType());
            return;
        }
    
        switch (command.getType()) {
            case JOIN -> handlePlayerJoin(playerName);
            case BET -> handleBet(playerName, command.getData(Integer.class));
            case HIT -> handleHit(playerName);
            case STAND -> handleStand(playerName);
            case START_NEW_GAME -> {
                if (isServer()) {
                    gameFlowController.startNewGame();
                    broadcastGameState();
                }
            }
            default -> System.err.println("Unknown command type: " + command.getType());
        }
    }
    

    /**
     * Handles a player joining the game in multiplayer mode.
     *
     * @param command The MultiplayerCommand containing the join request.
     */
    private void handlePlayerJoin(String playerName) {
        if (playerManager.getPlayerByName(playerName) == null) {
            playerManager.addPlayer(playerName, 1000);
            System.out.println("Player joined: " + playerName);
            broadcastGameState(); // Send current state to all clients
        }
    }
    

    /**
     * Handles a player's bet in multiplayer mode.
     *
     * @param command The MultiplayerCommand containing the bet details.
     */
    private void handleBet(String playerName, int amount) {
        Player player = playerManager.getPlayerByName(playerName);
        if (player != null && amount > 0) {
            if (placeBet(player, amount)) {
                broadcastGameState();
            }
        }
    }
    

    /**
     * Handles a player's "hit" action in multiplayer mode.
     *
     * @param command The MultiplayerCommand containing the hit action.
     */
    private void handleHit(String playerName) {
        Player player = playerManager.getPlayerByName(playerName);
        if (player != null && playerManager.isCurrentPlayer(player)) {
            this.hit(player);
            broadcastGameState();
    
            if (player.calculateScore() > 21) {
                checkPlayerBust();
            }
        }
    }
    

    /**
     * Handles a player's "stand" action in multiplayer mode.
     *
     * @param command The MultiplayerCommand containing the stand action.
     */
    private void handleStand(String playerName) {
        Player player = playerManager.getPlayerByName(playerName);
        if (player != null && playerManager.isCurrentPlayer(player)) {
            playerManager.incrementCurrentPlayerIndex();
            broadcastGameState();
        }
    }
    

    /**
     * Handles a player's "hit" action in single-player mode.
     *
     * @throws IOException If an error occurs while processing the action.
     */
    public void handlePlayerHit() throws IOException {
        if (multiplayerMode) {
            if (client != null) {
                Player current = players.get(playerManager.getCurrentPlayerIndex());
                client.sendAction(new MultiplayerCommand(
                    MultiplayerCommand.Type.HIT, current.getName(), current));
            }
        } else {
            if (playerManager.getCurrentPlayerIndex() < 0 || playerManager.getCurrentPlayerIndex() >= players.size()) {
                System.err.println("Invalid player index: " + playerManager.getCurrentPlayerIndex() + " (Total players: " + players.size() + ")");
                playerManager.setCurrentPlayerIndex(0);
            }
        
            Player currentPlayer = players.get(playerManager.getCurrentPlayerIndex());
        
            HitCommand hitCommand = new HitCommand(currentPlayer, this);
            commandManager.executeCommand(hitCommand);
            gui.updateUndoButtonState();
        
            gui.updateGameState(players, dealer, gameOver, false);
        
            if (currentPlayer.calculateScore() > 21) {
                checkPlayerBust();
            } else {
                gui.promptPlayerAction(currentPlayer);
            }
        
            AudioManager.getInstance().playSoundEffect("/resources/sounds/card-sounds.wav");
        }    
    }
    
    
    /**
     * Handles a player's "stand" action in single-player mode.
     *
     * @throws IOException If an error occurs while processing the action.
     */
    public void handlePlayerStand() throws IOException {
        if (multiplayerMode) {
            if (client != null) {
                Player current = players.get(playerManager.getCurrentPlayerIndex());
                // Use the action factory method
                client.sendAction(MultiplayerCommand.action(
                    MultiplayerCommand.Type.STAND, 
                    current.getName()
                ));
            }
        } else {
            if (!gameOver) {
                if(playerManager.getCurrentPlayerIndex() < players.size())
                    playerManager.incrementCurrentPlayerIndex(); // move to next player
                if (playerManager.getCurrentPlayerIndex() < players.size()) {
                    gui.promptPlayerAction(players.get(playerManager.getCurrentPlayerIndex()));
                if (playerManager.getCurrentPlayerIndex() == 1) {
                    gui.enableBetting(); // custom method to show betting UI
                }                    
                } else {
                    dealerManager.dealerTurn();
                }
            }
        }
    }
    
    /**
     * Checks if the current player has busted (score > 21).
     * Updates the game state and prompts the next player's turn if necessary.
     */
    public void checkPlayerBust() {
        Player player = players.get(playerManager.getCurrentPlayerIndex());
        if (player.calculateScore() > 21) {
            gui.updateGameMessage(player.getName() + " busts!");
            bettingManager.playerLoses(player.getName());
            bettingManager.dealerWins(null);
            
     
            if (playerManager.getCurrentPlayerIndex() < players.size()) {
                // Proceed to the next player
                gui.promptPlayerAction(players.get(playerManager.getCurrentPlayerIndex()));
            } else {
                // All players finished their turn, proceed to dealer's turn
                dealerManager.dealerTurn();
                gameOver = true;
                gameFlowController.setGameOver(true);
                gui.enableBetting(); // Allow next round
            }
        }
    }
    

    /**
     * Determines the winners of the game based on the scores of the players and the dealer.
     * Updates the game state and awards payouts accordingly.
     */
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

            if(multiplayerMode)broadcastGameState();
        }
    }

    /**
     * Resets the BettingManager with the specified player and dealer balances.
     *
     * @param playerBalance The initial balance for players.
     * @param dealerBalance The initial balance for the dealer.
     */
    public void resetBettingManager(int playerBalance, int dealerBalance) {
        this.bettingManager = new BettingManager(players, 1000, 1000);
    }

    /**
     * Places a bet for the specified player.
     *
     * @param player The player placing the bet.
     * @param betAmount The amount of the bet.
     * @return True if the bet was successfully placed, false otherwise.
     */
    public boolean placeBet(Player player, int betAmount) {
        boolean placed = bettingManager.placeBet(player.getName(), betAmount);
        if (placed) {
            int updatedBalance = bettingManager.getPlayerBalance(player.getName());
            int updatedBet = bettingManager.getPlayerBet(player.getName());
            player.setBalance(updatedBalance);
            player.setCurrentBet(updatedBet);
        
            AchievementManager.getInstance().unlock(Badge.FIRST_BET);
            AudioManager.getInstance().playSoundEffect("/resources/sounds/bet.wav");
            }
        
        return placed;
    }
    
    /**
     * Checks if the current player can place a bet.
     *
     * @param currentPlayer The current player.
     * @return True if the player can place a bet, false otherwise.
     */
    public boolean canPlaceBet(Player currentPlayer) {
        return gameFlowController.isGameOver() &&
                !isPaused &&
                currentPlayer.getBalance() > 0 &&
                dealerManager.getDealerBalance() > 0;
    }

    /**
     * Handles special card effects when a card is drawn by a player.
     *
     * @param card The card drawn.
     * @param recipient The player who drew the card.
     * @return The processed card with any special effects applied.
     */
    private Card handleSpecialCard(Card card, Player recipient) {
        if (recipient != dealer) { // Display special messages only when the player draws them
            switch (card.getType()) {
                case BLACKJACK_BOMB:
                    gui.updateSpecialMessage("Blackjack Bomb! Player wins instantly! 💣");
                    gameOver = true;
                    break;
                case SPLIT_ACE:
                    gui.updateSpecialMessage("Split Ace! Your score will be halved. ♠");
                    break;
                case JOKER_WILD:
                    int wildValue = gui.promptJokerWildValue();
                    card.setWildValue(wildValue);
                    gui.updateSpecialMessage("Joker Wild! set to " + wildValue + " 🤡");
                    break;
                default:
                    break;
            }
        }
        return card;
    }

    /**
     * Retrieves the filtered deck of cards, excluding cards already in use.
     *
     * @return A list of cards representing the filtered deck.
     */
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

    /**
     * Retrieves the current deck of cards.
     *
     * @return The current Deck instance.
     */
    public Deck getDeck() {
        return this.deck;
    }
    
    /**
     * Sets the current deck of cards.
     *
     * @param deck The Deck instance to set.
     */
    public void setDeck(Deck deck) {
        this.deck = deck;
    }
    
    /**
     * Sets the BettingManager instance for the game.
     *
     * @param bettingManager The BettingManager instance to set.
     */
    public void setBettingManager(BettingManager bettingManager) {
        this.bettingManager = bettingManager;
    }
    
    /**
     * Loads a saved game state into the GameManager.
     *
     * @param memento The GameState object representing the saved state.
     */
    public void loadGame(GameState memento) {
        this.players = new ArrayList<>(memento.getPlayers());
        this.dealer = memento.getDealer();
        this.deck = memento.getDeck();
        this.playerManager.setCurrentPlayerIndex(memento.getCurrentPlayerIndex());
        this.gameOver = memento.isGameOver();

        SwingUtilities.invokeLater(() -> {
            gui.updateGameMessage("Game loaded successfully!");
            gui.updateGameState(players, dealer, gameOver, isPaused);
            gui.setGameButtonsEnabled(true);
            gui.enableBetting();
            startNextPlayerTurn();
        });
    }

    /**
     * Saves the current game state using the GameStateManager.
     */
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
        
    /**
     * Applies a loaded game state to the GameManager.
     *
     * @param loadedState The GameState object representing the loaded state.
     */
    public void applyGameState(GameState loadedState) {
        System.out.println("Applying game state");
        
        loadedState.restore(this);

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

    /**
     * Applies a GameStateUpdate object to update the game state.
     *
     * @param update The GameStateUpdate object containing the updated state.
     */
    public void applyGameStateUpdate(GameStateUpdate update) {
        this.playerManager.setPlayers(new ArrayList<>(update.getPlayers()));
        this.dealer = new Player(update.getDealer());
        
        // ✅ CRITICAL — must update player manager
        this.playerManager.setPlayers(this.players);
    
        this.playerManager.setCurrentPlayerIndex(update.getCurrentPlayerIndex());
        this.gameOver = update.isGameOver();
    
        if (gui != null) {
            SwingUtilities.invokeLater(() -> {
                gui.updateGameState(players, dealer, gameOver, false);
                gui.setGameButtonsEnabled(!gameOver);
            });
        }
    }
    
    
    

    /**
     * Sets the BlackjackClient instance for multiplayer communication.
     *
     * @param client The BlackjackClient instance to set.
     */
    public void setClient(BlackjackClient client) {
        this.client = client;
    }

    /**
     * Undoes a "hit" action for the specified player.
     *
     * @param player The player whose action is being undone.
     * @param card The card to remove from the player's hand.
     */
    public void undoHit(Player player, Card card) {
        player.getHand().remove(card);
        gui.updateGameState(players, dealer, gameOver, false); // refresh GUI
    }
    
    /**
     * Undoes a bet action for the specified player.
     *
     * @param player The player whose bet is being undone.
     * @param amount The amount of the bet to undo.
     */
    public void undoBet(Player player, int amount) {
        player.addToBalance(amount);
        player.setCurrentBet(player.getCurrentBet() - amount);
        gui.updateGameState(players, dealer, gameOver, false); // refresh GUI
    }

    /**
     * Executes a "hit" action for the specified player.
     *
     * @param player The player performing the "hit" action.
     * @return The card drawn by the player.
     */
    public Card hit(Player player) {    
        Card drawnCard = deck.dealCard();
        if (drawnCard == null) {
            System.out.println("Deck is empty, no card dealt.");
        } else {
            System.out.println("Card dealt: " + drawnCard);
        }
    
        Card processedCard = handleSpecialCard(drawnCard, player);
        player.receiveCard(processedCard);
    
        this.gui.updatePlayerPanels();
    
        return processedCard;
    }
    

    /**
     * Retrieves the CommandManager instance for managing game commands.
     *
     * @return The CommandManager instance.
     */
    public CommandManager getCommandManager() {
        return commandManager;
    }


    /**
     * Retrieves the NetworkManager instance associated with the game.
     * The NetworkManager is responsible for handling multiplayer communication,
     * including sending and receiving messages between clients.
     *
     * @return The NetworkManager instance, or null if it has not been initialized.
     */
    public NetworkManager getNetworkManager() {
        return this.networkManager;
    }

    /**
     * Resets the singleton instance of the GameManager.
     * This is primarily used for testing purposes.
     */
    public static void resetInstance() {
        instance = null;
    }
    
}
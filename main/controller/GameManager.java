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
import java.util.Stack;

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
    

    private final int INITIAL_BET = 1000;
    private boolean isPaused = false;
    private Timer gameTimer; // If you have any timers running

    private ArrayList<Player> players;
    private Player dealer;
    private Deck deck;
    private BlackjackGUI gui;
    private boolean gameOver;
    private final Stack<GameState> undoStack = new Stack<>();
    private final Stack<GameState> redoStack = new Stack<>();
    private BettingManager bettingManager;
    private int currentPlayerIndex;
    private DifficultyStrategy difficultyStrategy = new MediumDifficulty();
    
    

    private GameManager() {
        this.players = new ArrayList<>();
        this.dealer = new Player("Dealer", INITIAL_BET);
        this.deck = new Deck();
        this.gameOver = false;
        this.bettingManager = new BettingManager(players, INITIAL_BET, INITIAL_BET); // Initial balance
        this.currentPlayerIndex = 0;
        players.add(new Player("PLAYER 1", INITIAL_BET)); // At least one player
        players.add(new Player("PLAYER 2", INITIAL_BET)); // At least one player
        System.out.println("GameManager initialized!");
    }

    private static GameManager instance;
    // Public method to provide access to the singleton instance
    public static GameManager getInstance() {
        if (instance == null) {
            instance = new GameManager();
        }
        return instance;
    }
    public void setDifficultyStrategy(DifficultyStrategy strategy) {
        this.difficultyStrategy = strategy;
    }
    
    private void saveGameState() {
        GameState currentState = new GameState(this.players, this.dealer, this.deck, this.currentPlayerIndex, this.gameOver);
        undoStack.push(currentState); // Save the current state to the undo stack
        redoStack.clear(); // Clear the redo stack since a new action invalidates the redo history
    }
    public void restoreFullState(GameManager manager) {
        // Restore core entities
        manager.getPlayers().clear();
        manager.getPlayers().addAll(this.players);
        manager.getDealer().getHand().clear();
        manager.getDealer().getHand().addAll(this.dealer.getHand());
    
        // Restore game progress
        manager.setCurrentPlayerIndex(this.currentPlayerIndex);
        manager.setGameOver(this.gameOver);
    
        // Restore deck
        manager.getDeck().getCards().clear();
        manager.getDeck().getCards().addAll(this.deck.getCards());
    
        // Restore difficulty
        manager.setDifficultyStrategy(manager.getDifficultyStrategy());
    }
    public void undoLastAction() {
        if (!undoStack.isEmpty()) {
            System.out.println("Undo: Restoring previous state...");
            GameState previousState = undoStack.pop(); // Pop the last state from the undo stack
            redoStack.push(new GameState(this)); // Save the current state to the redo stack
            previousState.restoreFullState(this); // Restore the previous state
            System.out.println("Undo: State restored successfully.");
            System.out.println("Undo: Current player index: " + currentPlayerIndex);
            System.out.println("Undo: Players: " + players);
            System.out.println("Undo: Dealer hand: " + dealer.getHand());
            this.players = new ArrayList<>(previousState.getPlayers()); // Convert List<Player> to ArrayList<Player>
            this.dealer = previousState.getDealer();
            this.deck = previousState.getDeck();
            this.currentPlayerIndex = previousState.getCurrentPlayerIndex();
            this.gameOver = previousState.isGameOver();
            gui.updateGameState(players, dealer, gameOver, isPaused);
            gui.updateGameMessage("Undo successful!");
            if (!gameOver) {
                startNextPlayerTurn(); // Ensure the game continues
            }
            
        } else {
            gui.updateGameMessage("No actions to undo!");
        }
    }
    public void redoLastAction() {
        if (!redoStack.isEmpty()) {
            System.out.println("Undo: Restoring previous state...");
            GameState nextState = redoStack.pop(); // Pop the last state from the redo stack
            undoStack.push(new GameState(this)); // Save the current state to the undo stack
            nextState.restoreFullState(this); // Restore the next state
            System.out.println("Undo: State restored successfully.");
            System.out.println("Undo: Current player index: " + currentPlayerIndex);
            System.out.println("Undo: Players: " + players);
            System.out.println("Undo: Dealer hand: " + dealer.getHand());
            this.players = new ArrayList<>(nextState.getPlayers()); // Convert List<Player> to ArrayList<Player>
            this.dealer = nextState.getDealer();
            this.deck = nextState.getDeck();
            this.currentPlayerIndex = nextState.getCurrentPlayerIndex();
            this.gameOver = nextState.isGameOver();
            gui.updateGameState(players, dealer, gameOver, isPaused);
            gui.updateGameMessage("Redo successful!");
            if (!gameOver) {
                startNextPlayerTurn(); // Ensure the game continues
            }
        } else {
            gui.updateGameMessage("No actions to redo!");
        }
    }
    public BettingManager getBettingManager() {
        return bettingManager;
    }


    public void setGui(BlackjackGUI gui) {
        this.gui = gui;
    }

    public void pauseGame() {
        isPaused = true;
        if (gameTimer != null && gameTimer.isRunning()) {
            gameTimer.stop();
        }
        gui.updateGameMessage(Texts.GAME_PAUSED[language]);
        gui.updateGameState(players, dealer, false, true); // gameOver=false, isPaused=true
    }
    public void handlePlayerHit() {
        if (!gameOver && !isPaused) {
            saveGameState(); // Save the current state before modifying it
            Player currentPlayer = players.get(currentPlayerIndex);
            currentPlayer.receiveCard(handleSpecialCard(deck.dealCard(), currentPlayer));
            gui.updateGameState(players, dealer, gameOver, false);
    
            if (currentPlayer.calculateScore() > 21) {
                checkPlayerBust(); // already moves to next player
            } else {
                gui.promptPlayerAction(currentPlayer);
            }
            AudioManager.getInstance().playSoundEffect("/sounds/card-sounds.wav");
        }
    }

    public boolean isPaused() {
        return isPaused;
    }

    public void resumeGame() {
        isPaused = false;
        gui.setGameButtonsEnabled(true);

        if (gameTimer != null) {
            gameTimer.start();
        }

        gui.updateGameMessage(Texts.gameManagerGameOn[language]);
        // Restore the actual game state (hidden card if game isn't over)
        gui.updateGameState(players, dealer, gameOver, false); // gameOver=actual state, isPaused=false;
    }

    public boolean isGamePaused() {
        return isPaused;
    }

    public void addPlayer(String name, int initialBalance) {
        players.add(new Player(name, initialBalance));
    }

    public void startNextPlayerTurn() {
        if (currentPlayerIndex < players.size()) {
            Player currentPlayer = players.get(currentPlayerIndex);
            gui.updateGameMessage(currentPlayer.getName() + "'s turn!");
            gui.promptPlayerAction(players.get(currentPlayerIndex));
            if (currentPlayerIndex == 0) {
                int dealerBet = bettingManager.getDealerBalance() / 10;
                bettingManager.placeDealerBet(dealerBet);
            }
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
                dealerTurn();
            }
        }
    }
    
    

    public void checkPlayerBust() {
        Player player = players.get(currentPlayerIndex);
        if (player.calculateScore() > 21) {
            gui.updateGameMessage(player.getName() + " busts! 😢");
            player.loseBet();
            bettingManager.dealerWins(null);
            currentPlayerIndex++;
    
            if (currentPlayerIndex < players.size()) {
                gui.promptPlayerAction(players.get(currentPlayerIndex));
            } else {
                dealerTurn(); // 👈 only trigger if all players finished
            }
        }
    }
    

    public void dealerTurn() {
        Player referencePlayer = players.get(0); // Or choose the strongest player
        for (Player p : players) {
            if (p.calculateScore() <= 21 && p.calculateScore() > referencePlayer.calculateScore()) {
                referencePlayer = p;
            }
        }
    
        while (difficultyStrategy.shouldDealerHit(dealer, referencePlayer)) {
            dealer.receiveCard(deck.dealCard());
        }
        checkDealerBust();
        determineWinners();
    }
    
    // In GameManager.java
    public DifficultyStrategy getDifficultyStrategy() {
        return this.difficultyStrategy;
    }

    private void checkDealerBust() {
        if (dealer.calculateScore() > 21) {
            gui.updateGameMessage("Dealer busts!");
            for (Player player : players) {
                if (player.calculateScore() <= 21) {
                    int payout = (int)(player.getCurrentBet() * difficultyStrategy.getPayoutMultiplier());
                    player.winBet(payout); // Use dynamic payout instead of hardcoded *2
                }
            }
        }
    }

    private void determineWinners() {
        if (!gameOver) {
            int dealerScore = dealer.calculateScore();
            for (Player player : players) {
                int playerScore = player.calculateScore();
                int basePayout = player.getCurrentBet();
                int payout = (int)(basePayout * difficultyStrategy.getPayoutMultiplier());
    
                if (playerScore > 21) {
                    gui.updateGameMessage(player.getName() + " busts! Dealer wins.");
                    player.loseBet();
                    AchievementManager.getInstance().unlock(Badge.FIRST_LOSS);
                    AudioManager.getInstance().playSoundEffect("/sounds/lose.wav");
                } else if (dealerScore > 21 || playerScore > dealerScore) {
                    String winMessage = player.getName() + " wins! (Payout: " + payout + ")";
                    gui.updateGameMessage(winMessage);
                    int originalBet = player.getCurrentBet();
                    player.winBet(payout);
                    AchievementManager.getInstance().resetDealerWinStreak();
                    AchievementManager.getInstance().unlock(Badge.FIRST_WIN);
                    if (originalBet * 2 >= 1000) {
                        AchievementManager.getInstance().unlock(Badge.BIG_WIN);
                    }
                    AudioManager.getInstance().playSoundEffect("/sounds/win.wav");
                } else if (playerScore < dealerScore) {
                    gui.updateGameMessage(player.getName() + " loses! Dealer wins.");
                    player.loseBet();
                    bettingManager.dealerWins(player.getName());
                    AchievementManager.getInstance().unlock(Badge.FIRST_LOSS);
                    AchievementManager.getInstance().trackDealerWin();
                    AudioManager.getInstance().playSoundEffect("/sounds/lose.wav");
                } else {
                    gui.updateGameMessage(player.getName() + " ties! Bets returned.");
                    player.tieBet();
                }
            }
            gui.updatePlayerPanels();
            gameOver = true;
            checkGameOver();
            SwingUtilities.invokeLater(() -> gui.updateGameState(players, dealer, true, false));
            AchievementManager am = AchievementManager.getInstance();
        }
    }

    public void resetBettingManager(int playerBalance, int dealerBalance) {
        this.bettingManager = new BettingManager(players, 1000, 1000);
    }

    private void checkGameOver() {
        for (Player player : players) {
            if (player.getBalance() <= 0) {
                gui.showGameOverMessage("Game Over! " + player.getName() + " ran out of money! 😢");
                return;
            }
        }
        if (dealer.getBalance() <= 0) {
            gui.showGameOverMessage("Congratulations! The dealer ran out of money! 🎉");
        }
    }

    public String getPlayerHand(Player player) {
        return player.getHand().toString();
    }

//    public String getDealerHand() {
//        return gameOver ? dealer.getHand().toString() : dealer.getHand().get(0) + " [Hidden]";
//    }

    /*
     * Betting system.
     */
    public boolean placeBet(Player player, int betAmount) {
        gui.updateGameState(players, dealer, gameOver, false);
        boolean placed = player.placeBet(betAmount);
        if (placed) {
            AchievementManager.getInstance().unlock(Badge.FIRST_BET);
        }
        return placed;
    }
    

    public int getPlayerBalance(Player player) {
        return player.getBalance();
    }

    public boolean canPlaceBet(Player currentPlayer) {
        return isGameOver() &&
                !isPaused &&
                currentPlayer.getBalance() > 0 &&
                getDealerBalance() > 0;
    }

    public boolean isGameRunning() {
        return !isGameOver(); // Or whatever logic determines if game is running
    }

    public boolean hasNextPlayer() {
        return currentPlayerIndex < players.size()-1;
    }

    public Player getCurrentPlayer() {
        if (currentPlayerIndex < players.size()) {
            return players.get(currentPlayerIndex);
        }
        return null; // In case there are no players or we've reached the dealer turn
    }

    public void startNewGame() {
        gameOver = false; // Mark that the game is no longer over
        isPaused = false;
        resumeGame();
        currentPlayerIndex = 0; // Start with the first player
        deck.shuffle(); // Shuffle the deck for the new game

        // Reset all players' hands and balances
        for (Player player : players) {
            player.reset();
            player.receiveCard(deck.dealCard());
            player.receiveCard(deck.dealCard());
            AchievementManager.getInstance().trackFirstBlackjack(player);
        }

        dealer.reset(); // Reset dealer's hand
        bettingManager.resetAllBets(); // Reset the bets

        //resumeGame();

        // Dealer receives one face-up and one face-down card
        dealer.receiveCard(deck.dealCard()); // Visible card

        AchievementManager.getInstance().trackMultiplayerGame(players);

        // Update GUI with the new game state
        gui.updateGameMessage("Starting a new game!");
        gui.updateGameState(players, dealer, false, false);


        // 🔍 Check if the game is over due to balance depletion
        //checkGameOver();

        SwingUtilities.invokeLater(() -> {
            gui.enableBetting();
            gui.setGameButtonsEnabled(true);
            startNextPlayerTurn(); // Start the first player's turn
        });
        return;
    }

    public boolean isCurrentPlayerStillInRound() {
        Player currentPlayer = getCurrentPlayer();
        if (currentPlayer == null) {
            return false; // If no player is available
        }
        return currentPlayer.calculateScore() <= 21; // Player is still in the round if they haven't busted
    }

    /**
     * Handles special cards when drawn.
     * //
     */

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

    /*
     * Betting system.
     */

    public boolean isGameOver() {
        return gameOver;
    }
    public int getCurrentPlayerIndex() {
        return this.currentPlayerIndex;
    }
    
    public void setCurrentPlayerIndex(int index) {
        this.currentPlayerIndex = index;
    }

    public int getDealerBalance() {
        return bettingManager.getDealerBalance();
    }

    public int getDealerBet() {
        return bettingManager.getDealerBet();
    }

    public int getDealerScore() {
        return dealer.calculateScore();
    }

    public int getPlayerScore(Player currentPlayer) {
        return currentPlayer.calculateScore();
    }

    public int getPlayerBet(Player currentPlayer) {
        return currentPlayer.getCurrentBet();
    }

    public List<Integer> getPlayerScores() {
        List<Integer> pScores = new ArrayList<>();
        for(Player p : players) {
            pScores.add(p.calculateScore());
        }
        return pScores;
    }

    public List<Integer> getPlayerBalances(){
        List<Integer> pBalances = new ArrayList<>();
        for(Player p : players) {
            pBalances.add(p.getBalance());
        }
        return pBalances;
    }

    public List<Integer> getPlayerBets(){
        List<Integer> pBets = new ArrayList<>();
        for(Player p : players) {
            pBets.add(p.getCurrentBet());
        }
        return pBets;
    }

    public List<List<Card>> getPlayerHands() {
        List<List<Card>> playerHands = new ArrayList<>();
        for(Player p : players) {
            List<Card> playerHand = p.getHand();
            playerHands.add(playerHand);
        }
        return playerHands;
    }

    public List<Card> getDealerHand(){
        List<Card> dealerHand = new ArrayList<>();
        for(Card c: dealer.getHand()) {
            dealerHand.add(c);
        }
        return dealerHand;
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



    public ArrayList<Player> getPlayers() {
        return this.players;
    }

    public void setGameOver(boolean b) {
        this.gameOver=b;
    }

    public Deck getDeck() {
        return this.deck;
    }

    public Player getDealer() {
        return this.dealer;
    }


    // SAVE/LOAD
    public void loadGame(GameState gs) throws IOException {
        applyGameState(gs);
    }

    private void applyGameState(GameState state) {
        System.out.println("Applying game state");
        this.players = new ArrayList<>(state.getPlayers());
        this.dealer = state.getDealer();
        this.deck = state.getDeck();
        this.currentPlayerIndex = determineCurrentPlayerIndex(state);
        this.gameOver = false;
        this.gui = new BlackjackGUI(this);

        // Restore each player's hand and score
        ArrayList<Player> loadedPlayers = new ArrayList<>(state.getPlayers());
        for (int i = 0; i < loadedPlayers.size(); i++) {
            Player player = loadedPlayers.get(i);
            player.setHand(state.getPlayerHands().get(i));
            player.setCurrentScore();
            player.setCurrentBet(state.getCurrentBets().get(i));
            player.setBalance(player.getBalance());
        }

        // Restore dealer's hand, score, and balance
        this.dealer.setHand(state.getDealerHand());
        this.dealer.setBalance(state.getDealerBalance());
        this.dealer.setCurrentBet(state.getDealerBet());

        // Restore deck
        //this.deck.setCards(state.getDeckCards());

        // Set betting manager
        this.bettingManager = new BettingManager(players, state.getPlayers().get(0).getBalance(), state.getDealerBalance());
        this.bettingManager.placeDealerBet(state.getDealerBet());

        // Update GUI
        SwingUtilities.invokeLater(() -> {
            gui.updateGameMessage("Game loaded successfully!");
            gui.updateGameState(players, dealer, false, false);
            gui.setGameButtonsEnabled(true);
            gui.enableBetting();
            startNextPlayerTurn();
        });

        System.out.println("Game loaded successfully!");
    }

    private int determineCurrentPlayerIndex(GameState state) {
       return state.getCurrentPlayerIndex();

    }

    public void save() throws IOException {
        // Save all the relevant data that is used in the .json files
        GameState saveState = new GameState(this);
        File saveFile = new File("main\\saveFile.json");

        // Set all the necessary data to the save state
        saveState.setDealer(this.dealer);
        saveState.setCurrentPlayerIndex(currentPlayerIndex);
        saveState.setDealerBalance(dealer.getBalance());
        saveState.setDealerBet(dealer.getCurrentBet());

        saveState.setPlayers(this.players);
        List<Integer> playerBalances = new ArrayList<>();
        List<Integer> playerScores = new ArrayList<>();
        List<Integer> playerBets = new ArrayList<>();
        for(Player p : this.players) {
            playerBalances.add(p.getBalance());
            playerScores.add(p.calculateScore());
            playerBets.add(p.getCurrentBet());
        }
        saveState.setPlayerBalances(playerBalances);
        saveState.setPlayerScores(playerScores);
        saveState.setDealerScore(dealer.calculateScore());
        saveState.setCurrentBets(playerBets);

        // Write it to a json file that can be loaded in.
        saveState.saveToFile(saveFile);
    }
}
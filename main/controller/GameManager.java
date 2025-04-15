package main.controller;

import main.model.Card;
import main.model.Deck;
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

    private GameManager() {
        this.players = new ArrayList<>();
        this.dealer = new Player("Dealer", INITIAL_BET);
        this.deck = new Deck();
        this.gameOver = false;
        this.bettingManager = new BettingManager(players, INITIAL_BET, INITIAL_BET); // Initial balance
        this.currentPlayerIndex = 0;
        players.add(new Player("PLAYER 1", INITIAL_BET)); // At least one player
        players.add(new Player("PLAYER 2", INITIAL_BET)); // At least one player
    }


    // Public method to provide access to the singleton instance
    public static GameManager getInstance() {
        if (instance == null) {
            instance = new GameManager();
        }
        return instance;
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
            gui.promptPlayerAction(players.get(currentPlayerIndex));
            if (currentPlayerIndex == 0) {
                int dealerBet = bettingManager.getDealerBalance() / 10;
                bettingManager.placeDealerBet(dealerBet);
            }
        } else {
            dealerTurn();
        }
    }

    public void handlePlayerHit() {
        if (!gameOver && !isPaused) {
            Player currentPlayer = players.get(currentPlayerIndex);
            currentPlayer.receiveCard(handleSpecialCard(deck.dealCard(), currentPlayer));
            checkPlayerBust();
            gui.updateGameState(players, dealer, gameOver, false);
        }
    }

    public void handlePlayerStand() {
        if (!gameOver) {
            currentPlayerIndex++; // Move to the next player
            startNextPlayerTurn();
        }
    }

    public void checkPlayerBust() {
        Player player = players.get(currentPlayerIndex); // Reference the current player
        if (player.calculateScore() > 21) {
            gui.updateGameMessage(player.getName() + " busts! 😢");
            player.loseBet();
            bettingManager.dealerWins(null); // Player busts, dealer wins
            currentPlayerIndex++;
            startNextPlayerTurn();
        }
    }

    public void dealerTurn() {
        while (dealer.calculateScore() < 17) {
            dealer.receiveCard(handleSpecialCard(deck.dealCard(), dealer));
        }
        checkDealerBust();
        determineWinners();
    }

    private void checkDealerBust() {
        if (dealer.calculateScore() > 21) {
            gui.updateGameMessage("Dealer busts! 🎉");
            for (Player player : players) {
                if (player.calculateScore() <= 21) {
                    player.winBet(player.getCurrentBet() * 2);
                }
            }
        }
    }

    private void determineWinners() {
        if (!gameOver) {
            int dealerScore = dealer.calculateScore();
            for (Player player : players) {
                int playerScore = player.calculateScore();

                if (playerScore > 21) {
                    gui.updateGameMessage(player.getName() + " busts! Dealer wins.");
                    player.loseBet();
                } else if (dealerScore > 21 || playerScore > dealerScore) {
                    gui.updateGameMessage(player.getName() + " wins! 🎉");
                    player.winBet(player.getCurrentBet() * 2);
                } else if (playerScore < dealerScore) {
                    gui.updateGameMessage(player.getName() + " loses! Dealer wins.");
                    player.loseBet();
                    bettingManager.dealerWins(player.getName());
                } else {
                    gui.updateGameMessage(player.getName() + " ties! Bets returned.");
                    player.tieBet();
                }

            }
            gui.updatePlayerPanels();
            gameOver = true;
            checkGameOver();
            SwingUtilities.invokeLater(() -> gui.updateGameState(players, dealer, true, false));
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
        return player.placeBet(betAmount);

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
        }

        dealer.reset(); // Reset dealer's hand
        bettingManager.resetAllBets(); // Reset the bets

        //resumeGame();

        // Dealer receives one face-up and one face-down card
        dealer.receiveCard(deck.dealCard()); // Visible card

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
    /*
     * Betting system.
     */

    public boolean isGameOver() {
        return gameOver;
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

    public int getCurrentPlayerIndex(){
        return currentPlayerIndex;
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

    /**
     * Handles special cards when drawn.
     * //
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

    // this method should apply the game state changes
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
            player.setBalance(state.getPlayerBalances().get(i));
        }

        // Restore dealer's hand, score, and balance
        this.dealer.setHand(state.getDealerHand());
        this.dealer.setBalance(state.getDealerBalance());
        this.dealer.setCurrentBet(state.getDealerBet());

        // Restore deck
        //this.deck.setCards(state.getDeckCards());

        // Set betting manager
        this.bettingManager = new BettingManager(players, state.getPlayerBalances().get(currentPlayerIndex), state.getDealerBalance());
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
        File saveFile = new File("saveFile.json");

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
        saveState.setCurrentBets(playerBets);
        saveState.setNumPlayers(this.players.size());

        // Method that filters the cards that are present in the hands and populates the deck with the rest of the cards


        // Write it to a json file that can be loaded in.
        saveState.saveToFile(saveFile);
    }
}
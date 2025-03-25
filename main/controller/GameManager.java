package main.controller;

import main.model.Card;
import main.model.Deck;
import main.model.Player;
import main.view.BlackjackGUI;
import main.view.Texts;

import static main.view.BlackJackMenu.language;

import java.util.ArrayList;

import javax.swing.SwingUtilities;

/*
 * Singleton class.
 */

/**
 * The GameManager class is responsible for managing the game state and logic.
 * It interacts with the Player, Deck, and BlackjackGUI classes to handle player actions,
 * dealer actions, and determine the game outcome.
 */
public class GameManager {
    private static GameManager instance;

    private ArrayList<Player> players;
    private Player dealer;
    private Deck deck;
    private BlackjackGUI gui;
    private boolean gameOver;
    private BettingManager bettingManager;
    private int currentPlayerIndex;

    private GameManager() {
        this.players = new ArrayList<>();
        this.dealer = new Player("Dealer", 1000);
        this.deck = new Deck();
        this.gameOver = false;
        this.bettingManager = new BettingManager(players, 1000, 1000); // Initial balance
        this.currentPlayerIndex=0;
    }

    // Public method to provide access to the singleton instance
    public static GameManager getInstance() {
        if (instance == null) {
            instance = new GameManager();
        }
        return instance;
    }

    public void setGui(BlackjackGUI gui) {
        this.gui = gui;
    }

    public void addPlayer(String name, int initialBalance) {
        players.add(new Player(name, initialBalance));
    }

    private void startNextPlayerTurn() {
        if (currentPlayerIndex < players.size()) {
            gui.promptPlayerAction(players.get(currentPlayerIndex));
        } else {
            dealerTurn();
        }
    }

    public void handlePlayerHit() {
        if (!gameOver) {
            Player currentPlayer = players.get(currentPlayerIndex);
            currentPlayer.receiveCard(handleSpecialCard(deck.dealCard(), currentPlayer));
            checkPlayerBust(currentPlayer);
            gui.updateGameState(players, dealer, gameOver);
        }
    }


    public void handlePlayerStand() {
        if (!gameOver) {
            currentPlayerIndex++; // Move to the next player
            startNextPlayerTurn();
        }
    }

    private void checkPlayerBust(Player player) {
        if (player.calculateScore() > 21) {
            gui.updateGameMessage(player.getName() + " busts! 😢");
            player.loseBet();
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
                } else {
                    gui.updateGameMessage(player.getName() + " ties! Bets returned.");
                    player.tieBet();
                }
            }

            gameOver = true;
            checkGameOver();
            SwingUtilities.invokeLater(() -> gui.updateGameState(players, dealer, true));
        }
    }

    public void resetBettingManager(int playerBalance, int dealerBalance) {
        this.bettingManager = new BettingManager(players, 1000,1000);
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

    public String getDealerHand() {
        return gameOver ? dealer.getHand().toString() : dealer.getHand().get(0) + " [Hidden]";
    }

    public void checkPlayerBust() {
        Player player = players.get(currentPlayerIndex); // Reference the current player
        if (player.calculateScore() > 21) {
            gameOver = true;
            gui.updateGameMessage(Texts.playerBusts[language]);
            bettingManager.dealerWins(null); // Player busts, dealer wins
        }
    }
    

    /*
     * Betting system.
     */
    public boolean placeBet(Player player, int betAmount) {
        return player.placeBet(betAmount);
    }

    public int getPlayerBalance(Player player) {
        return player.getBalance();
    }

    public int getDealerBalance() {
        return bettingManager.getDealerBalance();
    }

    public int getDealerBet() {
        return bettingManager.getDealerBet();
    }

    /**
     * Handles special cards when drawn.
//     */

private Card handleSpecialCard(Card card, Player recipient) {
    if (recipient != dealer) { // Special cards only affect players
        switch (card.getType()) {
            case BLACKJACK_BOMB:
                gui.updateSpecialMessage("💣 Blackjack Bomb! " + recipient.getName() + " wins instantly!");
                gameOver = true;
                break;
            case SPLIT_ACE:
                gui.updateSpecialMessage("♠ Split Ace! Your score is halved.");
                break;
            case JOKER_WILD:
                int wildValue = gui.promptJokerWildValue();
                card.setWildValue(wildValue);
                gui.updateSpecialMessage("🤡 Joker Wild! set to " + wildValue);
                break;
            default:
                break;
        }
    }
    return card;
}

    public boolean hasNextPlayer() {
        return currentPlayerIndex < players.size();
    }


    public Player getCurrentPlayer() {
        if (currentPlayerIndex < players.size()) {
            return players.get(currentPlayerIndex);
        }
        return null; // In case there are no players or we've reached the dealer turn
    }
    

    public Object startNewGame() {
        // Reset all players' hands and balances
        for (Player player : players) {
            player.reset();
        }
        dealer.reset(); // Reset dealer's hand
        deck.shuffle(); // Shuffle the deck for the new game
        bettingManager.resetAllBets(); // Reset the bets
    
        currentPlayerIndex = 0; // Start with the first player
        gameOver = false; // Mark that the game is no longer over
        
        // Deal new cards to players and dealer
        gui.updateGameMessage("Starting a new game!");
        gui.updateGameState(players, dealer, gameOver);
        
        // Start the first player's turn
        startNextPlayerTurn();
        return null; // Placeholder return type for the method signature, could be used for more logic if needed
    }
    

    public boolean isCurrentPlayerStillInRound() {
        Player currentPlayer = getCurrentPlayer();
        if (currentPlayer == null) {
            return false; // If no player is available
        }
        return currentPlayer.calculateScore() <= 21; // Player is still in the round if they haven't busted
    }

    public ArrayList<Player> getPlayers(){
        return players;
    }
    
}
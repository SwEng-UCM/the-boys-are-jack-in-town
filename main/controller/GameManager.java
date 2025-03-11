package main.controller;

import main.model.Card;
import main.model.Deck;
import main.model.Player;
import main.view.BlackjackGUI;
import main.view.Texts;

import static main.view.BlackJackMenu.language;

import javax.swing.SwingUtilities;

/**
 * The GameManager class is responsible for managing the game state and logic.
 * It interacts with the Player, Deck, and BlackjackGUI classes to handle player actions,
 * dealer actions, and determine the game outcome.
 */
public class GameManager {
    private Player player;
    private Player dealer;
    private Deck deck;
    private BlackjackGUI gui;
    private boolean gameOver;
    private BettingManager bettingManager;

    public GameManager() {
        this.player = new Player();
        this.dealer = new Player();
        this.deck = new Deck();
        this.gameOver = false;
        this.bettingManager = new BettingManager(1000, 1000); // Initial balance
    }

    public void setGui(BlackjackGUI gui) {
        this.gui = gui;
    }

    public void handlePlayerStand() {
        if (!gameOver) {
            dealerTurn();
            determineWinner();
            gui.updateGameState(player, dealer, gameOver);
        }
    }

    public void startNewGame() {
        player.reset();
        dealer.reset();
        deck = new Deck(); // Reset the deck for a new game
        gameOver = false;
        // bettingManager.resetBet(); // Reset the bet for a new game

        // Place dealer's bet (e.g., 10% of dealer's balance)
        // TODO: have a logic for dealer to place a bet
        int dealerBet = (int) (bettingManager.getDealerBalance() * 0.1);
        bettingManager.placeDealerBet(dealerBet);

        // Re-enable betting components
        gui.enableBetting();

        // Deal initial cards
        player.receiveCard(handleSpecialCard(deck.dealCard(), player));
        dealer.receiveCard(handleSpecialCard(deck.dealCard(), dealer));
        player.receiveCard(handleSpecialCard(deck.dealCard(), player));
        dealer.receiveCard(handleSpecialCard(deck.dealCard(), dealer));

        // Check for instant Blackjack win
        if (player.hasBlackjack()) {
            gui.updateGameMessage(Texts.playerBlackjack[language]);
            gameOver = true;
            gui.updateGameState(player, dealer, true);
            return;
        } else if (dealer.hasBlackjack()) {
            gui.updateGameMessage(Texts.dealerBlackjack[language]);
            gameOver = true;
            gui.updateGameState(player, dealer, true);
            return;
        }

        gui.updateGameMessage(Texts.gameManagerGameOn[language]);
        gui.updateGameState(player, dealer, false);
    }

    public String getPlayerHand() {
        return player.getHand().toString();
    }

    public String getDealerHand() {
        return gameOver ? dealer.getHand().toString() : dealer.getHand().get(0) + " [Hidden]";
    }

    private void dealerTurn() {
        while (dealer.calculateScore() < 17) {
            dealer.receiveCard(handleSpecialCard(deck.dealCard(), dealer));
        }
        checkDealerBust();
    }

    public void handlePlayerHit() {
        if (!gameOver && player.calculateScore() <= 21) {
            player.receiveCard(handleSpecialCard(deck.dealCard(), player));
            checkPlayerBust();
            gui.updateGameState(player, dealer, gameOver);
        }
    }

    public void checkPlayerBust() {
        if (player.calculateScore() > 21) {
            gameOver = true;
            gui.updateGameMessage(Texts.playerBusts[language]);
        }
    }

    private void checkDealerBust() {
        if (dealer.calculateScore() > 21) {
            gameOver = true;
            gui.updateGameMessage(Texts.dealerBusts[language]);
        }
    }

private void determineWinner() {
    if (!gameOver) {
        int playerScore = player.calculateScore();
        int dealerScore = dealer.calculateScore();

        if (playerScore > dealerScore) {
            gui.updateGameMessage(Texts.playerWins[language]);
            bettingManager.playerWins(); // Player wins, dealer loses
            System.out.println("Player wins! Player balance: " + bettingManager.getPlayerBalance() + 
                               ", Dealer balance: " + bettingManager.getDealerBalance());
        } else if (playerScore < dealerScore) {
            gui.updateGameMessage(Texts.dealerWins[language]);
            bettingManager.dealerWins(); // Dealer wins, player loses
            System.out.println("Dealer wins! Player balance: " + bettingManager.getPlayerBalance() + 
                               ", Dealer balance: " + bettingManager.getDealerBalance());
        } else {
            gui.updateGameMessage(Texts.tie[language]);
            bettingManager.tie(); // Tie, both get their bets back
            System.out.println("Tie! Player balance: " + bettingManager.getPlayerBalance() + 
                               ", Dealer balance: " + bettingManager.getDealerBalance());
        }
        gameOver = true;

        // Refresh the UI on the EDT
        SwingUtilities.invokeLater(() -> {
            gui.enableBetting();
            gui.updateGameState(player, dealer, true);
        });
    }
}

    /*
     * Betting system.
     */
    public boolean placeBet(int betAmount) {
        return bettingManager.placeBet(betAmount);
    }

    public int getPlayerBalance() {
        return bettingManager.getPlayerBalance();
    }

    public int getDealerBalance() {
        return bettingManager.getDealerBalance();
    }

    public int getDealerBet() {
        return bettingManager.getDealerBet();
    }

    /**
     * Handles special cards when drawn.
     */
    private Card handleSpecialCard(Card card, Player recipient) {
        switch (card.getType()) {
            case BLACKJACK_BOMB:
                if (recipient == player) {
                    gui.updateGameMessage("Blackjack Bomb! Player wins instantly! 💣");
                } else {
                    gui.updateGameMessage("Blackjack Bomb! Dealer wins instantly! 💣");
                }
                gameOver = true;
                break;
            case SPLIT_ACE:
                gui.updateGameMessage("Split Ace! Automatically splitting your hand! ♠♠");
                splitHand(recipient);
                break;
            case JOKER_WILD:
                if (recipient == player) {
                    int wildValue = gui.promptJokerWildValue();  // Only prompt the player
                    card.setWildValue(wildValue);
                    gui.updateGameMessage("Joker Wild set to " + wildValue + " 🤡");
                }
                break;
            default:
                // Standard cards, no special handling needed
                break;
        }
        return card;
    }

    private void splitHand(Player player) {
        // Logic to split the player's hand (assuming it’s implemented in Player class)
    }
}
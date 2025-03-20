package main.controller;

import main.model.Card;
import main.model.Deck;
import main.model.Player;
import main.view.BlackjackGUI;
import main.view.Texts;

import static main.view.BlackJackMenu.language;

/*
 * Singleton class !
 */

/*
 * Singleton class !
 */

/*
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

    public GameManager() {
        this.player = new Player();
        this.dealer = new Player();
        this.deck = new Deck();
        this.gameOver = false;
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
        deck = new Deck();
        gameOver = false;

        gui.resetSpecialMessage(); // Reset special card messages

        player.receiveCard(handleSpecialCard(deck.dealCard(), player));
        dealer.receiveCard(deck.dealCard()); // Dealer's cards don't trigger special messages
        player.receiveCard(handleSpecialCard(deck.dealCard(), player));
        dealer.receiveCard(deck.dealCard());

        if (player.hasBlackjack()) {
            gui.updateGameMessage(Texts.playerBlackjack[language]);
            gameOver = true;
        } else if (dealer.hasBlackjack()) {
            gui.updateGameMessage(Texts.dealerBlackjack[language]);
            gameOver = true;
        } else {
            gui.updateGameMessage(Texts.gameManagerGameOn[language]);
        }

        gui.updateGameState(player, dealer, gameOver);
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
            } else if (playerScore < dealerScore) {
                gui.updateGameMessage(Texts.dealerWins[language]);
            } else {
                gui.updateGameMessage(Texts.tie[language]);
            }
            gameOver = true;
        }
    }

    /**
     * Handles special cards when drawn.
//     */

private Card handleSpecialCard(Card card, Player recipient) {
    if (recipient == player) { // Display special messages only when the player draws them
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

    private void splitHand(Player player) {
        // Logic to split the player's hand (assuming it’s implemented in Player class)
    }
}
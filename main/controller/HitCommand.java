package main.controller;

import main.model.Player;
import main.model.Card;

/**
 * {@code HitCommand} is a command representing the "hit" action in Blackjack,
 * where a player draws a card from the deck. It encapsulates the action and
 * enables undo functionality by remembering the drawn card.
 * 
 */
public class HitCommand implements Command {
    private Player player;
    private Card card;
    private GameManager gameManager;

    /**
     * Constructs a {@code HitCommand} for a specific player and game manager.
     *
     * @param player the player who is drawing a card
     * @param gameManager the {@link GameManager} managing the game state
     */
    public HitCommand(Player player, GameManager gameManager) {
        this.player = player;
        this.gameManager = gameManager;
    }

    /**
     * Executes the hit action by drawing a card for the player
     * using the game manager and storing the drawn card for undoing later.
     */
    public void execute() {
            this.card = gameManager.hit(player);
    }

    /**
     * Undoes the hit action by removing the last drawn card from the player's hand.
     */
    public void undo() {
        gameManager.undoHit(player, card);
    }
}

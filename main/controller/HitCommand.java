package main.controller;

import main.model.Player;
import main.model.Card;

public class HitCommand implements Command {
    private Player player;
    private Card card;
    private GameManager gameManager;

    public HitCommand(Player player, GameManager gameManager) {
        this.player = player;
        this.gameManager = gameManager;
    }

    public void execute() {
        this.card = gameManager.hit(player);
    }

public void undo() {
    gameManager.undoHit(player, card);
}
}

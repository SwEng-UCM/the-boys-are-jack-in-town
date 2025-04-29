package main.controller;

import main.model.Player;
import main.controller.GameManager;

public class StandCommand implements Command {
    private GameManager gameManager;
    private int previousIndex;

    public StandCommand(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @Override
    public void execute() {
        previousIndex = gameManager.getCurrentPlayerIndex();
        gameManager.handlePlayerStand();
    }

    @Override
    public void undo() {
        gameManager.setCurrentPlayerIndex(previousIndex);
    }
}

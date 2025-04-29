package main.controller;

/**
 * {@code StandCommand} represents a command in the Command pattern that handles
 * the logic for when a player chooses to stand during their turn in Blackjack.
 * 
 * It is part of the Controller layer in MVC and interacts with the {@link GameManager}
 * to trigger and optionally undo the stand action.
 */
public class StandCommand implements Command {
    private GameManager gameManager;
    private int previousIndex;

    /**
     * Constructs a {@code StandCommand} with a reference to the game manager.
     *
     * @param gameManager the {@link GameManager} instance controlling game state
     */
    public StandCommand(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    /**
     * Executes the stand action by recording the current player index
     * and invoking the game manager's logic to handle a player's decision to stand.
     */
    @Override
    public void execute() {
        previousIndex = gameManager.getPlayerManager().getCurrentPlayerIndex();
        gameManager.handlePlayerStand();
    }

    /**
     * Undoes the stand action by restoring the player index to its previous value.
     * This allows for reversible actions if supported by the game logic.
     */
    @Override
    public void undo() {
        gameManager.getPlayerManager().setCurrentPlayerIndex(previousIndex);
    }
}

package main.controller;

import java.util.HashSet;
import java.util.Set;
import main.model.Badge;
import main.model.Player;
import main.view.AchievementsWindow;

/**
 * The {@code BetCommand} class represents a player's bet action in the Blackjack game.
 * It implements the {@link Command} interface, allowing the action to be executed and undone,
 * while also handling associated achievement unlocking logic.
 */
public class BetCommand implements Command {
    private final Player player;
    private final int amount;
    private final GameManager gameManager;
    private final Set<Badge> unlocked = new HashSet<>();

    /**
     * Constructs a new {@code BetCommand}.
     *
     * @param player the player placing the bet
     * @param amount the bet amount
     * @param gameManager the central game logic manager
     */
    public BetCommand(Player player, int amount, GameManager gameManager) {
        this.player = player;
        this.amount = amount;
        this.gameManager = gameManager;
    }

    /**
     * Executes the bet command by placing a bet for the player through the {@code GameManager}.
     * If this is the player's first bet, the {@code FIRST_BET} achievement is unlocked.
     */
    @Override
    public void execute() {
        boolean success = gameManager.placeBet(player, amount);
        if (success && !AchievementManager.getInstance().isUnlocked(Badge.FIRST_BET)) {
            AchievementManager.getInstance().unlock(Badge.FIRST_BET);
            unlocked.add(Badge.FIRST_BET);
        }
    }

    /**
     * Undoes the bet by refunding the bet amount and revoking any achievements unlocked during this command.
     * Also refreshes the achievements window if it's visible.
     */
    @Override
    public void undo() {
        gameManager.undoBet(player, amount);
        for (Badge badge : unlocked) {
            AchievementManager.getInstance().revoke(badge);
        }
        
        if (AchievementsWindow.getInstance().isVisible()) {
            AchievementsWindow.getInstance().refreshBadges();
        }
    }
}

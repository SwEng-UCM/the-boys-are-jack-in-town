package main.controller;

import java.util.HashSet;
import java.util.Set;
import main.model.Badge;
import main.model.Player;
import main.view.AchievementsWindow;

public class BetCommand implements Command {
    private final Player player;
    private final int amount;
    private final GameManager gameManager;
    private final Set<Badge> unlocked = new HashSet<>();

    public BetCommand(Player player, int amount, GameManager gameManager) {
        this.player = player;
        this.amount = amount;
        this.gameManager = gameManager;
    }

    @Override
    public void execute() {
        boolean success = gameManager.placeBet(player, amount);
        if (success && !AchievementManager.getInstance().isUnlocked(Badge.FIRST_BET)) {
            AchievementManager.getInstance().unlock(Badge.FIRST_BET);
            unlocked.add(Badge.FIRST_BET);
        }
    }

    @Override
    public void undo() {
        gameManager.undoBet(player, amount);
        for (Badge badge : unlocked) {
            AchievementManager.getInstance().revoke(badge);
        }
        
        if (AchievementsWindow.getInstance().isVisible()) {
            AchievementsWindow.getInstance().refreshBadges();
        }    }
}

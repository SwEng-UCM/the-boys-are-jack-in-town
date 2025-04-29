package main.controller;

import java.util.*;
import main.model.Badge;
import main.model.Player;

/**
 * The AchievementManager class is responsible for tracking and managing
 * achievements, badges, and player statistics in the game.
 * It follows the singleton pattern to ensure a single instance across the application.
 */
public class AchievementManager {
    private static AchievementManager instance;
    private final Map<String, Integer> dealerStats = new HashMap<>();
    private final Map<Player, Map<String, Integer>> playerStats = new HashMap<>();
    private final Set<Badge> unlockedBadges = EnumSet.noneOf(Badge.class);
    private int dealerWinStreak = 0;
    private AchievementManager() {}

    /**
     * Returns the singleton instance of AchievementManager.
     * If the instance does not exist, it initializes it.
     *
     * @return the singleton instance of AchievementManager
     */
    public static AchievementManager getInstance() {
        if (instance == null) {
        instance = new AchievementManager();
        }
        return instance;
    }

    /**
     * Tracks a win by the dealer and increments the dealer's win streak.
     * Unlocks the DEALER_STREAK badge if the dealer wins three times consecutively.
     */
    public void trackDealerWin() {
        dealerWinStreak++;
        if (dealerWinStreak == 3) {
            unlock(Badge.DEALER_STREAK);
        }
    }

    /**
     * Resets the dealer's win streak counter.
     */
    public void resetDealerWinStreak() {
        dealerWinStreak = 0;
    }

    /**
     * Tracks a player's win, updating their win count.
     * Unlocks the {@link Badge#FIRST_WIN} for the first win,
     * and {@link Badge#FIVE_WINS} after five wins.
     *
     * @param player the player whose win is being tracked
     */
    public void trackPlayerWin(Player player) {
        updatePlayerStat(player, "wins", 1);
    
        int wins = getPlayerStat(player, "wins");
        if (wins == 1) {
            unlock(Badge.FIRST_WIN);
        }
        if (wins == 5) {
            unlock(Badge.FIVE_WINS);
        }
    }
    
    /**
     * Tracks a multiplayer game session.
     * Unlocks the {@link Badge#MULTIPLAYER} if four or more players are involved.
     *
     * @param players the list of players in the game session
     */
    public void trackMultiplayerGame(List<Player> players) {
        if (players.size() >= 4) {
            unlock(Badge.MULTIPLAYER);
        }
    }

    /**
     * Tracks a big win by a player.
     * Unlocks the {@link Badge#BIG_WIN} if the win amount is 1000 or more.
     *
     * @param player the player who achieved the big win
     * @param winAmount the amount won
     */
    public void trackBigWin(Player player, int winAmount) {
        if (winAmount >= 1000) {
            unlock(Badge.BIG_WIN);
        }
    }

    /**
     * Updates a specific statistic for a player.
     *
     * @param player the player whose statistic is being updated
     * @param key the statistic key (e.g., "wins", "bets")
     * @param increment the amount to increment the statistic by
     */
    private void updatePlayerStat(Player player, String key, int increment) {
        playerStats.putIfAbsent(player, new HashMap<>());
        Map<String, Integer> stats = playerStats.get(player);
        stats.put(key, stats.getOrDefault(key, 0) + increment);
    }

    /**
     * Retrieves a specific statistic for a player.
     *
     * @param player the player whose statistic is being retrieved
     * @param key the statistic key (e.g., "wins", "bets")
     * @return the value of the statistic, or 0 if not found
     */
    private int getPlayerStat(Player player, String key) {
        return playerStats.getOrDefault(player, Collections.emptyMap()).getOrDefault(key, 0);
    }

    /**
     * Tracks a player's first bet.
     * Unlocks the {@link Badge#FIRST_BET} if this is their first bet.
     *
     * @param player the player whose bet is being tracked
     */
    public void trackFirstBet(Player player) {
        updatePlayerStat(player, "bets", 1);
        if (getPlayerStat(player, "bets") == 1) {
            unlock(Badge.FIRST_BET);
        }
    }
    
    /**
     * Tracks a player's first loss.
     * Unlocks the {@link Badge#FIRST_LOSS} if this is their first loss.
     *
     * @param player the player whose loss is being tracked
     */
    public void trackFirstLoss(Player player) {
        updatePlayerStat(player, "losses", 1);
        if (getPlayerStat(player, "losses") == 1) {
            unlock(Badge.FIRST_LOSS);
        }
    }
    
    /**
     * Tracks a player's first blackjack (21 points with two cards).
     * Unlocks the {@link Badge#FIRST_BLACKJACK} if the player hits blackjack.
     *
     * @param player the player whose blackjack is being tracked
     */
    public void trackFirstBlackjack(Player player) {
        if (player.getHand().size() == 2 && player.calculateScore() == 21) {
            unlock(Badge.FIRST_BLACKJACK);        }
    }    

    /**
     * Unlocks a badge if it hasn't been unlocked already.
     *
     * @param badge the badge to unlock
     */
    public void unlock(Badge badge) {
        unlockedBadges.add(badge);
    }

    /**
     * Checks if a badge has already been unlocked.
     *
     * @param badge the badge to check
     * @return true if the badge is unlocked, false otherwise
     */
    public boolean isUnlocked(Badge badge) {
        return unlockedBadges.contains(badge);
    }

    /**
     * Retrieves the set of all unlocked badges.
     *
     * @return an unmodifiable set of unlocked badges
     */
    public Set<Badge> getUnlockedBadges() {
        return Collections.unmodifiableSet(unlockedBadges);
    }

    public void revoke(Badge badge) {
        unlockedBadges.remove(badge);
    }
    
}

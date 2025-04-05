package main.controller;

import main.model.Player;

import java.util.*;

public class AchievementManager {
    private static AchievementManager instance;
    private final Map<String, Integer> dealerStats = new HashMap<>();
    private final Map<Player, Map<String, Integer>> playerStats = new HashMap<>();

    private final List<String> unlockedAchievements = new ArrayList<>();
    private int dealerWinStreak = 0;

    private AchievementManager() {}

    public static AchievementManager getInstance() {
        if (instance == null) {
            instance = new AchievementManager();
        }
        return instance;
    }

    public void trackDealerWin() {
        dealerWinStreak++;
        if (dealerWinStreak == 3) {
            unlock("Dealer: 3-Win Streak");
        }
    }

    public void resetDealerWinStreak() {
        dealerWinStreak = 0;
    }

    public void trackPlayerWin(Player player) {
        updatePlayerStat(player, "wins", 1);
    
        int wins = getPlayerStat(player, "wins");
        if (wins == 1) {
            unlock(player.getName() + ": First Win!");
        }
        if (wins == 5) {
            unlock(player.getName() + ": 5 Wins");
        }
    }
    

    public void trackMultiplayerGame(List<Player> players) {
        if (players.size() >= 4) {
            unlock("Multiplayer Madness: 4+ Players!");
        }
    }

    public void trackBigWin(Player player, int winAmount) {
        if (winAmount >= 1000) {
            unlock(player.getName() + ": Big Winner");
        }
    }

    private void updatePlayerStat(Player player, String key, int increment) {
        playerStats.putIfAbsent(player, new HashMap<>());
        Map<String, Integer> stats = playerStats.get(player);
        stats.put(key, stats.getOrDefault(key, 0) + increment);
    }

    private int getPlayerStat(Player player, String key) {
        return playerStats.getOrDefault(player, Collections.emptyMap()).getOrDefault(key, 0);
    }

    private void unlock(String achievement) {
        if (!unlockedAchievements.contains(achievement)) {
            unlockedAchievements.add(achievement);
            System.out.println("Achievement Unlocked: " + achievement);
        }
    }

    public void trackFirstBet(Player player) {
        updatePlayerStat(player, "bets", 1);
        if (getPlayerStat(player, "bets") == 1) {
            unlock(player.getName() + ": First Bet!");
        }
    }
    
    public void trackFirstLoss(Player player) {
        updatePlayerStat(player, "losses", 1);
        if (getPlayerStat(player, "losses") == 1) {
            unlock(player.getName() + ": First Loss");
        }
    }
    
    public void trackFirstBlackjack(Player player) {
        if (player.getHand().size() == 2 && player.calculateScore() == 21) {
            unlock(player.getName() + ": First Blackjack!");
        }
    }    

    public List<String> getUnlockedAchievements() {
        return unlockedAchievements;
    }
}

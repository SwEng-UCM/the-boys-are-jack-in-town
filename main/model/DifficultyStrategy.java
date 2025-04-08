package main.model;

import main.model.Player;

public interface DifficultyStrategy {
    boolean shouldDealerHit(Player dealer, Player player); // Core AI decision
    double getPayoutMultiplier(); // Reward scaling
    String getDifficultyName(); // For UI display
}
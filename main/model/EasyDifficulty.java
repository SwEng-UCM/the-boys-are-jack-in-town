// EasyDifficulty.java
package main.model;

public class EasyDifficulty implements DifficultyStrategy {
    @Override
    public boolean shouldDealerHit(Player dealer, Player player) {
        return dealer.calculateScore() <= 16; // Basic casino rules
    }

    @Override
    public double getPayoutMultiplier() {
        return 1.5; // Lower rewards
    }

    @Override
    public String getDifficultyName() {
        return "Easy";
    }
}
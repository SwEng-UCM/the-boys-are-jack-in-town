// HardDifficulty.java
package main.model;

public class HardDifficulty implements DifficultyStrategy {
    @Override
    public boolean shouldDealerHit(Player dealer, Player player) {
        // Aggressive when losing, conservative when winning
        if (dealer.calculateScore() < player.calculateScore()) {
            return dealer.calculateScore() <= 18; // Riskier hits
        }
        return dealer.calculateScore() <= 16; // Standard when ahead
    }

    @Override
    public double getPayoutMultiplier() {
        return 2.5; // Higher rewards for beating tough AI
    }

    @Override
    public String getDifficultyName() {
        return "Hard";
    }
}
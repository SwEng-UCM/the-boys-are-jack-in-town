// MediumDifficulty.java
package main.model;

public class MediumDifficulty implements DifficultyStrategy {
    @Override
    public boolean shouldDealerHit(Player dealer, Player player) {
        // Hits on soft 17 (Ace + 6)
        return dealer.calculateScore() <= 16 || 
              (dealer.calculateScore() == 17 && dealer.hasAce());
    }

    @Override
    public double getPayoutMultiplier() {
        return 2.0; // Standard payout
    }

    @Override
    public String getDifficultyName() {
        return "Medium";
    }
}
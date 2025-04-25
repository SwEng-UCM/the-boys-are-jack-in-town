package main.model;

/**
 * The {@code HardDifficulty} class implements the {@link DifficultyStrategy} interface
 * to define the dealer's behavior and payout rules for the "Hard" difficulty level
 * in the Blackjack game.
 *
 * In this mode, the dealer plays more aggressively when behind (hitting on 18 or below)
 * and conservatively when ahead (hitting on 16 or below). Players receive higher payouts
 * for winning against this tougher AI.
 */
public class HardDifficulty implements DifficultyStrategy {
    
    /**
     * Determines whether the dealer should hit (draw another card) in the "Hard" difficulty.
     * The dealer takes more risks when their score is lower than the player's score
     * (hits on 18 or below) and plays conservatively when ahead (hits on 16 or below).
     *
     * @param dealer the dealer player instance
     * @param player the opposing player instance (used to compare scores)
     * @return {@code true} if the dealer should hit based on the strategy, {@code false} otherwise
     */
    @Override
    public boolean shouldDealerHit(Player dealer, Player player) {
        // Aggressive when losing, conservative when winning
        if (dealer.calculateScore() < player.calculateScore()) {
            return dealer.calculateScore() <= 18; // Riskier hits
        }
        return dealer.calculateScore() <= 16; // Standard when ahead
    }

    /**
     * Returns the payout multiplier for this difficulty level.
     * Players receive 2.5x their bet for winning hands against this tougher AI.
     *
     * @return the payout multiplier (2.5 for Hard difficulty)
     */
    @Override
    public double getPayoutMultiplier() {
        return 2.5; // Higher rewards for beating tough AI
    }

    /**
     * Returns the name of this difficulty level for UI display purposes.
     *
     * @return the string "Hard"
     */
    @Override
    public String getDifficultyName() {
        return "Hard";
    }
}
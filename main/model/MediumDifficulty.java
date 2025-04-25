package main.model;

/**
 * The {@code MediumDifficulty} class implements the {@link DifficultyStrategy} interface
 * to define the dealer's behavior and payout rules for the "Medium" difficulty level
 * in the Blackjack game.
 *
 * In this mode, the dealer hits on 16 or below and also on soft 17 (a hand containing an Ace and a 6),
 * making the dealer slightly more challenging than in the Easy mode.
 * The payout multiplier for this difficulty is standard (2.0x).
 */
public class MediumDifficulty implements DifficultyStrategy {
    
    /**
     * Determines whether the dealer should hit (draw another card) in the "Medium" difficulty.
     * The dealer hits on 16 or below, and also hits on soft 17 (a hand containing an Ace and totaling 17).
     *
     * @param dealer the dealer player instance
     * @param player the opposing player instance (not used in this difficulty)
     * @return {@code true} if the dealer should hit based on the strategy, {@code false} otherwise
     */
    @Override
    public boolean shouldDealerHit(Player dealer, Player player) {
        // Hits on soft 17 (Ace + 6)
        return dealer.calculateScore() <= 16 || 
              (dealer.calculateScore() == 17 && dealer.hasAce());
    }

    /**
     * Returns the payout multiplier for this difficulty level.
     * Players receive 2.0x their bet for winning hands against this dealer.
     *
     * @return the payout multiplier (2.0 for Medium difficulty)
     */
    @Override
    public double getPayoutMultiplier() {
        return 2.0; // Standard payout
    }

    /**
     * Returns the name of this difficulty level for UI display purposes.
     *
     * @return the string "Medium"
     */
    @Override
    public String getDifficultyName() {
        return "Medium";
    }
}
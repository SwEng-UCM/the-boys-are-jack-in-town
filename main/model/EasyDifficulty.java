package main.model;

/**
 * The {@code EasyDifficulty} class implements the {@link DifficultyStrategy} interface
 * to define the dealer's behavior and payout rules for the "Easy" difficulty level
 * in the Blackjack game.
 *
 * In this mode, the dealer plays conservatively (hits on 16 or less),
 * and the payout multiplier is slightly reduced to reflect the easier gameplay.
 */
public class EasyDifficulty implements DifficultyStrategy {
    
    /**
     * Determines whether the dealer should hit (draw another card) in the "Easy" difficulty.
     * The dealer hits if their score is 16 or below, following basic casino rules.
     *
     * @param dealer the dealer player instance
     * @param player the opposing player instance (not used in this difficulty)
     * @return {@code true} if the dealer's score is 16 or less, {@code false} otherwise
     */
    @Override
    public boolean shouldDealerHit(Player dealer, Player player) {
        return dealer.calculateScore() <= 16;
    }

    /**
     * Returns the payout multiplier for this difficulty level.
     * Players receive 1.5x their bet for winning hands.
     *
     * @return the payout multiplier (1.5 for Easy difficulty)
     */
    @Override
    public double getPayoutMultiplier() {
        return 1.5;
    }

    /**
     * Returns the name of this difficulty level for UI display purposes.
     *
     * @return the string "Easy"
     */
    @Override
    public String getDifficultyName() {
        return "Easy";
    }
}
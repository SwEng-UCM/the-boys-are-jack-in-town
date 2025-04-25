package main.model;

/**
 * The DifficultyStrategy interface defines the strategy pattern for different
 * difficulty levels in the Blackjack game. It provides methods that determine
 * dealer behavior, payout multipliers, and difficulty descriptions.
 *
 * Implementations of this interface encapsulate the specific logic for dealer decision-making
 * and payout scaling based on the chosen difficulty (e.g., Easy, Medium, Hard).
 */
public interface DifficultyStrategy {
    boolean shouldDealerHit(Player dealer, Player player); // Core AI decision
    double getPayoutMultiplier();
    String getDifficultyName();
}
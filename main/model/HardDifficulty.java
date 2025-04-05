package main.model;

public class HardDifficulty implements DifficultyStrategy {
    @Override public int decideDealerAction(Dealer dealer, Player player) {
        if (dealer.getHandValue() < player.getHandValue() || dealer.getHandValue() <= 17) 
            return GameAction.HIT;
        return GameAction.STAND;
    }
    @Override public double getBettingOdds() { return 2.5; }
    @Override public String getDifficultyName() { return "Hard"; }
}

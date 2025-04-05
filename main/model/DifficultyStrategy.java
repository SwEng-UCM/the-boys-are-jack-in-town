package main.model;

public interface DifficultyStrategy {
    int decideDealerAction(Dealer dealer, Player player);
    double getBettingOdds();
    String getDifficultyName();
}

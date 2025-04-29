package main.model;

import main.view.Texts;

import static main.view.BlackJackMenu.language;

public enum Badge {
    FIRST_WIN("First Win!", "resources/img/badges/first_win.png", "resources/img/badges/first_win_grey.png"),
    FIRST_LOSS("First Loss", "resources/img/badges/first_loss.png", "resources/img/badges/first_loss_grey.png"),
    FIRST_BET("First Bet!", "resources/img/badges/first_bet.png", "resources/img/badges/first_bet_grey.png"),
    FIRST_BLACKJACK("First Blackjack!", "resources/img/badges/first_blackjack.png", "resources/img/badges/first_blackjack_grey.png"),
    FIVE_WINS("5 Wins", "resources/img/badges/five_wins.png", "resources/img/badges/five_wins_grey.png"),
    BIG_WIN("Big Winner", "resources/img/badges/big_win.png", "resources/img/badges/big_win_grey.png"),
    DEALER_STREAK("Dealer: 3-Win Streak", "resources/img/badges/dealer_streak.png", "resources/img/badges/dealer_streak_grey.png"),
    MULTIPLAYER("Multiplayer Madness", "resources/img/badges/multiplayer.png", "resources/img/badges/multiplayer_grey.png");

    public final String title;
    public final String coloredPath;
    public final String greyPath;
    private String textKey;

    Badge(String title, String coloredPath, String greyPath) {
        this.title = title;
        this.textKey = title;
        this.coloredPath = coloredPath;
        this.greyPath = greyPath;
    }

    public String getTitle() {

        switch (textKey) {
            case "firstWin": return Texts.firstWin[language];
            case "firstLoss": return Texts.firstLoss[language];
            case "firstBet": return Texts.firstBet[language];
            case "firstBlackjack": return Texts.firstBlackjack[language];
            case "fiveWins": return Texts.fiveWins[language];
            case "bigWin": return Texts.bigWin[language];
            case "dealerStreak": return Texts.dealerStreak[language];
            case "multiplayerAchievement": return Texts.multiplayerAchievement[language];
            default: return "Unknown";
        }
    }

}

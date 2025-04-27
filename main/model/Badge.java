package main.model;

import main.view.Texts;

import static main.view.BlackJackMenu.language;

public enum Badge {
    FIRST_WIN("firstWin", "img/badges/first_win.png", "img/badges/first_win_grey.png"),
    FIRST_LOSS("firstLoss", "img/badges/first_loss.png", "img/badges/first_loss_grey.png"),
    FIRST_BET("firstBet", "img/badges/first_bet.png", "img/badges/first_bet_grey.png"),
    FIRST_BLACKJACK("firstBlackjack", "img/badges/first_blackjack.png", "img/badges/first_blackjack_grey.png"),
    FIVE_WINS("fiveWins", "img/badges/five_wins.png", "img/badges/five_wins_grey.png"),
    BIG_WIN("bigWin", "img/badges/big_win.png", "img/badges/big_win_grey.png"),
    DEALER_STREAK("dealerStreak", "img/badges/dealer_streak.png", "img/badges/dealer_streak_grey.png"),
    MULTIPLAYER("multiplayerAchievement", "img/badges/multiplayer.png", "img/badges/multiplayer_grey.png");

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

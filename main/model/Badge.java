package main.model;

import main.view.Texts;
import static main.view.BlackJackMenu.language;

/**
 * The Badge enum represents various achievements that players can unlock during the game.
 * Each badge has a title, a path to its colored image, and a path to its greyed-out image.
 * The badges are used to track player progress and accomplishments.
 */
public enum Badge {
    FIRST_WIN("firstWin", "resources/img/badges/first_win.png", "resources/img/badges/first_win_grey.png"),
    FIRST_LOSS("firstLoss", "resources/img/badges/first_loss.png", "resources/img/badges/first_loss_grey.png"),
    FIRST_BET("firstBet", "resources/img/badges/first_bet.png", "resources/img/badges/first_bet_grey.png"),
    FIRST_BLACKJACK("firstBlackjack", "resources/img/badges/first_blackjack.png", "resources/img/badges/first_blackjack_grey.png"),
    FIVE_WINS("fiveWins", "resources/img/badges/five_wins.png", "resources/img/badges/five_wins_grey.png"),
    BIG_WIN("bigWin", "resources/img/badges/big_win.png", "resources/img/badges/big_win_grey.png"),
    DEALER_STREAK("dealerStreak", "resources/img/badges/dealer_streak.png", "resources/img/badges/dealer_streak_grey.png"),
    MULTIPLAYER("multiplayerAchievement", "resources/img/badges/multiplayer.png", "resources/img/badges/multiplayer_grey.png");

    public final String title;
    public final String coloredPath;
    public final String greyPath;
    private String textKey;

    /**
     * Constructs a Badge with the specified title, colored image path, and greyed-out image path.
     *
     * @param title The title of the badge.
     * @param coloredPath The file path to the colored version of the badge image.
     * @param greyPath The file path to the greyed-out version of the badge image.
     */
    Badge(String title, String coloredPath, String greyPath) {
        this.title = title;
        this.textKey = title;
        this.coloredPath = coloredPath;
        this.greyPath = greyPath;
    }

    /**
     * Retrieves the localized title of the badge based on the current language setting.
     *
     * @return The localized title of the badge.
     */
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

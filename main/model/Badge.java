package main.model;

/**
 * Enum representing all possible achievement badges in the game.
 * Each badge has a title, a colored icon path, and a greyed-out (locked) version icon path.
 * 
 * This enum is part of the Model layer in MVC and serves as a static definition
 * of achievement types that can be unlocked and displayed to the player.
 */
public enum Badge {
    FIRST_WIN("First Win!", "img/badges/first_win.png", "img/badges/first_win_grey.png"),
    FIRST_LOSS("First Loss", "img/badges/first_loss.png", "img/badges/first_loss_grey.png"),
    FIRST_BET("First Bet!", "img/badges/first_bet.png", "img/badges/first_bet_grey.png"),
    FIRST_BLACKJACK("First Blackjack!", "img/badges/first_blackjack.png", "img/badges/first_blackjack_grey.png"),
    FIVE_WINS("5 Wins", "img/badges/five_wins.png", "img/badges/five_wins_grey.png"),
    BIG_WIN("Big Winner", "img/badges/big_win.png", "img/badges/big_win_grey.png"),
    DEALER_STREAK("Dealer: 3-Win Streak", "img/badges/dealer_streak.png", "img/badges/dealer_streak_grey.png"),
    MULTIPLAYER("Multiplayer Madness", "img/badges/multiplayer.png", "img/badges/multiplayer_grey.png");

    public final String title;
    public final String coloredPath;
    public final String greyPath;

    /**
     * Constructs a Badge enum constant with title and image paths.
     *
     * @param title the display title of the badge
     * @param coloredPath the file path to the unlocked badge icon
     * @param greyPath the file path to the locked (grey) badge icon
     */
    Badge(String title, String coloredPath, String greyPath) {
        this.title = title;
        this.coloredPath = coloredPath;
        this.greyPath = greyPath;
    }
}

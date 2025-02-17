package main.controller;

/**
 * Represents the possible actions a player can take in the blackjack game.
 */
public enum GameAction {
    /** Start a new game. */
    NEW_GAME,
    /** Request another card. */
    HIT,
    /** Keep current cards and let dealer play. */
    STAND,
    /** Exit the game. */
    QUIT,
    /** No action or invalid input. */
    NONE
}
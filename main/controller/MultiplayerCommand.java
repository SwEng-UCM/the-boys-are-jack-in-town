package main.controller;

import java.io.Serializable;

/**
 * Represents a command sent by a client in a multiplayer Blackjack game.
 * <p>
 * Each command includes a type (such as JOIN, BET, HIT, etc.), the name of the player
 * who issued it, and optional data associated with the command.
 */
public class MultiplayerCommand implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Types of multiplayer commands.
     */
    public enum Type {
        JOIN,   // When a player joins the game
        BET,    // Place a bet
        HIT,    // Request another card
        STAND,  // End the player's turn
        QUIT    // Player leaves the game
    }

    private final Type type;
    private final String playerName;
    private final Object data;

    /**
     * Constructs a new {@code MultiplayerCommand} with the specified type, player name, and optional data.
     *
     * @param type        the type of command
     * @param playerName  the name of the player issuing the command
     * @param data        additional data associated with the command (may be {@code null})
     */
    public MultiplayerCommand(Type type, String playerName, Object data) {
        this.type = type;
        this.playerName = playerName;
        this.data = data;
    }

    /**
     * Creates a {@code JOIN} command for a new player.
     *
     * @param playerName the name of the player joining
     * @return a new {@code MultiplayerCommand} of type {@code JOIN}
     */
    public static MultiplayerCommand join(String playerName) {
        return new MultiplayerCommand(Type.JOIN, playerName, null);
    }

    /**
     * Creates a {@code BET} command with the specified amount.
     *
     * @param playerName the name of the player placing the bet
     * @param amount     the amount being bet
     * @return a new {@code MultiplayerCommand} of type {@code BET}
     */
    public static MultiplayerCommand bet(String playerName, int amount) {
        return new MultiplayerCommand(Type.BET, playerName, amount);
    }

    /**
     * Creates a {@code HIT} or {@code STAND} command.
     *
     * @param type        the type of action ({@code HIT} or {@code STAND})
     * @param playerName  the name of the player performing the action
     * @return a new {@code MultiplayerCommand} of the given type
     * @throws IllegalArgumentException if the command type is not {@code HIT} or {@code STAND}
     */
    public static MultiplayerCommand action(Type type, String playerName) {
        if (!type.equals(Type.HIT) && !type.equals(Type.STAND)) {
            throw new IllegalArgumentException("Invalid action type");
        }
        return new MultiplayerCommand(type, playerName, null);
    }

    /**
     * Returns the type of this command.
     *
     * @return the command type
     */
    public Type getType() {
        return type;
    }

    /**
     * Returns the name of the player who issued this command.
     *
     * @return the player's name
     */
    public String getPlayerName() {
        return playerName;
    }

    /**
     * Retrieves the associated data, cast to the expected type.
     *
     * @param dataType the expected class type of the data
     * @param <T>      the type of the data
     * @return the command data cast to the specified type
     * @throws ClassCastException if the data cannot be cast to the specified type
     */
    public <T> T getData(Class<T> dataType) {
        return dataType.cast(data);
    }

    /**
     * Returns a string representation of the command for debugging purposes.
     *
     * @return a string describing the command
     */
    @Override
    public String toString() {
        return "MultiplayerCommand{" +
                "type=" + type +
                ", player='" + playerName + '\'' +
                ", data=" + data +
                '}';
    }
}

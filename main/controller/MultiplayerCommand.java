package main.controller;

import java.io.Serializable;

public class MultiplayerCommand implements Serializable {
    public enum Type {
        JOIN,       // When a player joins
        BET,        // Place a bet
        HIT,        // Request a card
        STAND,      // End turn
        QUIT        // Leave game
    }

    private final Type type;
    private final String playerName;
    private final Object data;

    public MultiplayerCommand(Type type, String playerName, Object data) {
        this.type = type;
        this.playerName = playerName;
        this.data = data;
    }

    // Simplified constructors for different command types
    public static MultiplayerCommand join(String playerName) {
        return new MultiplayerCommand(Type.JOIN, playerName, null);
    }

    public static MultiplayerCommand bet(String playerName, int amount) {
        return new MultiplayerCommand(Type.BET, playerName, amount);
    }

    public static MultiplayerCommand action(Type type, String playerName) {
        if (!type.equals(Type.HIT) && !type.equals(Type.STAND)) {
            throw new IllegalArgumentException("Invalid action type");
        }
        return new MultiplayerCommand(type, playerName, null);
    }

    // Getters
    public Type getType() { return type; }
    public String getPlayerName() { return playerName; }
    
    public <T> T getData(Class<T> dataType) {
        return dataType.cast(data);
    }

    @Override
    public String toString() {
        return "MultiplayerCommand{" +
                "type=" + type +
                ", player='" + playerName + '\'' +
                ", data=" + data +
                '}';
    }
}
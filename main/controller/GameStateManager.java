package main.controller;
import com.fasterxml.jackson.databind.ObjectMapper;


import java.io.File;
import java.io.IOException;

public class GameStateManager {
    private static final String SAVE_FILE_PATH = "game_state.json";
    private ObjectMapper objectMapper = new ObjectMapper();

    public void saveGame(GameState gameState) {
        try {
            objectMapper.writeValue(new File(SAVE_FILE_PATH), gameState);
            System.out.println("Game saved successfully.");
        } catch (IOException e) {
            System.err.println("Failed to save game: " + e.getMessage());
        }
    }

    public GameState loadGame() {
        try {
            return objectMapper.readValue(new File(SAVE_FILE_PATH), GameState.class);
        } catch (IOException e) {
            System.err.println("Failed to load game: " + e.getMessage());
            return null;
        }
    }
}

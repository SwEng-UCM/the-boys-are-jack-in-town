package main.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;

/**
 * The GameStateManager class handles saving and loading the game state to and from a JSON file.
 * It uses the Jackson library for serialization and deserialization of the {@link GameState} object.
 * This class follows the Controller role in MVC by managing data flow between the Model (GameState) and storage.
 */
public class GameStateManager {
    private static final String SAVE_FILE_PATH = "saves/game_state.json";
    private ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Saves the current game state to a JSON file.
     *
     * @param gameState the current game state to save
     */
    public void saveGame(GameState gameState) throws IOException {
        objectMapper.writeValue(new File(SAVE_FILE_PATH), gameState);
    }

    /**
     * Loads the game state from the JSON file.
     *
     * @return the loaded {@link GameState}, or null if loading fails
     */
    public GameState loadGame() throws IOException {
        return objectMapper.readValue(new File(SAVE_FILE_PATH), GameState.class);
    }
}


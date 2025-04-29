package main.controller;

import com.fasterxml.jackson.databind.ObjectMapper; 
import java.io.File;
import java.io.IOException;

/**
 * The GameLoader class is responsible for loading the game state from a file.
 * It uses Jackson's ObjectMapper to deserialize a JSON file into a GameState object.
 */
public class GameLoader {

    /**
     * Loads a game state from a specified file.
     * This method uses Jackson's ObjectMapper to read and deserialize the JSON content
     * from the file into a GameState object.
     *
     * @param file the file containing the serialized game state in JSON format
     * @return the GameState object representing the game state loaded from the file
     * @throws IOException if an I/O error occurs while reading the file
     * @throws ClassNotFoundException if the GameState class cannot be found during deserialization
     */
    public static GameState loadFromFile(File file) throws IOException, ClassNotFoundException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(file, GameState.class);
    }
}

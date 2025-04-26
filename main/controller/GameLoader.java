package main.controller;

import com.fasterxml.jackson.databind.ObjectMapper; // or whatever library you use
import main.controller.GameState;

import java.io.File;
import java.io.IOException;

public class GameLoader {

    public static GameState loadFromFile(File file) throws IOException, ClassNotFoundException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(file, GameState.class);
    }
}

package main.controller;

import main.view.BlackJackMenu;
import main.model.EasyDifficulty;
import main.model.HardDifficulty;
import main.model.MediumDifficulty;

/**
 * The OptionsController class is responsible for managing user-selected
 * configuration options in the Blackjack game, including language and difficulty settings.
 *
 * It follows the Controller role in the MVC pattern, acting as an intermediary between
 * the OptionsPanel view and the GameManager model.
 */
public class OptionsController {
    private final GameManager gameManager = GameManager.getInstance();

    /**
     * Applies user-selected language and difficulty settings to the game.
     * 
     * This method updates the global language index and assigns the corresponding
     * difficulty strategy to the GameManager based on the given difficulty name.
     *
     * @param languageIndex   The index of the selected language (used in the {@code Texts} class).
     * @param difficultyName  The name of the selected difficulty (e.g., "Easy", "Medium", "Hard").
     */
    public void applySettings(int languageIndex, String difficultyName) {
        BlackJackMenu.language = languageIndex;
        switch (difficultyName) {
            case "Easy" -> gameManager.setDifficultyStrategy(new EasyDifficulty());
            case "Medium" -> gameManager.setDifficultyStrategy(new MediumDifficulty());
            case "Hard" -> gameManager.setDifficultyStrategy(new HardDifficulty());
        }
    }

    /**
     * Retrieves the name of the currently active difficulty strategy.
     *
     * @return The difficulty name (e.g., "Easy", "Medium", or "Hard").
     */
    public String getCurrentDifficultyName() {
        return gameManager.getDifficultyStrategy().getDifficultyName();
    }    
}


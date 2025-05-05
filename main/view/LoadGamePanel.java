package main.view;

import main.controller.GameManager;
import main.controller.GameState;
import main.controller.GameLoader; 

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.IOException;

/**
 * The LoadGamePanel class represents a dialog window that allows the user to load a saved game.
 * It provides a file chooser to select a JSON file containing a previously saved game state.
 * This panel interacts with the GameManager and GameLoader to restore the game's state.
 */
public class LoadGamePanel extends JDialog {
    private final JFileChooser fileChooser; // File chooser for selecting saved game file

    /**
     * Constructs a LoadGamePanel to allow the user to load a saved game.
     * The dialog presents a file chooser where the user can select a JSON file containing a saved game state.
     * If a valid file is selected, the game state is loaded, and the game manager is updated.
     * 
     * @param parent the parent menu from which this dialog is launched
     */
    public LoadGamePanel(BlackJackMenu parent) {
        super();
        setTitle("Load Saved Game");
        setModal(true); // Block input to other windows until closed
        setSize(400, 300);
        setLocationRelativeTo(parent);

        // Initialize file chooser with 'saves' directory and JSON file filter
        fileChooser = new JFileChooser(new File("saves"));
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.setFileFilter(new FileNameExtensionFilter("JSON Files", "json"));

        // Open the file chooser dialog
        int returnValue = fileChooser.showOpenDialog(this);

        // If the user selects a file, try to load the game state
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            System.out.println("Selected file: " + selectedFile.getAbsolutePath());

            try {
                // Load the game state from the selected file and apply it to the GameManager
                GameState loadedState = GameLoader.loadFromFile(selectedFile);
                GameManager.getInstance().applyGameState(loadedState); // <- correct place!
                JOptionPane.showMessageDialog(this, "Game loaded successfully!");
            } catch (IOException | ClassNotFoundException e) {
                // Handle loading errors and display an error message
                System.err.println("Error loading game: " + e.getMessage());
                JOptionPane.showMessageDialog(this,
                        "Failed to load game: " + e.getMessage(),
                        "Load Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        } else {
            System.out.println("Load cancelled.");
            dispose();
        }
    }
}

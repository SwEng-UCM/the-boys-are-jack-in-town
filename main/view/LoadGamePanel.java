package main.view;

import main.controller.GameManager;
import main.controller.GameState;
import main.controller.GameLoader; // (We will create this tiny helper)

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.io.IOException;

// CARETAKER Class
public class LoadGamePanel extends JDialog {
    private final BlackJackMenu menu;
    private final JFileChooser fileChooser;

    public LoadGamePanel(BlackJackMenu parent) {
        super();
        this.menu = parent;
        setTitle("Load Saved Game");
        setModal(true); // Block input to other windows until closed
        setSize(400, 300);
        setLocationRelativeTo(parent);

        fileChooser = new JFileChooser(new File("saves")); // Suggest a "saves" folder if you have it
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.setFileFilter(new FileNameExtensionFilter("JSON Files", "json"));

        int returnValue = fileChooser.showOpenDialog(this);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            System.out.println("Selected file: " + selectedFile.getAbsolutePath());

            try {
                GameState loadedState = GameLoader.loadFromFile(selectedFile);
                GameManager.getInstance().applyGameState(loadedState); // <- correct place!
                JOptionPane.showMessageDialog(this, "Game loaded successfully!");
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Error loading game: " + e.getMessage());
                JOptionPane.showMessageDialog(this,
                        "Failed to load game: " + e.getMessage(),
                        "Load Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        } else {
            System.out.println("Load cancelled.");
        }

        dispose(); 
    }
}

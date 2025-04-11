package main.view;

import main.controller.GameManager;
import main.controller.GameState;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

// CARE TAKER Class
public class LoadGamePanel extends JFrame {
    private BlackJackMenu menu;
    private JFileChooser fileChooser;
    private GameState loadedState;

    public LoadGamePanel(BlackJackMenu parent) {
        super("Load Game");
        this.menu = parent;
        setSize(300, 200);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        // Initialize the file chooser
        fileChooser = new JFileChooser(new File("main"));
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.setFileFilter(new FileNameExtensionFilter("JSON Files", "json"));

        int returnValue = fileChooser.showOpenDialog(this);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            System.out.println("Selected file: " + selectedFile.getAbsolutePath());

            try {
                loadedState = new GameState(selectedFile);
                System.out.println(loadedState);

                GameManager manager = GameManager.getInstance();
                manager.loadGame(loadedState);

                setVisible(false);
            } catch (IOException e) {
                System.err.println("Error reading file: " + e.getMessage());
                JOptionPane.showMessageDialog(this, "Error reading file: " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            System.out.println("No file selected");
        }




    }
}


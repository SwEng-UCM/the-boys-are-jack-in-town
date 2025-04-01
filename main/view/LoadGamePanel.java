package main.view;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;

public class LoadGamePanel extends JFrame {
    private BlackJackMenu menu;
    private JFileChooser fileChooser;

    public LoadGamePanel(BlackJackMenu parent) {
        super("Load Game");
        this.menu = parent;
        setSize(300, 200);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        // Initialize the file chooser
        fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.setFileFilter(new FileNameExtensionFilter("JSON Files", "json"));

        int returnValue = fileChooser.showOpenDialog(this);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            System.out.println("Selected file: " + selectedFile.getAbsolutePath());
        }


    }
}


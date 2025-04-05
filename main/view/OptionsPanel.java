package main.view;

import javax.swing.*;
import java.awt.*;

// Ensure this import exists
import main.model.*;
import main.controller.GameManager;

public class OptionsPanel extends JDialog {
    private JComboBox<String> languageDropdown;
    private JComboBox<String> difficultyCombo;
    private JButton applyButton, cancelButton;
    private BlackJackMenu menu;

    public OptionsPanel(BlackJackMenu parent) {
        super(parent, "Options", true);
        this.menu = parent;
        setSize(350, 250); // Increased size for additional component
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        // Main panel with GridLayout
        JPanel panel = new JPanel(new GridLayout(4, 1, 5, 5)); // 4 rows, 1 column, with gaps

        // Language selection
        panel.add(new JLabel("Select Language:"));
        String[] languages = {"English", "Español", "Gaelige", "Hungarian", "Arabic", "Français"};
        languageDropdown = new JComboBox<>(languages);
        languageDropdown.setSelectedIndex(BlackJackMenu.language);
        panel.add(languageDropdown);

        // Difficulty selection
        panel.add(new JLabel("Select Difficulty:"));
        difficultyCombo = new JComboBox<>(new String[]{"Easy", "Medium", "Hard"});
        
        // Set current difficulty
       // Set current difficulty - updated version
        DifficultyStrategy current = GameManager.getInstance().getDifficultyStrategy();
        difficultyCombo.setSelectedItem(current.getDifficultyName());
        
        difficultyCombo.setToolTipText("Easy: 1.5x Payout\nMedium: 2x Payout\nHard: 2.5x Payout");
        panel.add(difficultyCombo);

        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        applyButton = new JButton("Apply");
        cancelButton = new JButton("Cancel");

        applyButton.addActionListener(e -> {
            // Update language
            BlackJackMenu.language = languageDropdown.getSelectedIndex();
            
            // Update difficulty
            String selectedDifficulty = (String) difficultyCombo.getSelectedItem();
            switch(selectedDifficulty) {
                case "Easy":
                    GameManager.getInstance().setDifficultyStrategy(new EasyDifficulty());
                    break;
                case "Medium":
                    GameManager.getInstance().setDifficultyStrategy(new MediumDifficulty());
                    break;
                case "Hard":
                    GameManager.getInstance().setDifficultyStrategy(new HardDifficulty());
                    break;
            }
            
            menu.refreshMenu();
            dispose();
        });

        cancelButton.addActionListener(e -> dispose());

        buttonPanel.add(applyButton);
        buttonPanel.add(cancelButton);

        // Add components to dialog
        add(panel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Add some padding
        ((JComponent) getContentPane()).setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }
}
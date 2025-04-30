package main.view;

import javax.swing.*;
import java.awt.*;
import main.model.*;
import main.controller.GameManager;
import main.controller.OptionsController;

import static main.view.BlackJackMenu.language;

/**
 * OptionsPanel provides a modal dialog for users to select language and difficulty settings.
 * It follows MVC by delegating logic to the OptionsController.
 */
public class OptionsPanel extends JDialog {
    private JComboBox<String> languageDropdown;
    private JComboBox<String> difficultyCombo;
    private JButton applyButton, cancelButton;
    private BlackJackMenu menu;
    private final OptionsController controller;

    /**
     * Constructs a new OptionsPanel dialog.
     *
     * @param parent The parent BlackJackMenu frame
     */
    public OptionsPanel(BlackJackMenu parent) {
        super(parent, "Options", true);
        this.menu = parent;
        this.controller = new OptionsController();

        setSize(350, 250); // Increased size for additional component
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        // Main panel with GridLayout
        JPanel panel = new JPanel(new GridLayout(4, 1, 5, 5)); // 4 rows, 1 column, with gaps

        // Language selection
        panel.add(new JLabel(Texts.selectLanguage[language]));
        String[] languages = {"English", "Español", "Gaelige", "Hungarian", "Arabic", "Français"};
        languageDropdown = new JComboBox<>(languages);
        languageDropdown.setSelectedIndex(language);
        panel.add(languageDropdown);

        // Difficulty selection
        panel.add(new JLabel(Texts.selectDifficulty[language]));
        difficultyCombo = new JComboBox<>(new String[]{Texts.easy[language], Texts.medium[language],Texts.hard[language]});
        DifficultyStrategy current = GameManager.getInstance().getDifficultyStrategy();
        difficultyCombo.setSelectedItem(current.getDifficultyName());
        difficultyCombo.setToolTipText("Easy: 1.5x Payout\nMedium: 2x Payout\nHard: 2.5x Payout");
        panel.add(difficultyCombo);

        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        applyButton = new JButton("Apply");
        cancelButton = new JButton("Cancel");

        applyButton.addActionListener(e -> {
            int langIndex = languageDropdown.getSelectedIndex();
            String difficulty = (String) difficultyCombo.getSelectedItem();

            controller.applySettings(langIndex, difficulty);
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
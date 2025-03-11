package main.view;

import javax.swing.*;
import java.awt.*;

import static main.view.BlackJackMenu.language;
import main.view.Texts;

public class OptionsPanel extends JDialog {
    private JComboBox<String> languageDropdown;
    private JButton applyButton, cancelButton;
    private BlackJackMenu menu; // Reference to main menu

    public OptionsPanel(BlackJackMenu parent) {
        super(parent, "Options", true);
        this.menu = parent;
        setSize(300, 200);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        // Language selection
        String[] languages = {"English", "Español", "Gaelige", "Hungarian", "Arabic"};
        languageDropdown = new JComboBox<>(languages);
        languageDropdown.setSelectedIndex(BlackJackMenu.language);

        // Buttons
        applyButton = new JButton("Apply");
        cancelButton = new JButton("Cancel");

        applyButton.addActionListener(e -> {
            BlackJackMenu.language = languageDropdown.getSelectedIndex();
            menu.refreshMenu(); // Refresh UI
            dispose();
        });

        cancelButton.addActionListener(e -> dispose());

        // Layout
        JPanel panel = new JPanel(new GridLayout(2, 1));
        panel.add(new JLabel("Select Language:"));
        panel.add(languageDropdown);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(applyButton);
        buttonPanel.add(cancelButton);

        add(panel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
}


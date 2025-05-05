package main.view;

import javax.swing.*;
import java.awt.*;
import main.model.*;
import main.controller.GameManager;
import main.controller.OptionsController;
import static main.view.BlackJackMenu.language;

/**
 * Stylish OptionsPanel matching the Blackjack game's casino theme.
 */
public class OptionsPanel extends JDialog {
    private JComboBox<String> languageDropdown;
    private JComboBox<String> difficultyCombo;
    private JButton applyButton, cancelButton;
    private final OptionsController controller;

    public OptionsPanel(BlackJackMenu parent) {
        super(parent, Texts.optionsTitle[language], true);
        this.controller = new OptionsController();

        setSize(420, 300);
        setLocationRelativeTo(parent);
        setUndecorated(true); // borderless for custom style
        setLayout(new BorderLayout());

        // Casino-style main panel with glass effect
        JPanel mainPanel = new JPanel() {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setComposite(AlphaComposite.SrcOver.derive(0.95f));
                g2.setColor(new Color(0, 60, 0));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
                g2.dispose();
            }
        };
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));
        mainPanel.setOpaque(false);

        Font labelFont = new Font("Segoe UI", Font.BOLD, 16);
        Color labelColor = Color.WHITE;

        // Language
        JLabel langLabel = new JLabel("🌐 " + Texts.selectLanguage[language]);
        langLabel.setFont(labelFont);
        langLabel.setForeground(labelColor);
        langLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        languageDropdown = new JComboBox<>(new String[]{
            "English", "Español", "Gaelige", "Hungarian", "Arabic", "Français"
        });
        languageDropdown.setSelectedIndex(language);
        styleComboBox(languageDropdown);

        // Difficulty
        JLabel diffLabel = new JLabel("🎯 " + Texts.selectDifficulty[language]);
        diffLabel.setFont(labelFont);
        diffLabel.setForeground(labelColor);
        diffLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        difficultyCombo = new JComboBox<>(new String[]{
            Texts.easy[language], Texts.medium[language], Texts.hard[language]
        });
        DifficultyStrategy current = GameManager.getInstance().getDifficultyStrategy();
        difficultyCombo.setSelectedItem(current.getDifficultyName());
        difficultyCombo.setToolTipText(Texts.tooltipDifficulty[language]);
        styleComboBox(difficultyCombo);

        // Buttons
        applyButton = createStyledButton(Texts.apply[language], new Color(0, 153, 0));
        cancelButton = createStyledButton(Texts.cancel[language], new Color(153, 0, 0));

        applyButton.addActionListener(e -> {
            int langIndex = languageDropdown.getSelectedIndex();
            String difficulty = (String) difficultyCombo.getSelectedItem();
            controller.applySettings(langIndex, difficulty);
            parent.refreshMenu();
            dispose();
        });

        cancelButton.addActionListener(e -> dispose());

        // Add components
        mainPanel.add(langLabel);
        mainPanel.add(Box.createVerticalStrut(5));
        mainPanel.add(languageDropdown);
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(diffLabel);
        mainPanel.add(Box.createVerticalStrut(5));
        mainPanel.add(difficultyCombo);
        mainPanel.add(Box.createVerticalStrut(25));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.add(applyButton);
        buttonPanel.add(cancelButton);
        mainPanel.add(buttonPanel);

        add(mainPanel, BorderLayout.CENTER);
        getRootPane().setBorder(BorderFactory.createLineBorder(new Color(255, 215, 0), 3));
    }

    /**
     * Creates a modern, rounded casino-style JButton.
     */
    private JButton createStyledButton(String text, Color baseColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setBackground(baseColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(120, 35));
        button.setContentAreaFilled(true);
        button.setOpaque(true);
        return button;
    }

    /**
     * Applies consistent casino-style formatting to a JComboBox.
     */
    private void styleComboBox(JComboBox<String> comboBox) {
        comboBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        comboBox.setBackground(Color.WHITE);
        comboBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        comboBox.setAlignmentX(Component.CENTER_ALIGNMENT);
        comboBox.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }
}

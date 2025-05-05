package main.view;

import javax.swing.*;
import java.awt.*;
import main.model.*;
import main.controller.GameManager;
import main.controller.OptionsController;
import static main.view.BlackJackMenu.language;

public class OptionsPanel extends JDialog {
    private JComboBox<String> languageDropdown;
    private JComboBox<String> difficultyCombo;
    private JButton applyButton, cancelButton;
    private BlackJackMenu menu;
    private final OptionsController controller;

    public OptionsPanel(BlackJackMenu parent) {
        super(parent, Texts.optionsTitle[language], true);
        this.menu = parent;
        this.controller = new OptionsController();

        setSize(400, 300);
        setLocationRelativeTo(parent);
        setUndecorated(true); // Clean borderless look
        setLayout(new BorderLayout());

        // Glassmorphism-like panel
        JPanel glassPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.9f));
                g2d.setColor(new Color(30, 30, 30));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
                g2d.dispose();
            }
        };
        glassPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        glassPanel.setLayout(new BoxLayout(glassPanel, BoxLayout.Y_AXIS));
        glassPanel.setOpaque(false);

        // UIManager styling for combo boxes
        UIManager.put("ComboBox.background", new Color(44, 44, 44));
        UIManager.put("ComboBox.foreground", Color.WHITE);
        UIManager.put("ComboBox.buttonBackground", new Color(60, 60, 60));
        UIManager.put("ComboBox.selectionBackground", new Color(100, 100, 100));
        UIManager.put("ComboBox.selectionForeground", Color.WHITE);

        Font labelFont = new Font("Segoe UI", Font.PLAIN, 14);

        // Language section
        JLabel languageLabel = new JLabel(Texts.selectLanguage[language]);
        languageLabel.setForeground(Color.WHITE);
        languageLabel.setFont(labelFont);

        String[] languages = {"English", "Español", "Gaelige", "Hungarian", "Arabic", "Français"};
        languageDropdown = new JComboBox<>(languages);
        languageDropdown.setSelectedIndex(language);
        languageDropdown.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        // Difficulty section
        JLabel difficultyLabel = new JLabel(Texts.selectDifficulty[language]);
        difficultyLabel.setForeground(Color.WHITE);
        difficultyLabel.setFont(labelFont);

        difficultyCombo = new JComboBox<>(new String[]{
            Texts.easy[language],
            Texts.medium[language],
            Texts.hard[language]
        });

        DifficultyStrategy current = GameManager.getInstance().getDifficultyStrategy();
        difficultyCombo.setSelectedItem(current.getDifficultyName());
        difficultyCombo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        difficultyCombo.setToolTipText(Texts.tooltipDifficulty[language]);

        // Buttons
        applyButton = new JButton(Texts.apply[language]);
        cancelButton = new JButton(Texts.cancel[language]);

        applyButton.setBackground(new Color(0, 153, 0));
        applyButton.setForeground(Color.WHITE);
        applyButton.setFocusPainted(false);

        cancelButton.setBackground(new Color(153, 0, 0));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setFocusPainted(false);

        applyButton.setPreferredSize(new Dimension(100, 35));
        cancelButton.setPreferredSize(new Dimension(100, 35));

        applyButton.addActionListener(e -> {
            int langIndex = languageDropdown.getSelectedIndex();
            String difficulty = (String) difficultyCombo.getSelectedItem();

            controller.applySettings(langIndex, difficulty);
            menu.refreshMenu();
            dispose();
        });

        cancelButton.addActionListener(e -> dispose());

        // Build layout
        glassPanel.add(languageLabel);
        glassPanel.add(Box.createVerticalStrut(5));
        glassPanel.add(languageDropdown);
        glassPanel.add(Box.createVerticalStrut(15));
        glassPanel.add(difficultyLabel);
        glassPanel.add(Box.createVerticalStrut(5));
        glassPanel.add(difficultyCombo);
        glassPanel.add(Box.createVerticalStrut(20));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 5));
        buttonPanel.setOpaque(false);
        buttonPanel.add(applyButton);
        buttonPanel.add(cancelButton);
        glassPanel.add(buttonPanel);

        add(glassPanel, BorderLayout.CENTER);
        getRootPane().setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));
    }
}

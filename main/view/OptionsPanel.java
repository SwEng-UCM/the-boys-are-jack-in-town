package main.view;

import javax.swing.*;
import java.awt.*;
import main.controller.GameManager;
import main.model.*;

public class OptionsPanel extends JDialog {
    private JComboBox<String> languageDropdown;
    private JComboBox<String> difficultyCombo;
    private JButton applyButton, cancelButton;
    private BlackJackMenu menu;

    public OptionsPanel(BlackJackMenu parent) {
        super(parent, "Options", true);
        this.menu = parent;
        setSize(400, 300);
        setLocationRelativeTo(parent);
        setUndecorated(true);

        JPanel glassPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setComposite(AlphaComposite.SrcOver.derive(0.85f));
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                g2.dispose();
            }
        };
        glassPanel.setOpaque(false);
        glassPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setContentPane(glassPanel);

        JPanel contentPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        contentPanel.setOpaque(false);

        contentPanel.add(createLabel("Select Language:"));
        String[] languages = {"English", "Espa\u00f1ol", "Gaelige", "Hungarian", "Arabic", "Fran\u00e7ais"};
        languageDropdown = new JComboBox<>(languages);
        languageDropdown.setSelectedIndex(BlackJackMenu.language);
        styleComboBox(languageDropdown);
        contentPanel.add(languageDropdown);

        contentPanel.add(createLabel("Select Difficulty:"));
        difficultyCombo = new JComboBox<>(new String[]{"Easy", "Medium", "Hard"});
        DifficultyStrategy current = GameManager.getInstance().getDifficultyStrategy();
        difficultyCombo.setSelectedItem(current.getDifficultyName());
        styleComboBox(difficultyCombo);
        contentPanel.add(difficultyCombo);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setOpaque(false);
        applyButton = createStyledButton("Apply");
        cancelButton = createStyledButton("Cancel");

        applyButton.addActionListener(e -> {
            BlackJackMenu.language = languageDropdown.getSelectedIndex();
            String selected = (String) difficultyCombo.getSelectedItem();
            if ("Easy".equals(selected)) GameManager.getInstance().setDifficultyStrategy(new EasyDifficulty());
            else if ("Medium".equals(selected)) GameManager.getInstance().setDifficultyStrategy(new MediumDifficulty());
            else GameManager.getInstance().setDifficultyStrategy(new HardDifficulty());

            menu.refreshMenu();
            dispose();
        });

        cancelButton.addActionListener(e -> dispose());
        buttonPanel.add(applyButton);
        buttonPanel.add(cancelButton);

        add(contentPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 18));
        label.setForeground(new Color(40, 40, 40));
        return label;
    }

    private void styleComboBox(JComboBox<String> combo) {
        combo.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        combo.setBackground(Color.WHITE);
        combo.setFocusable(false);
        combo.setPreferredSize(new Dimension(200, 35));
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color start = getModel().isRollover() ? new Color(255, 230, 120) : new Color(255, 215, 0);
                Color end = getModel().isRollover() ? new Color(255, 190, 50) : new Color(240, 180, 0);
                GradientPaint gp = new GradientPaint(0, 0, start, 0, getHeight(), end);
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        button.setFont(new Font("Segoe UI", Font.BOLD, 20));
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(130, 40));
        return button;
    }
} 

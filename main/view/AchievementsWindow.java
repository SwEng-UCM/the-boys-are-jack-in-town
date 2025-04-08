package main.view;

import main.controller.AchievementManager;
import main.model.Badge;

import javax.swing.*;
import java.awt.*;

public class AchievementsWindow extends JFrame {

    public AchievementsWindow() {
        setTitle("Achievements");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        ImageIcon icon = new ImageIcon("img/icons/achievement_window.png");
        setIconImage(icon.getImage());

        // Background panel with image
        JPanel backgroundPanel = new JPanel() {
            private final Image bg = new ImageIcon("img/ach_background.png").getImage();

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(bg, 0, 0, getWidth(), getHeight(), this);
            }
        };
        backgroundPanel.setLayout(new BorderLayout());

        // Badge grid panel
        JPanel gridPanel = new JPanel(new GridLayout(0, 3, 20, 20)); // 3 badges per row
        gridPanel.setOpaque(false);

        for (Badge badge : Badge.values()) {
            boolean unlocked = AchievementManager.getInstance().isUnlocked(badge);
            gridPanel.add(createBadgePanel(badge, unlocked));
        }

        // Scroll pane
        JScrollPane scrollPane = new JScrollPane(gridPanel);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        // Glass-like wrapper around badge list
        JPanel glassPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setComposite(AlphaComposite.SrcOver.derive(0.35f));
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                g2.dispose();
            }
        };
        glassPanel.setOpaque(false);
        glassPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        glassPanel.add(scrollPane, BorderLayout.CENTER);

        backgroundPanel.add(glassPanel, BorderLayout.CENTER);
        setContentPane(backgroundPanel);
    }

    private JPanel createBadgePanel(Badge badge, boolean unlocked) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        String imagePath = unlocked ? badge.coloredPath : badge.greyPath;
        ImageIcon icon = new ImageIcon(imagePath);
        Image scaledImage = icon.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
        JLabel imageLabel = new JLabel(new ImageIcon(scaledImage));
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel titleLabel = new JLabel(badge.title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        titleLabel.setForeground(unlocked ? Color.WHITE : Color.WHITE);

        panel.add(imageLabel, BorderLayout.CENTER);
        panel.add(titleLabel, BorderLayout.SOUTH);
        return panel;
    }
}

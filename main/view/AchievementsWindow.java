package main.view;

import main.controller.AchievementManager;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class AchievementsWindow extends JFrame {

    public AchievementsWindow() {
        setTitle("Achievements");
        setSize(900, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        ImageIcon icon = new ImageIcon("img/icons/achievement_window.png");
        setIconImage(icon.getImage());

        // Create background panel
        JPanel backgroundPanel = new JPanel() {
            private final Image bg = new ImageIcon("img/achievement_background.jpg").getImage();
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(bg, 0, 0, getWidth(), getHeight(), this);
            }
        };
        backgroundPanel.setLayout(new BorderLayout());

        // Title label
        JLabel titleLabel = new JLabel("Your Achievements", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Georgia", Font.BOLD, 32));
        titleLabel.setForeground(Color.YELLOW);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));

        // Achievements list panel
        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setOpaque(false); // transparent background

        List<String> achievements = AchievementManager.getInstance().getUnlockedAchievements();

        if (achievements.isEmpty()) {
            JLabel emptyLabel = new JLabel("No achievements yet.");
            emptyLabel.setFont(new Font("Arial", Font.ITALIC, 22));
            emptyLabel.setForeground(Color.LIGHT_GRAY);
            emptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            listPanel.add(emptyLabel);
        } else {
            for (String achievement : achievements) {
                JPanel card = createAchievementCard(achievement);
                listPanel.add(card);
                listPanel.add(Box.createRigidArea(new Dimension(0, 10))); // spacing
            }
        }

        JScrollPane scrollPane = new JScrollPane(listPanel);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        backgroundPanel.add(titleLabel, BorderLayout.NORTH);
        backgroundPanel.add(scrollPane, BorderLayout.CENTER);
        setContentPane(backgroundPanel);
    }

    private JPanel createAchievementCard(String text) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
        card.setBackground(new Color(255, 215, 0, 200)); // Gold with transparency
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.BLACK, 2),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));

        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 20));
        label.setForeground(Color.BLACK);

        card.add(label, BorderLayout.CENTER);
        return card;
    }
}

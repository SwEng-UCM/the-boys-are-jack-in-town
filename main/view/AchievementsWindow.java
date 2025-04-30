package main.view;

import main.controller.AchievementManager;
import main.model.Badge;
import javax.swing.*;
import java.awt.*;
import static main.view.BlackJackMenu.language;

/**
 * {@code AchievementsWindow} is a singleton Swing window that displays a grid of achievement badges,
 * indicating which ones have been unlocked by the player.
 * 
 * It acts as the View in the MVC pattern and retrieves badge data from the {@link AchievementManager}.
 */
public class AchievementsWindow extends JFrame {
    private static AchievementsWindow instance;
    private JPanel gridPanel;

    /**
     * Returns the singleton instance of {@code AchievementsWindow}, creating it if necessary.
     *
     * @return the singleton {@code AchievementsWindow} instance
     */
    public static AchievementsWindow getInstance() {
        if (instance == null) {
            instance = new AchievementsWindow();
        }
        return instance;
    }

    /**
     * Constructs the achievements window, sets up UI layout, background image,
     * scrollable badge grid, and glass panel effects.
     */
    public AchievementsWindow() {
        setTitle(Texts.achievements[language]);
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        ImageIcon icon = new ImageIcon("resources/icons/achievement_window.png");
        setIconImage(icon.getImage());

        // Background panel with image
        JPanel backgroundPanel = new JPanel() {
            private final Image bg = new ImageIcon("resources/img/ach_background.png").getImage();

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
        refreshBadges();

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

        instance = this;
    }

    /**
     * Refreshes the grid of badges to reflect updated unlock status from {@link AchievementManager}.
     * This method removes all current badges and re-adds them based on their unlock state.
     */
    public void refreshBadges() {
        if (gridPanel == null) return;
        gridPanel.removeAll();
        for (Badge badge : Badge.values()) {
            boolean unlocked = AchievementManager.getInstance().isUnlocked(badge);
            gridPanel.add(createBadgePanel(badge, unlocked));
        }
        gridPanel.revalidate();
        gridPanel.repaint();
    }

    /**
     * Creates a JPanel containing a single badge's icon and title.
     * Badge appearance is rendered differently depending on whether it's unlocked.
     *
     * @param badge    the badge to render
     * @param unlocked true if the badge is unlocked, false if it's still locked
     * @return a JPanel containing the badge image and title label
     */
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

        JLabel titleLabel = new JLabel(badge.getTitle(), SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        titleLabel.setForeground(unlocked ? Color.WHITE : Color.WHITE);

        panel.add(imageLabel, BorderLayout.CENTER);
        panel.add(titleLabel, BorderLayout.SOUTH);
        return panel;
    }
}

package main.view;

import javax.swing.*;
import java.awt.*;

public class GUILayoutBuilder {
    private final BlackjackGUI window;

    public GUILayoutBuilder(BlackjackGUI window) {
        this.window = window;
    }

    public void layoutComponents() {
        window.mainPanel.setLayout(new BorderLayout());

        // Top panel setup
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);

        // Achievement button
        JButton achievementButton = new JButton();
        achievementButton.setPreferredSize(new Dimension(50, 50));
        achievementButton.setToolTipText("View Achievements");
        achievementButton.setFocusPainted(false);
        achievementButton.setContentAreaFilled(false);
        achievementButton.setBorderPainted(false);
        achievementButton.setOpaque(false);

        ImageIcon achievementIcon = new ImageIcon("resources/icons/achievement.png");
        Image scaledIcon = achievementIcon.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
        achievementButton.setIcon(new ImageIcon(scaledIcon));
        achievementButton.addActionListener(e -> new AchievementsWindow().setVisible(true));

        JPanel topLeftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topLeftPanel.setOpaque(false);
        topLeftPanel.add(achievementButton);

        window.topRightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        window.topRightPanel.setOpaque(false);
        window.topRightPanel.add(window.pauseButton);

        JLabel connectionStatus = new JLabel("Offline");
        connectionStatus.setFont(new Font("Arial", Font.BOLD, 14));
        connectionStatus.setForeground(Color.RED);
        window.topRightPanel.add(connectionStatus);

        JPanel dealerArea = new JPanel(new BorderLayout());
        dealerArea.setOpaque(false);
        dealerArea.add(window.dealerScorePanel, BorderLayout.NORTH);
        dealerArea.add(window.dealerPanel, BorderLayout.CENTER);

        topPanel.add(topLeftPanel, BorderLayout.WEST);
        topPanel.add(dealerArea, BorderLayout.CENTER);
        topPanel.add(window.topRightPanel, BorderLayout.EAST);

        // Center and bottom panels
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setOpaque(false);
        centerPanel.add(window.gameMessageLabel, BorderLayout.NORTH);
        centerPanel.add(window.buttonPanel, BorderLayout.CENTER);
        centerPanel.add(window.specialMessageLabel, BorderLayout.SOUTH);

        JPanel playersContainer = new JPanel(new BorderLayout());
        playersContainer.setPreferredSize(new Dimension(Toolkit.getDefaultToolkit().getScreenSize().width, 300));
        playersContainer.setOpaque(false);
        playersContainer.add(window.scrollPane, BorderLayout.CENTER);

        window.betPanel.setOpaque(false);

        JPanel southContainer = new JPanel(new BorderLayout());
        southContainer.setPreferredSize(new Dimension(window.getGameWidth(), (int) (window.getGameHeight() / 2.4)));
        southContainer.setOpaque(false);
        southContainer.add(playersContainer, BorderLayout.CENTER);
        southContainer.add(window.betPanel, BorderLayout.SOUTH);

        // Assemble all
        window.mainPanel.add(topPanel, BorderLayout.NORTH);
        window.mainPanel.add(centerPanel, BorderLayout.CENTER);
        window.mainPanel.add(southContainer, BorderLayout.SOUTH);
        window.add(window.mainPanel);

        window.gameManager.getGameFlowController().startNewGame();

    }
}

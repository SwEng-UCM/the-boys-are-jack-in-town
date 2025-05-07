package main.view;

import javax.swing.*;
import java.awt.*;

/**
 * The {@code GUILayoutBuilder} class is responsible for setting up and arranging
 * all major components of the Blackjack game's graphical user interface.
 * <p>
 * This class is used to modularize and encapsulate the layout logic, separating
 * it from the {@link BlackjackGUI} constructor.
 */
public class GUILayoutBuilder {
    private final BlackjackGUI window;

    /**
     * Constructs a {@code GUILayoutBuilder} for the specified Blackjack GUI window.
     *
     * @param window the {@link BlackjackGUI} instance whose layout is to be built
     */
    public GUILayoutBuilder(BlackjackGUI window) {
        this.window = window;
    }

    /**
     * Builds and arranges the components of the Blackjack GUI.
     * <p>
     * The layout consists of:
     * <ul>
     *     <li>A top panel with achievement and pause buttons, and dealer info</li>
     *     <li>A center panel for game messages and controls</li>
     *     <li>A south container with player panels and betting UI</li>
     * </ul>
     * This method also starts a new game via the {@link GameFlowController}.
     */
    public void layoutComponents() {
        window.mainPanel.setLayout(new BorderLayout());

        // Top panel setup
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);

        // Achievement button
        JButton achievementButton = window.createStyledButton("");
        ImageIcon rawIcon = new ImageIcon("resources/icons/achievement.png");
        Image scaledImage = rawIcon.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
        achievementButton.setIcon(new ImageIcon(scaledImage));
        achievementButton.setToolTipText("View Achievements");
        achievementButton.setPreferredSize(new Dimension(60, 60));
        achievementButton.setHorizontalAlignment(SwingConstants.CENTER);
        achievementButton.setVerticalAlignment(SwingConstants.CENTER);
        achievementButton.addActionListener(e -> new AchievementsWindow().setVisible(true));

        JPanel topLeftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
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

        // Center panel with messages and controls
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setOpaque(false);
        centerPanel.add(window.gameMessageLabel, BorderLayout.NORTH);
        centerPanel.add(window.buttonPanel, BorderLayout.CENTER);
        centerPanel.add(window.specialMessageLabel, BorderLayout.SOUTH);

        // Player and betting panels
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

        // Assemble layout
        window.mainPanel.add(topPanel, BorderLayout.NORTH);
        window.mainPanel.add(centerPanel, BorderLayout.CENTER);
        window.mainPanel.add(southContainer, BorderLayout.SOUTH);
        window.add(window.mainPanel);

        // Start the initial game round
        window.gameManager.getGameFlowController().startNewGame();
    }
}

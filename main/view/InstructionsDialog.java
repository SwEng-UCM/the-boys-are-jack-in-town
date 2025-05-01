package main.view;

import javax.swing.*;
import java.awt.*;
import static main.view.BlackJackMenu.language;

public class InstructionsDialog extends JDialog {

    public InstructionsDialog(JFrame parent) {
        super(parent, Texts.instructions[language], true);
        setUndecorated(true);
        setSize(600, 500);
        setLocationRelativeTo(parent);

        JPanel mainPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(10, 40, 20)); // Deep casino green
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 40, 40);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        mainPanel.setOpaque(false);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        // Title
        JLabel titleLabel = new JLabel("★ " + Texts.instructions[language].toUpperCase() + " ★");
        titleLabel.setFont(new Font("Georgia", Font.BOLD, 28));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setForeground(new Color(255, 215, 0)); // Gold

        // Instructions content
        String[] instructions = {
                "✔ 1. " + Texts.instructionsPopup[language][1],
                "✔ 2. " + Texts.instructionsPopup[language][2],
                "✔ 3. " + Texts.instructionsPopup[language][3],
                "✔ 4. Face cards are worth 10, Aces are 1 or 11.",
                "✔ 5. Place your bet using the 'Place Bet' field.",
                "✔ 6. You can undo your last bet using the 'Undo' button.",
                "✔ 7. You can save the game anytime using the Save option."
        };

        StringBuilder html = new StringBuilder("<html><body style='font-size:14px; color: #F0F0F0; font-family:Segoe UI;'>");
        for (String line : instructions) {
            html.append("<div style='margin-bottom:10px;'>").append(line).append("</div>");
        }
        html.append("</body></html>");

        JLabel contentLabel = new JLabel(html.toString());
        JScrollPane scrollPane = new JScrollPane(contentLabel);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);
        scrollPane.setPreferredSize(new Dimension(500, 300));
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

        // Close button
        JButton closeButton = new JButton("✖ " + Texts.exit[language]);
        closeButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        closeButton.setForeground(Color.WHITE);
        closeButton.setBackground(new Color(30, 80, 40));
        closeButton.setFocusPainted(false);
        closeButton.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
        closeButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        closeButton.addActionListener(e -> dispose());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.add(closeButton);

        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);
    }
}

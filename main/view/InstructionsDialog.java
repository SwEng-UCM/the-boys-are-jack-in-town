package main.view;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

import static main.view.BlackJackMenu.language;

public class InstructionsDialog extends JDialog {

    public InstructionsDialog(JFrame parent) {
        super(parent, Texts.instructions[language], true);
        setUndecorated(true);
        setSize(600, 500);
        setLocationRelativeTo(parent);
        setBackground(new Color(0, 0, 0, 0));

        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                RoundRectangle2D rounded = new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 40, 40);
                g2.setColor(new Color(0, 50, 30)); // deep green
                g2.fill(rounded);
            }
        };
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        mainPanel.setOpaque(false);

        JLabel titleLabel = new JLabel("How to Play");
        titleLabel.setFont(new Font("Lucida Handwriting", Font.BOLD, 36));
        titleLabel.setForeground(new Color(255, 255, 200));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel gameLabel = new JLabel("♠ BLACKJACK");
        gameLabel.setFont(new Font("Georgia", Font.BOLD, 24));
        gameLabel.setForeground(new Color(245, 220, 130));
        gameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        gameLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));

        JTextPane instructionsPane = new JTextPane();
        instructionsPane.setContentType("text/html");
        instructionsPane.setText(generateStyledHTML());
        instructionsPane.setEditable(false);
        instructionsPane.setOpaque(false);
        instructionsPane.setForeground(Color.WHITE);
        instructionsPane.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, Boolean.TRUE);
        instructionsPane.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        JScrollPane scrollPane = new JScrollPane(instructionsPane);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);
        scrollPane.setAlignmentX(Component.CENTER_ALIGNMENT);
        scrollPane.setPreferredSize(new Dimension(500, 260));

        JButton closeButton = new JButton("CLOSE");
        closeButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        closeButton.setForeground(Color.WHITE);
        closeButton.setBackground(new Color(30, 100, 60));
        closeButton.setFocusPainted(false);
        closeButton.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
        closeButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        closeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        closeButton.addActionListener(e -> dispose());

        mainPanel.add(titleLabel);
        mainPanel.add(gameLabel);
        mainPanel.add(scrollPane);
        mainPanel.add(Box.createVerticalStrut(15));
        mainPanel.add(closeButton);

        setContentPane(mainPanel);
    }

    private String generateStyledHTML() {
        String[] steps = Texts.instructionsPopup[language];
        StringBuilder html = new StringBuilder("<html><body style='font-family:Segoe UI; font-size:15px; color:white;'>");

        for (int i = 1; i < steps.length; i++) {
            html.append("<div style='margin-bottom: 15px;'>")
                .append("<span style='color:#F5DC82; font-weight:bold;'>")
                .append(i).append(".</span> ")
                .append(steps[i])
                .append("</div>");
        }

        html.append("</body></html>");
        return html.toString();
    }
}

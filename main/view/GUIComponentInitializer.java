package main.view;

import main.controller.GameManager;

import javax.swing.*;
import java.awt.*;

import static main.view.BlackJackMenu.language;

public class GUIComponentInitializer {
    private final BlackjackGUI window;

    public GUIComponentInitializer(BlackjackGUI window) {
        this.window = window;
    }

    public void initializeComponents() {
        window.mainPanel = new BlackjackGUI.BackgroundPanel();
        window.mainPanel.setLayout(new BorderLayout());

        window.playersPanel = new PlayersPanel();
        window.scrollPane = new JScrollPane(window.playersPanel);
        window.scrollPane.setOpaque(false);
        window.scrollPane.getViewport().setOpaque(false);
        window.scrollPane.setBorder(null);
        window.scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        window.scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        window.scrollPane.getViewport().setBackground(new Color(0, 0, 0, 0));
        window.scrollPane.setBackground(new Color(0, 0, 0, 0));

        window.dealerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        window.dealerPanel.setOpaque(false);
        window.betPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 10));
        window.buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        window.betLabel = window.createStyledLabel(Texts.bet[language] + " $0");
        window.balanceLabel = window.createStyledLabel(Texts.balance[language] + " $1000");

        // Buttons
        window.hitButton = window.createStyledButton(Texts.guiHit[language]);
        window.hitButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        window.standButton = window.createStyledButton(Texts.guiStand[language]);
        window.standButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        window.newGameButton = window.createStyledButton(Texts.guiNewGame[language]);
        window.newGameButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        window.placeBetButton = window.createStyledButton(Texts.placeBet[language]);
        window.placeBetButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        window.undoButton = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color start = getModel().isRollover() ? new Color(255, 240, 100) : new Color(255, 215, 0);
                Color end = getModel().isRollover() ? new Color(255, 210, 0) : new Color(240, 180, 0);
                g2.setPaint(new GradientPaint(0, 0, start, 0, getHeight(), end));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 40, 40);
                g2.setColor(new Color(0, 0, 0, 40));
                g2.fillRoundRect(4, 4, getWidth() - 8, getHeight() - 8, 40, 40);
                super.paintComponent(g);
                g2.dispose();
            }
        };
        window.undoButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        window.undoButton.setToolTipText("Undo last action");
        window.undoButton.setIcon(new ImageIcon(
                new ImageIcon("resources/icons/undo.png").getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH)));
        window.undoButton.setPreferredSize(new Dimension(100, 50));
        window.undoButton.setFocusPainted(false);
        window.undoButton.setBorderPainted(false);
        window.undoButton.setContentAreaFilled(false);
        window.undoButton.setOpaque(false);
        window.undoButton.addActionListener(e -> GameManager.getInstance().getCommandManager().undo());

        window.pauseButton = new JButton();
        window.pauseButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        window.pauseButton.setPreferredSize(new Dimension(50, 50));
        ImageIcon pauseIcon = new ImageIcon("resources/icons/pause.png");
        Image scaledPause = pauseIcon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
        window.pauseButton.setIcon(new ImageIcon(scaledPause));
        window.pauseButton.setBackground(new Color(255, 165, 0));
        window.pauseButton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        window.pauseButton.setContentAreaFilled(false);
        window.pauseButton.setOpaque(true);
        window.pauseButton.setFocusPainted(false);
        window.pauseButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                window.pauseButton.setBackground(new Color(255, 140, 0));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                window.pauseButton.setBackground(new Color(255, 165, 0));
            }
        });

        // Labels
        window.gameMessageLabel = new JLabel(Texts.welcomeMessage[language], SwingConstants.CENTER);
        window.gameMessageLabel.setFont(new Font("Arial", Font.BOLD, 26));
        window.gameMessageLabel.setForeground(Color.WHITE);

        window.specialMessageLabel = new JLabel("", SwingConstants.CENTER);
        window.specialMessageLabel.setFont(new Font("Arial", Font.BOLD, 32));
        window.specialMessageLabel.setForeground(Color.WHITE);

        // Dealer Score Panel
        window.dealerScorePanel = new JPanel(new GridLayout(2, 1));
        window.dealerScorePanel.setOpaque(false);

        window.dealerScoreLabel = window.createStyledLabel(Texts.guiDealerScore[language]);
        window.dealerBalanceLabel = window.createStyledLabel(Texts.dealerBalance[language] + " $1000");
        window.dealerBetLabel = window.createStyledLabel(Texts.dealerBet[language] + " $0");

        JPanel scoreRow = new JPanel(new FlowLayout(FlowLayout.CENTER));
        scoreRow.add(window.dealerScoreLabel);
        scoreRow.setOpaque(false);

        JPanel balanceRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        balanceRow.add(window.dealerBalanceLabel);
        balanceRow.add(window.dealerBetLabel);
        balanceRow.setOpaque(false);

        window.dealerScorePanel.add(scoreRow);
        window.dealerScorePanel.add(balanceRow);

        // Bet Field + Label
        window.betField = new JTextField(5);
        window.betField.setPreferredSize(new Dimension(350, 40));
        window.betField.setMaximumSize(new Dimension(300, 30));
        window.betField.setFont(new Font("Arial", Font.PLAIN, 24));
        window.betField.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));

        window.enterBetLabel = new JLabel(Texts.enterBet[language]);
        window.enterBetLabel.setFont(new Font("Arial", Font.BOLD, 28));
        window.enterBetLabel.setForeground(Color.WHITE);

        // Assemble Bet Panel
        window.betPanel.setOpaque(false);
        window.betPanel.add(window.enterBetLabel);
        window.betPanel.add(window.betField);
        window.betPanel.add(window.placeBetButton);
        window.betPanel.add(Box.createHorizontalGlue());
        window.betPanel.add(window.betLabel);
        window.betPanel.add(window.balanceLabel);

        // Assemble Button Panel
        window.buttonPanel.setOpaque(false);
        window.buttonPanel.add(window.hitButton);
        window.buttonPanel.add(window.standButton);
        window.buttonPanel.add(window.newGameButton);
        window.buttonPanel.add(window.undoButton);
        window.buttonPanel.add(window.pauseButton);
    }
}

package main.view;

import main.model.Card;
import javax.swing.*;

import main.model.Player;

import java.awt.*;
import java.util.List;

                          
class PlayersPanel extends JPanel {
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); // Always call super first
        Graphics2D g2d = (Graphics2D) g.create();
        Color transparent = new Color(0, 0, 0, 0);
        Color darkGreen = new Color(0, 100, 0, 180);
        GradientPaint gp = new GradientPaint(0, 0, darkGreen, getWidth(), getHeight(), transparent);
        g2d.setPaint(gp);
        g2d.fillRect(0, 0, getWidth(), getHeight());
        g2d.dispose();
    }

    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private int gameHeight = (int) screenSize.getHeight();
    private int gameWidth = (int)screenSize.getWidth();
    private int cardWidth = (int) (gameWidth * 0.07);
    private int cardHeight = (int) (gameHeight * 0.16);
    private int cardFontSize = gameWidth / 95;

    public PlayersPanel() {
        setOpaque(false);
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setBackground(new Color(0, 0, 0, 0)); // ✅ Fully transparent background
    }
    

    public void updatePanel(List<Player> players) {
        removeAll();
        players.forEach(player -> {
            // Player container setup
            JPanel playerContainer = new JPanel(new BorderLayout(5, 5));
            playerContainer.setOpaque(false); // ✅ Transparent
            playerContainer.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    
            // Player info section
            JPanel infoPanel = new JPanel();
            infoPanel.setOpaque(false); // ✅ Transparent
            infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
            infoPanel.add(createPlayerLabel(player));
    
            // Cards display
            JComponent handArea = createHandArea(player);
    
            // Assemble components
            playerContainer.add(infoPanel, BorderLayout.NORTH);
            playerContainer.add(handArea, BorderLayout.CENTER);
    
            add(playerContainer);
            add(Box.createHorizontalStrut(10));
        });
        revalidate();
        repaint();
    }
    

    private JLabel createPlayerLabel(Player player) {
        JLabel label = new JLabel(String.format(
            "%s - $%d (BET: $%d)", 
            player.getName(), 
            player.getBalance(), 
            player.getCurrentBet()
        ), SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 26));
        label.setForeground(Color.WHITE);
        label.setOpaque(false); // ✅ Just in case
        label.setBorder(BorderFactory.createEmptyBorder(5, 5, 0, 5));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        return label;
    }

    private JComponent createHandArea(Player player) {
        JPanel handPanel = new JPanel(new BorderLayout());
        handPanel.setOpaque(false);
    
        // Cards panel setup
        JPanel cardsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        cardsPanel.setOpaque(false);
    
        // Add cards to panel
        for (Card card : player.getHand()) {
            JPanel cardPanel = createCardPanel(card);
            cardPanel.setPreferredSize(new Dimension(cardWidth, cardHeight));
            cardsPanel.add(cardPanel);
        }
    
        // Total score label
        JLabel totalLabel = new JLabel("TOTAL: " + player.calculateScore(), SwingConstants.CENTER);
        totalLabel.setForeground(Color.WHITE);
        totalLabel.setFont(new Font("Arial", Font.BOLD, 26));

    
        // Assemble hand panel
        handPanel.add(cardsPanel, BorderLayout.CENTER);
        handPanel.add(totalLabel, BorderLayout.SOUTH);
    
        // Scroll pane configuration
        JScrollPane scrollPane = new JScrollPane(handPanel);
        scrollPane.setPreferredSize(new Dimension(gameWidth/2, cardHeight + 60));
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    
        return scrollPane;
    }

    private JPanel createCardPanel(Card card) {
        JPanel cardPanel = new JPanel();
        cardPanel.setPreferredSize(new Dimension(cardWidth, cardHeight));
        cardPanel.setBackground(Color.WHITE);
        cardPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 4));

        JLabel rankLabel = new JLabel(card.getRank(), SwingConstants.CENTER);
        rankLabel.setFont(new Font("Arial", Font.BOLD, cardFontSize));

        JLabel suitLabel = new JLabel(card.getSuit(), SwingConstants.CENTER);
        suitLabel.setFont(new Font("Arial", Font.BOLD, cardFontSize));

        cardPanel.setLayout(new BorderLayout());
        cardPanel.add(rankLabel, BorderLayout.CENTER);
        cardPanel.add(suitLabel, BorderLayout.SOUTH);

        return cardPanel;
    }
}

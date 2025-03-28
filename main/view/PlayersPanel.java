package main.view;

import main.model.Card;
import javax.swing.*;

import main.model.Player;

import java.awt.*;
import java.util.List;

                          
class PlayersPanel extends JPanel {
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private int gameHeight = (int) screenSize.getHeight();
    private int gameWidth = (int)screenSize.getWidth();
    private int cardWidth = (int) (gameWidth * 0.07);
    private int cardHeight = (int) (gameHeight * 0.16);
    private int cardFontSize = gameWidth / 95;

    public PlayersPanel() {
        setOpaque(true);
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setBackground(new Color(34, 139, 34));

    }

    public void updatePanel(List<Player> players) {
        removeAll();
        players.forEach(player -> {
            // Player container setup
            JPanel playerContainer = new JPanel(new BorderLayout(5, 5));
            playerContainer.setBackground(new Color(34, 139, 34));
            playerContainer.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    
            // Player info section
            JPanel infoPanel = new JPanel();
            infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
            infoPanel.add(createPlayerLabel(player));
            infoPanel.setBackground(new Color(34, 139, 34));

    
            // Cards display using createHandArea
            JComponent handArea = createHandArea(player);
    
            // Assemble components
            playerContainer.add(infoPanel, BorderLayout.NORTH);
            playerContainer.add(handArea, BorderLayout.CENTER);
    
            // Add spacing between players
            add(playerContainer);
            add(Box.createHorizontalStrut(20));
        });
        revalidate();
        repaint();
    }

    private JLabel createPlayerLabel(Player player) {
        JLabel label = new JLabel(String.format(
            "%s - $%d (Bet: $%d)", 
            player.getName(), 
            player.getBalance(), 
            player.getCurrentBet()
        ));
        label.setFont(new Font("Arial", Font.BOLD, 14));
        label.setBackground(new Color(34, 139, 34));

        return label;
    }

    private JComponent createHandArea(Player player) {
        JPanel handPanel = new JPanel(new BorderLayout());
        handPanel.setOpaque(false);
    
        // Cards panel setup
        JPanel cardsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        cardsPanel.setOpaque(false);
    
        // Add cards to panel
        for (Card card : player.getHand()) {
            JPanel cardPanel = createCardPanel(card);
            cardPanel.setPreferredSize(new Dimension(cardWidth, cardHeight));
            cardsPanel.add(cardPanel);
        }
    
        // Total score label
        JLabel totalLabel = new JLabel("Total: " + player.calculateScore(), SwingConstants.LEFT);
        totalLabel.setForeground(Color.WHITE);
        totalLabel.setFont(new Font("Arial", Font.BOLD, 16));

    
        // Assemble hand panel
        handPanel.add(cardsPanel, BorderLayout.CENTER);
        handPanel.add(totalLabel, BorderLayout.SOUTH);
    
        // Scroll pane configuration
        JScrollPane scrollPane = new JScrollPane(handPanel);
        scrollPane.setPreferredSize(new Dimension(gameWidth/2, cardHeight + 50));
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
        suitLabel.setFont(new Font("Arial", Font.PLAIN, cardFontSize));

        cardPanel.setLayout(new BorderLayout());
        cardPanel.add(rankLabel, BorderLayout.CENTER);
        cardPanel.add(suitLabel, BorderLayout.SOUTH);

        return cardPanel;
    }
}

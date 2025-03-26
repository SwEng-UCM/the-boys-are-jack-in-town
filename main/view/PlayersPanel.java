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
    private int cardWidth = (int) (gameWidth * 0.05);
    private int cardHeight = (int) (gameHeight * 0.11);
    private int cardFontSize = gameWidth / 100;

    public PlayersPanel() {
        setOpaque(true);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setBackground(new Color(34, 139, 34));

    }

    public void updatePanel(List<Player> players) {
        removeAll();
        players.forEach(player -> {
            JPanel panel = new JPanel(new BorderLayout(5, 5));
            panel.add(createPlayerLabel(player), BorderLayout.NORTH);
            panel.add(createHandArea(player), BorderLayout.CENTER);
            add(panel);
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
        return label;
    }

    private JComponent createHandArea(Player player) {
        JPanel handPanel = new JPanel(new BorderLayout());

        
        // Panel to hold the cards
        JPanel cardsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        cardsPanel.setOpaque(false);

        
        // Add individual card panels
        for (Card card : player.getHand()) {
            cardsPanel.add(createCardPanel(card));

        }
        
        // Add total score at bottom
        JLabel totalLabel = new JLabel("Total: " + player.calculateScore(), SwingConstants.CENTER);
        totalLabel.setOpaque(true);
        totalLabel.setBackground(new Color(34, 139, 34));
        totalLabel.setForeground(Color.WHITE);
        totalLabel.setFont(new Font("Arial", Font.BOLD, 16));
        
        handPanel.add(cardsPanel, BorderLayout.CENTER);
        handPanel.add(totalLabel, BorderLayout.SOUTH);
        handPanel.setOpaque(false);
    
        // Wrap in scroll pane for many cards
        JScrollPane scrollPane = new JScrollPane(handPanel);
        scrollPane.setBackground(new Color(34, 139, 34));
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        
        JScrollBar hScroll = scrollPane.getHorizontalScrollBar();
        hScroll.setBackground(new Color(34, 139, 34));
        hScroll.setForeground(Color.WHITE);

        return scrollPane;
    }
    
    // Remove the formatHand() method since we're not using it anymore


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

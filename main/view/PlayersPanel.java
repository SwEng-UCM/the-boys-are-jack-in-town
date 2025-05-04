package main.view;

import main.model.Card;
import javax.swing.*;
import main.model.Player;
import java.awt.*;
import java.util.List;
import static main.view.BlackJackMenu.language;

/**
 * The PlayersPanel class represents a custom panel for displaying player information
 * and their respective hands of cards in the game. It provides a visually appealing
 * layout with a gradient background and dynamically updates based on the current game state.
 */
class PlayersPanel extends JPanel {
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        Color transparent = new Color(0, 0, 0, 0);
        Color darkGreen = new Color(0, 100, 0, 180);
        GradientPaint gp = new GradientPaint(0, 0, darkGreen, getWidth(), getHeight(), transparent);
        g2d.setPaint(gp);
        g2d.fillRect(0, 0, getWidth(), getHeight());
        g2d.dispose();
    }

    /**
     * Constructs a PlayersPanel instance.
     * Initializes the panel with a transparent background and a horizontal layout.
     */
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private int gameHeight = (int) screenSize.getHeight();
    private int gameWidth = (int)screenSize.getWidth();
    private int cardWidth = (int) (gameWidth * 0.07);
    private int cardHeight = (int) (gameHeight * 0.16);
    private int cardFontSize = gameWidth / 95;

    public PlayersPanel() {
        setOpaque(false);
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setBackground(new Color(0, 0, 0, 0));
    }
    
    /**
     * Updates the panel with the provided list of players.
     * Dynamically creates and displays player information and their hands of cards.
     *
     * @param players A list of Player objects representing the current players in the game.
     */
    public void updatePanel(List<Player> players) {
        removeAll();
        players.forEach(player -> {
            // Player container setup
            JPanel playerContainer = new JPanel(new BorderLayout(5, 5));
            playerContainer.setOpaque(false);
            playerContainer.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    
            // Player info section
            JPanel infoPanel = new JPanel();
            infoPanel.setOpaque(false);
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
    
    /**
     * Creates a JLabel displaying the player's name, balance, and current bet.
     *
     * @param player The Player object whose information is displayed.
     * @return A JLabel containing the player's information.
     */
    private JLabel createPlayerLabel(Player player) {
        JLabel label = new JLabel(String.format(
            "%s - $%d ("+Texts.bet[language]+" $%d)",
            player.getName(), 
            player.getBalance(), 
            player.getCurrentBet()
        ), SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 26));
        label.setForeground(Color.WHITE);
        label.setOpaque(false);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        return label;
    }

    /**
     * Creates a component displaying the player's hand of cards and total score.
     *
     * @param player The Player object whose hand is displayed.
     * @return A JComponent containing the player's hand and total score.
     */
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
        JLabel totalLabel = new JLabel(Texts.total[language]+": " + player.calculateScore(), SwingConstants.CENTER);
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

    /**
     * Creates a JPanel representing a single card with its rank and suit.
     *
     * @param card The Card object to display.
     * @return A JPanel containing the card's rank and suit.
     */
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

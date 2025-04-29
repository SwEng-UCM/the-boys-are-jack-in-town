package main.view;

import main.model.Card;
import javax.swing.*;
import main.model.Player;
import java.awt.*;
import java.util.List;

/**
 * The PlayersPanel class represents a custom JPanel that displays the players' information, 
 * including their names, balances, bets, and card hands during a game.
 * The panel dynamically updates as the players' status changes.
 */
class PlayersPanel extends JPanel {

    /**
     * Paints the background of the panel with a gradient from dark green to transparent.
     * This method is overridden to customize the background painting.
     * 
     * @param g the Graphics object to paint on
     */
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

    // Dimensions for various game components (screen size, card size, font size)
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private int gameHeight = (int) screenSize.getHeight();
    private int gameWidth = (int) screenSize.getWidth();
    private int cardWidth = (int) (gameWidth * 0.07);
    private int cardHeight = (int) (gameHeight * 0.16);
    private int cardFontSize = gameWidth / 95;

    /**
     * Constructs a PlayersPanel that displays a list of players and their hands.
     * This constructor sets the layout and ensures the panel has a transparent background.
     */
    public PlayersPanel() {
        setOpaque(false);
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setBackground(new Color(0, 0, 0, 0)); // Fully transparent background
    }

    /**
     * Updates the panel to display the given list of players and their current hands.
     * The method rebuilds the panel to show each player's information and card hand.
     * 
     * @param players the list of players whose information and hands need to be displayed
     */
    public void updatePanel(List<Player> players) {
        removeAll(); // Clear the current panel content
        players.forEach(player -> {
            // Player container setup
            JPanel playerContainer = new JPanel(new BorderLayout(5, 5));
            playerContainer.setOpaque(false); // Transparent
            playerContainer.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    
            // Player info section
            JPanel infoPanel = new JPanel();
            infoPanel.setOpaque(false); // Transparent
            infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
            infoPanel.add(createPlayerLabel(player));
    
            // Cards display section
            JComponent handArea = createHandArea(player);
    
            // Assemble player components
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
     * The label is styled with a font and alignment for proper display.
     * 
     * @param player the player whose information will be displayed on the label
     * @return a JLabel containing the player's information
     */
    private JLabel createPlayerLabel(Player player) {
        JLabel label = new JLabel(String.format(
            "%s - $%d (BET: $%d)", 
            player.getName(), 
            player.getBalance(), 
            player.getCurrentBet()
        ), SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 26));
        label.setForeground(Color.WHITE);
        label.setOpaque(false); // Transparent
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        return label;
    }

    /**
     * Creates a scrollable area displaying the player's hand of cards and total score.
     * Each card in the hand is displayed in a separate panel, and the total score is shown at the bottom.
     * 
     * @param player the player whose hand and score are displayed
     * @return a JScrollPane containing the player's hand and total score
     */
    private JComponent createHandArea(Player player) {
        JPanel handPanel = new JPanel(new BorderLayout());
        handPanel.setOpaque(false);
    
        // Cards panel setup
        JPanel cardsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        cardsPanel.setOpaque(false);
    
        // Add cards to the cards panel
        for (Card card : player.getHand()) {
            JPanel cardPanel = createCardPanel(card);
            cardPanel.setPreferredSize(new Dimension(cardWidth, cardHeight));
            cardsPanel.add(cardPanel);
        }
    
        // Display total score
        JLabel totalLabel = new JLabel("TOTAL: " + player.calculateScore(), SwingConstants.CENTER);
        totalLabel.setForeground(Color.WHITE);
        totalLabel.setFont(new Font("Arial", Font.BOLD, 26));
    
        // Assemble the hand panel
        handPanel.add(cardsPanel, BorderLayout.CENTER);
        handPanel.add(totalLabel, BorderLayout.SOUTH);
    
        // Scroll pane configuration
        JScrollPane scrollPane = new JScrollPane(handPanel);
        scrollPane.setPreferredSize(new Dimension(gameWidth / 2, cardHeight + 60));
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    
        return scrollPane;
    }

    /**
     * Creates a JPanel displaying a single card with its rank and suit.
     * Each card is represented as a simple panel with labels for the rank and suit.
     * 
     * @param card the card to be displayed
     * @return a JPanel containing the card's rank and suit
     */
    private JPanel createCardPanel(Card card) {
        JPanel cardPanel = new JPanel();
        cardPanel.setPreferredSize(new Dimension(cardWidth, cardHeight));
        cardPanel.setBackground(Color.WHITE);
        cardPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 4));

        // Display card rank and suit
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

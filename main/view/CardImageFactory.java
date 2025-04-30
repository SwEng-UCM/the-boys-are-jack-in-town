package main.view;

import main.model.Card;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class CardImageFactory {

    public static JPanel createCardPanel(Card card, int width, int height) {
        JPanel cardPanel = new JPanel(new BorderLayout());
        cardPanel.setPreferredSize(new Dimension(width, height));
        cardPanel.setBackground(Color.WHITE);
        cardPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

        JLabel imageLabel;

        if (card.getType() != Card.CardType.STANDARD) {
            imageLabel = new JLabel(loadSpecialCardImage(card, width, height));
        } else {
            imageLabel = new JLabel(loadStandardCardImage(card, width, height));
        }

        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setVerticalAlignment(SwingConstants.CENTER);
        cardPanel.add(imageLabel, BorderLayout.CENTER);

        return cardPanel;
    }

    private static ImageIcon loadStandardCardImage(Card card, int width, int height) {
        String rank = card.getRank().toLowerCase();
        String suit = card.getSuit().toLowerCase();
        String path = "/resources/img/cards/" + rank + "_of_" + suit + ".png";

        java.net.URL imageURL = CardImageFactory.class.getResource(path);
        if (imageURL == null) {
            System.err.println("Missing image: " + path);
            return createFallbackIcon(width, height, rank + " of " + suit);
        }

        return scaleIcon(new ImageIcon(imageURL), width, height);
    }

    private static ImageIcon loadSpecialCardImage(Card card, int width, int height) {
        String filename = switch (card.getType()) {
            case BLACKJACK_BOMB -> "blackjack_bomb.png";
            case SPLIT_ACE -> "split_ace.png";
            case JOKER_WILD -> "joker_wild.png";
            default -> "unknown.png";
        };
        String path = "/resources/img/cards/" + filename;

        java.net.URL imageURL = CardImageFactory.class.getResource(path);
        if (imageURL == null) {
            System.err.println("Missing special image: " + path);
            return createFallbackIcon(width, height, card.toString());
        }

        return scaleIcon(new ImageIcon(imageURL), width, height);
    }

    private static ImageIcon scaleIcon(ImageIcon icon, int width, int height) {
        Image scaled = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(scaled);
    }

    private static ImageIcon createFallbackIcon(int width, int height, String labelText) {
        BufferedImage placeholder = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = placeholder.createGraphics();
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(0, 0, width, height);
        g.setColor(Color.RED);
        g.setFont(new Font("Arial", Font.BOLD, 14));
        g.drawString("Missing", 10, height / 2 - 10);
        g.drawString(labelText, 10, height / 2 + 10);
        g.dispose();
        return new ImageIcon(placeholder);
    }
}

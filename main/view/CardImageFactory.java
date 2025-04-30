package main.view;

import main.model.Card;

import javax.swing.*;
import java.awt.*;

public class CardImageFactory {
    public static JPanel createCardPanel(Card card, int width, int height) {
        JPanel cardPanel = new JPanel(new BorderLayout());
        cardPanel.setPreferredSize(new Dimension(width, height));
        cardPanel.setBackground(Color.WHITE);
        cardPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

        JLabel imageLabel;
        if (card.getType() != Card.CardType.STANDARD) {
            imageLabel = new JLabel(getSpecialCardIcon(card, width, height));
        } else {
            imageLabel = new JLabel(getStandardCardIcon(card, width, height));
        }

        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setVerticalAlignment(SwingConstants.CENTER);

        cardPanel.add(imageLabel, BorderLayout.CENTER);
        return cardPanel;
    }

    private static ImageIcon getStandardCardIcon(Card card, int width, int height) {
        String rank = card.getRank().toLowerCase();
        String suit = card.getSuit().toLowerCase();
        String path = "resources/img/cards/" + rank + "_of_" + suit + ".png";

        ImageIcon icon = new ImageIcon(path);
        Image scaled = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(scaled);
    }

    private static ImageIcon getSpecialCardIcon(Card card, int width, int height) {
        String filename = switch (card.getType()) {
            case BLACKJACK_BOMB -> "blackjack_bomb.png";
            case SPLIT_ACE -> "split_ace.png";
            case JOKER_WILD -> "joker_wild.png";
            default -> "unknown.png";
        };
        String path = "resources/img/cards/" + filename;

        ImageIcon icon = new ImageIcon(path);
        Image scaled = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(scaled);
    }
}

package main.view;

import main.model.Card;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class CardImageRenderer {
    private static final String BASE_PATH = "resources/img/cards/";
    private static final Map<String, ImageIcon> imageCache = new HashMap<>();

    public static JPanel createCardPanel(Card card, int width, int height) {
        JPanel cardPanel = new JPanel(new BorderLayout());
        cardPanel.setPreferredSize(new Dimension(width, height));
        cardPanel.setOpaque(false);

        String imagePath = getCardImagePath(card);
        ImageIcon icon = loadImageIcon(imagePath, width, height);

        JLabel imageLabel = new JLabel(icon);
        cardPanel.add(imageLabel, BorderLayout.CENTER);

        return cardPanel;
    }

    private static ImageIcon loadImageIcon(String path, int width, int height) {
        if (imageCache.containsKey(path)) {
            return imageCache.get(path);
        }

        try {
            ImageIcon original = new ImageIcon(path);
            Image scaled = original.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
            ImageIcon scaledIcon = new ImageIcon(scaled);
            imageCache.put(path, scaledIcon);
            return scaledIcon;
        } catch (Exception e) {
            System.err.println("Failed to load image: " + path);
            return new ImageIcon();
        }
    }

    private static String getCardImagePath(Card card) {
        String filename;

        switch (card.getType()) {
            case BLACKJACK_BOMB:
                filename = "blackjack_bomb.png";
                break;
            case SPLIT_ACE:
                filename = "split_ace.png";
                break;
            case JOKER_WILD:
                filename = "joker_wild.png";
                break;
            case STANDARD:
            default:
                filename = card.getRank().toLowerCase() + "_of_" + card.getSuit().toLowerCase() + ".png";
                break;
        }
        return BASE_PATH + filename;
    }
}

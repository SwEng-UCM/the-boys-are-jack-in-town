package main.view;

import main.model.Card;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
//import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class CardImageRenderer {

    //private static final String IMAGE_BASE_PATH = "src/main/resources/cards/";  // ✅ updated path

    public static JPanel createCardPanel(Card card, int width, int height) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setPreferredSize(new Dimension(width, height));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

        ImageIcon icon = loadImageIcon(getImagePath(card));

        if (icon != null) {
            Image scaled = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
            panel.add(new JLabel(new ImageIcon(scaled)), BorderLayout.CENTER);
        } else {
            JLabel fallback = new JLabel(card.getRank() + " of " + card.getSuit(), SwingConstants.CENTER);
            fallback.setFont(new Font("Arial", Font.BOLD, 14));
            panel.add(fallback, BorderLayout.CENTER);
        }

        return panel;
    }

    private static String getImagePath(Card card) {
        String rank = card.getRank().toLowerCase().replace(" ", "_");
        String suit = card.getSuit().toLowerCase().replace(" ", "_");
        return "/cards/" + rank + "_of_" + suit;
    }
    private static ImageIcon loadImageIcon(String path) {
        try {
            java.net.URL cardsUrl = CardImageRenderer.class.getResource(path);
            if (cardsUrl != null) {
                return new ImageIcon(cardsUrl);
            } else {
                System.err.println("Image not found at: " + path);
            }
        } catch (Exception e) {
            System.err.println("Error loading image: " + e.getMessage());
        }
        return null;
    }
    
}

package main.controller;

import javax.swing.*;


import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class CardImageLoader {
    private static final Map<String, ImageIcon> cardImages = new HashMap<>();

    public static void loadCardImages() {
        String[] ranks = {"ace", "2", "3", "4", "5", "6", "7", "8", "9", "10", "jack", "queen", "king"};
        String[] suits = {"hearts", "diamonds", "clubs", "spades"};

        for (String rank : ranks) {
            for (String suit : suits) {
                String key = rank + "_of_" + suit;
                String path =  "src/main/java/main/view/img/cards/" + key + ".png"; // ✅ correct path
                try {
                    ImageIcon icon = new ImageIcon(CardImageLoader.class.getResource(path));
                    cardImages.put(key, icon);
                } catch (Exception e) {
                    System.err.println("Failed to load image: " + path);
                }
            }
        }
    }

    public static ImageIcon getCardImage(String rank, String suit) {
        String path = "src/main/java/main/view/img/cards/" + rank.toLowerCase() + "_of_" + suit.toLowerCase() + ".png";
        File imageFile = new File(path);
        if (imageFile.exists()) {
         return new ImageIcon(imageFile.getAbsolutePath());
     } else {
        System.err.println("Card image not found: " + path);
        return null;
    }
    }
}

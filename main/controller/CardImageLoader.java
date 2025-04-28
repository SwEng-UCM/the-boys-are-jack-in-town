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
                String path =  "/img/cards/" + key; // ✅ correct path
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
        String key = rank.toLowerCase() + "_of_" + suit.toLowerCase();
        ImageIcon icon = cardImages.get(key);
        if (icon == null) {
        System.err.println("Card image not found: " + key);
        }
        return icon;
    }
    
}

package main.controller;

import main.view.BlackJackMenu;
import javax.swing.SwingUtilities;

/**
 * Main class to start the Blackjack game.
 */
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            BlackJackMenu menu = new BlackJackMenu();
            menu.setVisible(true);
        });
    }
}
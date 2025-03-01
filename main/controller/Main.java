package main.controller;

import main.view.BlackjackGUI;

import javax.swing.SwingUtilities;

public class Main {
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GameManager gameManager = new GameManager();
            BlackjackGUI gui = new BlackjackGUI(gameManager);
            gui.setVisible(true);
        });
    }
}
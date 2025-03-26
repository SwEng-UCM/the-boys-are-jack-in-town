package main.view;

import main.model.Card;
import javax.swing.*;

import main.model.Player;

import java.awt.*;
import java.util.List;

                          
class PlayersPanel extends JPanel {
    public PlayersPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }

    public void updatePanel(List<Player> players) {
        removeAll();
        players.forEach(player -> {
            JPanel panel = new JPanel(new BorderLayout(5, 5));
            panel.add(createPlayerLabel(player), BorderLayout.NORTH);
            panel.add(createHandArea(player), BorderLayout.CENTER);
            add(panel);
        });
        revalidate();
        repaint();
    }

    private JLabel createPlayerLabel(Player player) {
        JLabel label = new JLabel(String.format(
            "%s - $%d (Bet: $%d)", 
            player.getName(), 
            player.getBalance(), 
            player.getCurrentBet()
        ));
        label.setFont(new Font("Arial", Font.BOLD, 14));
        return label;
    }

    private JComponent createHandArea(Player player) {
        JTextArea area = new JTextArea(formatHand(player), 3, 20);
        area.setEditable(false);
        return new JScrollPane(area);
    }

    private String formatHand(Player player) {
        return String.join("\n", 
            player.getHand().stream()
                .map(Card::toString)
                .toList()
        ) + "\nTotal: " + player.calculateScore();
    }
}

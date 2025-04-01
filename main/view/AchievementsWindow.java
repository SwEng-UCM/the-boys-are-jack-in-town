package main.view;

import main.DAO.AchievementDAO;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class AchievementsWindow extends JFrame {

    public AchievementsWindow(int userId) {
        setTitle("Achievements");
        setSize(400, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        AchievementDAO dao = new AchievementDAO();
        List<String> achievements = dao.getUserAchievements(userId);

        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        for (String achievement : achievements) {
            JLabel label = new JLabel("🏆 " + achievement);
            label.setFont(new Font("Arial", Font.PLAIN, 18));
            listPanel.add(label);
        }

        JScrollPane scrollPane = new JScrollPane(listPanel);
        add(scrollPane, BorderLayout.CENTER);
    }
}

package main.DAO;

import main.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/*
 * DAO class is fetching the data from the database and returning it to the controller.
 * It is also responsible for updating the database with the data from the controller.
 */
public class AchievementDAO {

    private final Connection conn;

    // connection to the database
    public AchievementDAO() {
        try {
            conn = DatabaseConnection.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to connect to DB", e);
        }
    }

    public List<String> getUserAchievements(int userId) {
        List<String> achievements = new ArrayList<>();
        String sql = "SELECT name FROM achievements a JOIN user_achievements ua ON a.achievement_id = ua.achievement_id WHERE ua.user_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                achievements.add(rs.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return achievements;
    }

    public void unlockAchievement(int userId, String achievementName) {
        try {
            String getIdSQL = "SELECT achievement_id FROM achievements WHERE name = ?";
            PreparedStatement getIdStmt = conn.prepareStatement(getIdSQL);
            getIdStmt.setString(1, achievementName);
            ResultSet rs = getIdStmt.executeQuery();
            if (rs.next()) {
                int achievementId = rs.getInt("achievement_id");

                // Insert if not already unlocked
                String insertSQL = "INSERT IGNORE INTO user_achievements (user_id, achievement_id) VALUES (?, ?)";
                PreparedStatement insertStmt = conn.prepareStatement(insertSQL);
                insertStmt.setInt(1, userId);
                insertStmt.setInt(2, achievementId);
                insertStmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

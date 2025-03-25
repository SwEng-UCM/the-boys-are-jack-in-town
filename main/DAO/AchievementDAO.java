package main.DAO;

import main.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/*
 * DAO = Data Access Object
 * This class is responsible for handling the database operations related to achievements.
 */
public class AchievementDAO {

        /**
     * Retrieves the achievements of a specific user.
     *
     * @param userId The ID of the user.
     * @return A list of achievements (name and description) for the user.
     */
    public List<String> getUserAchievements(int userId) {
        List<String> achievements = new ArrayList<>();
        String query = """
            SELECT a.name, a.description
            FROM achievements a
            JOIN user_achievements ua ON a.achievement_id = ua.achievement_id
            WHERE ua.user_id = ?
        """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    String name = resultSet.getString("name");
                    String description = resultSet.getString("description");
                    achievements.add(name + ": " + description);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return achievements;
    }

        /**
     * Adds a new achievement to the database.
     *
     * @param name        The name of the achievement.
     * @param description The description of the achievement.
     * @return True if the achievement was added successfully, false otherwise.
     */
    public boolean addAchievement(String name, String description) {
        String query = "INSERT INTO achievements (name, description) VALUES (?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, name);
            statement.setString(2, description);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

        /**
     * Links a user to an achievement.
     *
     * @param userId        The ID of the user.
     * @param achievementId The ID of the achievement.
     * @return True if the link was created successfully, false otherwise.
     */
    public boolean addUserAchievement(int userId, int achievementId) {
        String query = "INSERT INTO user_achievements (user_id, achievement_id) VALUES (?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);
            statement.setInt(2, achievementId);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}

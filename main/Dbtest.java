// package main;

// import java.sql.Connection;
// import java.sql.PreparedStatement;
// import java.sql.ResultSet;
// import java.sql.SQLException;

// public class Dbtest {
//     public static void main(String[] args) {
//         try (Connection connection = DatabaseConnection.getConnection()) {
//             System.out.println("Connected to the database.");

//             // Insert a user
//             String insertUserSQL = "INSERT INTO users () VALUES ()";
//             try (PreparedStatement insertUserStmt = connection.prepareStatement(insertUserSQL, PreparedStatement.RETURN_GENERATED_KEYS)) {
//                 insertUserStmt.executeUpdate();
//                 try (ResultSet generatedKeys = insertUserStmt.getGeneratedKeys()) {
//                     if (generatedKeys.next()) {
//                         int userId = generatedKeys.getInt(1);
//                         System.out.println("Inserted user with ID: " + userId);

//                         // Insert an achievement
//                         String insertAchievementSQL = "INSERT INTO achievements (name, description) VALUES (?, ?)";
//                         try (PreparedStatement insertAchievementStmt = connection.prepareStatement(insertAchievementSQL, PreparedStatement.RETURN_GENERATED_KEYS)) {
//                             insertAchievementStmt.setString(1, "First Win");
//                             insertAchievementStmt.setString(2, "Win your first game of Blackjack.");
//                             insertAchievementStmt.executeUpdate();
//                             try (ResultSet achievementKeys = insertAchievementStmt.getGeneratedKeys()) {
//                                 if (achievementKeys.next()) {
//                                     int achievementId = achievementKeys.getInt(1);
//                                     System.out.println("Inserted achievement with ID: " + achievementId);

//                                     // Link user and achievement
//                                     String insertUserAchievementSQL = "INSERT INTO user_achievements (user_id, achievement_id) VALUES (?, ?)";
//                                     try (PreparedStatement insertUserAchievementStmt = connection.prepareStatement(insertUserAchievementSQL)) {
//                                         insertUserAchievementStmt.setInt(1, userId);
//                                         insertUserAchievementStmt.setInt(2, achievementId);
//                                         insertUserAchievementStmt.executeUpdate();
//                                         System.out.println("Linked user ID " + userId + " with achievement ID " + achievementId);
//                                     }
//                                 }
//                             }
//                         }
//                     }
//                 }
//             }

//             // Retrieve and display data
//             String selectSQL = "SELECT u.user_id, a.name, a.description FROM users u " +
//                                "JOIN user_achievements ua ON u.user_id = ua.user_id " +
//                                "JOIN achievements a ON ua.achievement_id = a.achievement_id";
//             try (PreparedStatement selectStmt = connection.prepareStatement(selectSQL);
//                  ResultSet resultSet = selectStmt.executeQuery()) {
//                 while (resultSet.next()) {
//                     int userId = resultSet.getInt("user_id");
//                     String achievementName = resultSet.getString("name");
//                     String achievementDescription = resultSet.getString("description");
//                     System.out.println("User ID: " + userId + ", Achievement: " + achievementName + ", Description: " + achievementDescription);
//                 }
//             }
//         } catch (SQLException e) {
//             e.printStackTrace();
//         }
//     }
// }
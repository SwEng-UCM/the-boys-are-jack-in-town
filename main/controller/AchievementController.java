package main.controller;

import main.DAO.AchievementDAO;

import java.util.List;

/*
 * Controller class for handling achievements.
 * This class is responsible for handling the achievements of the player.
 * Acts as a bridge between the AchievementDAO and the GameManager.
 */
public class AchievementController {
    private final AchievementDAO achievementDAO;

    public AchievementController() {
        this.achievementDAO = new AchievementDAO();
    }

        /**
     * Retrieves the achievements of a specific user.
     *
     * @param userId The ID of the user.
     * @return A list of achievements (name and description) for the user.
     */
    public List<String> getUserAchievements(int userId) {
        return achievementDAO.getUserAchievements(userId);
    }

    /**
     * Adds a new achievement to the database.
     *
     * @param name        The name of the achievement.
     * @param description The description of the achievement.
     * @return True if the achievement was added successfully, false otherwise.
     */
    public boolean addAchievement(String name, String description) {
        return achievementDAO.addAchievement(name, description);
    }

    /**
     * Links a user to an achievement.
     *
     * @param userId        The ID of the user.
     * @param achievementId The ID of the achievement.
     * @return True if the link was created successfully, false otherwise.
     */
    public boolean addUserAchievement(int userId, int achievementId) {
        return achievementDAO.addUserAchievement(userId, achievementId);
    }
}

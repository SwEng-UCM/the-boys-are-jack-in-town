package main.controller;

import java.util.Set;
import main.model.Badge;
/**
 * The {@code Command} interface defines a contract for encapsulating user actions
 * in the Blackjack game that can be executed and undone.
 **/
public interface Command {
    /**
     * Executes the command logic. This is typically triggered by a user action
     * such as hitting, standing, or placing a bet.
     */
    void execute();

    /**
     * Undoes the previously executed command, reverting the game state to its prior condition.
     * This allows features such as undo functionality or replays.
     */
    void undo();

    /**
     * Returns a set of {@link Badge} achievements unlocked as a result of executing this command.
     * By default, this returns an empty set if no achievements are associated.
     *
     * @return a set of unlocked badges (may be empty)
     */
    default Set<Badge> getUnlockedBadges() { return Set.of(); }
}

package main.controller;

/**
 * The {@code CommandManager} class acts as an invoker in the Command pattern.
 * It manages the execution and undoing of commands in the Blackjack game.
 */
public class CommandManager {
    private Command lastCommand;

    /**
     * Executes a given command and stores it for potential undoing later.
     *
     * @param command the command to execute
     */
    public void executeCommand(Command command) {
        command.execute();
        lastCommand = command;
    }
    public boolean canUndo() {
        return lastCommand != null;
    }
    

    /**
     * Undoes the last executed command, if any, and plays an undo sound effect.
     * After undoing, it clears the stored command reference.
     */
    public void undo() {
        if (lastCommand != null) {
            lastCommand.undo();
            AudioManager.getInstance().playSoundEffect("/resources/sounds/undo.wav");
            lastCommand = null;
        }
    }
}

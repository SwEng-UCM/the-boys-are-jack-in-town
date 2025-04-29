package main.controller;

public class CommandManager {
    private Command lastCommand;

    public void executeCommand(Command command) {
        command.execute();
        lastCommand = command;
    }

    public void undo() {
        if (lastCommand != null) {
            lastCommand.undo();
            AudioManager.getInstance().playSoundEffect("/sounds/undo.wav");
            lastCommand = null;
        }
    }
    
}

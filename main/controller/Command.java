package main.controller;

import java.util.Set;

import main.model.Badge;

public interface Command {
    void execute();
    void undo();
    default Set<Badge> getUnlockedBadges() { return Set.of(); }
}

package me.KP56.InteractiveConsole;

import java.util.Arrays;
import java.util.List;

public class Command {
    private String name;
    private List<StateType> requiredStates;
    public final CommandRunnable runnable;

    public Command(String name, CommandRunnable runnable, StateType... states) {
        this.name = name;
        this.runnable = runnable;
        this.requiredStates = Arrays.asList(states);
    }

    public String getName() {
        return name;
    }

    public List<StateType> getRequiredStates() {
        return requiredStates;
    }
}

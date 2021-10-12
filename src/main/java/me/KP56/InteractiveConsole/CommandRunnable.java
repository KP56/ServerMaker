package me.KP56.InteractiveConsole;

@FunctionalInterface
public interface CommandRunnable {
    String execute(String[] args, ConsoleState state);
}

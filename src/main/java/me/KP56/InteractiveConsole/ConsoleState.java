package me.KP56.InteractiveConsole;

import java.util.ArrayList;
import java.util.List;

public class ConsoleState {
    private StateType stateType;
    private List<String> flags;

    public ConsoleState(StateType stateType, List<String> flags) {
        this.stateType = stateType;
        this.flags = flags;
    }

    public ConsoleState(StateType stateType) {
        this(stateType, new ArrayList<>());
    }

    public StateType getStateType() {
        return stateType;
    }

    public List<String> getFlags() {
        return flags;
    }
}

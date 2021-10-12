package me.KP56.CodeBlock.Nodes.Function;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

public class Arguments implements Serializable {
    private static final long serialVersionUID = 1941594099039623704L;

    public final Map<String, Var> arguments;

    public Arguments(LinkedHashMap<String, Var> arguments) {
        this.arguments = arguments;
    }

    public void setArgument(int index, Var newValue) {
        arguments.replace((String) arguments.keySet().toArray()[index], newValue);
    }
}

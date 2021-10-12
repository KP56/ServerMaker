package me.KP56.CodeBlock.Nodes.Function;

import java.io.Serializable;
import java.util.Map;

public class Function implements Serializable {

    private static final long serialVersionUID = 3570703560482360273L;

    private String name;
    private Arguments arguments;
    private String javaCode;

    public Function(String name, Arguments arguments) {
        this.name = name;
        this.arguments = arguments;
        this.javaCode = getJavaCode(name, arguments);
    }

    public String getName() {
        return name;
    }

    public String getJavaCode() {
        return javaCode;
    }

    public Arguments getArguments() {
        return arguments;
    }

    private static String getJavaCode(String functionName, Arguments arguments) {
        String args = "";

        for (Map.Entry<String, Var> entry : arguments.arguments.entrySet()) {
            args += entry.getValue().getVal();
        }

        return functionName + "(" + args + ");";
    }
}

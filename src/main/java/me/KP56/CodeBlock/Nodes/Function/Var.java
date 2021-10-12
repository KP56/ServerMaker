package me.KP56.CodeBlock.Nodes.Function;

import java.io.Serializable;

public class Var implements Serializable {

    private static final long serialVersionUID = 4150694428707805850L;

    private String type;
    private String val;

    public Var(String type, String val) {
        this.type = type;
        this.val = val;
    }

    public String getType() {
        return type;
    }

    public String getVal() {
        return val;
    }
}

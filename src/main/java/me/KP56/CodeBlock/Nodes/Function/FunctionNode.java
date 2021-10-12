package me.KP56.CodeBlock.Nodes.Function;

import me.KP56.CodeBlock.Nodes.Node;

import java.awt.*;

public class FunctionNode extends Node {
    private Function function;

    public FunctionNode(double x, double y, double width, double height, Function nodeFunction) {
        super(x, y, width, height);

        this.function = nodeFunction;
    }

    public Function getFunction() {
        return function;
    }

    public String getName() {
        return function.getName();
    }

    public Color getColor() {
        return new Color(0x00E500);
    }

    public String compile() {
        return function.getJavaCode();
    }
}

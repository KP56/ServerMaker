package me.KP56.CodeBlock.Nodes.Types;

import me.KP56.CodeBlock.Nodes.Function.FunctionCreationNode;
import me.KP56.CodeBlock.Nodes.Function.Var;
import me.KP56.CodeBlock.Nodes.Node;

import java.awt.*;
import java.util.Map;
import java.util.List;

public class TypeNode extends Node {

    private static final long serialVersionUID = 3904265483271045439L;

    private String name;
    private Map<String, Var> fields;

    protected List<FunctionCreationNode> functions;

    public TypeNode(double x, double y, double width, double height, String name, Map<String, Var> fields, List<FunctionCreationNode> functions) {
        super(x, y, width, height);
        this.name = name;
        this.fields = fields;
        this.functions = functions;
    }

    @Override
    public String getName() {
        return "Type " + name;
    }

    @Override
    public Color getColor() {
        return new Color(0xFF6347);
    }

    public String getTypeName() {
        return name;
    }

    public void addFunction(FunctionCreationNode function) {
        functions.add(function);
    }

    public Var getValue(String field) {
        return fields.get(field);
    }

    public String compile() {
        String java = "public class " + name + "{";

        for (Map.Entry<String, Var> entry : fields.entrySet()) {
            java += "public static " + entry.getValue().getType() + " " + entry.getKey() + " = " + entry.getValue().getVal() + ";";
        }

        for (FunctionCreationNode function : functions) {
            java += function.compile();
        }

        java += "}";

        return java;
    }

    public FunctionCreationNode getFunction(String name) {
        for (FunctionCreationNode function : functions) {
            if (function.getFunctionName().equals(name)) {
                return function;
            }
        }

        return null;
    }

    public List<FunctionCreationNode> getFunctions() {
        return functions;
    }
}

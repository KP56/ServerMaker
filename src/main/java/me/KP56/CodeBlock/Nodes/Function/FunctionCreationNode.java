package me.KP56.CodeBlock.Nodes.Function;

import me.KP56.CodeBlock.Nodes.Node;

import java.awt.*;
import java.util.List;
import java.util.Map;

public class FunctionCreationNode extends Node {

    private static final long serialVersionUID = 8143077631406766209L;

    private String name;
    private Arguments arguments;
    private List<FunctionNode> functionNodes;
    private String javaCode = "";

    public FunctionCreationNode(double x, double y, double width, double height, String name, Arguments arguments, List<FunctionNode> functionNodes) {
        super(x, y, width, height);
        this.name = name;
        this.arguments = arguments;
        this.functionNodes = functionNodes;
    }

    @Override
    public String getName() {
        return "Function " + name;
    }

    public String getFunctionName() {
        return name;
    }

    public String getJavaCode() {
        return javaCode;
    }

    public String compile() {
        if (javaCode.equals("")) {
            ReturnType returnType = getReturnType();

            String sReturnType = "void";

            if (returnType == ReturnType.OTHER) {
                FunctionNode returnFunction = getReturnFunction();

                sReturnType = ((Var) returnFunction.getFunction().getArguments().arguments.values().toArray()[0]).getType();
            }

            javaCode += "public static " + sReturnType + " " + name + "(";

            for (Map.Entry<String, Var> entry : arguments.arguments.entrySet()) {
                String argType = entry.getValue().getType();

                javaCode += argType + " " + entry.getKey() + ",";
            }

            if (arguments.arguments.entrySet().size() >= 1) {
                javaCode = javaCode.substring(0, javaCode.length() - 1);
            }

            javaCode += "){";

            for (FunctionNode functionNode : functionNodes) {
                javaCode += functionNode.compile();
            }

            if (name.equals("onEnable")) {
                javaCode += "Bukkit.getPluginManager().registerEvents(new CustomEventListener(), this);";
            }

            javaCode += "}";
        }

        return javaCode;
    }

    public FunctionNode getReturnFunction() {
        for (FunctionNode node : functionNodes) {
            if (node.getName().equals("return")) {
                return node;
            }
        }

        return null;
    }

    public ReturnType getReturnType() {
        for (FunctionNode node : functionNodes) {
            if (node.getName().equals("return")) {
                if (node.getFunction().getArguments().arguments.size() == 0) {
                    return ReturnType.VOID;
                } else {
                    return ReturnType.OTHER;
                }
            }
        }

        return ReturnType.VOID;
    }

    @Override
    public Color getColor() {
        return new Color(0x00E500);
    }

    public List<FunctionNode> getFunctionNodes() {
        return functionNodes;
    }
}

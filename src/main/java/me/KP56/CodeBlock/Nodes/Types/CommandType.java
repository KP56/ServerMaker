package me.KP56.CodeBlock.Nodes.Types;

import me.KP56.CodeBlock.Nodes.Function.FunctionCreationNode;
import me.KP56.CodeBlock.Nodes.Function.Var;

import java.util.List;
import java.util.Map;

public class CommandType extends TypeNode {

    private static final long serialVersionUID = 6993963728463167978L;

    public CommandType(double x, double y, double width, double height, String name, Map<String, Var> fields, List<FunctionCreationNode> functions) {
        super(x, y, width, height, name + " implements CommandExecutor", fields, functions);
    }
}

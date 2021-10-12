package me.KP56.CodeBlock.Nodes.Types;

import me.KP56.CodeBlock.Nodes.Function.FunctionCreationNode;
import me.KP56.CodeBlock.Nodes.Function.Var;

import java.util.List;
import java.util.Map;

public class ListenerType extends TypeNode {

    private static final long serialVersionUID = 1786977467024599182L;

    public ListenerType(Map<String, Var> fields, List<FunctionCreationNode> events) {
        super(0, 0, 0, 0, "CustomEventListener implements Listener", fields, events);
    }

    @Override
    public String compile() {
        return super.compile().replace("public void", "@EventHandler public void");
    }
}

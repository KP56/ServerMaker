package me.KP56.CodeBlock.Nodes.Events;

import me.KP56.CodeBlock.Nodes.Function.Arguments;
import me.KP56.CodeBlock.Nodes.Function.FunctionCreationNode;
import me.KP56.CodeBlock.Nodes.Function.FunctionNode;

import java.util.List;
import java.awt.*;

public class EventNode extends FunctionCreationNode {

    protected Event event;

    public EventNode(double x, double y, double width, double height, Event event, List<FunctionNode> functionNodes) {
        super(x, y, width, height, event.getName().toLowerCase(), new Arguments(Event.getArgs(event)), functionNodes);
        this.event = event;
    }

    @Override
    public String getName() {
        return event.getName();
    }

    @Override
    public Color getColor() {
        return new Color(0x0000FF);
    }

    @Override
    public String compile() {
        return super.compile().replace("public static", "public");
    }
}

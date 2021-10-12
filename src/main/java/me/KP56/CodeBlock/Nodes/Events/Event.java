package me.KP56.CodeBlock.Nodes.Events;

import me.KP56.CodeBlock.Nodes.Function.Var;

import java.util.LinkedHashMap;

public class Event {

    private String name;

    public Event(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static LinkedHashMap<String, Var> getArgs(Event event) {
        LinkedHashMap<String, Var> map = new LinkedHashMap<>();

        map.put("event", new Var(event.getName(), ""));

        return map;
    }
}

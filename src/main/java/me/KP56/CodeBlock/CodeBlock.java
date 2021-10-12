package me.KP56.CodeBlock;

import me.KP56.CodeBlock.Nodes.Events.EventNode;
import me.KP56.CodeBlock.Nodes.Function.FunctionCreationNode;
import me.KP56.CodeBlock.Nodes.Function.FunctionNode;
import me.KP56.CodeBlock.Nodes.Types.CommandType;
import me.KP56.CodeBlock.Nodes.Types.ListenerType;
import me.KP56.CodeBlock.Nodes.Types.MainType;
import me.KP56.CodeBlock.Nodes.Types.TypeNode;
import me.KP56.JavaCompiler.JavaCompiler;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CodeBlock implements Serializable {

    private static final long serialVersionUID = 7645576391244530381L;

    private MainType mainTypeNode;
    private ListenerType listenerTypeNode;

    private List<CommandType> commandTypeNodes;

    private List<TypeNode> typeNodes;

    public CodeBlock(MainType mainTypeNode, ListenerType listenerTypeNode, List<CommandType> commandTypeNodes, List<TypeNode> typeNodes) {
        this.mainTypeNode = mainTypeNode;
        this.listenerTypeNode = listenerTypeNode;
        this.commandTypeNodes = commandTypeNodes;
        this.typeNodes = typeNodes;
    }

    public Map<String, String> compile(String main) throws IOException {
        Map<String, String> files = new HashMap<>();
        String[] split = main.split("\\.");

        String mainDir = "";

        for (int i = 0; i < split.length - 1; i++) {
            mainDir += split[i] + "/";
        }

        String pckg = mainDir.replace('/', '.');
        pckg = pckg.substring(0, pckg.length() - 1);

        files.put(mainDir + mainTypeNode.getTypeName().split(" ")[0], "package " + pckg + ";" + JavaCompiler.autoImport(mainTypeNode.compile()));
        files.put(mainDir + listenerTypeNode.getTypeName().split(" ")[0], "package " + pckg + ";" + JavaCompiler.autoImport(listenerTypeNode.compile()));

        for (CommandType command : commandTypeNodes) {
            files.put(mainDir + command.getTypeName(), "package " + pckg + ";" + JavaCompiler.autoImport(command.compile()));
        }

        for (TypeNode node : typeNodes) {
            files.put(mainDir + node.getTypeName(), "package " + pckg + ";" + JavaCompiler.autoImport(node.compile()));
        }

        return files;
    }

    public void compileAndSave(String main) throws IOException {
        compile(main).forEach((key, value) -> {
            try {
                JavaCompiler.saveString(value, "plugin_source\\" + key + ".java");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void addFunction(FunctionCreationNode function) {

        FunctionCreationNode currentFunction = getFunction(function.getFunctionName());

        if (currentFunction != null) {
            mainTypeNode.getFunctions().remove(currentFunction);
        }

        mainTypeNode.addFunction(function);
    }

    public void addFunction(TypeNode typeNode, FunctionCreationNode function) {
        typeNode.addFunction(function);
    }

    public void addEvent(EventNode eventNode) {
        listenerTypeNode.addFunction(eventNode);
    }

    public void addType(TypeNode typeNode) {
        TypeNode currentNode = getType(typeNode.getTypeName());

        if (currentNode != null) {
            typeNodes.remove(currentNode);
        }

        typeNodes.add(typeNode);
    }

    public void addCommand(CommandType command) {
        commandTypeNodes.add(command);
    }

    public FunctionCreationNode getFunction(String functionName) {
        return mainTypeNode.getFunction(functionName);
    }

    public TypeNode getType(String typeName) {
        for (TypeNode typeNode : typeNodes) {
            if (typeNode.getTypeName().equals(typeName)) {
                return typeNode;
            }
        }

        return null;
    }

    public void addNodeToFunction(String functionName, FunctionNode node) {
        getFunction(functionName).getFunctionNodes().add(node);
    }
}

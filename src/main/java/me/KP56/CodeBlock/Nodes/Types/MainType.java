package me.KP56.CodeBlock.Nodes.Types;

import me.KP56.CodeBlock.Nodes.Function.FunctionCreationNode;
import me.KP56.CodeBlock.Nodes.Function.Var;

import java.util.List;
import java.util.Map;

public class MainType extends TypeNode {

    private static final long serialVersionUID = 1455993049651514096L;

    public MainType(Map<String, Var> fields, List<FunctionCreationNode> functions) {
        super(0, 0, 0, 0, "Main extends JavaPlugin", fields, functions);
    }

    @Override
    public String compile() {
        String compilation = super.compile();

        compilation = compilation.substring(0, compilation.length() - 1);

        if (!compilation.contains("public void onEnable()")) {
            compilation += "public void onEnable() {Bukkit.getPluginManager().registerEvents(new CustomEventListener(), this);}}";
        }

        return compilation;
    }
}

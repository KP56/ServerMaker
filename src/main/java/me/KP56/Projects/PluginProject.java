package me.KP56.Projects;

import me.KP56.CodeBlock.CodeBlock;
import me.KP56.JavaCompiler.JavaCompiler;

import java.io.*;
import java.util.Arrays;

public class PluginProject implements Project, Serializable {

    private static final long serialVersionUID = 5754518364177527151L;

    private String name;
    private String author;
    private String version;

    private CodeBlock codeBlock;

    public PluginProject(String name, String author, String version, CodeBlock codeBlock) {
        this.name = name;
        this.author = author;
        this.codeBlock = codeBlock;
        this.version = version;

        new File("projects\\" + name).mkdir();
    }

    public void extract() throws Exception {
        String main = "me." + author + "." + name + ".Main";

        new File("plugin_source\\me").mkdir();
        new File("plugin_source\\me\\" + author).mkdir();
        new File("plugin_source\\me\\" + author + "\\" + name).mkdir();

        codeBlock.compileAndSave(main);

        new JavaCompiler(JavaCompiler.JavaCompilerSetting.EXPORT_PLUGIN, Arrays.asList("..\\output\\" + name + ".jar", main, name, version, author)).export("plugin_source\\");
    }

    public CodeBlock getCodeBlock() {
        return codeBlock;
    }

    @Override
    public String getName() {
        return name;
    }

    public String getAuthor() {
        return author;
    }

    public String getVersion() {
        return version;
    }
}

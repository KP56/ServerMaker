package me.KP56.InteractiveConsole;

public enum StateType {
    MAIN_MENU(""),
    CODEBLOCK_EDITION("CodeBlock Edit"),
    CODEBLOCK_FUNCTION_EDIT("CodeBlock Edit/Function Edit"),
    CODEBLOCK_TYPE_EDIT("CodeBlock Edit/Type Edit"),
    CODEBLOCK_TYPE_EDIT_FUNCTION_EDIT("CodeBlock Edit/Type Edit/Function Edit"),
    PLUGIN_PROJECT_EDITION("Project Edit");

    private String text;

    StateType(String text) {
        this.text = text;
    }

    String getText() {
        return this.text;
    }
}

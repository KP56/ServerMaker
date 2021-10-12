package me.KP56.InteractiveConsole;

import me.KP56.CodeBlock.CodeBlock;
import me.KP56.CodeBlock.Nodes.Function.*;
import me.KP56.CodeBlock.Nodes.Types.ListenerType;
import me.KP56.CodeBlock.Nodes.Types.MainType;
import me.KP56.CodeBlock.Nodes.Types.TypeNode;
import me.KP56.Main;
import me.KP56.Projects.PluginProject;
import me.KP56.Projects.Project;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

public class Console {

    private static Console console = new Console();

    private ConsoleState state;
    private List<Command> commands;

    private Console() {
        state = new ConsoleState(StateType.MAIN_MENU);
        commands = new ArrayList<>();
        registerCommands();

        System.out.println("Server Maker BETA 1.0 (2021) Interactive Console");
        System.out.println("================================================");
        System.out.println("You are responsible for any change you make with");
        System.out.println("this tool. It's originally meant to be used by");
        System.out.println("Server Maker's developers to test functionalities");
        System.out.println("before making GUI elements for them.");
        System.out.println("================================================");

        while (true) {
            System.out.print(state.getStateType().getText() + "> ");
            Scanner scanner = new Scanner(System.in);

            System.out.println(input(scanner.nextLine()));
        }
    }

    private void registerCommands() {
        commands.add(new Command("help", (args, state) -> {
            String cmds = "List of commands:\n";
            for (Command cmd : commands) {
                if (cmd.getRequiredStates().contains(state.getStateType())) {
                    if (!cmd.getName().equals("help")) {
                        cmds += cmd.getName() + "\n";
                    }
                }
            }
            return cmds;
        }, StateType.MAIN_MENU, StateType.CODEBLOCK_FUNCTION_EDIT, StateType.PLUGIN_PROJECT_EDITION,
                StateType.CODEBLOCK_EDITION, StateType.CODEBLOCK_TYPE_EDIT, StateType.CODEBLOCK_TYPE_EDIT_FUNCTION_EDIT));

        commands.add(new Command("getstateflags", (args, state) -> "State flags:\n" + Arrays.deepToString(state.getFlags().toArray()),
                StateType.MAIN_MENU, StateType.CODEBLOCK_EDITION, StateType.PLUGIN_PROJECT_EDITION, StateType.CODEBLOCK_FUNCTION_EDIT, StateType.CODEBLOCK_TYPE_EDIT,
                StateType.CODEBLOCK_TYPE_EDIT_FUNCTION_EDIT));

        commands.add(new Command("createproject", (args, state) -> {
            if (args.length >= 2) {
                if (!new File("projects\\" + args[1]).exists()) {
                    if (args[0].equals("plugin")) {
                        if (args.length >= 4) {
                            PluginProject pluginProject = new PluginProject(args[1], args[2], args[3],
                                    new CodeBlock(new MainType(new HashMap<>(), new ArrayList<>()), new ListenerType(new HashMap<>(), new ArrayList<>()),
                                            new ArrayList<>(), new ArrayList<>()));
                            try {
                                pluginProject.save();
                                return "Project has been successfully created!";
                            } catch (IOException e) {
                                if (Main.DEBUG_MODE) {
                                    e.printStackTrace();
                                    return "Failed to create a project!";
                                }
                                return "Failed to create a project! (Enable debug mode for more details)";
                            }
                        } else {
                            return "Creating a plugin project requires atleast 2 flags specified: author and version";
                        }
                    }
                } else {
                    return "This project already exists! Delete it using a command 'deleteproject' and try again.";
                }
            }

            return "Correct usage: createproject <network/server/plugin> <name> [flags (seperated by spaces)]";
        }, StateType.MAIN_MENU));

        commands.add(new Command("deleteproject", (args, state) -> {
            if (args.length == 1) {
                File project = new File("projects\\" + args[0]);
                if (project.exists()) {
                    deleteDir(project);
                    return "Project has been successfully deleted!";
                } else {
                    return "This project does not exist!";
                }
            } else {
                return "Correct usage: deleteproject <name>";
            }
        }, StateType.MAIN_MENU));

        commands.add(new Command("infoproject", (args, state) -> {
            if (args.length == 1) {
                File project = new File("projects\\" + args[0]);
                if (project.exists()) {
                    try {
                        Project pr = Project.load(args[0]);

                        if (pr instanceof PluginProject) {
                            PluginProject pluginProject = (PluginProject) pr;

                            return "Name: " + pluginProject.getName() + "\nAuthor: " + pluginProject.getAuthor() + "\nVersion: " + pluginProject.getVersion() + "\nType: Plugin Project";
                        }

                        return "Invalid project.";
                    } catch (IOException e) {
                        if (Main.DEBUG_MODE) {
                            e.printStackTrace();
                        }
                        return "Could not load this project.";
                    } catch (ClassNotFoundException e) {
                        if (Main.DEBUG_MODE) {
                            e.printStackTrace();
                        }
                        return "This project is not compatible with your version!";
                    }
                } else {
                    return "This project does not exist!";
                }
            } else {
                return "Correct usage: infoproject <name>";
            }
        }, StateType.MAIN_MENU));

        commands.add(new Command("projectlist", (args, state) -> {
            String list = "Projects: \n";
            int counter = 0;
            for (File file : new File("projects").listFiles()) {
                counter += file.getName().length();
                if (counter < 12) {
                    list += file.getName() + ", ";
                } else {
                    list += file.getName() + ",\n";
                }
            }

            list = list.substring(0, list.length() - 2);

            return list;
        }, StateType.MAIN_MENU));

        commands.add(new Command("editproject", (args, state) -> {
            if (args.length == 1) {
                File project = new File("projects\\" + args[0]);
                    if (project.exists()) {
                        Project pr;

                        try {
                            pr = Project.load(args[0]);
                        } catch (IOException e) {
                            if (Main.DEBUG_MODE) {
                                e.printStackTrace();
                            }
                            return "Could not load this project.";
                        } catch (ClassNotFoundException e) {
                            if (Main.DEBUG_MODE) {
                                e.printStackTrace();
                            }
                            return "This project is not compatible with your version!";
                        }

                        if (pr instanceof PluginProject) {
                            PluginProject pluginProject = (PluginProject) pr;
                            this.state = new ConsoleState(StateType.PLUGIN_PROJECT_EDITION, Arrays.asList(pluginProject.getName(), pluginProject.getAuthor(),
                                    pluginProject.getVersion()));
                        }

                        return "================\nProject Edit 1.0\n================";
                } else {
                    return "This project does not exist!";
                }
            } else {
                return "Correct usage: editproject <name>";
            }
        }, StateType.MAIN_MENU));

        commands.add(new Command("setname", (args, state) -> {
            if (args.length == 1) {
                Project project;
                try {
                    project = Project.load(state.getFlags().get(0));
                } catch (IOException e) {
                    if (Main.DEBUG_MODE) {
                        e.printStackTrace();
                    }
                    return "Could not load this project.";
                } catch (ClassNotFoundException e) {
                    if (Main.DEBUG_MODE) {
                        e.printStackTrace();
                    }
                    return "This project is not compatible with your version!";
                }

                if (project instanceof PluginProject) {
                    PluginProject pluginProject = (PluginProject) project;
                    PluginProject newProject = new PluginProject(args[0], pluginProject.getAuthor(), pluginProject.getVersion(), pluginProject.getCodeBlock());

                    this.state = new ConsoleState(StateType.MAIN_MENU, state.getFlags());
                    input("deleteproject " + pluginProject.getName());
                    this.state = new ConsoleState(StateType.PLUGIN_PROJECT_EDITION, state.getFlags());

                    try {
                        newProject.save();
                    } catch (IOException e) {
                        try {
                            pluginProject.save();
                        } catch (IOException ex) {
                            if (Main.DEBUG_MODE) {
                                ex.printStackTrace();
                            }
                            return "Failed to save a renamed project and restore the old one!";
                        }
                        if (Main.DEBUG_MODE) {
                            e.printStackTrace();
                        }
                        return "Failed to save a renamed project!";
                    }
                }

                state.getFlags().set(0, args[0]);

                return "Name has been successfully set!";
            } else {
                return "Correct usage: setname <name>";
            }
        }, StateType.PLUGIN_PROJECT_EDITION));

        commands.add(new Command("setauthor", (args, state) -> {
            if (args.length == 1) {
                Project project;
                try {
                    project = Project.load(state.getFlags().get(0));
                } catch (IOException e) {
                    if (Main.DEBUG_MODE) {
                        e.printStackTrace();
                    }
                    return "Could not load this project.";
                } catch (ClassNotFoundException e) {
                    if (Main.DEBUG_MODE) {
                        e.printStackTrace();
                    }
                    return "This project is not compatible with your version!";
                }

                if (project instanceof PluginProject) {
                    PluginProject pluginProject = (PluginProject) project;

                    this.state = new ConsoleState(StateType.MAIN_MENU, state.getFlags());
                    input("deleteproject " + pluginProject.getName());
                    this.state = new ConsoleState(StateType.PLUGIN_PROJECT_EDITION, state.getFlags());

                    PluginProject newProject = new PluginProject(pluginProject.getName(), args[0], pluginProject.getVersion(), pluginProject.getCodeBlock());

                    try {
                        newProject.save();
                    } catch (IOException e) {
                        if (Main.DEBUG_MODE) {
                            e.printStackTrace();
                        }

                        try {
                            pluginProject.save();
                        } catch (IOException ex) {
                            if (Main.DEBUG_MODE) {
                                ex.printStackTrace();
                            }

                            return "Failed to save an edited project and restore the old one!";
                        }

                        return "Failed to save an edited project!";
                    }
                }

                return "Author has been successfully set!";
            } else {
                return "Correct usage: setauthor <name>";
            }
        }, StateType.PLUGIN_PROJECT_EDITION));

        commands.add(new Command("setversion", (args, state) -> {
            if (args.length == 1) {
                Project project;
                try {
                    project = Project.load(state.getFlags().get(0));
                } catch (IOException e) {
                    if (Main.DEBUG_MODE) {
                        e.printStackTrace();
                    }
                    return "Could not load this project.";
                } catch (ClassNotFoundException e) {
                    if (Main.DEBUG_MODE) {
                        e.printStackTrace();
                    }
                    return "This project is not compatible with your version!";
                }

                if (project instanceof PluginProject) {
                    PluginProject pluginProject = (PluginProject) project;

                    this.state = new ConsoleState(StateType.MAIN_MENU, state.getFlags());
                    input("deleteproject " + pluginProject.getName());
                    this.state = new ConsoleState(StateType.PLUGIN_PROJECT_EDITION, state.getFlags());

                    PluginProject newProject = new PluginProject(pluginProject.getName(), pluginProject.getAuthor(), args[0], pluginProject.getCodeBlock());

                    try {
                        newProject.save();
                    } catch (IOException e) {
                        if (Main.DEBUG_MODE) {
                            e.printStackTrace();
                        }

                        try {
                            pluginProject.save();
                        } catch (IOException ex) {
                            if (Main.DEBUG_MODE) {
                                ex.printStackTrace();
                            }

                            return "Failed to save an edited project and restore the old one!";
                        }

                        return "Failed to save an edited project!";
                    }
                }

                return "Version has been successfully set!";
            } else {
                return "Correct usage: setversion <name>";
            }
        }, StateType.PLUGIN_PROJECT_EDITION));

        commands.add(new Command("quit", (args, state) -> {
            if (state.getStateType() == StateType.CODEBLOCK_FUNCTION_EDIT) {
                this.state = new ConsoleState(StateType.CODEBLOCK_EDITION, this.state.getFlags());
            } else if (state.getStateType() == StateType.CODEBLOCK_EDITION) {
                this.state = new ConsoleState(StateType.MAIN_MENU, this.state.getFlags());
            } else if (state.getStateType() == StateType.CODEBLOCK_TYPE_EDIT_FUNCTION_EDIT) {
                this.state = new ConsoleState(StateType.CODEBLOCK_TYPE_EDIT, this.state.getFlags());
            } else if (state.getStateType() == StateType.CODEBLOCK_TYPE_EDIT) {
                this.state = new ConsoleState(StateType.CODEBLOCK_EDITION, this.state.getFlags());
            } else if (state.getStateType() == StateType.MAIN_MENU) {
                System.exit(0);
            }
            return "";
        }, StateType.MAIN_MENU, StateType.PLUGIN_PROJECT_EDITION, StateType.CODEBLOCK_EDITION, StateType.CODEBLOCK_FUNCTION_EDIT,
                StateType.CODEBLOCK_TYPE_EDIT, StateType.CODEBLOCK_TYPE_EDIT_FUNCTION_EDIT));

        commands.add(new Command("editcodeblock", (args, state) -> {
            if (args.length == 1) {
                File project = new File("projects\\" + args[0]);
                if (project.exists()) {
                    this.state = new ConsoleState(StateType.CODEBLOCK_EDITION, Collections.singletonList(args[0]));

                    return "================\nCodeBlock Edit 1.0\n================";
                } else {
                    return "This CodeBlock does not exist!";
                }
            } else {
                return "Correct usage: editcodeblock <name>";
            }
        }, StateType.MAIN_MENU));

        commands.add(new Command("addfunction", (args, state) -> {
            if (args.length == 1) {
                if (state.getStateType() == StateType.CODEBLOCK_EDITION) {
                    this.state = new ConsoleState(StateType.CODEBLOCK_FUNCTION_EDIT, Arrays.asList(this.state.getFlags().get(0), args[0]));
                } else {
                    this.state = new ConsoleState(StateType.CODEBLOCK_TYPE_EDIT_FUNCTION_EDIT, Arrays.asList(this.state.getFlags().get(0),
                            this.state.getFlags().get(1), args[0]));
                }

                return "";
            } else {
                return "Correct usage: addfunction <name>";
            }
        }, StateType.CODEBLOCK_EDITION, StateType.CODEBLOCK_TYPE_EDIT));

        commands.add(new Command("addtype", (args, state) -> {
            if (args.length >= 1) {
                Project project;
                try {
                    project = Project.load(this.state.getFlags().get(0));
                } catch (IOException e) {
                    if (Main.DEBUG_MODE) {
                        e.printStackTrace();
                    }
                    return "Could not load this CodeBlock.";
                } catch (ClassNotFoundException e) {
                    if (Main.DEBUG_MODE) {
                        e.printStackTrace();
                    }
                    return "This CodeBlock is not compatible with your version!";
                }

                if (project instanceof PluginProject) {
                    PluginProject pluginProject = (PluginProject) project;

                    pluginProject.getCodeBlock().addType(new TypeNode(0, 0, 0, 0, args[0], new HashMap<>(), new ArrayList<>()));

                    this.state = new ConsoleState(StateType.CODEBLOCK_TYPE_EDIT, Arrays.asList(this.state.getFlags().get(0), args[0]));

                    try {
                        pluginProject.save();
                    } catch (IOException e) {
                        if (Main.DEBUG_MODE) {
                            e.printStackTrace();
                            return "Failed to add a type!";
                        }
                        return "Failed to add a type! (Enable debug mode for more details)";
                    }

                    return "Successfully created a custom type!";
                } else {
                    return "This is not a CodeBlock!";
                }
            } else {
                return "Correct usage: addtype <name>";
            }
        }, StateType.CODEBLOCK_EDITION));

        commands.add(new Command("export", (args, state) -> {
            if (args.length == 1) {
                Project project;
                try {
                    project = Project.load(args[0]);
                } catch (IOException e) {
                    if (Main.DEBUG_MODE) {
                        e.printStackTrace();
                    }
                    return "Could not load this CodeBlock.";
                } catch (ClassNotFoundException e) {
                    if (Main.DEBUG_MODE) {
                        e.printStackTrace();
                    }
                    return "This CodeBlock is not compatible with your version!";
                }

                if (project instanceof PluginProject) {
                    PluginProject pluginProject = (PluginProject) project;

                    try {
                        pluginProject.extract();
                    } catch (Exception e) {
                        if (Main.DEBUG_MODE) {
                            e.printStackTrace();
                        }
                        return "Failed to export!";
                    }

                    return "Successfully exported a plugin!";
                } else {
                    return "This project is corrupted.";
                }
            } else {
                return "Correct usage: export <name>";
            }
        }, StateType.MAIN_MENU));

        commands.add(new Command("editfunction", (args, state) -> {
            if (args.length == 1) {
                Project project;
                try {
                    project = Project.load(this.state.getFlags().get(0));
                } catch (IOException e) {
                    if (Main.DEBUG_MODE) {
                        e.printStackTrace();
                    }
                    return "Could not load this CodeBlock.";
                } catch (ClassNotFoundException e) {
                    if (Main.DEBUG_MODE) {
                        e.printStackTrace();
                    }
                    return "This CodeBlock is not compatible with your version!";
                }

                if (project instanceof PluginProject) {
                    PluginProject pluginProject = (PluginProject) project;

                    if (pluginProject.getCodeBlock().getFunction(args[0]) != null) {
                        return input("addfunction " + args[0]);
                    } else {
                        return "This function does not exist!";
                    }
                } else {
                    return "This is not a CodeBlock!";
                }
            } else {
                return "Correct usage: editfunction <name>";
            }
        }, StateType.CODEBLOCK_EDITION));

        commands.add(new Command("addargument", (args, state) -> {
            if (args.length == 2) {

                List<String> stateFlags = new ArrayList<>(this.state.getFlags());

                stateFlags.add(args[0] + " " + args[1]);

                this.state = new ConsoleState(StateType.CODEBLOCK_FUNCTION_EDIT, stateFlags);

                Project project;
                try {
                    project = Project.load(this.state.getFlags().get(0));
                } catch (IOException e) {
                    if (Main.DEBUG_MODE) {
                        e.printStackTrace();
                    }
                    return "Could not load this CodeBlock.";
                } catch (ClassNotFoundException e) {
                    if (Main.DEBUG_MODE) {
                        e.printStackTrace();
                    }
                    return "This CodeBlock is not compatible with your version!";
                }

                if (project instanceof PluginProject) {
                    PluginProject pluginProject = (PluginProject) project;

                    if (state.getStateType() == StateType.CODEBLOCK_FUNCTION_EDIT) {
                        rebuildFunction(pluginProject, pluginProject.getCodeBlock().getFunction(this.state.getFlags().get(1)));
                    } else {
                        rebuildFunction(pluginProject, stateFlags.get(1),  pluginProject.getCodeBlock().getFunction(this.state.getFlags().get(1)));
                    }

                    try {
                        pluginProject.save();
                    } catch (IOException e) {
                        if (Main.DEBUG_MODE) {
                            e.printStackTrace();
                            return "Failed to add an argument!";
                        }
                        return "Failed to add an argument! (Enable debug mode for more details)";
                    }
                } else {
                    return "This is not a CodeBlock!";
                }

                return "Successfully added an argument to the function!";
            } else {
                return "Correct usage: addargument <type> <name>";
            }
        }, StateType.CODEBLOCK_FUNCTION_EDIT, StateType.CODEBLOCK_TYPE_EDIT_FUNCTION_EDIT));

        commands.add(new Command("addnode", (args, state) -> {
            if (args.length >= 1) {
                Project project;
                try {
                    project = Project.load(this.state.getFlags().get(0));
                } catch (IOException e) {
                    if (Main.DEBUG_MODE) {
                        e.printStackTrace();
                    }
                    return "Could not load this CodeBlock.";
                } catch (ClassNotFoundException e) {
                    if (Main.DEBUG_MODE) {
                        e.printStackTrace();
                    }
                    return "This CodeBlock is not compatible with your version!";
                }

                if (project instanceof PluginProject) {
                    PluginProject pluginProject = (PluginProject) project;

                    FunctionCreationNode functionCreationNode;

                    List<String> functionArguments = new ArrayList<>(Arrays.asList(args).subList(1, args.length));

                    LinkedHashMap<String, Var> linkedHashMap = new LinkedHashMap<>();

                    for (String s : functionArguments) {
                        linkedHashMap.put("", new Var("", s));
                    }

                    if (state.getStateType() == StateType.CODEBLOCK_FUNCTION_EDIT) {
                        functionCreationNode = pluginProject.getCodeBlock().getFunction(this.state.getFlags().get(1));
                        rebuildFunction(pluginProject, functionCreationNode);
                    } else {
                        functionCreationNode = pluginProject.getCodeBlock().getType(this.state.getFlags().get(1)).getFunction(this.state.getFlags().get(2));
                        rebuildFunction(pluginProject, state.getFlags().get(1),  functionCreationNode);
                    }

                    functionCreationNode.getFunctionNodes().add(new FunctionNode(0, 0, 0, 0, new Function(args[0],
                            new Arguments(linkedHashMap))));

                    try {
                        pluginProject.save();
                    } catch (IOException e) {
                        if (Main.DEBUG_MODE) {
                            e.printStackTrace();
                            return "Failed to add a node!";
                        }
                        return "Failed to add a node! (Enable debug mode for more details)";
                    }
                } else {
                    return "This is not a CodeBlock - it's a project!";
                }

                return "Successfully added a node to the function!";
            } else {
                return "Correct usage: addnode <function name> [function arguments]";
            }
        }, StateType.CODEBLOCK_FUNCTION_EDIT, StateType.CODEBLOCK_TYPE_EDIT_FUNCTION_EDIT));

        commands.add(new Command("currentfunction", (args, state) -> {
            Project project;
            try {
                project = Project.load(this.state.getFlags().get(0));
            } catch (IOException e) {
                if (Main.DEBUG_MODE) {
                    e.printStackTrace();
                }
                return "Could not load this CodeBlock.";
            } catch (ClassNotFoundException e) {
                if (Main.DEBUG_MODE) {
                    e.printStackTrace();
                }
                return "This CodeBlock is not compatible with your version!";
            }

            if (project instanceof PluginProject) {

                PluginProject pluginProject = (PluginProject) project;

                FunctionCreationNode functionCreationNode;

                if (state.getStateType() == StateType.CODEBLOCK_FUNCTION_EDIT) {
                    functionCreationNode = pluginProject.getCodeBlock().getFunction(this.state.getFlags().get(1));
                } else {
                    functionCreationNode = pluginProject.getCodeBlock().getType(this.state.getFlags().get(1)).getFunction(this.state.getFlags().get(2));
                }

                if (functionCreationNode != null) {
                    String compiled = functionCreationNode.compile();

                    compiled = compiled.replace(";", ";\n");
                    compiled = compiled.replace("{", " {\n");
                    compiled = compiled.replace("}", "}\n");
                    compiled = compiled.replace(",", ", ");

                    int counter = 2;

                    compiled = "1. " + compiled;

                    for (int i = 0; i < compiled.length(); i++) {
                        char c = compiled.charAt(i);

                        if (c == '\n') {
                            compiled = compiled.substring(0, i + 1) + counter +". " + compiled.substring(i + 1);
                            i += 2 + String.valueOf(counter).length();
                            counter++;
                        }
                    }

                    return compiled;
                } else {
                    return "There is no nodes in this function.";
                }
            } else {
                return "This is not a CodeBlock - it's a project!";
            }
        }, StateType.CODEBLOCK_FUNCTION_EDIT, StateType.CODEBLOCK_TYPE_EDIT_FUNCTION_EDIT));
    }

    public static Console getInstance() {
        return console;
    }

    private void deleteDir(File file) {
        File[] contents = file.listFiles();
        if (contents != null) {
            for (File f : contents) {
                if (!Files.isSymbolicLink(f.toPath())) {
                    deleteDir(f);
                }
            }
        }
        file.delete();
    }

    public String input(String input) {
        if (input.equals("")) {
            return "";
        }

        List<String> split = splitArguments(input);

        String command = split.get(0);
        String[] args = new String[split.size() - 1];

        for (int i = 1; i < split.size(); i++) {
            args[i - 1] = split.get(i);
        }

        for (Command cmd : commands) {
            if (cmd.getName().equals(command) && cmd.getRequiredStates().contains(state.getStateType())) {
                return cmd.runnable.execute(args, state);
            }
        }

        return "Could not find a suitable command.";
    }

    public static List<String> splitArguments(String args) {
        List<String> strings = new ArrayList<>();

        String currentStr = "";
        boolean insideOfString = false;
        for (char c : args.toCharArray()) {
            if (c == '"') {
                insideOfString = !insideOfString;
            }
            if (c != ' ') {
                currentStr += c;
            } else {
                if (!insideOfString) {
                    strings.add(currentStr);
                    currentStr = "";
                } else {
                    currentStr += c;
                }
            }
        }

        if (!currentStr.equals("")) {
            strings.add(currentStr);
        }

        return strings;
    }

    private FunctionCreationNode rebuildFunction(PluginProject pluginProject, FunctionCreationNode functionCreationNode) {
        List<String> arguments = new ArrayList<>();

        for (int i = 2; i < this.state.getFlags().size(); i++) {
            arguments.add(this.state.getFlags().get(i));
        }

        LinkedHashMap<String, Var> functionCreationArguments = new LinkedHashMap<>();

        for (String s : arguments) {
            String[] split = s.split(" ");

            functionCreationArguments.put(split[1], new Var(split[0], ""));
        }

        if (functionCreationNode == null) {
            pluginProject.getCodeBlock().addFunction(new FunctionCreationNode(0, 0, 0, 0, this.state.getFlags().get(1),
                    new Arguments(functionCreationArguments), new ArrayList<>()));

            functionCreationNode = pluginProject.getCodeBlock().getFunction(this.state.getFlags().get(1));
        } else {
            pluginProject.getCodeBlock().addFunction(new FunctionCreationNode(0, 0, 0, 0, this.state.getFlags().get(1),
                    new Arguments(functionCreationArguments), functionCreationNode.getFunctionNodes()));
        }

        return functionCreationNode;
    }

    private FunctionCreationNode rebuildFunction(PluginProject pluginProject, String typeName, FunctionCreationNode functionCreationNode) {
        List<String> arguments = new ArrayList<>();

        for (int i = 2; i < this.state.getFlags().size(); i++) {
            arguments.add(this.state.getFlags().get(i));
        }

        LinkedHashMap<String, Var> functionCreationArguments = new LinkedHashMap<>();

        for (String s : arguments) {
            String[] split = s.split(" ");

            functionCreationArguments.put(split[1], new Var(split[0], ""));
        }

        TypeNode typeNode = pluginProject.getCodeBlock().getType(typeName);

        if (functionCreationNode == null) {
            pluginProject.getCodeBlock().addFunction(typeNode, new FunctionCreationNode(0, 0, 0, 0, this.state.getFlags().get(1),
                    new Arguments(functionCreationArguments), new ArrayList<>()));

            functionCreationNode = typeNode.getFunction(this.state.getFlags().get(1));
        } else {
            pluginProject.getCodeBlock().addFunction(typeNode, new FunctionCreationNode(0, 0, 0, 0, this.state.getFlags().get(1),
                    new Arguments(functionCreationArguments), functionCreationNode.getFunctionNodes()));
        }

        return functionCreationNode;
    }
}

package me.KP56.JavaCompiler;

import java.io.*;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class JavaCompiler {

    private static final String DEPENDENCY_PATH = "C:\\Users\\jkoci\\OneDrive\\Pulpit\\ServerMaker\\libs\\spigot-1.16.5.jar";

    private JavaCompilerSetting setting;
    private List<String> modifiers;

    public JavaCompiler(JavaCompilerSetting setting, List<String> modifiers) {
        this.modifiers = modifiers;
        this.setting = setting;
    }

    public JavaCompiler(JavaCompilerSetting setting) {
        this(setting, new ArrayList<>());
    }

    private static List<File> listFilesDeep(File dir) {
        File[] files = dir.listFiles();
        List<File> result = new ArrayList<>();
        for (File file : files) {
            if (file.isDirectory()) {
                result.addAll(listFilesDeep(file));
            } else {
                result.add(file);
            }
        }

        return result;
    }

    private static List<File> listDirectoriesDeep(File dir) {
        File[] files = dir.listFiles();
        List<File> result = new ArrayList<>();
        for (File file : files) {
            if (file.isDirectory()) {
                result.add(file);
                result.addAll(listDirectoriesDeep(file));
            }
        }

        return result;
    }

    public static void saveString(String string, String path) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(path));

        writer.write(string);
        writer.close();
    }

    public static String autoImport(String string) throws IOException {

        Set<String> classesToImport = new HashSet<>();

        String[] split = string.split("[\\s,();{}.@]");

        for (String s : split) {
            if (s.length() > 0) {
                if (Character.isUpperCase(s.charAt(0))) {
                    if (!string.contains("class " + s)) {
                        classesToImport.add(s);
                    }
                }
            }
        }

        ZipFile zipFile = new ZipFile(DEPENDENCY_PATH);

        for (String s : classesToImport) {
            File realFile = getFile("plugin_source", s + ".java");
            if (realFile == null) {
                Enumeration<? extends ZipEntry> entries = zipFile.entries();
                while (entries.hasMoreElements()) {
                    ZipEntry zipEntry = entries.nextElement();
                    String[] splittedName = zipEntry.getName().split("/");
                    String classesName = splittedName[splittedName.length - 1];


                    if (classesName.equals(s + ".class")) {
                        string = "import " + zipEntry.getName().replace("/", ".").replace(".class", "") + ";" + string;
                        break;
                    }
                }
            }
        }
        zipFile.close();

        return string;
    }

    private static File getFile(String path, String name) {
        for (File file : listFilesDeep(new File(path))) {
            if (file.getName().equals(name)) {
                return file;
            }
        }

        return null;
    }

    public void export(String path) throws Exception {
        if (setting == JavaCompilerSetting.EXPORT_CLASS) {
            exportClass(path);
        } else if (setting == JavaCompilerSetting.EXPORT_JAR) {
            exportJAR(path);
        } else if (setting == JavaCompilerSetting.EXPORT_PLUGIN) {
            exportPlugin(path);
        }
    }

    public void exportClass(String path) throws Exception {
        String newPath = path.substring(14);
        runProcess("cmd /c javac -cp " + DEPENDENCY_PATH + " " + newPath);
    }

    public void exportJAR(String path) throws Exception {
        File dir = new File(path);

        for (File file : listDirectoriesDeep(dir)) {
            exportClass(file.getPath() + "\\*.java");
        }

        runProcess("cmd /c jar cvf " + modifiers.get(0) + " *");

        for (File file : listFilesDeep(dir)) {
            if (file.getName().endsWith(".class")) {
                file.delete();
            }
        }

        deleteAllJavaFilesFromZIP("plugin_source\\" + modifiers.get(0));
    }

    public void exportPlugin(String path) throws Exception {
        exportJAR(path);
        BufferedWriter writer = new BufferedWriter(new FileWriter("plugin.yml"));

        writer.write("main: " + modifiers.get(1) + "\n");
        writer.write("name: " + modifiers.get(2) + "\n");
        writer.write("version: " + modifiers.get(3) + "\n");
        writer.write("author: " + modifiers.get(4) + "\n");

        writer.close();

        appendToZIP("plugin.yml", "plugin_source\\" + modifiers.get(0));

        new File("plugin.yml").delete();
    }

    private void runProcess(String command) throws Exception {
        ProcessBuilder pro = new ProcessBuilder(command.split(" ")).directory(new File("plugin_source"));
        pro.redirectErrorStream(true);
        Process process = pro.start();
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null)
            System.out.println(line);

        process.waitFor();
    }

    private void appendToZIP(String filePath, String ZIPPath) throws IOException {
        try (FileSystem fs = FileSystems.newFileSystem(Paths.get(ZIPPath), null)) {
            Files.copy(Paths.get(filePath), fs.getPath(new File(filePath).getName()));
        }
    }

    private void deleteAllJavaFilesFromZIP(String ZIPPath) throws IOException {
        ZipFile zipFile = new ZipFile(ZIPPath);
        Enumeration<? extends ZipEntry> entries = zipFile.entries();

        List<String> paths = new ArrayList<>();

        while(entries.hasMoreElements()) {
            ZipEntry zipEntry = entries.nextElement();
            if (!zipEntry.isDirectory()) {
                if (zipEntry.getName().endsWith(".java")) {
                    paths.add(zipEntry.getName());
                }
            }
        }
        zipFile.close();

        for (String path : paths) {
            deleteFromZIP(ZIPPath, path);
        }
    }

    private void deleteFromZIP(String ZIPPath, String filePath) throws IOException {
        try (FileSystem fs = FileSystems.newFileSystem(Paths.get(ZIPPath), null)) {
            Files.delete(fs.getPath(filePath));
        }
    }

    public enum JavaCompilerSetting {
        EXPORT_JAR,
        EXPORT_CLASS,
        EXPORT_PLUGIN;
    }
}

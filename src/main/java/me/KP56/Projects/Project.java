package me.KP56.Projects;

import java.io.*;

public interface Project {
    String getName();

    default void save() throws IOException {
        FileOutputStream fileOut = new FileOutputStream("projects\\" + getName() + "\\project.smproject");
        ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
        objectOut.writeObject(this);
        objectOut.close();
    }

    static Project load(String projectName) throws IOException, ClassNotFoundException {
        FileInputStream fileIn = new FileInputStream("projects\\" + projectName + "\\project.smproject");
        ObjectInputStream objectIn = new ObjectInputStream(fileIn);

        Project project = (Project) objectIn.readObject();
        objectIn.close();

        return project;
    }
}

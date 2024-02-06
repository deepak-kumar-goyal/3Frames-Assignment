import java.io.*;
import java.util.*;

/*
Problem-1:
Build an approach for storing versions and files. A simple approach is to store each file as a
separate version. However that is inefficient, Instead of storing each version as a separate
file, look at storing the deltas. Come up with an approach to store the base version and
deltas in the file. Persist them all in one file. Come up with an efficient data structure to
store the file, and deltas, and persist in that. Write methods to generate any version
immediately.

*/
class VersionControlSystem implements Serializable {

    private String baseVersion;
    private List<String> deltas;

    public VersionControlSystem(String baseVersion) {
        this.baseVersion = baseVersion;
        this.deltas = new ArrayList<>();
    }

    public void addDelta(String delta) {
        deltas.add(delta);
    }

    public String generateVersion(int versionNumber) {
        StringBuilder fileContent = new StringBuilder(baseVersion);
        for (int i = 0; i < Math.min(versionNumber, deltas.size()); i++) {
            applyDelta(fileContent, deltas.get(i));
        }
        return fileContent.toString();
    }

    private void applyDelta(StringBuilder fileContent, String delta) {
        String[] parts = delta.split(":");
        int index = Integer.parseInt(parts[0]);
        String content = parts[1];
        fileContent.replace(index, index + content.length(), content);
    }


    public void saveToFile(String filename) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(this);
        }
    }

    public static VersionControlSystem loadFromFile(String filename) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            return (VersionControlSystem) ois.readObject();
        }
    }
}

public class Solution1 {
    public static void main(String[] args) {
        // Example usage:
        VersionControlSystem vcs = new VersionControlSystem("Hello, World!");
        vcs.addDelta("7:3Frames Lab!");
        vcs.addDelta("0: Hi  ");
        vcs.addDelta("14:Software");

        // Generating versions
        System.out.println("Version 0: " + vcs.generateVersion(0));
        System.out.println("Version 1: " + vcs.generateVersion(1));
        System.out.println("Version 2: " + vcs.generateVersion(2));
        System.out.println("Version 3: " + vcs.generateVersion(3));

        // Saving to file
        try {
            vcs.saveToFile("version_control_system.ser");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Loading from file
        try {
            VersionControlSystem loadedVCS = VersionControlSystem.loadFromFile("version_control_system.ser");
            System.out.println("Version 3 (loaded): " + loadedVCS.generateVersion(3));
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}

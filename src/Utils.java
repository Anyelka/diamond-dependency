import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Utils {
    private static final String FILE_NAME = "example_big";
    private static final String INPUT_PATH = "resources/" + FILE_NAME + ".in";
    private static final String OUTPUT_PATH = "out/" + FILE_NAME + ".out";
    private static final String EXPECTED_PATH = "resources/" + FILE_NAME + ".out";

    public static List<List<String>> getInputData() {
        List<String> allLines = readAllLines();
        int remainingLinesFromCurrentLibrary = 0;
        List<List<String>> allLibraries = new ArrayList<>();
        List<String> currentLibrary = new ArrayList<>();
        for(int i = 1; i <= allLines.size(); i++) {
            if(remainingLinesFromCurrentLibrary == 0) {
                if(!currentLibrary.isEmpty()) {
                    allLibraries.add(currentLibrary);
                    currentLibrary = new ArrayList<>();
                }
                if(i < allLines.size()) {
                    remainingLinesFromCurrentLibrary = Integer.parseInt(allLines.get(i));
                }
            } else {
                currentLibrary.add(allLines.get(i));
                remainingLinesFromCurrentLibrary--;
            }
        }
        return allLibraries;
    }

    public static List<String> readAllLines() {
        return readAllLines(INPUT_PATH);
    }

    public static List<String> readAllLines(String path) {
        try {
            return Files.readAllLines(Paths.get(path));
        } catch (IOException e) {
            throw new RuntimeException("Error reading file from path: " + path);
        }
    }

    public static String read(String path) {
        try {
            return Files.readString(Paths.get(path));
        } catch (IOException e) {
            throw new RuntimeException("Error reading file from path: " + path);
        }
    }

    public static void writeRowsFile(String content) {
        writeFile(OUTPUT_PATH, content);
    }
    public static void writeFile(String content) {
        writeFile(OUTPUT_PATH, content);
    }

    public static void writeFile(String path, String content) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(path));
            writer.write(content);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void checkFileContent(String result) {
        List<String> expectedLines = readAllLines(EXPECTED_PATH);
        List<String> resultLines = Arrays.asList(result.split("\n"));

        boolean correct = true;
        if (expectedLines.size() == resultLines.size()) {
            for (int i = 0; i < expectedLines.size(); i++) {
                String exp = expectedLines.get(i);
                String res = resultLines.get(i);
                if (!exp.equals(res)) {
                    System.out.println((i + 1) + ". line is :" + res + " instead of " + exp);
                    correct = false;
                }
            }
        } else {
            System.out.println("File size: " + resultLines.size() + " does not match expected length: " + expectedLines.size());
            correct = false;
        }

        if (correct) {
            System.out.println("NICE SOLUTION!!! ");
        } else {
            System.out.println("incorrect....");
        }
    }
}

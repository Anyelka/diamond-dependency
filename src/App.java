import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class App {
    private static final String FILE_NAME = "test";
    private static final String INPUT_PATH = "resources/" + FILE_NAME + ".in";
    private static final String EXPECTED_PATH = "resources/" + FILE_NAME + ".out";
    private static final String OUTPUT_PATH = "out/" + FILE_NAME + ".out";

    public static void main(String[] args) {
        // read file
        List<String> content = readAllLines(INPUT_PATH);
        // calculate result
        Collections.rotate(content, -1);
        String result = getDiamondDependencies(content);
//        List<String> resultLines = Arrays.asList(result.split("\n"));
        // write result
//        List<String> expected = readAllLines(EXPECTED_PATH);
//        checkFileContent(expected, resultLines);
        writeFile(OUTPUT_PATH, result);
    }

    private static String getDiamondDependencies(List<String> content) {
        StringBuilder result = new StringBuilder();
        Map<String, List<String>> dependencyGraph = new HashMap<>();

        for (int i = 1; i < content.size(); i++) {
            String line = content.get(i);
            if (isNumeric(line)) {
                String hasDiamondDependency = hasDiamondDependency(dependencyGraph) ? "yes" :"no";
                result.append(hasDiamondDependency + "\n");
                dependencyGraph = new HashMap<>();
            } else {
                String[] splitLine = line.split(" ");
                String library = splitLine[1];
                for (int j = 1; j < splitLine.length; j++) {
                    if (j == 1) {
                        dependencyGraph.put(library, new ArrayList<>());
                    } else {
                        dependencyGraph.get(library).add(splitLine[j]);
                    }
                }
            }
        }
        return result.toString();
    }

    private static boolean hasDiamondDependency(Map<String, List<String>> dependencyGraph) {
        return dependencyGraph.entrySet().stream().anyMatch(entry -> {
            String lib = entry.getKey();
            List<List<String>> dependencyPaths = getDependencyPaths(lib, dependencyGraph);
            for (int i = 0; i < dependencyPaths.size(); i++) {
                List<String> subDepPath = dependencyPaths.get(i);
                for (int j = 0; j < subDepPath.size(); j++) {
                    List<String> prevDepPath = subDepPath.subList(0, j);
                        String currentDep = subDepPath.get(j);
                        if(dependencyPaths.stream().anyMatch(otherDepPath ->
                                !otherDepPath.containsAll(prevDepPath) && otherDepPath.contains(currentDep))) {
                            return true;
                        }
                }
            }
            return false;
        });
    }

    private static List<List<String>> getDependencyPaths(String library, Map<String, List<String>> dependencyGraph) {
        List<List<String>> dependencyPaths = new ArrayList<>();
        List<String> directDeps = dependencyGraph.get(library);
        if(directDeps.isEmpty()) {
            dependencyPaths.add(Collections.singletonList(library));
        }
        directDeps.forEach(directDep -> {
            List<List<String>> directDepDepPaths = getDependencyPaths(directDep, dependencyGraph);
            directDepDepPaths.forEach(directDepDepPath -> {
                List<String> depPath = new ArrayList<>();
                depPath.add(library);
                depPath.addAll(directDepDepPath);
                dependencyPaths.add(depPath);
            });
        });
        return dependencyPaths;
    }


    private static boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }


    public static List<String> readAllLines(String path) {
        try {
            return Files.readAllLines(Paths.get(path));
        } catch (IOException e) {
            throw new RuntimeException("Error reading file from path: " + path);
        }
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

    public static void checkFileContent(List<String> resultLines, List<String> expected) {
        boolean correct = true;
        if(expected.size() == resultLines.size()) {
            for (int i = 0; i < expected.size(); i++) {
                String exp = expected.get(i);
                String res = resultLines.get(i);
                if(!exp.equals(res)) {
                    System.out.println((i+1) + ". line is :" + res + " instead of " + exp);
                    correct = false;
                }
            }
        } else {
            correct = false;
        }
        System.out.println("File is correct: " + correct);
    }
}

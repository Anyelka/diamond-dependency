import java.util.*;

public class Solution1 {

    private static String getDiamondDependencies(String content) {
        String[] lines = content.split("\n");
        StringBuilder result = new StringBuilder();

        int testCases = Integer.parseInt(lines[0]);

        int startLineNumber = 1;
        for(int i = 1; i <= testCases; i++) {
            int depGraphLength = Integer.parseInt(lines[startLineNumber]);
            startLineNumber += 1;
            Map<String, List<String>> dependencyGraph = new HashMap<>();
            for(int j = 0; j < depGraphLength; j++) {
                String currentLine = lines[startLineNumber + j];

                String[] libraryDefinition = currentLine.split(" ");
                String library = libraryDefinition[1];
                for (int k = 1; k < libraryDefinition.length; k++) {
                    if (k == 1) {
                        dependencyGraph.put(library, new ArrayList<>());
                    } else {
                        dependencyGraph.get(library).add(libraryDefinition[k]);
                    }
                }

                if(j == depGraphLength - 1) {
                    String hasDiamondDependency = hasDiamondDependency2(dependencyGraph) ? "yes" : "no";
                    result.append(hasDiamondDependency).append("\n");
                    startLineNumber += depGraphLength;
                }
            }

        }

        return result.toString();
    }

    private static boolean hasDiamondDependency2(Map<String, List<String>> depGraph) {
        String firstLib = depGraph.keySet().stream().findFirst().get();
        return hasDiamondDependency2(firstLib, depGraph);
    }

    private static boolean hasDiamondDependency2(String library, Map<String, List<String>> depGraph) {

        boolean hasDiamondDependency = false;

        Map<String, List<String>> newDepGraph = Map.copyOf(depGraph);
        newDepGraph.remove(library);
        String firstLib = newDepGraph.keySet().stream().findFirst().get();
        return hasDiamondDependency || hasDiamondDependency2(firstLib, newDepGraph);
    }

    public static String getDiamondDependencies(List<String> content) {
        content.remove(0);
        StringBuilder result = new StringBuilder();
        Map<String, List<String>> dependencyGraph = new HashMap<>();

        int dependencyGraphLength = 0;
        int i = 0;
        for (String line : content) {
            if (isNumeric(line)) {
                i = 0;
                dependencyGraphLength = Integer.parseInt(line);
                dependencyGraph = new HashMap<>();
            } else {
                String[] libraryDefinition = line.split(" ");
                String library = libraryDefinition[1];
                for (int j = 1; j < libraryDefinition.length; j++) {
                    if (j == 1) {
                        dependencyGraph.put(library, new ArrayList<>());
                    } else {
                        dependencyGraph.get(library).add(libraryDefinition[j]);
                    }
                }
                i++;
            }
            if (i == dependencyGraphLength) {
                String hasDiamondDependency = hasDiamondDependency(dependencyGraph) ? "yes" : "no";
                result.append(hasDiamondDependency).append("\n");
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
                    if (dependencyPaths.stream().anyMatch(otherDepPath ->
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
        if (directDeps.isEmpty()) {
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

}

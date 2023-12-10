import java.util.*;
import java.util.stream.Collectors;

public class Solution2 {
    private static Map<String, Library> libraries = new HashMap<>();

    public List<String> getDiamondDependencies(List<List<String>> inputData) {
        return inputData.stream().map(this::getResultForSingleGroup).collect(Collectors.toList());
    }

    public String getResultForSingleGroup(List<String> rows) {
        List<Library> libraries = rows.stream().map(row -> getLibrary(row, rows)).collect(Collectors.toList());

        boolean result = libraries.stream().anyMatch(Library::hasDiamondDependency);

        return result ? "yes" : "no";
    }

    /** 1. Build all the libraries with dependencies */
    private Library getLibrary(String row, List<String> rows) {

        String[] words = row.split(" ");
        String libraryName = words[1];

        Library library = libraries.get(libraryName);

        if(library == null) {
            if(words[0].equals("0")) {
                library = new Library(libraryName);
            } else {
                library = new Library(libraryName, getDependencies(rows, words));
                libraries.put(libraryName, library);
            }
        }
        return library;
    }

    private List<Library> getDependencies(List<String> rows, String[] words) {
        List<String> dependencyNames = new ArrayList<>();
        for(int i = 2; i < words.length; i++) {
            dependencyNames.add(words[i]);
        }
        List<Library> dependencies = dependencyNames.stream()
                .map(depName -> this.getLibrary(getRowByName(depName, rows), rows))
                .collect(Collectors.toList());
        return dependencies;
    }

    private String getRowByName(String libraryName, List<String> rows) {
        return rows.stream().filter(row -> row.split(" ")[1].equals(libraryName)).findFirst().get();
    }

    private class Library {
        String name;
        List<Library> dependencies;

        public Library(String name) {
            this.name = name;
            this.dependencies = Collections.emptyList();
        }

        public Library(String name, List<Library> dependencies) {
            this.name = name;
            this.dependencies = dependencies;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Library library = (Library) o;
            return Objects.equals(name, library.name);
        }

        private boolean dependsOnDirectly(Library other) {
            return this.dependencies.contains(other);
        }

        private boolean dependsOnTransitively(Library other) {
            return this.dependencies.stream().anyMatch(dependency -> dependency.dependsOn(other));
        }

        boolean dependsOn(Library other) {
            return dependsOnDirectly(other) || dependsOnTransitively(other);
        }

        boolean dependsOnAny(List<Library> others) {
            return others.stream().anyMatch(this::dependsOn)
                    || others.stream().anyMatch(other -> this.dependsOnAny(other.dependencies));
        }

        boolean dependsOnAnySubLibrary(Library other) {
            return this.dependsOn(other) || this.dependsOnAny(other.dependencies);
        }

        boolean hasDiamondDependency() {
            return this.dependencies.stream().anyMatch(library ->
                    this.dependencies.stream().anyMatch(other ->
                        !library.equals(other) && library.dependsOnAnySubLibrary(other)
                    )
            );
        }

    }
}

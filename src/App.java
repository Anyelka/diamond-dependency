import java.util.*;
import java.util.stream.Collectors;

public class App {

    public static void main(String[] args) {
        // read file
//        List<String> content = Utils.readAllLines();

        List<List<String>> inputData = Utils.getInputData();
        // calculate result
//        String result = Solution2.getDiamondDependencies(content);
        List<String> resultRows = new Solution2().getDiamondDependencies(inputData);
        // write result
        String result = resultRows.stream().collect(Collectors.joining("\n"));
        Utils.writeFile(result);
        Utils.checkFileContent(result);
    }

}

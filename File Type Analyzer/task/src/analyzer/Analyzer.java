package analyzer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class Analyzer {
    private Path file;
    private Map<String, String> patternTypes = new HashMap<>();

    public void setFile(String fileName) {
        file = Path.of(fileName);
    }

    public void addPattern(String pattern, String fileType) {
        patternTypes.put(pattern, fileType);
    }

    public String analyze() {
        try (var reader = Files.newBufferedReader(file)) {
            for (String pattern : patternTypes.keySet()) {
                if (reader.lines().anyMatch(line -> Pattern.compile(pattern).matcher(line).find())) {
                    return patternTypes.get(pattern);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Unknown file type";
    }
}

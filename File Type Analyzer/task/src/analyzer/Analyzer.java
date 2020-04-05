package analyzer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Analyzer {
    private Path file;
    private Map<String, String> patternTypes = new HashMap<>();

    private List<String> result;

    private final ExecutorService pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());


    public void setFile(String fileName) {
        file = Path.of(fileName);
    }

    public List<String> getResult() {
        return result;
    }

    public void addPattern(String pattern, String fileType) {
        patternTypes.put(pattern, fileType);
    }

    public void analyze() throws IOException, InterruptedException {
        result = new ArrayList<>();
        try (var walk = Files.walk(file)) {
            walk.filter(Files::isRegularFile)
                    .map(path -> (Runnable) () -> kmp(path))
                    .forEach(pool::submit);
        }
        pool.shutdown();
        while (!pool.isTerminated()) {
            pool.awaitTermination(10, TimeUnit.SECONDS);
        }
    }

    private void kmp(Path path) {
        var res = path.getFileName().toString() + ": ";
        try {
            var bytes = Files.readAllBytes(path);
            for (String pattern : patternTypes.keySet()) {
                var patternBytes = pattern.getBytes();
                var prefix = getPrefix(patternBytes);

                int j = 0;
                for (int i = 0; i < bytes.length; i++) {
                    while (j > 0 && bytes[i] != patternBytes[j]) {
                        j = prefix[j - 1];
                    }
                    if (bytes[i] == patternBytes[j]) {
                        j += 1;
                    }
                    if (j == patternBytes.length) {
                        res +=  patternTypes.get(pattern);
                        result.add(res);
                        return;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        res += "Unknown file type";
        result.add(res);
    }

    private int[] getPrefix(byte[] bytes) {
        var prefix = new int[bytes.length];

        for (int i = 1; i < bytes.length; i++) {
            int j = prefix[i - 1];
            while (j > 0 && bytes[i] != bytes[j]) {
                j = prefix[j - 1];
            }

            if (bytes[i] == bytes[j]) {
                j++;
            }

            prefix[i] = j;
        }
        return prefix;
    }
}

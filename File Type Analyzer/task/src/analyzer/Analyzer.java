package analyzer;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Analyzer {
    private Path file;
    private Map<String, String> patternTypes = new HashMap<>();

    public void setFile(String fileName) {
        file = Path.of(fileName);
    }

    public void addPattern(String pattern, String fileType) {
        patternTypes.put(pattern, fileType);
    }

    public String analyze(String alg) {
        try (var bis = new BufferedInputStream(Files.newInputStream(file))) {
            var bytes = bis.readAllBytes();
            switch (alg) {
                case "--naive":
                    return naive(bytes);
                case "--KMP":
                    return kmp(bytes);
                default:
                    return "Unknown algorithm";
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "Error occurred while working with file";
        }
    }

    private String naive(byte[] bytes) {
        for (String pattern : patternTypes.keySet()) {
            var patternBytes = pattern.getBytes();

            for (int i = pattern.length(); i <= bytes.length; i++) {
                if (Arrays.equals(patternBytes, 0, patternBytes.length,
                        bytes, i - patternBytes.length, i)) {
                    return patternTypes.get(pattern);
                }
            }
        }
        return "Unknown file type";
    }

    private String kmp(byte[] bytes) {
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
                    return patternTypes.get(pattern);
                }
            }
        }
        return "Unknown file type";
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

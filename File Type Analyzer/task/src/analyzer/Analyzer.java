package analyzer;

import java.util.ArrayList;
import java.util.List;

public class Analyzer {
    private List<FileTypePattern> patterns = new ArrayList<>();

    public void addPattern(FileTypePattern pattern) {
        patterns.add(pattern);
    }

    public String analyze(byte[] bytes, String fileName) {
        return kmp(bytes, fileName);
    }

    private String kmp(byte[] bytes, String fileName) {
        FileTypePattern res = null;
        for (var pattern : patterns) {
            var regexBytes = pattern.getRegex().getBytes();
            var prefix = getPrefix(regexBytes);

            int j = 0;
            for (byte aByte : bytes) {
                while (j > 0 && aByte != regexBytes[j]) {
                    j = prefix[j - 1];
                }
                if (aByte == regexBytes[j]) {
                    j += 1;
                }
                if (j == regexBytes.length) {
                    if (res == null || res.getPrio() < pattern.getPrio()) {
                        res = pattern;
                        break;
                    }
                }
            }
        }
        return res == null ? fileName + ": Unknown file type" : fileName + ": " + res.getName();
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

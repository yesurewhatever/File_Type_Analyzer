package analyzer;

public class FileTypePattern {
    private final int prio;
    private final String regex;
    private final String name;

    public FileTypePattern(int prio, String regex, String name) {
        this.prio = prio;
        this.regex = regex;
        this.name = name;
    }

    public int getPrio() {
        return prio;
    }

    public String getRegex() {
        return regex;
    }

    public String getName() {
        return name;
    }
}

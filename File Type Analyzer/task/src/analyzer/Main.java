package analyzer;

public class Main {
    public static void main(String[] args) {
        var analyzer = new Analyzer();
        analyzer.setFile(args[0]);
        analyzer.addPattern(args[1], args[2]);

        System.out.println(analyzer.analyze());
    }
}

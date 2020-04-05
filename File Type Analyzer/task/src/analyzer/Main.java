package analyzer;

public class Main {
    public static void main(String[] args) {
        var analyzer = new Analyzer();
        analyzer.setFile(args[1]);
        analyzer.addPattern(args[2], args[3]);

        System.out.println(analyzer.analyze(args[0]));
    }
}

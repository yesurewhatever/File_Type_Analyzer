package analyzer;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        var analyzer = new Analyzer();

        analyzer.setFile(args[0]);
        analyzer.addPattern(args[1], args[2]);
        analyzer.analyze();
        analyzer.getResult().forEach(System.out::println);
    }
}

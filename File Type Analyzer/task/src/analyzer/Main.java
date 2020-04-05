package analyzer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException, ExecutionException {
        var analyzer = new Analyzer();
        Files.lines(Path.of(args[1]))
                .forEach(line -> {
                    var split = line.split(";");
                    analyzer.addPattern(new FileTypePattern(Integer.parseInt(split[0]),
                            split[1].replaceAll("^\"|\"$", ""),
                            split[2].replaceAll("^\"|\"$", "")));
                });

        var threadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        try (var walk = Files.walk(Path.of(args[0]))) {
            var l = walk.filter(Files::isRegularFile)
                    .map(path -> {
                        try {
                            var name = path.getFileName().toString();
                            var bytes = Files.readAllBytes(path);
                            return (Callable<String>) () -> analyzer.analyze(bytes, name);
                        } catch (IOException e) {
                            e.printStackTrace();
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .map(threadPool::submit)
                    .collect(Collectors.toList());
            threadPool.shutdown();
            while (!threadPool.isTerminated()) {
                threadPool.awaitTermination(10, TimeUnit.SECONDS);
            }

            for (Future<String> stringFuture : l) {
                System.out.println(stringFuture.get());
            }
        }
    }
}

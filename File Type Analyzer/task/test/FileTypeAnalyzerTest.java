
import analyzer.Main;
import org.hyperskill.hstest.v6.stage.BaseStageTest;
import org.hyperskill.hstest.v6.testcase.CheckResult;
import org.hyperskill.hstest.v6.testcase.TestCase;

import java.util.List;

class Clue {
    String response;
    String feedback;

    Clue(String response, String feedback) {
        this.response = response;
        this.feedback = feedback;
    }
}

public class FileTypeAnalyzerTest extends BaseStageTest<Clue> {
    public FileTypeAnalyzerTest() throws Exception {
        super(Main.class);
    }

    @Override
    public List<TestCase<Clue>> generate() {
        return List.of(
            new TestCase<Clue>()
                .addArguments(new String[]
                    {"doc.pdf", "%PDF-", "PDF document"})
                .addFile("doc.pdf", "PFDF%PDF-PDF")
                .setAttach(new Clue("PDF document", "The file had following content: " +
                    "PFDF%PDF-PDF and was analyzed for pattern %PDF-")),

            new TestCase<Clue>()
                .addArguments(new String[]
                    {"doc.txt", "%PDF-", "PDF document"})
                .addFile("doc.txt", "PFDF%PDF-PDF")
                .setAttach(new Clue("PDF document", "The file had following content: " +
                    "PFDF%PDF-PDF and was analyzed for pattern %PDF-")),

            new TestCase<Clue>()
                .addArguments(new String[]
                    {"doc.pdf", "%PDF-", "PDF document"})
                .addFile("doc.pdf", "PFDFPDF")
                .setAttach(new Clue("Unknown file type", "The file had following content: " +
                    "PFDFPDF and was analyzed for pattern %PDF-")),

            new TestCase<Clue>()
                .addArguments(new String[]
                    {"doc.txt", "%PDF-", "PDF document"})
                .addFile("doc.txt", "PFDFPDF")
                .setAttach(new Clue("Unknown file type", "The file had following content: " +
                    "PFDFPDF and was analyzed for pattern %PDF-")),



            new TestCase<Clue>()
                .addArguments(new String[]
                    {"doc.pdf", "%DOC-", "DOC document"})
                .addFile("doc.pdf", "PFDF%DOC-PDF")
                .setAttach(new Clue("DOC document", "The file had following content: " +
                    "PFDF%PDF-PDF and was analyzed for pattern %PDF-")),

            new TestCase<Clue>()
                .addArguments(new String[]
                    {"doc.txt", "%DOC-", "DOC document"})
                .addFile("doc.txt", "PFDF%DOC-PDF")
                .setAttach(new Clue("DOC document", "")),

            new TestCase<Clue>()
                .addArguments(new String[]
                    {"doc.pdf", "%DOC-", "DOC document"})
                .addFile("doc.pdf", "PFDFPDF")
                .setAttach(new Clue("Unknown file type", "")),

            new TestCase<Clue>()
                .addArguments(new String[]
                    {"doc.txt", "%DOC-", "DOC document"})
                .addFile("doc.txt", "PFDFPDF")
                .setAttach(new Clue("Unknown file type", ""))
        );
    }

    @Override
    public CheckResult check(String reply, Clue clue) {
        String actual = reply.strip();
        String expected = clue.response.strip();
        return new CheckResult(actual.equals(expected),
            clue.feedback + "\nExpected result: " + expected +
                "\nActual result: " + actual);
    }
}

package cotuba.application;

import java.nio.file.Path;

public interface CotubaParameters {
    Path getMarkdownFolder();

    String getEbookFormat();

    Path getOutputFile();
}

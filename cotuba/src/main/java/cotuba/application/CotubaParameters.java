package cotuba.application;

import cotuba.domain.EbookFormat;

import java.nio.file.Path;

public interface CotubaParameters {
    Path getMarkdownFolder();

    EbookFormat getEbookFormat();

    Path getOutputFile();
}

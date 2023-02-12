package cotuba.epub;

import cotuba.domain.Chapter;
import cotuba.domain.Ebook;
import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.Resource;
import nl.siegmann.epublib.epub.EpubWriter;
import nl.siegmann.epublib.service.MediatypeService;

import java.io.IOException;
import java.nio.file.Files;

public class EPUBGenerator {
    public void generates(Ebook ebook) {
        var outputFile = ebook.getOutputFile();
        var epub = new Book();

        for (Chapter chapter : ebook.getChapters()) {
            epub.addSection(
                chapter.getTitle(),
                new Resource(chapter.getHtmlContent().getBytes(), MediatypeService.XHTML)
            );
        }

        try {
            var epubWriter = new EpubWriter();
            epubWriter.write(epub, Files.newOutputStream(outputFile));

        } catch (IOException ex) {
            throw new IllegalStateException("Erro ao criar arquivo EPUB: " + outputFile.toAbsolutePath(), ex);
        }
    }
}

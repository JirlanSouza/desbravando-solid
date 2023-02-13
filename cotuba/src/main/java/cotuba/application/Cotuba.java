package cotuba.application;

import cotuba.domain.Ebook;
import org.springframework.stereotype.Component;

import java.nio.file.Path;

@Component
public class Cotuba {
    private final MarkdownToHtmlRenderer htmlRenderer;
    private final EPUBGenerator epubGenerator;
    private final PDFGenerator pdfGenerator;

    public Cotuba(MarkdownToHtmlRenderer htmlRenderer, EPUBGenerator epubGenerator, PDFGenerator pdfGenerator) {
        this.htmlRenderer = htmlRenderer;
        this.epubGenerator = epubGenerator;
        this.pdfGenerator = pdfGenerator;
    }

    public void execute(String ebookFormat, Path markdownFolder, Path outputFile) {
        var chapters = htmlRenderer.render(markdownFolder);
        var ebook = new Ebook();
        ebook.setFormat(ebookFormat);
        ebook.setOutputFile(outputFile);
        ebook.setChapters(chapters);


        if ("pdf".equals(ebookFormat)) {
            pdfGenerator.generates(ebook);

        } else if ("epub".equals(ebookFormat)) {
            epubGenerator.generates(ebook);

        } else {
            throw new IllegalArgumentException("Formato do ebook inválido: " + ebookFormat);
        }
    }
}

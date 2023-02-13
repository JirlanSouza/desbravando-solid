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

    public void execute(CotubaParameters parameters) {
        var chapters = htmlRenderer.render(parameters.getMarkdownFolder());
        var ebook = new Ebook();
        ebook.setFormat(parameters.getEbookFormat());
        ebook.setOutputFile(parameters.getOutputFile());
        ebook.setChapters(chapters);


        if ("pdf".equals(parameters.getEbookFormat())) {
            pdfGenerator.generates(ebook);

        } else if ("epub".equals(parameters.getEbookFormat())) {
            epubGenerator.generates(ebook);

        } else {
            throw new IllegalArgumentException("Formato do ebook inválido: " + parameters.getEbookFormat());
        }
    }
}

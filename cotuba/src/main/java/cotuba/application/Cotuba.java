package cotuba.application;

import cotuba.epub.EPUBGenerator;
import cotuba.markdown.MarkdownToHtmlRenderer;
import cotuba.pdf.PDFGenerator;
import cotuba.domain.Ebook;

import java.nio.file.Path;

public class Cotuba {
    public void execute(String ebookFormat,Path markdownFolder, Path outputFile) {
        var htmlRenderer = new MarkdownToHtmlRenderer();
        var chapters = htmlRenderer.render(markdownFolder);
        var ebook = new Ebook();
        ebook.setFormat(ebookFormat);
        ebook.setOutputFile(outputFile);
        ebook.setChapters(chapters);

        if ("pdf".equals(ebookFormat)) {
            var pdfGenerator = new PDFGenerator();
            pdfGenerator.generates(ebook);

        } else if ("epub".equals(ebookFormat)) {
            var epubGenerator = new EPUBGenerator();
            epubGenerator.generates(ebook);

        } else {
            throw new IllegalArgumentException("Formato do ebook inválido: " + ebookFormat);
        }
    }
}

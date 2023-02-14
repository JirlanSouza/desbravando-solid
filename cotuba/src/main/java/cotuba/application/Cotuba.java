package cotuba.application;

import cotuba.domain.Ebook;
import cotuba.markdown.MarkdownToHtmlRenderer;

public class Cotuba {
    public void execute(CotubaParameters parameters) {
        var htmlRenderer = new MarkdownToHtmlRenderer();
        var chapters = htmlRenderer.render(parameters.getMarkdownFolder());
        var ebook = new Ebook();

        ebook.setFormat(parameters.getEbookFormat());
        ebook.setOutputFile(parameters.getOutputFile());
        ebook.setChapters(chapters);

        var ebookGenerator = ebook.getFormat().getGenerator();
        ebookGenerator.generates(ebook);
    }
}

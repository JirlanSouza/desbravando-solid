package cotuba;

public class Main {

    public static void main(String[] args) {
        var CLIOptions = new CLIOptionsReader(args);
        var markdownFolder = CLIOptions.getMarkdownFolder();
        var ebookFormat = CLIOptions.getEbookFormat();
        var outputFile = CLIOptions.getOutputFile();
        var verboseMode = CLIOptions.isVerboseMode();

        try {
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

            System.out.println("Arquivo gerado com sucesso: " + outputFile);

        } catch (Exception ex) {
            System.err.println(ex.getMessage());
            if (verboseMode) {
                ex.printStackTrace();
            }
            System.exit(1);
        }
    }

}

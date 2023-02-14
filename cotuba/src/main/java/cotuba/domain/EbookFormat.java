package cotuba.domain;

import cotuba.HTMLGenerator;
import cotuba.application.EbookGenerator;
import cotuba.epub.EPUBGenerator;
import cotuba.pdf.PDFGenerator;

public enum EbookFormat {
    PDF(new PDFGenerator()),
    EPUB(new EPUBGenerator()),
    HTML(new HTMLGenerator());

    final EbookGenerator generator;

    EbookFormat(EbookGenerator generator) {
        this.generator = generator;
    }

    public EbookGenerator getGenerator() {
        return generator;
    }
}

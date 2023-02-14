package cotuba;

import cotuba.application.EbookGenerator;
import cotuba.domain.Chapter;
import cotuba.domain.Ebook;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.Normalizer;

public class HTMLGenerator implements EbookGenerator {
    @Override
    public void generates(Ebook ebook) {
        try {
            Path htmlDirectory = Files.createDirectories(ebook.getOutputFile());
            var chapterNumber = 1;
            for (Chapter chapter : ebook.getChapters()) {
                var html = """
                    <!DOCTYPE html>
                    <html lang="pt-br">
                        <head>
                            <meta charset="utf-8">
                            <title>%s</title>
                        </head>
                        <body>
                            %s
                        </body>
                    </html>
                    """.formatted(chapter.getTitle(), chapter.getHtmlContent());

                var htmlFilePath = htmlDirectory.resolve(getFileName(chapter, chapterNumber));
                Files.writeString(htmlFilePath, html, StandardCharsets.UTF_8);
                chapterNumber++;
            }

        } catch (IOException exception) {
            throw new RuntimeException("Erro ao gerar o ebook no formato HTML", exception);
        }
    }

    private String getFileName(Chapter chapter, int chapterNumber) {
        return
            chapterNumber + "-" +
                removeSpecialCharacters(
                    chapter.getTitle().replaceAll("\\W", "")
                ) + ".html";
    }

    private String removeSpecialCharacters(String name) {
        return Normalizer.normalize(name, Normalizer.Form.NFD)
            .replaceAll("[^\\p{ASCII}]", "");
    }
}

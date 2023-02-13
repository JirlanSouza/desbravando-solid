package cotuba.markdown;

import cotuba.application.MarkdownToHtmlRenderer;
import cotuba.domain.Chapter;
import org.commonmark.node.AbstractVisitor;
import org.commonmark.node.Heading;
import org.commonmark.node.Node;
import org.commonmark.node.Text;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.util.List;

@Component
public class CommonmarkMarkdownToHtmlRenderer implements MarkdownToHtmlRenderer {
    @Override
    public List<Chapter> render(Path markdownFolder) {
        return getMarkdownFiles(markdownFolder)
            .stream()
            .map(markdownFile -> {
                var chapter = new Chapter();
                var document = markdownParse(markdownFile);
                document.accept(abstractVisitorFactory(chapter));
                renderToHtml(markdownFile, chapter, document);

                return chapter;
            })
            .toList();
    }

    private List<Path> getMarkdownFiles(Path markdownFolder) {
        try (var files = Files.list(markdownFolder)) {
            PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:**/*.md");
            return files.filter(matcher::matches).sorted().toList();

        } catch (IOException exception) {
            throw new IllegalStateException(
                "Erro tentando encontrar arquivos .md em " + markdownFolder.toAbsolutePath(),
                exception
            );
        }
    }

    private Node markdownParse(Path markdownFile) {
        try {
            var parser = Parser.builder().build();
            return parser.parseReader(Files.newBufferedReader(markdownFile));

        } catch (Exception exception) {
            throw new IllegalStateException("Erro ao fazer parse do arquivo " + markdownFile, exception);
        }
    }

    private void renderToHtml(Path markdownFile, Chapter chapter, Node document) {
        try {
            var renderer = HtmlRenderer.builder().build();
            var html = renderer.render(document);
            chapter.setHtmlContent(html);

        } catch (Exception ex) {
            throw new IllegalStateException("Erro ao renderizar para HTML o arquivo " + markdownFile, ex);
        }
    }

    private AbstractVisitor abstractVisitorFactory(Chapter chapter) {
        return new AbstractVisitor() {
            @Override
            public void visit(Heading heading) {
                if (heading.getLevel() == 1) {
                    String chapterTitle = ((Text) heading.getFirstChild()).getLiteral();
                    chapter.setTitle(chapterTitle);

                } else if (heading.getLevel() == 2) {
                    // seção
                } else if (heading.getLevel() == 3) {
                    // título
                }
            }
        };
    }
}

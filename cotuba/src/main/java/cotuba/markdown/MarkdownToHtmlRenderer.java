package cotuba.markdown;

import cotuba.domain.Chapter;
import org.commonmark.node.AbstractVisitor;
import org.commonmark.node.Heading;
import org.commonmark.node.Node;
import org.commonmark.node.Text;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class MarkdownToHtmlRenderer {
    public List<Chapter> render(Path markdownFolder) {
        var chapters = new ArrayList<Chapter>();
        PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:**/*.md");

        try (Stream<Path> markdownFiles = Files.list(markdownFolder)) {
            markdownFiles.filter(matcher::matches).sorted().forEach(markdownFile -> {
                var parser = Parser.builder().build();
                Node document = null;
                var chapter = new Chapter();

                try {
                    document = parser.parseReader(Files.newBufferedReader(markdownFile));
                    document.accept(new AbstractVisitor() {
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
                    });
                } catch (Exception ex) {
                    throw new IllegalStateException("Erro ao fazer parse do arquivo " + markdownFile, ex);
                }

                try {
                    var renderer = HtmlRenderer.builder().build();
                    var html = renderer.render(document);
                    chapter.setHtmlContent(html);
                    chapters.add(chapter);

                } catch (Exception ex) {
                    throw new IllegalStateException("Erro ao renderizar para HTML o arquivo " + markdownFile, ex);
                }
            });
        } catch (IOException ex) {
            throw new IllegalStateException("Erro tentando encontrar arquivos .md em " + markdownFolder.toAbsolutePath(), ex);
        }

        return chapters;
    }
}

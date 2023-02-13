package cotuba.application;

import cotuba.domain.Chapter;

import java.nio.file.Path;
import java.util.List;

public interface MarkdownToHtmlRenderer {
    List<Chapter> render(Path markdownFolder);
}

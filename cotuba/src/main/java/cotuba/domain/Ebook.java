package cotuba.domain;

import cotuba.domain.Chapter;

import java.nio.file.Path;
import java.util.List;

public class Ebook {
    private String format;
    private Path outputFile;
    private List<Chapter> chapters;

    public boolean isLastChapter(Chapter chapter) {
        return chapters.get(chapters.size() - 1)
            .equals(chapter);
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public Path getOutputFile() {
        return outputFile;
    }

    public void setOutputFile(Path outputFile) {
        this.outputFile = outputFile;
    }

    public List<Chapter> getChapters() {
        return chapters;
    }

    public void setChapters(List<Chapter> chapters) {
        this.chapters = chapters;
    }
}

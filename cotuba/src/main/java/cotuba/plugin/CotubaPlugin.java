package cotuba.plugin;

import cotuba.domain.Ebook;

public interface CotubaPlugin {
    String afterRendering(String html);
    void afterGeneration(Ebook ebook);
}

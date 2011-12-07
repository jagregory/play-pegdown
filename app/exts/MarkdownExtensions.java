package exts;

import play.templates.BaseTemplate;
import play.templates.JavaExtensions;

public class MarkdownExtensions extends JavaExtensions {
    public static BaseTemplate.RawData markdown(String markdown) {
        return JavaExtensions.raw(Markdown.toHtml(markdown));
    }
}

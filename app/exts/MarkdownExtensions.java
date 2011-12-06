package exts;

import play.templates.JavaExtensions;

public class MarkdownExtensions extends JavaExtensions {
    public static String markdown(String markdown) {
        return Markdown.toHtml(markdown);
    }
}

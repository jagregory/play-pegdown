package exts;

import play.templates.BaseTemplate;
import play.templates.JavaExtensions;

import java.util.HashMap;
import java.util.Map;

public class MarkdownExtensions extends JavaExtensions {
    public static BaseTemplate.RawData markdown(String markdown) {
        return JavaExtensions.raw(Markdown.toHtml(markdown));
    }

    public static BaseTemplate.RawData markdownInline(String markdown) {
        Map<Object, Object> opts = new HashMap<Object, Object>();

        opts.put("inlineSingleParagraph", "true");

        return JavaExtensions.raw(Markdown.toHtml(markdown, opts));
    }
}

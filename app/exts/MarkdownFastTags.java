package exts;

import groovy.lang.Closure;
import play.templates.FastTags;
import play.templates.GroovyTemplate;
import play.templates.JavaExtensions;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

@FastTags.Namespace("markdown")
public class MarkdownFastTags extends FastTags {
    @SuppressWarnings("unchecked")
    public static void _inline(Map args, Closure body, PrintWriter out, GroovyTemplate.ExecutableTemplate template, int fromLine) {
        String markdown = JavaExtensions.toString(body);

        Map<Object, Object> opts = new HashMap<Object, Object>();
        opts.put("inlineSingleParagraph", "true");
        opts.putAll(args);

        String html = Markdown.toHtml(markdown, opts);

        out.print(html);
    }
}
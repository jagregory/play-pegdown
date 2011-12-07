package exts;

import groovy.lang.Closure;
import play.templates.FastTags;
import play.templates.GroovyTemplate;
import play.templates.JavaExtensions;

import java.io.PrintWriter;
import java.util.Map;

public class MarkdownFastTag extends FastTags {
    @SuppressWarnings("unchecked")
    public static void _markdown(Map args, Closure body, PrintWriter out, GroovyTemplate.ExecutableTemplate template, int fromLine) {
        String markdown = JavaExtensions.toString(body);
        String html = Markdown.toHtml(markdown, args);

        out.print(html);
    }
}
package exts;

import groovy.lang.Closure;
import play.templates.FastTags;
import play.templates.GroovyTemplate;
import play.templates.JavaExtensions;

import java.io.PrintWriter;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MarkdownFastTags extends FastTags {
    final static Pattern WHITESPACE = Pattern.compile("^([\\s]+).*");

    @SuppressWarnings("unchecked")
    public static void _markdown(Map args, Closure body, PrintWriter out, GroovyTemplate.ExecutableTemplate template, int fromLine) {
        String markdown = JavaExtensions.toString(body);
        Object preserveWhitespace = args.get("preserveWhitespace");

        if (preserveWhitespace == null || preserveWhitespace.equals(false)) {
            int indentToTrim = getMinimumIndentLevel(markdown);

            markdown = dedent(markdown, indentToTrim);
        }
        
        String html = Markdown.toHtml(markdown, args);

        out.print(html);
    }

    static int getMinimumIndentLevel(String input) {
        String[] lines = input.split("\\r?\\n|\\r");
        
        int indent = Integer.MAX_VALUE;

        for (String line : lines) {
            Matcher m = WHITESPACE.matcher(line);

            if (m.find()) {
                String whitespace = m.group(1);
                
                if (whitespace.length() == line.length()) {
                    continue; // skip whitespace lines
                }

                if (indent > whitespace.length()) {
                    indent = whitespace.length();
                }
            }
        }
        
        return indent;
    }

    static String dedent(String input, int amount) {
        if (amount == 0) {
            return input;
        }

        String[] lines = input.split("\\r?\\n|\\r");
        StringBuilder sb = new StringBuilder(input.length());
        
        for (String line : lines) {
            sb.append(ltrim(line, amount))
              .append("\n");
        }
        
        return sb.toString();
    }

    static String ltrim(String input, int amount) {
        return input.replaceAll("^[\\s]{0,"+amount+"}(.*)", "$1");
    }
}
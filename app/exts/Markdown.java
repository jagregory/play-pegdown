package exts;

import org.pegdown.Extensions;
import org.pegdown.PegDownProcessor;
import play.Play;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Markdown {
    final static Pattern WHITESPACE = Pattern.compile("^([\\s]+).*");

    public static String toHtml(String markdown) {
        return toHtml(markdown, new HashMap<Object, Object>());
    }
    
    public static String toHtml(String markdown, Map<Object, Object> options) {
        int extensions = 0;

        OverriddenConfigOptions overriddenConfigOptions = new OverriddenConfigOptions(options);

        if (enabled("abbreviations", overriddenConfigOptions)) {
            extensions |= Extensions.ABBREVIATIONS;
        }
        if (enabled("all", overriddenConfigOptions)) {
            extensions |= Extensions.ALL;
        }
        if (enabled("autolinks", overriddenConfigOptions)) {
            extensions |= Extensions.AUTOLINKS;
        }
        if (enabled("definitions", overriddenConfigOptions)) {
            extensions |= Extensions.DEFINITIONS;
        }
        if (enabled("fencedCodeBlocks", overriddenConfigOptions)) {
            extensions |= Extensions.FENCED_CODE_BLOCKS;
        }
        if (enabled("hardwraps", overriddenConfigOptions)) {
            extensions |= Extensions.HARDWRAPS;
        }
        if (enabled("none", overriddenConfigOptions)) {
            extensions |= Extensions.NONE;
        }
        if (enabled("quotes", overriddenConfigOptions)) {
            extensions |= Extensions.QUOTES;
        }
        if (enabled("smarts", overriddenConfigOptions)) {
            extensions |= Extensions.SMARTS;
        }
        if (enabled("smartypants", overriddenConfigOptions)) {
            extensions |= Extensions.SMARTYPANTS;
        }
        if (enabled("suppressAllHtml", overriddenConfigOptions)) {
            extensions |= Extensions.SUPPRESS_ALL_HTML;
        }
        if (enabled("suppressHtmlBlocks", overriddenConfigOptions)) {
            extensions |= Extensions.SUPPRESS_HTML_BLOCKS;
        }
        if (enabled("suppressInlineHtml", overriddenConfigOptions)) {
            extensions |= Extensions.SUPPRESS_INLINE_HTML;
        }
        if (enabled("tables", overriddenConfigOptions)) {
            extensions |= Extensions.TABLES;
        }
        if (enabled("wikilinks", overriddenConfigOptions)) {
            extensions |= Extensions.WIKILINKS;
        }

        if (enabled("preserveWhitespace", "true", overriddenConfigOptions)) {
            int indentToTrim = getMinimumIndentLevel(markdown);

            markdown = dedent(markdown, indentToTrim);
        }

        String html = new PegDownProcessor(extensions)
            .markdownToHtml(markdown);
        
        if (enabled("inline", overriddenConfigOptions)) {
            html = inlineSingleParagraph(html);
        }

        return html;
    }

    static String inlineSingleParagraph(String html) {
        if (countMatches(html, "<p>") > 1) {
            return html;
        }

        return html
            .replace("<p>", "")
            .replace("</p>", "");
    }

    public static int countMatches(String haystack, String needle) {
        int count = 0;
        int i = 0;
        while ((i = haystack.indexOf(needle, i)) != -1) {
            count++;
            i += needle.length();
        }
        return count;
    }

    static boolean enabled(String key, OverriddenConfigOptions options) {
        return enabled(key, null, options);
    }

    static boolean enabled(String key, Object defaultValue, OverriddenConfigOptions options) {
        Object value = options.get(key);
        
        if (value == null) {
            value = defaultValue;
        }

        return value != null && "true".equalsIgnoreCase(value.toString());
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
    
    static class OverriddenConfigOptions {
        Map<Object, Object> options;
        
        public OverriddenConfigOptions(Map<Object, Object> options) {
            this.options = options;
        }
        
        public Object get(Object key) {
            Object result = options.get(key);
            
            if (result != null)
                return result;

            return Play.configuration.get("pegdown."+key);
        }
    }
}
package exts;

import org.pegdown.Extensions;
import org.pegdown.PegDownProcessor;
import play.Play;

public class Markdown {
    public static String toHtml(String markdown) {
        int extensions = 0;
        
        if (enabled("abbreviations")) {
            extensions |= Extensions.ABBREVIATIONS;
        }
        if (enabled("all")) {
            extensions |= Extensions.ALL;
        }
        if (enabled("autolinks")) {
            extensions |= Extensions.AUTOLINKS;
        }
        if (enabled("definitions")) {
            extensions |= Extensions.DEFINITIONS;
        }
        if (enabled("fencedCodeBlocks")) {
            extensions |= Extensions.FENCED_CODE_BLOCKS;
        }
        if (enabled("hardwraps")) {
            extensions |= Extensions.HARDWRAPS;
        }
        if (enabled("none")) {
            extensions |= Extensions.NONE;
        }
        if (enabled("quotes")) {
            extensions |= Extensions.QUOTES;
        }
        if (enabled("smarts")) {
            extensions |= Extensions.SMARTS;
        }
        if (enabled("smartypants")) {
            extensions |= Extensions.SMARTYPANTS;
        }
        if (enabled("suppressAllHtml")) {
            extensions |= Extensions.SUPPRESS_ALL_HTML;
        }
        if (enabled("suppressHtmlBlocks")) {
            extensions |= Extensions.SUPPRESS_HTML_BLOCKS;
        }
        if (enabled("suppressInlineHtml")) {
            extensions |= Extensions.SUPPRESS_INLINE_HTML;
        }
        if (enabled("tables")) {
            extensions |= Extensions.TABLES;
        }
        if (enabled("wikilinks")) {
            extensions |= Extensions.WIKILINKS;
        }

        return new PegDownProcessor(extensions)
            .markdownToHtml(markdown);
    }
    
    static boolean enabled(String key) {
        Object value = Play.configuration.get("pegdown."+key);

        return value != null && "true".equalsIgnoreCase(value.toString());
    }
}
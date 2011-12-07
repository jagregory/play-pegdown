package exts;

import org.pegdown.Extensions;
import org.pegdown.PegDownProcessor;
import play.Play;

import java.util.HashMap;
import java.util.Map;

public class Markdown {
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

        return new PegDownProcessor(extensions)
            .markdownToHtml(markdown);
    }
    
    static boolean enabled(String key, OverriddenConfigOptions options) {
        Object value = options.get(key);

        return value != null && "true".equalsIgnoreCase(value.toString());
    }
    
    public static class OverriddenConfigOptions {
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
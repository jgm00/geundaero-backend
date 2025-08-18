package hexagon_talent.geundaero.util;

import org.springframework.web.util.HtmlUtils;

public final class TextSanitizer {
    private TextSanitizer() {}

    public static String clean(String src) {
        if (src == null) return null;
        String s = HtmlUtils.htmlUnescape(src);
        s = s.replaceAll("(?is)<br\\s*/?>", "\n");
        s = s.replaceAll("(?is)</p\\s*>", "\n");
        s = s.replaceAll("(?is)<[^>]+>", " ");
        s = s.replaceAll("[ \\t\\x0B\\f\\r]+", " ");
        s = s.replaceAll("\\s*\\n\\s*", "\n");
        return s.strip();
    }
}


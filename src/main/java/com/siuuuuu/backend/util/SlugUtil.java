package com.siuuuuu.backend.util;

import java.text.Normalizer;
import java.util.Locale;
import java.util.regex.Pattern;

public class SlugUtil {
    private static final Pattern NON_LATIN = Pattern.compile("[^\\w-]");
    private static final Pattern WHITESPACE = Pattern.compile("[\\s]");

    public static String toSlug(String input) {
        // Normalize the input to decompose accents
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        // Remove accents and other non-Latin characters
        String noAccents = normalized.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        // Replace whitespace with hyphens and remove non-alphanumeric characters
        String slug = WHITESPACE.matcher(noAccents).replaceAll("-");
        slug = NON_LATIN.matcher(slug).replaceAll("").toLowerCase(Locale.ENGLISH);
        return slug;
    }
}

package api;

import java.text.Normalizer;

public class StringUtil {

    /**
     * normalize a string by trimming spaces and converting to lowercase string
     * and deleting tones because the text is greek
     * @param input The string we want to normalize
     * @return the normalized string
     */
    public static String normalize(String input) {
        if (input == null) {
            throw new IllegalArgumentException("Input cannot be null");
        }
        String normalized = input.trim().toLowerCase(); //lowercase and trim spaces
        normalized = Normalizer.normalize(normalized, Normalizer.Form.NFD); //normalize to remove accents
        return normalized.replaceAll("\\p{M}", "");

    }
}

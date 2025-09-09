//simplificación de urls títulos legibles sin caracteres especiales

package com.eventos.ofertas.util;
import org.springframework.stereotype.Component;
@Component
public class SlugUtil {
    public static String slugify(String input) {
        if (input == null) return null;
        String s = input.toLowerCase()
            .replaceAll("[^a-z0-9\u00f1\u00e1\u00e9\u00ed\u00f3\u00fa\s-]", "")
            .replaceAll("[\u00f1]", "n")
            .replaceAll("[\u00e1]", "a")
            .replaceAll("[\u00e9]", "e")
            .replaceAll("[\u00ed]", "i")
            .replaceAll("[\u00f3]", "o")
            .replaceAll("[\u00fa]", "u")
            .trim()
            .replaceAll("\s+", "-");
        return s;
    }   
}

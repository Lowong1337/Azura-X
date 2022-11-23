package fr.ducouscous.csl.compiler.preprocessor;

import java.util.ArrayList;
import java.util.List;

public class PreProcessorUtil {
    public static String[] split(String str, char separator) {
        List<String> strings = new ArrayList<>();
        String current = "";
        boolean inString = false;
        for (char c : str.toCharArray()) {
            if (c == '"') inString = !inString;
            if (c == separator && !inString) {
                strings.add(current);
                current = "";
            } else {
                current += c;
            }
        }
        strings.add(current);

        return strings.toArray(new String[0]);
    }

    public static String replace(String str, String old, String n) {
        StringBuilder current = new StringBuilder();
        boolean inString = false;
        for (char c : str.toCharArray()) {
            if (c == '"') inString = !inString;
            current.append(c);
            if (current.toString().endsWith(old) && !inString) {
                current = new StringBuilder(current.substring(0, current.length() - old.length()));
                current.append(n);
            }
        }
        return current.toString();
    }
}

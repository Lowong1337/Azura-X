package fr.ducouscous.csl.compiler.preprocessor;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Preprocessor {
    HashMap<String, String> define = new HashMap<>();
    List<Condition> conditions = new ArrayList<>();
    public void instruction(String str, StringBuilder code, StringBuilder builder, int index, int bIndex) {
        String[] split = PreProcessorUtil.split(str, ' ');
        String name = split[0];
        switch (name) {
            case "define":
                if (split.length > 2) {
                    String value = str.substring(split[0].length() + split[1].length() + 2);
                    define.put(split[1], value);
                } else {
                    define.put(split[1], "");
                }
                break;
            case "ifndef":
                conditions.add(new Condition(split[1], Condition.Type.UNDEFINED));
                break;
            case "ifdef":
                conditions.add(new Condition(split[1], Condition.Type.DEFINED));
                break;
            case "endif":
                //conditions.remove(conditions.size() - 1);
                break;
            case "include":
                String path = str.substring(split[0].length()+1);
                File file = new File(path.substring(1, path.length()-1));
                try {
                    Scanner scanner = new Scanner(file);
                    StringBuilder buffer = new StringBuilder();
                    code.delete(index - str.length() - 1, index);
                    builder.delete(bIndex - str.length() - 1, bIndex);
                    buffer.append("\n");
                    while (scanner.hasNextLine()) {
                        buffer.append(scanner.nextLine());
                        buffer.append("\n");
                    }
                    buffer.append("\n");
                    code.insert(index, buffer);
                } catch (IOException ignored) {}
                System.out.println("========");
                System.out.println(code);
                System.out.println("========");

                break;
        }
    }
    public String preprocess(String str) {
        StringBuilder builder = new StringBuilder(), code = new StringBuilder(str), current = new StringBuilder();
        int bIndex = 0;
        for (int i = 0; i < code.toString().toCharArray().length; i++) {
            boolean canUse = true;
            for (Condition condition : conditions) {
                if (!condition.isValid(define))
                    canUse = false;
            }
            if (current.toString().endsWith("#endif")) {
                conditions.remove(conditions.size() - 1);
            }
            if (!canUse) {
                current.append(code.charAt(i));
                continue;
            }
            if (code.charAt(i) == '#') {
                StringBuilder buffer = new StringBuilder();
                while (code.charAt(i) != '\n') {
                    i++;
                    buffer.append(code.charAt(i));
                }
                buffer.delete(buffer.length()-1, buffer.length());
                instruction(buffer.toString(), code, builder, i, bIndex);
                continue;
            }
            builder.append(code.charAt(i));
            bIndex++;
        }
        String out = builder.toString();
        for (String s : define.keySet()) {
            out = PreProcessorUtil.replace(out, s, define.get(s));
        }

        System.out.println("=============");
        System.out.println(out);
        return out;
    }
}

package fr.ducouscous.csl.util;

import fr.ducouscous.csl.running.Instruction;
import fr.ducouscous.csl.running.variable.values.Table;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicReference;

public class StringUtil {
    public static String[] getMethodArguments(String path) {
        /*List<String> out = new ArrayList<>();
        String str = "";
        int p = 0;
        boolean inString = false;

        for (char c : path.toCharArray()) {
            if (c == '"') inString = !inString;
            if (c == ')' && !inString) p--;
            if (c == ',' && !inString) {
                if (p == 1) {
                    out.add(str);
                    str = "";
                }
            } else if (p > 0) {
                str += c;
            }
            if (c == '(' && !inString) p++;
        }
        if (!str.isEmpty()) out.add(str);

        return out.toArray(new String[0]);*/
        List<String> out = new ArrayList<>();
        String[] buffer = {""};
        update(path, (context)-> {
            if (context.getParen() == 1 && context.getBracket() == 0 && context.getCurrent() == ',') {
                out.add(buffer[0]);
                buffer[0] = "";
            } else if (context.getParen() > 0) {
                buffer[0] += context.getCurrent();
            }
            return null;
        });
        if (!buffer[0].isEmpty()) out.add(buffer[0]);

        return out.toArray(new String[0]);
    }

    public static boolean contains(String str, char c) {
        /*boolean inString = false;
        int p = 0, a = 0;

        for (char c1 : str.toCharArray()) {
            if (c1 == '"') inString = !inString;;
            if (c1 == '(') p++;
            if (c1 == ')') p--;
            if (c1 == '[') a++;
            if (c1 == ']') a--;
            if (c1 == c && !inString && a == 0 && p == 0) return true;
        }*/
        String out = update(str, (context) -> {
            if (!context.isInString() && context.getCurrent() == c && context.isOut()) return "";
            return null;
        });

        return out != null;
    }

    public static String[] split(String str, char c) {
        /*boolean inString = false;
        List<String> strings = new ArrayList<>();
        String s = "";
        int a = 0;

        for (char c1 : str.toCharArray()) {
            if (c1 == '"') inString = !inString;;
            if (c1 == '[') a++;
            if (c1 == ']') a--;
            if (c1 == c && a == 0 && !inString) {
                strings.add(s);
                s = "";
            } else {
                s += c1;
            }
        }
        if (!s.isEmpty()) strings.add(s);

        return strings.toArray(new String[0]);*/

        List<String> out = new ArrayList<>();
        String[] buffer = {""};

        update(str, (context) -> {
            if (context.getCurrent() == c && !context.isInString() && context.isOut()) {
                out.add(buffer[0]);
                buffer[0] = "";
            } else {
                buffer[0] += context.getCurrent();
            }
            return null;
        });
        if (!buffer[0].isEmpty()) out.add(buffer[0]);

        return out.toArray(new String[0]);
    }

    public static String getArrayIndex(String array) {
        /*int a = 0;
        boolean inString = false;
        String out = "";
        for (char c : array.toCharArray()) {
            if (c == '"') inString = !inString;
            if (c == ']' && !inString) a--;
            if (a > 0) out += c;
            if (c == '[' && !inString) a++;
        }

        return out;*/

        String[] out = new String[] {""};
        update(array, (context) -> {
            if (context.getBracket() > 0) out[0] += context.getCurrent();
            if (context.getCurrent() == ']') return out[0];
            return null;
        });
        return out[0];
    }

    public static String[] splitBool(String bool) {
        String[] sep = new String[] {"==", ">=", "<=", ">", "<"};
        for (String s : sep) {
            String[] split = split(bool, s);
            if (split.length == 2) {
                return split;
            }
        }
        return new String[] {bool};
    }

    public static boolean containsBool(String bool) {
        return contains(bool, "||") || contains(bool, "&&");
    }

    public static String getSign(String sign) {
        if (contains(sign, "==")) return "equals";
        if (contains(sign, ">=")) return "greater_equals";
        if (contains(sign, "<=")) return "lesser_equals";
        if (contains(sign, ">")) return "greater";
        if (contains(sign, "<")) return "lesser";
        return "";
    }

    public static boolean contains(String str, String content) {
        /*boolean inString = false;
        int p = 0;
        String b = "";
        for (char c : str.toCharArray()) {
            if (c == '"') inString = !inString;
            if (!inString) {
                if (c == '(') p++;
                if (c == ')') p--;
            }
            b += c;
            if (b.length() > content.length()) b = b.substring(1);
            if (b.equals(content) && !inString && p == 0) return true;
        }
        return false;*/

        String[] buffer = {""};
        String out = update(str, (context) -> {
            buffer[0] += context.getCurrent();
            if (!context.isInString() && buffer[0].endsWith(content) && context.isOut())
                return "";
            return null;
        });
        return out != null;
    }

    public static String update(String str, StringUpdate update) {
        int p = 0, a = 0;
        boolean inString = false;
        boolean i = false;
        for (char c : str.toCharArray()) {
            if (inString && c == '\\') {
                i = true;
                continue;
            }
            if (c == '"') {
                if (!(inString && i))
                    inString = !inString;
            }
            if (!inString) {
                if (c == ')') p--;
                if (c == ']') a--;
            }
            String s = update.update(new StringContext(c, inString, p, a));
            if (s != null) return s;
            if (!inString) {
                if (c == '(') p++;
                if (c == '[') a++;
            }
            if (i) i = false;
        }
        return null;
    }

    public static boolean contains(String str, String content, int pa) {
        /*boolean inString = false;
        int p = 0;
        String b = "";
        for (char c : str.toCharArray()) {
            if (c == '"') inString = !inString;
            if (!inString) {
                if (c == '(') p++;
                if (c == ')') p--;
            }
            b += c;
            if (b.length() > content.length()) b = b.substring(1);
            if (b.equals(content) && !inString && p == pa) return true;
        }
        return false;*/

        String[] buffer = {""};

        String s = update(str, (context) -> {
            if (context.getBracket() == 0 && context.getParen() == pa && context.isInString() && buffer[0].endsWith(content)) {
                return "";
            } else {
                buffer[0] += context.getCurrent();
            }
            return null;
        });

        return s != null;
    }

    public static String[] split(String str, String content) {
        /*List<String> strings = new ArrayList<>();
        boolean inString = false;
        int p = 0, a = 0;
        String b = "";
        for (char c : str.toCharArray()) {
            if (c == '"') inString = !inString;
            if (!inString) {
                if (c == '(') p++;
                if (c == ')') p--;
                if (c == '[') a++;
                if (c == ']') a--;
            }
            b += c;
            if (str.length() > content.length()) str = str.substring(1);
            if (b.endsWith(content) && !inString && p == 0) {
                strings.add(b.substring(0, b.length()-content.length()));
                b = "";
            }
        }
        if (!b.isEmpty()) strings.add(b);
        return strings.toArray(new String[0]);*/
        List<String> out = new ArrayList<>();
        String[] buffer = {""};

        update(str, (context) -> {
            buffer[0] += context.getCurrent();
            if (context.isOut() && !context.isInString() && buffer[0].endsWith(content)) {
                out.add(buffer[0].substring(0, buffer[0].length()-content.length()));
                buffer[0] = "";
            }
            return null;
        });
        if (!buffer[0].isEmpty()) out.add(buffer[0]);

        return out.toArray(new String[0]);
    }

    public static String[] splitPath(String path) {
        /*List<String> out = new ArrayList<>();
        String str = "";
        int p = 0;
        boolean inString = false;

        for (char c : path.toCharArray()) {
            if (c == '"') inString = !inString;
            if (c == '(' && !inString) p++;
            if (c == ')' && !inString) p--;
            if (c == '[' && !inString && p == 0) {
                out.add(str);
                str = "";
            }
            if (c == '.' && p == 0 && !inString) {
                out.add(str);
                str = "";
            } else {
                str += c;
            }
        }
        if (!str.isEmpty()) out.add(str);

        return out.toArray(new String[0]);*/
        List<String> out = new ArrayList<>();
        String[] buffer = {""};

        update(path, (context) -> {
            if (context.isOut() && !context.isInString() && context.getCurrent() == '.') {
                out.add(buffer[0]);
                buffer[0] = "";
            } else {
                buffer[0] += context.getCurrent();
            }
            return null;
        });
        if (!buffer[0].isEmpty())
            out.add(buffer[0]);

        return out.toArray(new String[0]);
    }

    public static String[] splitArray(String array) {
        /*List<String> out = new ArrayList<>();
        String str = "";
        int p = 0, a = 0;
        boolean inString = false;

        for (char c : array.toCharArray()) {
            if (c == '"') inString = !inString;
            if (c == '(' && !inString) p++;
            if (c == ')' && !inString) p--;
            if (c == '[' && !inString) a++;
            if (c == ']' && !inString) a--;
            if (c == ',' && p == 0 && a == 0 && !inString) {
                out.add(str);
                str = "";
            } else {
                str += c;
            }
        }
        if (!str.isEmpty()) out.add(str);

        return out.toArray(new String[0]);*/
        List<String> out = new ArrayList<>();
        String[] buffer = {""};

        update(array, (context) -> {
            if (!context.isInString() && context.isOut() && context.getCurrent() == ',') {
                out.add(buffer[0]);
                buffer[0] = "";
            } else {
                buffer[0] += context.getCurrent();
            }
            return null;
        });
        if (!buffer[0].isEmpty()) out.add(buffer[0]);

        return out.toArray(new String[0]);
    }

    public static String tableToString(Table table) {
        StringBuffer buf = new StringBuffer();
        buf.append("{");
        for (int i = 0; i < table.length(); i++) {
            buf.append(table.getValue(i).getValue());
            if (i != table.length()-1 || !table.getValues().isEmpty())
                buf.append(", ");
        }
        int i = 0;
        for (String s : table.getValues().keySet()) {
            buf.append(s);
            buf.append(" = ");
            buf.append(table.getValue(s).getValue().toString());
            i++;
            if (i < table.getValues().size())
                buf.append(", ");
        }
        buf.append("}");
        return buf.toString();
    }

    public static Object parseValue(String str) {
        if (str.matches("-?[0-9]+(\\.?[0-9]+)")) {
            return Double.parseDouble(str);
        }
        if (str.matches("-?[0-9]+")) {
            return Integer.parseInt(str);
        }
        if (str.matches("\".*\"")) {
            return str.substring(1, str.length()-1);
        }
        if (str.startsWith("ldc_")) {
            try {
                return Instruction.class.getField(str.toUpperCase(Locale.ROOT)).get(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return str;
    }

    public static int getStatementLabel(List<String> buffer, int i) {
        int k = 0, a = 0;
        for (int j = 0; j < buffer.size(); j++) {
            if (buffer.get(j).startsWith("if ")) {
                k++;
            }
            if (buffer.get(j).startsWith("label")) {
                k--;
                a++;
                if (k == 0 && j > i) return a;
            }
        }
        return -1;
    }
}

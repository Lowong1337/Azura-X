package fr.ducouscous.csl.compiler;

import fr.ducouscous.csl.running.Script;
import fr.ducouscous.csl.running.variable.Variable;
import fr.ducouscous.csl.running.variable.values.Function;
import fr.ducouscous.csl.running.variable.values.Number;
import fr.ducouscous.csl.running.variable.values.Table;
import fr.ducouscous.csl.util.ByteUtil;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static class Print extends Function {
        @Override
        public Variable run(Variable... variables) {
            System.out.println(variables[0]);
            return null;
        }
    }
    public static class Length extends Function {
        @Override
        public Variable run(Variable... variables) {
            return new Variable(new Number(((Table) variables[0].getValue()).length()));
        }
    }

    public static void main(String[] args) throws IOException {
        Compiler compiler = new Compiler();
        File out = new File("script.casl");
        Script script = compiler.compile(new File("script.asl"));
        compiler.write(out, script);
        script.setGlobalValue("print", new Print());
        script.load();
        script.main();
        /*URL url = new URL("https://api.namemc.com/server/play.norules.wtf/likes");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        String content = readString(connection.getInputStream());
        System.out.println(getArray(content).size());*/
    }
    static List<String> getArray(String str) {
        List<String> out = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        for (char c : str.toCharArray()) {
            if (c == '[') continue;
            if (c == ']') continue;
            if (c == '\n') continue;
            if (c == ' ') continue;
            if (c == ',') {
                out.add(current.toString());
                current = new StringBuilder();
            } else {
                current.append(c);
            }
        }
        if (!current.toString().isEmpty())
            out.add(current.toString());
        return out;
    }

    static String readString(InputStream stream) throws IOException {
        StringBuilder buffer = new StringBuilder();
        while (true) {
            int b = stream.read();
            if (b == -1) break;
            buffer.append((char) b);
        }
        return buffer.toString();
    }

    /*static int test(int x, int y) {
        int r = (x * 90) + (90 * y * y - y * 90) + (y * x * 135) + (y * (y + 1) / 2 * 180 * x * x);
        while (r < 0) r += 360;
        return r % 360;
    }*/
}

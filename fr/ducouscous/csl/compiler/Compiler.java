package fr.ducouscous.csl.compiler;

import fr.ducouscous.csl.compiler.preprocessor.Preprocessor;
import fr.ducouscous.csl.running.Instruction;
import fr.ducouscous.csl.running.Script;
import fr.ducouscous.csl.running.variable.Variable;
import fr.ducouscous.csl.running.variable.values.Function;
import fr.ducouscous.csl.util.ByteUtil;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Compiler {
    private final Lexer lexer = new Lexer();

    /*
    TODO:
        - PREPROCESSOR:
            - IFNDEF
            - IFDEFINE
            - ENDIF
        - BASIC FUNCTIONS:
            - PUSH
            - LENGTH (Table & String)
            - REMOVE
            - INDEX_OF
        - FOR STATEMENTS
        - ELSE and ELSE IF
        - SWITCH STATEMENT
     */
    void writeBuffer(List<Instruction> buffer, OutputStream writer) throws IOException {
        writeBuffer(buffer.toArray(new Instruction[0]), writer);
    }
    void writeBuffer(Instruction[] buffer, OutputStream writer) throws IOException {
        ByteUtil.writeInt(buffer.length, writer);
        for (Instruction instruction : buffer) {
            writer.write(instruction.getIndex());
            writer.write(instruction.getData().length);
            for (Object data : instruction.getData()) {
                if (data instanceof Double) {
                    writer.write(0);
                    ByteUtil.writeDouble((double) data, writer);
                } else if (data instanceof Integer) {
                    writer.write(1);
                    ByteUtil.writeInt((int) data, writer);
                } else if (data instanceof String) {
                    writer.write(2);
                    ByteUtil.writeString((String) data, writer);
                } else if (data instanceof Boolean) {
                    writer.write(3);
                    ByteUtil.writeBoolean((boolean) data, writer);
                } else {
                    System.out.println("Unknown Data ! (" + data + ")");
                }
            }
        }
    }

    public Script compile(File file) throws IOException {
        Scanner scanner = new Scanner(Files.newInputStream(file.toPath()));
        StringBuilder content = new StringBuilder();
        while (scanner.hasNextLine()) content.append(scanner.nextLine()).append("\n");
        return compile(content.toString());
    }

    public Script compile(String str) throws IOException {
        Script script = new Script();
        //str = new Preprocessor().preprocess(str);
        //ByteArrayOutputStream writer = new ByteArrayOutputStream();

        int func_amount = 1;

        lexer.read(str);
        List<LexerComponent> mainComponents = new ArrayList<>();
        for (int i = 0; i < lexer.getComponents().size(); i++) {
            LexerComponent component = lexer.getComponents().get(i);

            if (component.is(LexerType.KEYWORD)) {
                if (component.getData().equals(Keyword.IMPORT)) {
                    i++;
                    String name = ((String) lexer.getComponents().get(i).getData());
                    Script toMerge = new Compiler().compile(new File(name));

                    for (String s : toMerge.getGlobalTable().getValues().keySet()) {
                        if (toMerge.getGlobalValue(s).getValue() instanceof Function) {
                            script.setGlobalValue(s, toMerge.getGlobalValue(s));
                        }
                    }
                    continue;
                }
                if (component.getData().equals(Keyword.FUNCTION)) {
                    func_amount++;
                    String name = (String) lexer.getComponents().get(++i).getData();
                    //ByteUtil.writeString(name, writer);
                    List<String> arguments = new ArrayList<>();
                    i+=2;
                    while ((lexer.getComponents().get(i)).getType() != LexerType.R_PAREN) {
                        arguments.add((String) lexer.getComponents().get(i).getData());
                        if (lexer.getComponents().get(++i).getType() == LexerType.COMMA) {
                            i++;
                        }
                    }
                    i++;
                    int bracket = 1;
                    List<Instruction> buffer = new ArrayList<>();
                    CompileFunction current = new CompileFunction(false, new ArrayList<>(), buffer);
                    while (true) {
                        i++;
                        if (lexer.getComponents().get(i).getType() == LexerType.R_BRACKET) bracket--;
                        if (lexer.getComponents().get(i).getType() == LexerType.L_BRACKET) bracket++;
                        if (bracket == 0) break;
                        current.getComponents().add(lexer.getComponents().get(i));
                    }
                    for (String argument : arguments) {
                        current.getArguments().add(argument);
                    }
                    current.compile();
                    script.setGlobalValue(name, new Function(buffer.toArray(new Instruction[0])));
                    continue;
                }
            }
            mainComponents.add(component);
        }

        List<Instruction> buffer = new ArrayList<>();
        CompileFunction function = new CompileFunction(true, mainComponents, buffer);
        function.compile();
        script.setGlobalFunction(new Function(buffer.toArray(new Instruction[0])));
        //ByteUtil.writeString("global", writer);
        //writeBuffer(buffer, writer);
        //OutputStream out = Files.newOutputStream(output.toPath());
        //ByteUtil.writeInt(func_amount, out);
        //for (byte b : writer.toByteArray()) out.write(b);
        //writer.close();
        return script;
    }

    public void write(File file, Script script) throws IOException {
        FileOutputStream writer = new FileOutputStream(file);
        writer.write(script.getGlobalTable().size() + 1);

        for (String key : script.getGlobalTable().getValues().keySet()) {
            Variable value = script.getGlobalTable().getValue(key);
            if (value.getValue() instanceof Function) {
                ByteUtil.writeString(key, writer);
                writeBuffer(((Function) value.getValue()).getInstructions(), writer);
            }
        }
        ByteUtil.writeString("global", writer);
        writeBuffer(script.getGlobalFunction().getInstructions(), writer);
    }

    public Script load(File file) throws IOException {
        InputStream stream = Files.newInputStream(file.toPath());
        Script out = new Script();

        int func_amount = ByteUtil.readInt(stream);

        for (int i = 0; i < func_amount; i++) {
            String name = ByteUtil.readString(stream);
            List<Instruction> instructions = new ArrayList<>();
            int instruction_count = ByteUtil.readInt(stream);
            for (int j = 0; j < instruction_count; j++) {
                int instruction = stream.read();
                int data_amount = stream.read();
                List<Object> data = new ArrayList<>();

                for (int k = 0; k < data_amount; k++) {
                    int type = stream.read();
                    Object o = null;

                    switch (type) {
                        case 0:
                            //double
                            o = ByteUtil.readDouble(stream);
                            break;
                        case 1:
                            //int
                            o = ByteUtil.readInt(stream);
                            break;
                        case 2:
                            //string
                            o = ByteUtil.readString(stream);
                            break;
                        case 3:
                            //boolean
                            o = ByteUtil.readBoolean(stream);
                            break;
                    }
                    data.add(o);
                }
                instructions.add(new Instruction(instruction, data.toArray()));
            }
            if (name.equals("global")) {
                out.setGlobalFunction(new Function(instructions.toArray(new Instruction[0])));
            } else {
                out.setGlobalValue(name, new Function(instructions.toArray(new Instruction[0])));
            }
        }

        return out;
    }
}

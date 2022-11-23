package fr.ducouscous.csl.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;

public class ByteUtil {
    public static void writeString(String str, OutputStream stream) {
        writeInt(str.length(), stream);
        for (char c : str.toCharArray()) {
            writeChar(c, stream);
        }
    }

    public static void writeBoolean(boolean b, OutputStream stream) {
        try {
            stream.write(b ? 0 : 1);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean readBoolean(InputStream stream) {
        try {
            return stream.read() == 1;
        } catch (IOException e) {}
        return false;
    }

    public static char readChar(InputStream stream) {
        try {
            return (char) (stream.read() + (stream.read() << 8));
        } catch (IOException ignored) {}
        return 0;
    }
    public static int readInt(InputStream stream) {
        try {
            return stream.read() + (stream.read() << 8) + (stream.read() << 16) + (stream.read() << 24);
        } catch (IOException ignored) {}
        return 0;
    }
    public static String readString(InputStream stream) {
        StringBuffer buffer = new StringBuffer();

        int length = readInt(stream);
        while (length > 0) {
            buffer.append(readChar(stream));
            length--;
        }

        return buffer.toString();
    }
    public static void writeChar(char c, OutputStream stream) {
        try {
            stream.write(c & 0xFF);
            stream.write((c & 0xFF00) << 8);
        } catch (IOException ignored) {}
    }
    public static void writeInt(int i, OutputStream stream) {
        try {
            stream.write(i & 0xFF);
            stream.write((i & 0xFF00) >> 8);
            stream.write((i & 0xFF0000) >> 16);
            stream.write((i & 0xFF000000) >> 24);
        } catch (IOException ignored) {}
    }

    public static void writeDouble(double d, OutputStream stream) {
        try {
            byte[] output = new byte[8];
            long lng = Double.doubleToLongBits(d);
            for(int i = 0; i < 8; i++) output[i] = (byte)((lng >> ((7 - i) * 8)) & 0xff);
            stream.write(output);
        } catch (IOException ignored) {}
    }

    public static double readDouble(InputStream stream) {
        try {
            byte[] array = new byte[8];
            for (int i = 0; i < 8; i++) array[i] = (byte) stream.read();
            return ByteBuffer.wrap(array).getDouble();
        } catch (IOException ignored) {}
        return Double.MIN_VALUE;
    }
}

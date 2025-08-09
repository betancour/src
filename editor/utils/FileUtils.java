// -----------------------------
// File: src/editor/utils/FileUtils.java
// -----------------------------
package editor.utils;

import java.nio.file.*;
import java.io.*;

public class FileUtils {
    public static String readFile(Path p) throws IOException {
        // read with buffered reader into StringBuilder
        var sb = new StringBuilder();
        try (var reader = Files.newBufferedReader(p)) {
            char[] buf = new char[8192];
            int r;
            while ((r = reader.read(buf)) != -1) sb.append(buf, 0, r);
        }
        return sb.toString();
    }

    public static void writeFile(Path p, String content) throws IOException {
        try (var writer = Files.newBufferedWriter(p)) {
            writer.write(content);
        }
    }
}


// -----------------------------
// File: src/editor/utils/OSUtils.java
// -----------------------------
package editor.utils;

import java.io.IOException;

public class OSUtils {
    public static void openTerminal() throws IOException {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            // Open PowerShell in a new window
            new ProcessBuilder("cmd.exe", "/c", "start", "powershell").start();
        } else if (os.contains("mac")) {
            // Open Terminal.app
            new ProcessBuilder("open", "-a", "Terminal").start();
        } else {
            // Try common terminals on Linux; prefer x-terminal-emulator if available
            String[] cmds = {"x-terminal-emulator", "gnome-terminal", "konsole", "xterm", "alacritty", "kitty"};
            IOException last = null;
            for (var c : cmds) {
                try { new ProcessBuilder(c).start(); return; } catch (IOException ex) { last = ex; }
            }
            throw last != null ? last : new IOException("No terminal found");
        }
    }
}

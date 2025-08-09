// Project: ProgrammersEditorPrototype
// Java 21 - Modular, Swing-based, inspired by Programmer's File Editor (PFE)
// Structure: src/editor/... each class in its own file. Use `javac` to compile and `java` to run.

// -----------------------------
// File: src/editor/Main.java
// -----------------------------
package editor;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import editor.ui.EditorFrame;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Use system look and feel for better icon support
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                // Fall back to default look and feel
                System.err.println("Could not set system look and feel: " + e.getMessage());
            }
            
            var frame = new EditorFrame();
            frame.setVisible(true);
        });
    }
}

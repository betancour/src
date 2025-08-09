// -----------------------------
// File: src/editor/actions/FileActions.java
// -----------------------------
package editor.actions;

import javax.swing.*;
import editor.ui.TextAreaPanel;
import java.nio.file.Path;
import editor.utils.OSUtils;

public class FileActions {
    public static void newFile(JFrame parent, TextAreaPanel panel) {
        panel.newFile();
        parent.setTitle("Programmer's Editor - New File");
    }

    public static void openFile(JFrame parent, TextAreaPanel panel) {
        var chooser = new JFileChooser();
        if (chooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
            Path p = chooser.getSelectedFile().toPath();
            panel.loadFile(p);
            parent.setTitle("Programmer's Editor - " + p.getFileName());
        }
    }

    public static void saveFile(JFrame parent, TextAreaPanel panel, boolean saveAs) {
        if (saveAs || panel.getCurrentFile() == null) {
            var chooser = new JFileChooser();
            if (chooser.showSaveDialog(parent) == JFileChooser.APPROVE_OPTION) {
                Path p = chooser.getSelectedFile().toPath();
                panel.saveFile(p);
                parent.setTitle("Programmer's Editor - " + p.getFileName());
            }
        } else {
            panel.saveFile(panel.getCurrentFile());
        }
    }

    public static boolean confirmCloseAll(JFrame parent, TextAreaPanel panel) {
        if (panel.isModified()) {
            int ans = JOptionPane.showConfirmDialog(parent, "Save changes?", "Unsaved", JOptionPane.YES_NO_CANCEL_OPTION);
            if (ans == JOptionPane.CANCEL_OPTION) return false;
            if (ans == JOptionPane.YES_OPTION) {
                saveFile(parent, panel, false);
            }
        }
        return true;
    }

    public static void openTerminal(JFrame parent) {
        try {
            OSUtils.openTerminal();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(parent, "Unable to open terminal: " + e.getMessage());
        }
    }
}

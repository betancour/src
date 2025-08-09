// -----------------------------
// File: src/editor/ui/TextAreaPanel.java
// -----------------------------
package editor.ui;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;
import javax.swing.undo.*;
import java.awt.*;
import java.nio.file.Path;
import java.nio.file.Files;
import editor.utils.FileUtils;
import java.util.concurrent.*;

// Lightweight panel that uses JTextArea (fast for large files)
public class TextAreaPanel extends JPanel {
    private final JTextArea textArea;
    private final JScrollPane scrollPane;
    private Path currentFile;
    private volatile boolean modified = false;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private String fileType = "OS";
    private boolean readOnly = false;
    private final UndoManager undoManager = new UndoManager();

    public TextAreaPanel() {
        super(new BorderLayout());
        textArea = new JTextArea();
        textArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
        textArea.setLineWrap(false);
        textArea.setWrapStyleWord(false);

        scrollPane = new JScrollPane(textArea);
        scrollPane.setRowHeaderView(new LineNumberView(textArea));
        add(scrollPane, BorderLayout.CENTER);
        
        // Add undo support
        textArea.getDocument().addUndoableEditListener(undoManager);

        textArea.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { 
                setModified(true); 
                updateFileInfo();
            }
            public void removeUpdate(DocumentEvent e) { 
                setModified(true); 
                updateFileInfo();
            }
            public void changedUpdate(DocumentEvent e) { 
                setModified(true); 
            }
        });

        // caret update example
        textArea.addCaretListener(e -> {
            int pos = textArea.getCaretPosition();
            try {
                int line = textArea.getLineOfOffset(pos) + 1;
                int col = pos - textArea.getLineStartOffset(line - 1) + 1;
                // publish to status bar via property change
                firePropertyChange("caret", null, line + "," + col);
                
                // Also update file info
                int totalLines = textArea.getLineCount();
                firePropertyChange("fileInfo", null, new FileInfo(totalLines, readOnly, fileType));
            } catch (BadLocationException ex) { /* ignore */ }
        });
    }

    public void newFile() {
        textArea.setText("");
        currentFile = null;
        setModified(false);
        fileType = "OS";
        readOnly = false;
        firePropertyChange("fileInfo", null, new FileInfo(1, readOnly, fileType));
    }

    public void loadFile(Path path) {
        // load in background
        executor.submit(() -> {
            try {
                String content = FileUtils.readFile(path);
                SwingUtilities.invokeLater(() -> {
                    textArea.setText(content);
                    textArea.setCaretPosition(0);
                    currentFile = path;
                    setModified(false);
                    
                    // Detect file type based on line endings
                    fileType = detectFileType(content);
                    
                    // Check if file is read-only
                    readOnly = !Files.isWritable(path);
                    
                    firePropertyChange("file", null, path.toString());
                    firePropertyChange("fileInfo", null, new FileInfo(textArea.getLineCount(), readOnly, fileType));
                });
            } catch (Exception e) {
                SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(this, "Error: " + e.getMessage()));
            }
        });
    }

    public void saveFile(Path path) {
        final String content = textArea.getText();
        executor.submit(() -> {
            try {
                FileUtils.writeFile(path, content);
                SwingUtilities.invokeLater(() -> {
                    currentFile = path;
                    setModified(false);
                    firePropertyChange("file", null, path.toString());
                });
            } catch (Exception e) {
                SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(this, "Error: " + e.getMessage()));
            }
        });
    }

    public Path getCurrentFile() { return currentFile; }
    public boolean isModified() { return modified; }
    public void setModified(boolean m) { boolean old = this.modified; this.modified = m; firePropertyChange("modified", old, m); }
    public String getTextContent() { return textArea.getText(); }
    public void setTextContent(String s) { textArea.setText(s); }
    
    private void updateFileInfo() {
        SwingUtilities.invokeLater(() -> {
            int totalLines = textArea.getLineCount();
            firePropertyChange("fileInfo", null, new FileInfo(totalLines, readOnly, fileType));
        });
    }
    
    private String detectFileType(String content) {
        if (content.contains("\r\n")) {
            return "OS";  // Windows (CRLF)
        } else if (content.contains("\n")) {
            return "Unix"; // Unix/Linux (LF)
        } else {
            return "OS";  // Default to OS if no line endings found
        }
    }
    
    public void refreshReadOnlyStatus() {
        if (currentFile != null) {
            try {
                boolean oldReadOnly = readOnly;
                readOnly = !Files.isWritable(currentFile);
                if (oldReadOnly != readOnly) {
                    updateFileInfo();
                }
            } catch (Exception e) {
                // If we can't check permissions, assume read-only
                readOnly = true;
                updateFileInfo();
            }
        }
    }
    
    public void setFileType(String newFileType) {
        if (!newFileType.equals(fileType)) {
            fileType = newFileType;
            updateFileInfo();
        }
    }
    
    public String getFileType() {
        return fileType;
    }
    
    public boolean isReadOnly() {
        return readOnly;
    }
    
    public JTextArea getTextArea() {
        return textArea;
    }
    
    public UndoManager getUndoManager() {
        return undoManager;
    }
    
    // Helper class for file information
    public static class FileInfo {
        public final int totalLines;
        public final boolean readOnly;
        public final String fileType;
        
        public FileInfo(int totalLines, boolean readOnly, String fileType) {
            this.totalLines = totalLines;
            this.readOnly = readOnly;
            this.fileType = fileType;
        }
    }
}

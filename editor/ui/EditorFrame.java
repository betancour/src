// -----------------------------
// File: src/editor/ui/EditorFrame.java
// -----------------------------
package editor.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import editor.actions.FileActions;
import editor.actions.TerminalAction;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;


public class EditorFrame extends JFrame {
    private final TextAreaPanel textPanel;
    private final StatusBar statusBar;

    public EditorFrame() {
        super("Programmer's Editor - Prototype");
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);

        // Central editor
        textPanel = new TextAreaPanel();
        add(textPanel, BorderLayout.CENTER);

        // Status bar
        statusBar = new StatusBar();
        add(statusBar, BorderLayout.SOUTH);

        // Menu and toolbar
        setJMenuBar(MenuBarFactory.create(this, textPanel, statusBar));
        var tb = ToolBarFactory.create(this, textPanel);
        add(tb, BorderLayout.NORTH);

        // Connect caret position updates to status bar
        textPanel.addPropertyChangeListener("caret", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                String caretPos = (String) evt.getNewValue();
                if (caretPos != null) {
                    statusBar.setCaretPos("Ln " + caretPos.replace(",", ", Col "));
                }
            }
        });
        
        // Connect file and modified status updates
        textPanel.addPropertyChangeListener("file", evt -> {
            String fileName = (String) evt.getNewValue();
            if (fileName != null) {
                setTitle("Programmer's Editor - " + fileName);
            }
        });
        
        textPanel.addPropertyChangeListener("modified", evt -> {
            boolean modified = (Boolean) evt.getNewValue();
            String title = getTitle();
            if (modified && !title.endsWith(" *")) {
                setTitle(title + " *");
            } else if (!modified && title.endsWith(" *")) {
                setTitle(title.substring(0, title.length() - 2));
            }
        });
        
        // Connect file info updates to status bar
        textPanel.addPropertyChangeListener("fileInfo", evt -> {
            TextAreaPanel.FileInfo fileInfo = (TextAreaPanel.FileInfo) evt.getNewValue();
            if (fileInfo != null) {
                statusBar.setFileInfo(fileInfo.totalLines, fileInfo.readOnly, fileInfo.fileType);
            }
        });
        
        // Initialize status bar with initial caret position
        SwingUtilities.invokeLater(() -> {
            statusBar.setCaretPos("Ln 1, Col 1");
            statusBar.setFileInfo(1, false, "OS");
        });

        // Window close -> check unsaved
        addWindowListener(new WindowAdapter() {
            @Override public void windowClosing(WindowEvent e) {
                var ok = FileActions.confirmCloseAll(EditorFrame.this, textPanel);
                if (ok) dispose();
            }
        });

        // Quick terminal action demo (Ctrl+T)
        var terminalAction = new TerminalAction();
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
            .put(KeyStroke.getKeyStroke("control T"), "open-term");
        getRootPane().getActionMap().put("open-term", terminalAction);
    }

    public TextAreaPanel getTextPanel() { return textPanel; }
    public StatusBar getStatusBar() { return statusBar; }
}

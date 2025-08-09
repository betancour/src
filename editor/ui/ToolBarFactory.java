// -----------------------------
// File: src/editor/ui/ToolBarFactory.java
// -----------------------------
package editor.ui;

import javax.swing.*;
import java.awt.*;
import editor.actions.FileActions;
import editor.actions.EditActions;
import editor.actions.SearchActions;
import editor.utils.SystemIconHelper;

public class ToolBarFactory {
    public static JToolBar create(JFrame parent, TextAreaPanel panel) {
        var tb = new JToolBar();
        tb.setFloatable(false);
        tb.setRollover(true);
        tb.setMargin(new Insets(2, 2, 2, 2));

        // File operations
        var newBtn = createToolBarButton(SystemIconHelper.getBestIcon("new"), "New File (Ctrl+N)"); 
        newBtn.addActionListener(e -> FileActions.newFile(parent, panel));
        var openBtn = createToolBarButton(SystemIconHelper.getBestIcon("open"), "Open File (Ctrl+O)"); 
        openBtn.addActionListener(e -> FileActions.openFile(parent, panel));
        var saveBtn = createToolBarButton(SystemIconHelper.getBestIcon("save"), "Save File (Ctrl+S)"); 
        saveBtn.addActionListener(e -> FileActions.saveFile(parent, panel, false));

        // Edit operations
        var undoAction = new EditActions.UndoAction(panel.getUndoManager());
        var redoAction = new EditActions.RedoAction(panel.getUndoManager());
        var cutAction = new EditActions.CutAction(panel.getTextArea());
        var copyAction = new EditActions.CopyAction(panel.getTextArea());
        var pasteAction = new EditActions.PasteAction(panel.getTextArea());
        
        var undoBtn = createToolBarButton(SystemIconHelper.getBestIcon("undo"), "Undo (Ctrl+Z)");
        undoBtn.setAction(undoAction);
        undoBtn.setIcon(SystemIconHelper.getBestIcon("undo"));
        undoBtn.setText("");
        
        var redoBtn = createToolBarButton(SystemIconHelper.getBestIcon("redo"), "Redo (Ctrl+Y)");
        redoBtn.setAction(redoAction);
        redoBtn.setIcon(SystemIconHelper.getBestIcon("redo"));
        redoBtn.setText("");
        
        var cutBtn = createToolBarButton(SystemIconHelper.getBestIcon("cut"), "Cut (Ctrl+X)");
        cutBtn.setAction(cutAction);
        cutBtn.setIcon(SystemIconHelper.getBestIcon("cut"));
        cutBtn.setText("");
        
        var copyBtn = createToolBarButton(SystemIconHelper.getBestIcon("copy"), "Copy (Ctrl+C)");
        copyBtn.setAction(copyAction);
        copyBtn.setIcon(SystemIconHelper.getBestIcon("copy"));
        copyBtn.setText("");
        
        var pasteBtn = createToolBarButton(SystemIconHelper.getBestIcon("paste"), "Paste (Ctrl+V)");
        pasteBtn.setAction(pasteAction);
        pasteBtn.setIcon(SystemIconHelper.getBestIcon("paste"));
        pasteBtn.setText("");
        
        // Search operations
        var findAction = new SearchActions.FindAction(parent, panel);
        var findBtn = createToolBarButton(SystemIconHelper.getBestIcon("find"), "Find (Ctrl+F)");
        findBtn.setAction(findAction);
        findBtn.setIcon(SystemIconHelper.getBestIcon("find"));
        findBtn.setText("");
        
        // Line numbers toggle
        var lineNumBtn = createToolBarButton(SystemIconHelper.getLineNumbersIcon(), "Toggle Line Numbers"); 
        lineNumBtn.addActionListener(e -> {
            panel.toggleLineNumbers();
            // Update button appearance based on state
            if (panel.isLineNumbersVisible()) {
                lineNumBtn.setToolTipText("Hide Line Numbers");
            } else {
                lineNumBtn.setToolTipText("Show Line Numbers");
            }
        });
        
        // Terminal
        var termBtn = createToolBarButton(SystemIconHelper.getBestIcon("terminal"), "Open Terminal (Ctrl+T)"); 
        termBtn.addActionListener(e -> FileActions.openTerminal(parent));

        // Add buttons to toolbar
        tb.add(newBtn); 
        tb.add(openBtn); 
        tb.add(saveBtn); 
        tb.addSeparator();
        
        tb.add(undoBtn);
        tb.add(redoBtn);
        tb.addSeparator();
        
        tb.add(cutBtn);
        tb.add(copyBtn);
        tb.add(pasteBtn);
        tb.addSeparator();
        
        tb.add(findBtn);
        tb.addSeparator();
        
        tb.add(lineNumBtn);
        tb.add(termBtn);
        
        return tb;
    }
    
    /**
     * Creates a toolbar button with consistent Windows 3.1 style proportions
     */
    private static JButton createToolBarButton(Icon icon, String tooltip) {
        JButton button = new JButton(icon);
        button.setToolTipText(tooltip);
        
        // Windows 3.1 style proportions
        button.setPreferredSize(new Dimension(24, 24));
        button.setMinimumSize(new Dimension(24, 24));
        button.setMaximumSize(new Dimension(24, 24));
        
        // Button styling
        button.setMargin(new Insets(2, 2, 2, 2));
        button.setFocusPainted(false);
        button.setBorderPainted(true);
        button.setContentAreaFilled(true);
        
        return button;
    }
}

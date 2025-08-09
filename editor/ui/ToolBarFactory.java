// -----------------------------
// File: src/editor/ui/ToolBarFactory.java
// -----------------------------
package editor.ui;

import javax.swing.*;
import editor.actions.FileActions;
import editor.actions.EditActions;
import editor.actions.SearchActions;
import editor.utils.SystemIconHelper;

public class ToolBarFactory {
    public static JToolBar create(JFrame parent, TextAreaPanel panel) {
        var tb = new JToolBar();
        tb.setFloatable(false);
        tb.setRollover(true);

        // File operations
        var newBtn = new JButton(SystemIconHelper.getBestIcon("new")); 
        newBtn.setToolTipText("New File (Ctrl+N)");
        newBtn.addActionListener(e -> FileActions.newFile(parent, panel));
        var openBtn = new JButton(SystemIconHelper.getBestIcon("open")); 
        openBtn.setToolTipText("Open File (Ctrl+O)");
        openBtn.addActionListener(e -> FileActions.openFile(parent, panel));
        var saveBtn = new JButton(SystemIconHelper.getBestIcon("save")); 
        saveBtn.setToolTipText("Save File (Ctrl+S)");
        saveBtn.addActionListener(e -> FileActions.saveFile(parent, panel, false));

        // Edit operations
        var undoAction = new EditActions.UndoAction(panel.getUndoManager());
        var redoAction = new EditActions.RedoAction(panel.getUndoManager());
        var cutAction = new EditActions.CutAction(panel.getTextArea());
        var copyAction = new EditActions.CopyAction(panel.getTextArea());
        var pasteAction = new EditActions.PasteAction(panel.getTextArea());
        
        var undoBtn = new JButton(SystemIconHelper.getBestIcon("undo"));
        undoBtn.setAction(undoAction);
        undoBtn.setIcon(SystemIconHelper.getBestIcon("undo"));
        undoBtn.setText("");
        undoBtn.setToolTipText("Undo (Ctrl+Z)");
        
        var redoBtn = new JButton(SystemIconHelper.getBestIcon("redo"));
        redoBtn.setAction(redoAction);
        redoBtn.setIcon(SystemIconHelper.getBestIcon("redo"));
        redoBtn.setText("");
        redoBtn.setToolTipText("Redo (Ctrl+Y)");
        
        var cutBtn = new JButton(SystemIconHelper.getBestIcon("cut"));
        cutBtn.setAction(cutAction);
        cutBtn.setIcon(SystemIconHelper.getBestIcon("cut"));
        cutBtn.setText("");
        cutBtn.setToolTipText("Cut (Ctrl+X)");
        
        var copyBtn = new JButton(SystemIconHelper.getBestIcon("copy"));
        copyBtn.setAction(copyAction);
        copyBtn.setIcon(SystemIconHelper.getBestIcon("copy"));
        copyBtn.setText("");
        copyBtn.setToolTipText("Copy (Ctrl+C)");
        
        var pasteBtn = new JButton(SystemIconHelper.getBestIcon("paste"));
        pasteBtn.setAction(pasteAction);
        pasteBtn.setIcon(SystemIconHelper.getBestIcon("paste"));
        pasteBtn.setText("");
        pasteBtn.setToolTipText("Paste (Ctrl+V)");
        
        // Search operations
        var findAction = new SearchActions.FindAction(parent, panel);
        var findBtn = new JButton(SystemIconHelper.getBestIcon("find"));
        findBtn.setAction(findAction);
        findBtn.setIcon(SystemIconHelper.getBestIcon("find"));
        findBtn.setText("");
        findBtn.setToolTipText("Find (Ctrl+F)");
        
        // Terminal
        var termBtn = new JButton(SystemIconHelper.getBestIcon("terminal")); 
        termBtn.setToolTipText("Open Terminal (Ctrl+T)");
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
        
        tb.add(termBtn);
        
        return tb;
    }
}

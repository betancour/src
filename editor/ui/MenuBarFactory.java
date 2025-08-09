// -----------------------------
// File: src/editor/ui/MenuBarFactory.java
// -----------------------------
package editor.ui;

import javax.swing.*;
import editor.actions.FileActions;
import editor.actions.EditActions;
import editor.actions.SearchActions;
import editor.utils.SystemIconHelper;


public class MenuBarFactory {
    public static JMenuBar create(JFrame parent, TextAreaPanel panel, StatusBar status) {
        var mb = new JMenuBar();

        // File Menu
        var file = new JMenu("File");
        var newIt = new JMenuItem("New", SystemIconHelper.getBestIcon("new"));
        var openIt = new JMenuItem("Open...", SystemIconHelper.getBestIcon("open"));
        var saveIt = new JMenuItem("Save", SystemIconHelper.getBestIcon("save"));
        var saveAsIt = new JMenuItem("Save As...", SystemIconHelper.getBestIcon("save"));
        var exitIt = new JMenuItem("Exit");

        newIt.addActionListener(e -> FileActions.newFile(parent, panel));
        openIt.addActionListener(e -> FileActions.openFile(parent, panel));
        saveIt.addActionListener(e -> FileActions.saveFile(parent, panel, false));
        saveAsIt.addActionListener(e -> FileActions.saveFile(parent, panel, true));
        exitIt.addActionListener(e -> {
            if (FileActions.confirmCloseAll(parent, panel)) parent.dispose();
        });

        file.add(newIt); file.add(openIt); file.add(saveIt); file.add(saveAsIt); file.addSeparator(); file.add(exitIt);

        // Edit Menu with enhanced functionality
        var edit = new JMenu("Edit");
        
        // Undo/Redo actions
        var undoAction = new EditActions.UndoAction(panel.getUndoManager());
        var redoAction = new EditActions.RedoAction(panel.getUndoManager());
        var undoItem = new JMenuItem(undoAction);
        undoItem.setIcon(SystemIconHelper.getBestIcon("undo"));
        var redoItem = new JMenuItem(redoAction);
        redoItem.setIcon(SystemIconHelper.getBestIcon("redo"));
        
        // Cut/Copy/Paste actions
        var cutAction = new EditActions.CutAction(panel.getTextArea());
        var copyAction = new EditActions.CopyAction(panel.getTextArea());
        var pasteAction = new EditActions.PasteAction(panel.getTextArea());
        var selectAllAction = new EditActions.SelectAllAction(panel.getTextArea());
        
        var cutItem = new JMenuItem(cutAction);
        cutItem.setIcon(SystemIconHelper.getBestIcon("cut"));
        var copyItem = new JMenuItem(copyAction);
        copyItem.setIcon(SystemIconHelper.getBestIcon("copy"));
        var pasteItem = new JMenuItem(pasteAction);
        pasteItem.setIcon(SystemIconHelper.getBestIcon("paste"));
        var selectAllItem = new JMenuItem(selectAllAction);
        
        edit.add(undoItem);
        edit.add(redoItem);
        edit.addSeparator();
        edit.add(cutItem);
        edit.add(copyItem);
        edit.add(pasteItem);
        edit.addSeparator();
        edit.add(selectAllItem);
        
        // Search Menu
        var search = new JMenu("Search");
        var findAction = new SearchActions.FindAction(parent, panel);
        var replaceAction = new SearchActions.ReplaceAction(parent, panel);
        var findNextAction = new SearchActions.FindNextAction(parent, panel);
        
        var findItem = new JMenuItem(findAction);
        findItem.setIcon(SystemIconHelper.getBestIcon("find"));
        var replaceItem = new JMenuItem(replaceAction);
        replaceItem.setIcon(SystemIconHelper.getBestIcon("replace"));
        var findNextItem = new JMenuItem(findNextAction);
        findNextItem.setIcon(SystemIconHelper.getBestIcon("find"));
        
        search.add(findItem);
        search.add(replaceItem);
        search.addSeparator();
        search.add(findNextItem);
        
        // Tools Menu
        var tools = new JMenu("Tools");
        var term = new JMenuItem("Open Terminal", SystemIconHelper.getBestIcon("terminal"));
        term.addActionListener(e -> FileActions.openTerminal(parent));
        tools.add(term);

        mb.add(file); 
        mb.add(edit); 
        mb.add(search);
        mb.add(tools);
        
        // Update undo/redo states periodically
        javax.swing.Timer timer = new javax.swing.Timer(500, e -> {
            undoAction.updateUndoState();
            redoAction.updateRedoState();
        });
        timer.start();
        
        return mb;
    }
}

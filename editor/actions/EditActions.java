// -----------------------------
// File: src/editor/actions/EditActions.java
// -----------------------------
package editor.actions;

import javax.swing.*;

import javax.swing.undo.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.Toolkit;
import java.awt.datatransfer.*;


public class EditActions {
    
    // Undo/Redo support
    public static class UndoAction extends AbstractAction {
        private final UndoManager undoManager;
        
        public UndoAction(UndoManager undoManager) {
            super("Undo");
            this.undoManager = undoManager;
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_Z, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
            putValue(SHORT_DESCRIPTION, "Undo the last action");
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            if (undoManager.canUndo()) {
                undoManager.undo();
            }
        }
        
        public void updateUndoState() {
            if (undoManager.canUndo()) {
                setEnabled(true);
                putValue(NAME, undoManager.getUndoPresentationName());
            } else {
                setEnabled(false);
                putValue(NAME, "Undo");
            }
        }
    }
    
    public static class RedoAction extends AbstractAction {
        private final UndoManager undoManager;
        
        public RedoAction(UndoManager undoManager) {
            super("Redo");
            this.undoManager = undoManager;
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_Y, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
            putValue(SHORT_DESCRIPTION, "Redo the last undone action");
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            if (undoManager.canRedo()) {
                undoManager.redo();
            }
        }
        
        public void updateRedoState() {
            if (undoManager.canRedo()) {
                setEnabled(true);
                putValue(NAME, undoManager.getRedoPresentationName());
            } else {
                setEnabled(false);
                putValue(NAME, "Redo");
            }
        }
    }
    
    // Enhanced Cut Action
    public static class CutAction extends AbstractAction {
        private final JTextArea textArea;
        
        public CutAction(JTextArea textArea) {
            super("Cut");
            this.textArea = textArea;
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_X, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
            putValue(SHORT_DESCRIPTION, "Cut selected text to clipboard");
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            if (textArea.getSelectedText() != null) {
                textArea.cut();
            }
        }
    }
    
    // Enhanced Copy Action
    public static class CopyAction extends AbstractAction {
        private final JTextArea textArea;
        
        public CopyAction(JTextArea textArea) {
            super("Copy");
            this.textArea = textArea;
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_C, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
            putValue(SHORT_DESCRIPTION, "Copy selected text to clipboard");
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            if (textArea.getSelectedText() != null) {
                textArea.copy();
            }
        }
    }
    
    // Enhanced Paste Action
    public static class PasteAction extends AbstractAction {
        private final JTextArea textArea;
        
        public PasteAction(JTextArea textArea) {
            super("Paste");
            this.textArea = textArea;
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_V, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
            putValue(SHORT_DESCRIPTION, "Paste text from clipboard");
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                if (clipboard.isDataFlavorAvailable(DataFlavor.stringFlavor)) {
                    textArea.paste();
                }
            } catch (Exception ex) {
                // Handle clipboard errors silently
            }
        }
    }
    
    // Select All Action
    public static class SelectAllAction extends AbstractAction {
        private final JTextArea textArea;
        
        public SelectAllAction(JTextArea textArea) {
            super("Select All");
            this.textArea = textArea;
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_A, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
            putValue(SHORT_DESCRIPTION, "Select all text");
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            textArea.selectAll();
        }
    }
}
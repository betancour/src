// -----------------------------
// File: src/editor/actions/SearchActions.java
// -----------------------------
package editor.actions;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.Toolkit;
import editor.ui.TextAreaPanel;
import editor.ui.FindReplaceDialog;

public class SearchActions {
    
    public static class FindAction extends AbstractAction {
        private final JFrame parent;
        private final TextAreaPanel textPanel;
        private FindReplaceDialog dialog;
        
        public FindAction(JFrame parent, TextAreaPanel textPanel) {
            super("Find...");
            this.parent = parent;
            this.textPanel = textPanel;
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_F, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
            putValue(SHORT_DESCRIPTION, "Find text in document");
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            if (dialog == null) {
                dialog = new FindReplaceDialog(parent, textPanel.getTextArea());
            }
            
            // If text is selected, use it as search text
            String selectedText = textPanel.getTextArea().getSelectedText();
            if (selectedText != null && !selectedText.trim().isEmpty()) {
                dialog.showWithText(selectedText);
            } else {
                dialog.showDialog();
            }
        }
    }
    
    public static class ReplaceAction extends AbstractAction {
        private final JFrame parent;
        private final TextAreaPanel textPanel;
        private FindReplaceDialog dialog;
        
        public ReplaceAction(JFrame parent, TextAreaPanel textPanel) {
            super("Replace...");
            this.parent = parent;
            this.textPanel = textPanel;
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_H, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
            putValue(SHORT_DESCRIPTION, "Find and replace text in document");
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            if (dialog == null) {
                dialog = new FindReplaceDialog(parent, textPanel.getTextArea());
            }
            
            // If text is selected, use it as search text
            String selectedText = textPanel.getTextArea().getSelectedText();
            if (selectedText != null && !selectedText.trim().isEmpty()) {
                dialog.showWithText(selectedText);
            } else {
                dialog.showDialog();
            }
        }
    }
    
    public static class FindNextAction extends AbstractAction {
        private final JFrame parent;
        private final TextAreaPanel textPanel;
        private FindReplaceDialog dialog;
        
        public FindNextAction(JFrame parent, TextAreaPanel textPanel) {
            super("Find Next");
            this.parent = parent;
            this.textPanel = textPanel;
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_F3, 0));
            putValue(SHORT_DESCRIPTION, "Find next occurrence");
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            if (dialog == null) {
                dialog = new FindReplaceDialog(parent, textPanel.getTextArea());
            }
            dialog.showDialog();
        }
    }
}
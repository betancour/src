// -----------------------------
// File: src/editor/actions/TerminalAction.java
// -----------------------------
package editor.actions;

import javax.swing.*;
import java.awt.event.ActionEvent;
import editor.utils.OSUtils;


public class TerminalAction extends AbstractAction {
    public TerminalAction() { super("Terminal"); }
    @Override public void actionPerformed(ActionEvent e) {
        try { OSUtils.openTerminal(); } catch (Exception ex) { JOptionPane.showMessageDialog(null, "No terminal: " + ex.getMessage()); }
    }
}

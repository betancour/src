// -----------------------------
// File: src/editor/ui/StatusBar.java
// -----------------------------
package editor.ui;

import javax.swing.*;
import java.awt.*;

public class StatusBar extends JPanel {
    private final JLabel left = new JLabel("Ln 1, Col 1");
    private final JLabel right = new JLabel("Lines: 1 | RO | OS");

    public StatusBar() {
        super(new BorderLayout());
        setBorder(BorderFactory.createEtchedBorder());
        
        // Add some padding to left label
        left.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));
        add(left, BorderLayout.WEST);
        
        // Add some padding to right label
        right.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));
        add(right, BorderLayout.EAST);
    }

    public void setCaretPos(String s) { left.setText(s); }
    
    public void setFileInfo(int totalLines, boolean readOnly, String fileType) {
        String mode = readOnly ? "RO" : "RW";
        right.setText(String.format("Lines: %d  |  %s  |  %s", totalLines, mode, fileType));
    }
}

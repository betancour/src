// -----------------------------
// File: src/editor/ui/LineNumberView.java
// -----------------------------
package editor.ui;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;


// Lightweight line numbers for JTextArea
public class LineNumberView extends JComponent implements DocumentListener {
    private final JTextArea textArea;
    private final FontMetrics fm;
    private int currentDigits = 2;

    public LineNumberView(JTextArea textArea) {
        this.textArea = textArea;
        this.fm = textArea.getFontMetrics(textArea.getFont());
        textArea.getDocument().addDocumentListener(this);
        setPreferredWidth();
    }

    private void setPreferredWidth() {
        int lines = Math.max(1, textArea.getLineCount());
        int digits = String.valueOf(lines).length();
        if (digits != currentDigits) {
            currentDigits = digits;
            int width = fm.charWidth('0') * digits + 10;
            setPreferredSize(new Dimension(width, Integer.MAX_VALUE));
            revalidate();
        }
    }

    @Override protected void paintComponent(Graphics g) {
        Rectangle clip = g.getClipBounds();
        g.setColor(new Color(240,240,240));
        g.fillRect(clip.x, clip.y, clip.width, clip.height);
        g.setColor(Color.GRAY);
        try {
            int start = textArea.viewToModel2D(new Point(0, clip.y));
            int end = textArea.viewToModel2D(new Point(0, clip.y + clip.height));
            while (start <= end) {
                int line = textArea.getLineOfOffset(start);
                int y = (int) textArea.modelToView2D(start).getY() + fm.getAscent();
                String num = String.valueOf(line + 1);
                g.drawString(num, getWidth() - fm.stringWidth(num) - 5, y);
                start = textArea.getLineEndOffset(line) + 1;
            }
        } catch (Exception e) { /* ignore */ }
    }

    @Override public void insertUpdate(DocumentEvent e) { setPreferredWidth(); repaint(); }
    @Override public void removeUpdate(DocumentEvent e) { setPreferredWidth(); repaint(); }
    @Override public void changedUpdate(DocumentEvent e) { }
}

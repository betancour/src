// -----------------------------
// File: src/editor/utils/SystemIconHelper.java
// -----------------------------
package editor.utils;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.io.File;

public class SystemIconHelper {
    
    /**
     * Gets system icons for common operations, with fallbacks to created icons
     */
    public static Icon getNewFileIcon() {
        Icon icon = UIManager.getIcon("FileView.fileIcon");
        if (icon == null) {
            icon = createTextIcon("N", Color.GREEN);
        }
        return icon;
    }
    
    public static Icon getOpenFileIcon() {
        Icon icon = UIManager.getIcon("FileChooser.upFolderIcon");
        if (icon == null) {
            icon = UIManager.getIcon("Tree.openIcon");
        }
        if (icon == null) {
            icon = createTextIcon("O", Color.BLUE);
        }
        return icon;
    }
    
    public static Icon getSaveFileIcon() {
        Icon icon = UIManager.getIcon("FileView.floppyDriveIcon");
        if (icon == null) {
            // Try to get a document icon
            try {
                File tempFile = File.createTempFile("temp", ".txt");
                icon = FileSystemView.getFileSystemView().getSystemIcon(tempFile);
                tempFile.delete();
            } catch (Exception e) {
                // Fallback to created icon
            }
        }
        if (icon == null) {
            icon = createTextIcon("S", new Color(0, 150, 0));
        }
        return icon;
    }
    
    public static Icon getCutIcon() {
        // Try system icon first
        Icon icon = UIManager.getIcon("Tree.leafIcon");
        if (icon != null) {
            return icon;
        }
        // Fallback to geometric scissors icon
        return createGeometricIcon("cut", new Color(200, 50, 50));
    }
    
    public static Icon getCopyIcon() {
        // Try system icon first
        Icon icon = UIManager.getIcon("FileView.fileIcon");
        if (icon != null) {
            return icon;
        }
        // Fallback to geometric copy icon
        return createGeometricIcon("copy", new Color(50, 100, 200));
    }
    
    public static Icon getPasteIcon() {
        // Fallback to geometric clipboard icon
        return createGeometricIcon("paste", new Color(150, 100, 50));
    }
    
    public static Icon getUndoIcon() {
        // Try system icon first
        Icon icon = UIManager.getIcon("OptionPane.questionIcon");
        if (icon != null && icon.getIconWidth() <= 16) {
            return icon;
        }
        // Fallback to geometric undo arrow
        return createGeometricIcon("undo", new Color(150, 50, 150));
    }
    
    public static Icon getRedoIcon() {
        // Fallback to geometric redo arrow
        return createGeometricIcon("redo", new Color(150, 50, 150));
    }
    
    public static Icon getFindIcon() {
        // Fallback to geometric magnifying glass
        return createGeometricIcon("find", new Color(80, 80, 180));
    }
    
    public static Icon getReplaceIcon() {
        // Fallback to geometric replace icon
        return createGeometricIcon("replace", new Color(150, 100, 50));
    }
    
    public static Icon getTerminalIcon() {
        Icon icon = UIManager.getIcon("FileView.computerIcon");
        if (icon == null) {
            // Terminal icon - command prompt symbol
            icon = createSymbolIcon("⌘", Color.BLACK);
        }
        return icon;
    }
    
    /**
     * Creates a simple text-based icon
     */
    private static Icon createTextIcon(String text, Color color) {
        return new Icon() {
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Background circle
                g2.setColor(color.brighter());
                g2.fillOval(x, y, 16, 16);
                
                // Border
                g2.setColor(color.darker());
                g2.drawOval(x, y, 16, 16);
                
                // Text
                g2.setColor(Color.WHITE);
                g2.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
                FontMetrics fm = g2.getFontMetrics();
                int textX = x + (16 - fm.stringWidth(text)) / 2;
                int textY = y + (16 - fm.getHeight()) / 2 + fm.getAscent();
                g2.drawString(text, textX, textY);
                
                g2.dispose();
            }
            
            @Override
            public int getIconWidth() { return 16; }
            
            @Override
            public int getIconHeight() { return 16; }
        };
    }
    
    /**
     * Creates an icon using Unicode symbols
     */
    private static Icon createSymbolIcon(String symbol, Color color) {
        return new Icon() {
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                
                g2.setColor(color);
                g2.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
                FontMetrics fm = g2.getFontMetrics();
                
                int textX = x + (16 - fm.stringWidth(symbol)) / 2;
                int textY = y + (16 - fm.getHeight()) / 2 + fm.getAscent();
                g2.drawString(symbol, textX, textY);
                
                g2.dispose();
            }
            
            @Override
            public int getIconWidth() { return 16; }
            
            @Override
            public int getIconHeight() { return 16; }
        };
    }
    
    /**
     * Creates a simple geometric icon for operations that don't have good Unicode symbols
     */
    public static Icon createGeometricIcon(String operation, Color color) {
        return new Icon() {
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(color);
                
                switch (operation.toLowerCase()) {
                    case "cut":
                        // Draw scissors-like shape
                        g2.setStroke(new BasicStroke(1.5f));
                        // Scissor blades
                        g2.drawLine(x + 3, y + 3, x + 8, y + 8);
                        g2.drawLine(x + 3, y + 13, x + 8, y + 8);
                        g2.drawLine(x + 8, y + 8, x + 13, y + 3);
                        g2.drawLine(x + 8, y + 8, x + 13, y + 13);
                        // Handle circles
                        g2.fillOval(x + 2, y + 2, 3, 3);
                        g2.fillOval(x + 11, y + 11, 3, 3);
                        break;
                    case "copy":
                        // Draw two overlapping rectangles
                        g2.drawRect(x + 2, y + 2, 8, 8);
                        g2.drawRect(x + 6, y + 6, 8, 8);
                        break;
                    case "paste":
                        // Draw clipboard shape
                        g2.drawRect(x + 3, y + 2, 10, 12);
                        g2.drawRect(x + 6, y + 1, 4, 2);
                        g2.drawLine(x + 5, y + 6, x + 11, y + 6);
                        g2.drawLine(x + 5, y + 8, x + 11, y + 8);
                        g2.drawLine(x + 5, y + 10, x + 9, y + 10);
                        break;
                    case "find":
                        // Draw magnifying glass
                        g2.setStroke(new BasicStroke(1.5f));
                        g2.drawOval(x + 2, y + 2, 8, 8);
                        g2.drawLine(x + 8, y + 8, x + 13, y + 13);
                        break;
                    case "replace":
                        // Draw find and replace arrows
                        g2.setStroke(new BasicStroke(1.5f));
                        // Left arrow
                        g2.drawLine(x + 2, y + 5, x + 7, y + 5);
                        g2.drawLine(x + 2, y + 5, x + 4, y + 3);
                        g2.drawLine(x + 2, y + 5, x + 4, y + 7);
                        // Right arrow
                        g2.drawLine(x + 9, y + 11, x + 14, y + 11);
                        g2.drawLine(x + 14, y + 11, x + 12, y + 9);
                        g2.drawLine(x + 14, y + 11, x + 12, y + 13);
                        break;
                    case "undo":
                        // Draw curved arrow pointing left
                        g2.setStroke(new BasicStroke(1.5f));
                        g2.drawArc(x + 3, y + 3, 10, 10, 45, 180);
                        g2.drawLine(x + 6, y + 3, x + 3, y + 6);
                        g2.drawLine(x + 6, y + 3, x + 9, y + 6);
                        break;
                    case "redo":
                        // Draw curved arrow pointing right
                        g2.setStroke(new BasicStroke(1.5f));
                        g2.drawArc(x + 3, y + 3, 10, 10, 135, 180);
                        g2.drawLine(x + 10, y + 3, x + 13, y + 6);
                        g2.drawLine(x + 10, y + 3, x + 7, y + 6);
                        break;
                    default:
                        // Default to a simple rectangle
                        g2.drawRect(x + 2, y + 2, 12, 12);
                        break;
                }
                
                g2.dispose();
            }
            
            @Override
            public int getIconWidth() { return 16; }
            
            @Override
            public int getIconHeight() { return 16; }
        };
    }
    
    /**
     * Try to get the best available system icon, with fallback hierarchy
     */
    public static Icon getBestIcon(String operation) {
        switch (operation.toLowerCase()) {
            case "new": return getNewFileIcon();
            case "open": return getOpenFileIcon();
            case "save": return getSaveFileIcon();
            case "cut": return getCutIcon();
            case "copy": return getCopyIcon();
            case "paste": return getPasteIcon();
            case "undo": return getUndoIcon();
            case "redo": return getRedoIcon();
            case "find": return getFindIcon();
            case "replace": return getReplaceIcon();
            case "terminal": return getTerminalIcon();
            default: return createTextIcon("?", Color.GRAY);
        }
    }
    
    /**
     * Get system look and feel specific icons where available
     */
    public static void printAvailableSystemIcons() {
        System.out.println("Available System Icons:");
        String[] iconKeys = {
            "FileView.directoryIcon",
            "FileView.fileIcon", 
            "FileView.computerIcon",
            "FileView.hardDriveIcon",
            "FileView.floppyDriveIcon",
            "FileChooser.newFolderIcon",
            "FileChooser.upFolderIcon",
            "FileChooser.homeFolderIcon",
            "FileChooser.detailsViewIcon",
            "FileChooser.listViewIcon",
            "Tree.openIcon",
            "Tree.closedIcon",
            "Tree.leafIcon",
            "OptionPane.informationIcon",
            "OptionPane.warningIcon",
            "OptionPane.errorIcon",
            "OptionPane.questionIcon"
        };
        
        for (String key : iconKeys) {
            Icon icon = UIManager.getIcon(key);
            if (icon != null) {
                System.out.println("  " + key + ": Available (" + icon.getIconWidth() + "x" + icon.getIconHeight() + ")");
            }
        }
    }
}
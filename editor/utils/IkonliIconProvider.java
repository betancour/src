// -----------------------------
// File: src/editor/utils/IkonliIconProvider.java
// -----------------------------
package editor.utils;

import javax.swing.Icon;
import java.awt.Color;

/**
 * Provider for Ikonli icons with dynamic loading support.
 * This class uses reflection to load Ikonli icons only if the library is available.
 */
public class IkonliIconProvider {
    
    private static boolean ikonliAvailable = false;
    private static Class<?> fontIconClass = null;
    private static Class<?> fontAwesomeSolidClass = null;
    private static Class<?> material2Class = null;
    
    static {
        try {
            // Try to load Ikonli classes
            fontIconClass = Class.forName("org.kordamp.ikonli.swing.FontIcon");
            fontAwesomeSolidClass = Class.forName("org.kordamp.ikonli.fontawesome5.FontAwesomeSolid");
            material2Class = Class.forName("org.kordamp.ikonli.material2.Material2MZ");
            ikonliAvailable = true;
            System.out.println("✓ Ikonli-Swing loaded successfully - Using professional icons");
        } catch (ClassNotFoundException e) {
            ikonliAvailable = false;
            System.out.println("! Ikonli-Swing not found - Using fallback icons");
        }
    }
    
    /**
     * Check if Ikonli is available
     */
    public static boolean isAvailable() {
        return ikonliAvailable;
    }
    
    /**
     * Create a FontAwesome icon
     */
    public static Icon createFontAwesomeIcon(String iconName, int size, Color color) {
        if (!ikonliAvailable) {
            return null;
        }
        
        try {
            // Get the icon field from FontAwesome class
            Object iconCode = fontAwesomeSolidClass.getField(iconName).get(null);
            
            // Create FontIcon using reflection
            Object fontIcon = fontIconClass.getMethod("of", Class.forName("org.kordamp.ikonli.Ikon"), int.class, Color.class)
                    .invoke(null, iconCode, size, color);
            
            return (Icon) fontIcon;
        } catch (Exception e) {
            System.err.println("Error creating FontAwesome icon '" + iconName + "': " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Create a Material Design icon
     */
    public static Icon createMaterialIcon(String iconName, int size, Color color) {
        if (!ikonliAvailable) {
            return null;
        }
        
        try {
            // Get the icon field from Material2 class
            Object iconCode = material2Class.getField(iconName).get(null);
            
            // Create FontIcon using reflection
            Object fontIcon = fontIconClass.getMethod("of", Class.forName("org.kordamp.ikonli.Ikon"), int.class, Color.class)
                    .invoke(null, iconCode, size, color);
            
            return (Icon) fontIcon;
        } catch (Exception e) {
            System.err.println("Error creating Material icon '" + iconName + "': " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Get specific icons for common operations
     */
    public static Icon getNewFileIcon(int size, Color color) {
        return createFontAwesomeIcon("FILE", size, color);
    }
    
    public static Icon getOpenFileIcon(int size, Color color) {
        return createFontAwesomeIcon("FOLDER_OPEN", size, color);
    }
    
    public static Icon getSaveFileIcon(int size, Color color) {
        return createFontAwesomeIcon("SAVE", size, color);
    }
    
    public static Icon getCutIcon(int size, Color color) {
        return createFontAwesomeIcon("CUT", size, color);
    }
    
    public static Icon getCopyIcon(int size, Color color) {
        return createFontAwesomeIcon("COPY", size, color);
    }
    
    public static Icon getPasteIcon(int size, Color color) {
        return createFontAwesomeIcon("PASTE", size, color);
    }
    
    public static Icon getUndoIcon(int size, Color color) {
        return createFontAwesomeIcon("UNDO_ALT", size, color);
    }
    
    public static Icon getRedoIcon(int size, Color color) {
        return createFontAwesomeIcon("REDO_ALT", size, color);
    }
    
    public static Icon getFindIcon(int size, Color color) {
        return createFontAwesomeIcon("SEARCH", size, color);
    }
    
    public static Icon getReplaceIcon(int size, Color color) {
        return createMaterialIcon("SEARCH_REPLACE", size, color);
    }
    
    public static Icon getTerminalIcon(int size, Color color) {
        return createFontAwesomeIcon("TERMINAL", size, color);
    }
}
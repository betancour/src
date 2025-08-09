// -----------------------------
// File: src/test/editor/utils/SystemIconHelperTest.java
// -----------------------------
package test.editor.utils;

import editor.utils.SystemIconHelper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;
import javax.swing.Icon;

/**
 * Unit tests for SystemIconHelper class
 */
public class SystemIconHelperTest {

    @BeforeAll
    static void setUp() {
        // Initialize headless mode for testing
        System.setProperty("java.awt.headless", "true");
    }

    @Test
    @DisplayName("Should return non-null icons for all operations")
    void testAllIconsAreAvailable() {
        // Test all icon methods return non-null values
        assertNotNull(SystemIconHelper.getNewFileIcon(), "New file icon should not be null");
        assertNotNull(SystemIconHelper.getOpenFileIcon(), "Open file icon should not be null");
        assertNotNull(SystemIconHelper.getSaveFileIcon(), "Save file icon should not be null");
        assertNotNull(SystemIconHelper.getCutIcon(), "Cut icon should not be null");
        assertNotNull(SystemIconHelper.getCopyIcon(), "Copy icon should not be null");
        assertNotNull(SystemIconHelper.getPasteIcon(), "Paste icon should not be null");
        assertNotNull(SystemIconHelper.getUndoIcon(), "Undo icon should not be null");
        assertNotNull(SystemIconHelper.getRedoIcon(), "Redo icon should not be null");
        assertNotNull(SystemIconHelper.getFindIcon(), "Find icon should not be null");
        assertNotNull(SystemIconHelper.getReplaceIcon(), "Replace icon should not be null");
        assertNotNull(SystemIconHelper.getTerminalIcon(), "Terminal icon should not be null");
        assertNotNull(SystemIconHelper.getLineNumbersIcon(), "Line numbers icon should not be null");
    }

    @Test
    @DisplayName("Should return icons with proper dimensions")
    void testIconDimensions() {
        Icon cutIcon = SystemIconHelper.getCutIcon();
        assertTrue(cutIcon.getIconWidth() > 0, "Icon width should be positive");
        assertTrue(cutIcon.getIconHeight() > 0, "Icon height should be positive");
        assertTrue(cutIcon.getIconWidth() <= 32, "Icon width should be reasonable (<=32px)");
        assertTrue(cutIcon.getIconHeight() <= 32, "Icon height should be reasonable (<=32px)");
    }

    @Test
    @DisplayName("Should handle getBestIcon method correctly")
    void testGetBestIcon() {
        assertNotNull(SystemIconHelper.getBestIcon("new"), "getBestIcon('new') should not be null");
        assertNotNull(SystemIconHelper.getBestIcon("cut"), "getBestIcon('cut') should not be null");
        assertNotNull(SystemIconHelper.getBestIcon("paste"), "getBestIcon('paste') should not be null");
        assertNotNull(SystemIconHelper.getBestIcon("unknown"), "getBestIcon with unknown operation should return fallback");
    }

    @Test
    @DisplayName("Should handle case insensitive operations")
    void testCaseInsensitiveOperations() {
        Icon lowerCase = SystemIconHelper.getBestIcon("cut");
        Icon upperCase = SystemIconHelper.getBestIcon("CUT");
        Icon mixedCase = SystemIconHelper.getBestIcon("CuT");
        
        assertNotNull(lowerCase, "Lowercase operation should work");
        assertNotNull(upperCase, "Uppercase operation should work");
        assertNotNull(mixedCase, "Mixed case operation should work");
    }

    @Test
    @DisplayName("Should create geometric icons as fallbacks")
    void testGeometricIconFallbacks() {
        // Test that geometric icons can be created for all operations
        Icon cutIcon = SystemIconHelper.createGeometricIcon("cut", java.awt.Color.RED);
        Icon copyIcon = SystemIconHelper.createGeometricIcon("copy", java.awt.Color.BLUE);
        Icon pasteIcon = SystemIconHelper.createGeometricIcon("paste", java.awt.Color.GREEN);
        Icon lineNumIcon = SystemIconHelper.createGeometricIcon("linenumbers", java.awt.Color.GRAY);
        
        assertNotNull(cutIcon, "Geometric cut icon should not be null");
        assertNotNull(copyIcon, "Geometric copy icon should not be null");
        assertNotNull(pasteIcon, "Geometric paste icon should not be null");
        assertNotNull(lineNumIcon, "Geometric line numbers icon should not be null");
        
        // Test dimensions
        assertEquals(20, cutIcon.getIconWidth(), "Geometric icon width should be 20");
        assertEquals(20, cutIcon.getIconHeight(), "Geometric icon height should be 20");
    }

    @Test
    @DisplayName("Should handle null and empty operation names gracefully")
    void testNullAndEmptyOperations() {
        assertNotNull(SystemIconHelper.getBestIcon(""), "Empty string should return fallback");
        assertNotNull(SystemIconHelper.getBestIcon(null), "Null should return fallback");
    }

    @Test
    @DisplayName("Should provide consistent icon sizes across operations")
    void testIconSizeConsistency() {
        Icon[] icons = {
            SystemIconHelper.getCutIcon(),
            SystemIconHelper.getCopyIcon(),
            SystemIconHelper.getPasteIcon(),
            SystemIconHelper.getUndoIcon(),
            SystemIconHelper.getRedoIcon()
        };
        
        // All toolbar icons should have reasonable and consistent sizing
        for (Icon icon : icons) {
            assertTrue(icon.getIconWidth() >= 10 && icon.getIconWidth() <= 32, 
                "Icon width should be between 10-32 pixels");
            assertTrue(icon.getIconHeight() >= 10 && icon.getIconHeight() <= 32, 
                "Icon height should be between 10-32 pixels");
        }
    }
}
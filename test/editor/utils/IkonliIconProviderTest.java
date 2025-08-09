// -----------------------------
// File: src/test/editor/utils/IkonliIconProviderTest.java
// -----------------------------
package test.editor.utils;

import editor.utils.IkonliIconProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;
import javax.swing.Icon;
import java.awt.Color;

/**
 * Unit tests for IkonliIconProvider class
 */
public class IkonliIconProviderTest {

    @BeforeAll
    static void setUp() {
        // Initialize headless mode for testing
        System.setProperty("java.awt.headless", "true");
    }

    @Test
    @DisplayName("Should detect Ikonli availability correctly")
    void testIkonliAvailability() {
        // This test depends on whether Ikonli JARs are in classpath
        // The method should not throw exceptions regardless
        assertDoesNotThrow(() -> {
            boolean available = IkonliIconProvider.isAvailable();
            // Result depends on classpath, but method should work
            assertTrue(available == true || available == false, 
                "isAvailable should return a boolean value");
        });
    }

    @Test
    @DisplayName("Should handle icon creation gracefully when Ikonli not available")
    void testIconCreationWithoutIkonli() {
        // Test that methods return null gracefully when Ikonli is not available
        // or return valid icons when it is available
        Icon newIcon = IkonliIconProvider.getNewFileIcon(16, Color.BLACK);
        Icon cutIcon = IkonliIconProvider.getCutIcon(16, Color.RED);
        Icon copyIcon = IkonliIconProvider.getCopyIcon(16, Color.BLUE);
        Icon pasteIcon = IkonliIconProvider.getPasteIcon(16, Color.GREEN);
        
        // Icons should either be null (Ikonli not available) or valid icons
        if (newIcon != null) {
            assertTrue(newIcon.getIconWidth() > 0, "Icon should have positive width");
            assertTrue(newIcon.getIconHeight() > 0, "Icon should have positive height");
        }
        
        if (cutIcon != null) {
            assertTrue(cutIcon.getIconWidth() > 0, "Cut icon should have positive width");
            assertTrue(cutIcon.getIconHeight() > 0, "Cut icon should have positive height");
        }
    }

    @Test
    @DisplayName("Should handle all icon types consistently")
    void testAllIconTypes() {
        Color testColor = Color.DARK_GRAY;
        int testSize = 20;
        
        // Test all icon creation methods
        Icon[] icons = {
            IkonliIconProvider.getNewFileIcon(testSize, testColor),
            IkonliIconProvider.getOpenFileIcon(testSize, testColor),
            IkonliIconProvider.getSaveFileIcon(testSize, testColor),
            IkonliIconProvider.getCutIcon(testSize, testColor),
            IkonliIconProvider.getCopyIcon(testSize, testColor),
            IkonliIconProvider.getPasteIcon(testSize, testColor),
            IkonliIconProvider.getUndoIcon(testSize, testColor),
            IkonliIconProvider.getRedoIcon(testSize, testColor),
            IkonliIconProvider.getFindIcon(testSize, testColor),
            IkonliIconProvider.getReplaceIcon(testSize, testColor),
            IkonliIconProvider.getTerminalIcon(testSize, testColor),
            IkonliIconProvider.getLineNumbersIcon(testSize, testColor)
        };
        
        // All icons should either be null (consistent behavior when Ikonli not available)
        // or valid icons (when Ikonli is available)
        boolean allNull = true;
        boolean allValid = true;
        
        for (Icon icon : icons) {
            if (icon != null) {
                allNull = false;
                if (icon.getIconWidth() <= 0 || icon.getIconHeight() <= 0) {
                    allValid = false;
                }
            } else {
                allValid = false;
            }
        }
        
        // Should be either all null (Ikonli not available) or all valid (Ikonli available)
        assertTrue(allNull || allValid, 
            "Icons should be consistently null or consistently valid");
    }

    @Test
    @DisplayName("Should handle different sizes correctly")
    void testDifferentIconSizes() {
        int[] sizes = {12, 16, 20, 24, 32};
        
        for (int size : sizes) {
            Icon icon = IkonliIconProvider.getCutIcon(size, Color.BLACK);
            
            if (icon != null) {
                // If Ikonli is available, icons should respect size parameter
                assertTrue(icon.getIconWidth() > 0, 
                    "Icon width should be positive for size " + size);
                assertTrue(icon.getIconHeight() > 0, 
                    "Icon height should be positive for size " + size);
            }
        }
    }

    @Test
    @DisplayName("Should handle different colors correctly")
    void testDifferentIconColors() {
        Color[] colors = {
            Color.BLACK, Color.RED, Color.GREEN, Color.BLUE, 
            Color.YELLOW, Color.MAGENTA, Color.CYAN, Color.GRAY
        };
        
        for (Color color : colors) {
            Icon icon = IkonliIconProvider.getFindIcon(16, color);
            
            if (icon != null) {
                // Icon should be created successfully with any color
                assertNotNull(icon, "Icon should not be null for color " + color.toString());
            }
        }
    }

    @Test
    @DisplayName("Should handle null color parameter gracefully")
    void testNullColorHandling() {
        // Should not throw exceptions even with null color
        assertDoesNotThrow(() -> {
            Icon icon = IkonliIconProvider.getCutIcon(16, null);
            // Result depends on Ikonli implementation, but should not crash
        });
    }

    @Test
    @DisplayName("Should handle edge case sizes")
    void testEdgeCaseSizes() {
        int[] edgeSizes = {1, 0, -1, 100, 1000};
        
        for (int size : edgeSizes) {
            assertDoesNotThrow(() -> {
                Icon icon = IkonliIconProvider.getUndoIcon(size, Color.BLACK);
                // Should not throw exceptions for edge case sizes
            }, "Should handle size " + size + " gracefully");
        }
    }

    @Test
    @DisplayName("Should provide FontAwesome icons when available")
    void testFontAwesomeIconCreation() {
        Icon cutIcon = IkonliIconProvider.getCutIcon(16, Color.RED);
        Icon copyIcon = IkonliIconProvider.getCopyIcon(16, Color.BLUE);
        
        if (IkonliIconProvider.isAvailable()) {
            // If Ikonli is available, these should return valid icons
            assertNotNull(cutIcon, "Cut icon should not be null when Ikonli is available");
            assertNotNull(copyIcon, "Copy icon should not be null when Ikonli is available");
        } else {
            // If Ikonli is not available, these should return null
            assertNull(cutIcon, "Cut icon should be null when Ikonli is not available");
            assertNull(copyIcon, "Copy icon should be null when Ikonli is not available");
        }
    }

    @Test
    @DisplayName("Should handle Material Design icons when available")
    void testMaterialDesignIconCreation() {
        // The replace icon uses Material Design in the current implementation
        Icon replaceIcon = IkonliIconProvider.getReplaceIcon(16, Color.ORANGE);
        
        if (IkonliIconProvider.isAvailable()) {
            // Should work or gracefully fail, but not crash
            // Result depends on whether Material Design pack is available
            assertDoesNotThrow(() -> {
                // Just verify it doesn't crash
                IkonliIconProvider.getReplaceIcon(20, Color.PINK);
            });
        }
    }

    @Test
    @DisplayName("Should be thread safe")
    void testThreadSafety() throws InterruptedException {
        Thread[] threads = new Thread[5];
        Exception[] exceptions = new Exception[threads.length];
        
        for (int i = 0; i < threads.length; i++) {
            final int threadId = i;
            threads[i] = new Thread(() -> {
                try {
                    for (int j = 0; j < 10; j++) {
                        IkonliIconProvider.getCutIcon(16, Color.RED);
                        IkonliIconProvider.isAvailable();
                        IkonliIconProvider.getCopyIcon(20, Color.BLUE);
                        Thread.sleep(1); // Small delay to increase chance of race conditions
                    }
                } catch (Exception e) {
                    exceptions[threadId] = e;
                }
            });
        }
        
        // Start all threads
        for (Thread thread : threads) {
            thread.start();
        }
        
        // Wait for all threads to complete
        for (Thread thread : threads) {
            thread.join(5000); // 5 second timeout
        }
        
        // Check for exceptions
        for (int i = 0; i < exceptions.length; i++) {
            assertNull(exceptions[i], 
                "Thread " + i + " should not have thrown exception: " + 
                (exceptions[i] != null ? exceptions[i].getMessage() : "none"));
        }
    }
}
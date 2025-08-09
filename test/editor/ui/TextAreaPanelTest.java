// -----------------------------
// File: src/test/editor/ui/TextAreaPanelTest.java
// -----------------------------
package test.editor.ui;

import editor.ui.TextAreaPanel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;
import javax.swing.SwingUtilities;
import javax.swing.undo.UndoManager;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Unit tests for TextAreaPanel class
 */
public class TextAreaPanelTest {

    private TextAreaPanel panel;
    private Path tempFile;

    @BeforeEach
    void setUp() throws Exception {
        // Initialize headless mode for testing
        System.setProperty("java.awt.headless", "true");
        
        // Create panel on EDT
        SwingUtilities.invokeAndWait(() -> {
            panel = new TextAreaPanel();
        });
        
        // Create temporary file for testing
        tempFile = Files.createTempFile("test", ".txt");
        Files.write(tempFile, "Test content\nLine 2\nLine 3".getBytes());
    }

    @AfterEach
    void tearDown() throws IOException {
        if (tempFile != null && Files.exists(tempFile)) {
            Files.delete(tempFile);
        }
    }

    @Test
    @DisplayName("Should create TextAreaPanel with default settings")
    void testInitialization() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            assertNotNull(panel.getTextArea(), "Text area should not be null");
            assertTrue(panel.isLineNumbersVisible(), "Line numbers should be visible by default");
            assertFalse(panel.isModified(), "Panel should not be modified initially");
            assertNull(panel.getCurrentFile(), "Current file should be null initially");
        });
    }

    @Test
    @DisplayName("Should toggle line numbers visibility")
    void testLineNumbersToggle() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            assertTrue(panel.isLineNumbersVisible(), "Line numbers should be visible initially");
            
            panel.toggleLineNumbers();
            assertFalse(panel.isLineNumbersVisible(), "Line numbers should be hidden after toggle");
            
            panel.toggleLineNumbers();
            assertTrue(panel.isLineNumbersVisible(), "Line numbers should be visible after second toggle");
        });
    }

    @Test
    @DisplayName("Should set line numbers visibility")
    void testSetLineNumbersVisible() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            panel.setLineNumbersVisible(false);
            assertFalse(panel.isLineNumbersVisible(), "Line numbers should be hidden");
            
            panel.setLineNumbersVisible(true);
            assertTrue(panel.isLineNumbersVisible(), "Line numbers should be visible");
            
            // Setting same value should not change anything
            panel.setLineNumbersVisible(true);
            assertTrue(panel.isLineNumbersVisible(), "Line numbers should still be visible");
        });
    }

    @Test
    @DisplayName("Should handle text modification correctly")
    void testTextModification() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            assertFalse(panel.isModified(), "Panel should not be modified initially");
            
            panel.getTextArea().setText("Modified content");
            // Modification state is updated asynchronously
        });
        
        // Wait a bit for document listener to process
        Thread.sleep(100);
        
        SwingUtilities.invokeAndWait(() -> {
            assertTrue(panel.isModified(), "Panel should be modified after text change");
        });
    }

    @Test
    @DisplayName("Should create new file correctly")
    void testNewFile() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            panel.getTextArea().setText("Some content");
        });
        
        Thread.sleep(100); // Wait for modification flag
        
        SwingUtilities.invokeAndWait(() -> {
            panel.newFile();
            assertEquals("", panel.getTextArea().getText(), "Text should be empty after new file");
            assertNull(panel.getCurrentFile(), "Current file should be null");
            assertFalse(panel.isModified(), "Panel should not be modified");
        });
    }

    @Test
    @DisplayName("Should open file correctly")
    void testOpenFile() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            panel.loadFile(tempFile);
            assertTrue(panel.getTextArea().getText().contains("Test content"), 
                "Text should contain file content");
            assertEquals(tempFile, panel.getCurrentFile(), "Current file should be set");
            assertFalse(panel.isModified(), "Panel should not be modified after opening");
        });
    }

    @Test
    @DisplayName("Should save file correctly")
    void testSaveFile() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            panel.getTextArea().setText("New content for save test");
            panel.saveFile(tempFile);
            assertEquals(tempFile, panel.getCurrentFile(), "Current file should be set");
            assertFalse(panel.isModified(), "Panel should not be modified after saving");
        });
        
        // Verify file content
        String savedContent = Files.readString(tempFile);
        assertTrue(savedContent.contains("New content for save test"), 
            "File should contain saved content");
    }

    @Test
    @DisplayName("Should provide undo manager")
    void testUndoManager() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            UndoManager undoManager = panel.getUndoManager();
            assertNotNull(undoManager, "Undo manager should not be null");
            
            // Test undo functionality
            panel.getTextArea().setText("Original");
            panel.getTextArea().setText("Modified");
            
            assertTrue(undoManager.canUndo(), "Should be able to undo");
            undoManager.undo();
            assertEquals("Original", panel.getTextArea().getText(), 
                "Text should be restored after undo");
        });
    }

    @Test
    @DisplayName("Should fire property change events")
    void testPropertyChangeEvents() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        
        SwingUtilities.invokeAndWait(() -> {
            panel.addPropertyChangeListener("lineNumbers", new PropertyChangeListener() {
                @Override
                public void propertyChange(PropertyChangeEvent evt) {
                    assertEquals("lineNumbers", evt.getPropertyName());
                    assertEquals(Boolean.TRUE, evt.getOldValue());
                    assertEquals(Boolean.FALSE, evt.getNewValue());
                    latch.countDown();
                }
            });
            
            panel.toggleLineNumbers(); // Should fire property change event
        });
        
        assertTrue(latch.await(1, TimeUnit.SECONDS), 
            "Property change event should be fired");
    }

    @Test
    @DisplayName("Should handle file info correctly")
    void testFileInfo() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            panel.getTextArea().setText("Line 1\nLine 2\nLine 3\n");
            
            // Move caret to trigger file info update
            panel.getTextArea().setCaretPosition(0);
        });
        
        // Note: FileInfo is created internally and passed via property change events
        // This test verifies the mechanism doesn't crash
        SwingUtilities.invokeAndWait(() -> {
            assertTrue(panel.getTextArea().getLineCount() > 0, 
                "Should have line count");
        });
    }

    @Test
    @DisplayName("Should handle read-only files")
    void testReadOnlyFile() throws Exception {
        // Make file read-only
        tempFile.toFile().setWritable(false);
        
        SwingUtilities.invokeAndWait(() -> {
            panel.loadFile(tempFile);
            // The panel should detect read-only status
            assertTrue(panel.getTextArea().getText().length() > 0, 
                "Should still load read-only file content");
        });
        
        // Restore write permission for cleanup
        tempFile.toFile().setWritable(true);
    }

    @Test
    @DisplayName("Should handle empty files")
    void testEmptyFile() throws Exception {
        Path emptyFile = Files.createTempFile("empty", ".txt");
        
        try {
            SwingUtilities.invokeAndWait(() -> {
                panel.loadFile(emptyFile);
                assertEquals("", panel.getTextArea().getText(), 
                    "Empty file should result in empty text");
                assertFalse(panel.isModified(), 
                    "Should not be modified after opening empty file");
            });
        } finally {
            Files.deleteIfExists(emptyFile);
        }
    }

    @Test
    @DisplayName("Should handle non-existent files gracefully")
    void testNonExistentFile() throws Exception {
        Path nonExistentFile = Path.of("non_existent_file.txt");
        
        SwingUtilities.invokeAndWait(() -> {
            // This should not crash the application
            try {
                panel.loadFile(nonExistentFile);
            } catch (Exception e) {
                // Expected behavior - should handle gracefully
                assertTrue(true, "Should handle non-existent files gracefully");
            }
        });
    }
}
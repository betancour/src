# Java Text Editor - API Documentation

This document provides comprehensive API documentation for the Java Text Editor application.

## Package Structure

### `editor` Package
The root package containing the main application entry point.

#### Classes
- **`Main`** - Application entry point with main method

### `editor.actions` Package
Contains action classes that handle user interactions and commands.

#### Classes

##### `EditActions`
Provides text editing operations with full undo/redo support.

**Inner Classes:**
- `UndoAction extends AbstractAction` - Handles undo operations
- `RedoAction extends AbstractAction` - Handles redo operations  
- `CutAction extends AbstractAction` - Handles cut to clipboard
- `CopyAction extends AbstractAction` - Handles copy to clipboard
- `PasteAction extends AbstractAction` - Handles paste from clipboard
- `SelectAllAction extends AbstractAction` - Handles select all text

**Key Methods:**
- `updateUndoState()` - Updates undo action availability and name
- `updateRedoState()` - Updates redo action availability and name

##### `FileActions`
Manages file operations including new, open, save, and save as.

**Static Methods:**
- `newFile(JFrame parent, TextAreaPanel panel)` - Creates new document
- `openFile(JFrame parent, TextAreaPanel panel)` - Opens file dialog and loads file
- `saveFile(JFrame parent, TextAreaPanel panel, boolean saveAs)` - Saves current document
- `confirmCloseAll(JFrame parent, TextAreaPanel panel)` - Prompts to save unsaved changes
- `openTerminal(JFrame parent)` - Opens system terminal

##### `SearchActions`
Provides find and replace functionality.

**Inner Classes:**
- `FindAction extends AbstractAction` - Opens find dialog
- `ReplaceAction extends AbstractAction` - Opens find/replace dialog
- `FindNextAction extends AbstractAction` - Finds next occurrence

##### `TerminalAction`
Launches system terminal application.

**Methods:**
- `actionPerformed(ActionEvent e)` - Opens terminal using OSUtils

### `editor.ui` Package
Contains user interface components and factories.

#### Classes

##### `EditorFrame extends JFrame`
Main application window that coordinates all UI components.

**Constructor:**
- `EditorFrame()` - Initializes main window with all components

**Methods:**
- `getTextPanel()` - Returns the text editing panel
- `getStatusBar()` - Returns the status bar component

**Features:**
- Integrates MenuBar, ToolBar, TextAreaPanel, and StatusBar
- Handles property change events for status updates
- Manages window closing with unsaved file confirmation

##### `TextAreaPanel extends JPanel`
Central text editing component with comprehensive editing features.

**Constructor:**
- `TextAreaPanel()` - Initializes text area with line numbers and listeners

**Public Methods:**
- `newFile()` - Clears content and resets state
- `loadFile(Path path)` - Loads file content in background thread
- `saveFile(Path path)` - Saves content to file in background thread
- `getCurrentFile()` - Returns current file path
- `isModified()` - Returns modification status
- `setModified(boolean modified)` - Sets modification status
- `getTextContent()` - Returns current text content
- `setTextContent(String content)` - Sets text content
- `getTextArea()` - Returns underlying JTextArea
- `getUndoManager()` - Returns undo manager
- `getFileType()` - Returns file type (OS/Unix)
- `isReadOnly()` - Returns read-only status
- `refreshReadOnlyStatus()` - Updates read-only status from file system

**Inner Classes:**
- `FileInfo` - Data class containing file metadata (lines, read-only, type)

**Property Change Events:**
- "caret" - Fired when cursor position changes
- "file" - Fired when file is loaded/saved
- "modified" - Fired when modification status changes
- "fileInfo" - Fired when file metadata changes

##### `FindReplaceDialog extends JDialog`
Advanced find and replace dialog with comprehensive search options.

**Constructor:**
- `FindReplaceDialog(JFrame parent, JTextArea textArea)` - Creates non-modal dialog

**Public Methods:**
- `showDialog()` - Shows dialog and focuses find field
- `showWithText(String selectedText)` - Shows dialog with pre-filled search text

**Features:**
- Case sensitive/insensitive matching
- Whole word matching
- Regular expression support
- Find next/previous navigation
- Replace single/all occurrences
- Real-time status feedback
- Keyboard shortcuts (Enter, F3, ESC)

##### `LineNumberView extends JComponent`
Displays line numbers for the text area with dynamic sizing.

**Constructor:**
- `LineNumberView(JTextArea textArea)` - Attaches to text area

**Features:**
- Dynamic width adjustment based on line count
- Synchronized scrolling with text area
- Efficient painting with clipping
- Document change listener integration

##### `StatusBar extends JPanel`
Displays current file and cursor information.

**Constructor:**
- `StatusBar()` - Initializes with default values

**Methods:**
- `setCaretPos(String position)` - Updates cursor position display
- `setFileInfo(int totalLines, boolean readOnly, String fileType)` - Updates file information

**Display Format:**
- Left: "Ln X, Col Y" (cursor position)
- Right: "Lines: X | RW/RO | OS/Unix" (file metadata)

##### `MenuBarFactory`
Factory class for creating the application menu bar.

**Static Methods:**
- `create(JFrame parent, TextAreaPanel panel, StatusBar statusBar)` - Creates complete menu bar

**Menu Structure:**
- **File:** New, Open, Save, Save As, Exit
- **Edit:** Undo, Redo, Cut, Copy, Paste, Select All
- **Search:** Find, Replace, Find Next
- **Tools:** Open Terminal

##### `ToolBarFactory`
Factory class for creating the application toolbar.

**Static Methods:**
- `create(JFrame parent, TextAreaPanel panel)` - Creates toolbar with system icons

**Toolbar Buttons:**
- File operations: New, Open, Save
- Edit operations: Undo, Redo, Cut, Copy, Paste
- Search: Find
- Tools: Terminal

### `editor.utils` Package
Contains utility classes for file operations, system integration, and icons.

#### Classes

##### `FileUtils`
Utility methods for file I/O operations.

**Static Methods:**
- `readFile(Path path)` - Reads file content as string
- `writeFile(Path path, String content)` - Writes string content to file

**Features:**
- UTF-8 encoding support
- Proper exception handling
- Cross-platform compatibility

##### `OSUtils`
Operating system integration utilities.

**Static Methods:**
- `openTerminal()` - Opens system terminal application

**Platform Support:**
- Windows: cmd.exe
- macOS: Terminal.app
- Linux: Various terminal emulators (gnome-terminal, konsole, xterm)

##### `SystemIconHelper`
Provides system icons with fallback to custom-drawn icons.

**Static Methods:**
- `getBestIcon(String operation)` - Returns best available icon for operation
- `getNewFileIcon()` - Returns new file icon
- `getOpenFileIcon()` - Returns open file icon
- `getSaveFileIcon()` - Returns save file icon
- `getCutIcon()` - Returns cut operation icon
- `getCopyIcon()` - Returns copy operation icon
- `getPasteIcon()` - Returns paste operation icon
- `getUndoIcon()` - Returns undo operation icon
- `getRedoIcon()` - Returns redo operation icon
- `getFindIcon()` - Returns find operation icon
- `getReplaceIcon()` - Returns replace operation icon
- `getTerminalIcon()` - Returns terminal operation icon

**Features:**
- UIManager system icon detection
- Custom geometric icon fallbacks
- Look and Feel integration
- 16x16 pixel icon size standardization

##### `IconTester`
Utility application for testing and displaying available system icons.

**Methods:**
- `main(String[] args)` - Launches icon testing GUI
- `printSystemIcons()` - Prints available icons to console

## Event System

### Property Change Events

The application uses Java's PropertyChangeListener system for component communication:

#### TextAreaPanel Events
- **"caret"** - `String` format "line,column" - Fired when cursor moves
- **"file"** - `String` file path - Fired when file is loaded/saved
- **"modified"** - `Boolean` - Fired when document modification status changes
- **"fileInfo"** - `FileInfo` object - Fired when file metadata changes

### Document Events

Standard Swing DocumentListener events are used for:
- Text insertion/deletion tracking
- Undo/redo history management
- Line count updates
- Modification status changes

## Threading Model

### Background Operations
- **File Loading:** Performed in background thread to prevent UI blocking
- **File Saving:** Performed in background thread with progress indication
- **Status Updates:** UI updates dispatched to Event Dispatch Thread

### Thread Safety
- All UI updates use `SwingUtilities.invokeLater()`
- File operations are properly synchronized
- Property change events are thread-safe

## Error Handling

### File Operations
- IOException handling with user-friendly messages
- Permission checks before file operations
- Graceful handling of missing files or directories

### Search Operations
- Regular expression syntax error handling
- Empty search term validation
- Wrap-around search with user notification

### System Integration
- Graceful degradation when system features unavailable
- Fallback icon rendering when system icons missing
- Cross-platform compatibility handling

## Extension Points

### Adding New Actions
1. Create action class extending `AbstractAction`
2. Add to appropriate factory (MenuBarFactory/ToolBarFactory)
3. Register keyboard shortcuts in EditorFrame
4. Add system icon support in SystemIconHelper

### Custom File Formats
1. Extend FileUtils with format-specific methods
2. Add file type detection in TextAreaPanel
3. Update status bar display format

### New UI Components
1. Create component extending appropriate Swing class
2. Integrate with EditorFrame layout
3. Add property change listener support
4. Update factory classes as needed

## Dependencies

### Required Java Packages
- `javax.swing.*` - Swing GUI framework
- `java.awt.*` - AWT graphics and events
- `java.nio.file.*` - Modern file I/O API
- `java.util.regex.*` - Regular expression support
- `java.util.concurrent.*` - Background threading
- `java.beans.*` - Property change support

### Minimum Requirements
- Java 8 or higher
- Swing/AWT support (included in standard JRE)
- File system access permissions

## Performance Considerations

### Memory Usage
- Efficient text storage with JTextArea's Document model
- Bounded undo/redo history to prevent memory leaks
- Background file operations to maintain responsiveness

### Rendering Performance
- Optimized line number painting with clipping regions
- Efficient status bar updates to minimize redraws
- Icon caching in SystemIconHelper

### Search Performance
- Compiled regex patterns cached during search sessions
- Efficient string matching algorithms
- Incremental search result highlighting
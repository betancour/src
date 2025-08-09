# Java Text Editor

A modern, feature-rich text editor built with Java Swing, designed for programmers and power users. Features professional icons powered by Ikonli with automatic fallback support.

## Features

### User Interface
- **Professional Icons** - FontAwesome and Material Design icons via Ikonli-Swing
- **Cross-platform compatibility** - Windows, macOS, and Linux support
- **Graceful fallback** - Works with or without professional icon dependencies
- **Modern toolbar** - Clean, intuitive interface with properly proportioned icons

### Core Editing
- **Full-featured text editing** with syntax highlighting support
- **Undo/Redo** functionality with complete edit history
- **Cut, Copy, Paste** operations with system clipboard integration
- **Select All** and advanced text selection

### Search & Replace
- **Advanced Find Dialog** with comprehensive search options
- **Find and Replace** functionality with regex support
- **Case-sensitive/insensitive** matching
- **Whole word** matching option
- **Regular expression** pattern matching
- **Find Next/Previous** navigation
- **Replace All** with occurrence counting

### File Operations
- **New File** creation
- **Open File** with file browser
- **Save** and **Save As** functionality
- **Auto-detection** of file encoding (Unix/Windows line endings)
- **Read-only** file detection and display

### User Interface
- **Professional toolbar** with system icons
- **Comprehensive menu system** with keyboard shortcuts
- **Status bar** showing:
  - Current line and column position
  - Total line count
  - Read/Write mode indicator
  - File type (OS/Unix line endings)
- **Line numbers** with dynamic width adjustment
- **System Look and Feel** integration

### Keyboard Shortcuts
- **Ctrl+N** - New File
- **Ctrl+O** - Open File
- **Ctrl+S** - Save File
- **Ctrl+Z** - Undo
- **Ctrl+Y** - Redo
- **Ctrl+X** - Cut
- **Ctrl+C** - Copy
- **Ctrl+V** - Paste
- **Ctrl+A** - Select All
- **Ctrl+F** - Find
- **Ctrl+H** - Replace
- **F3** - Find Next
- **Ctrl+T** - Open Terminal
- **ESC** - Close dialogs

## Project Structure

```
src/
├── editor/
│   ├── Main.java                 # Application entry point
│   ├── actions/                  # Action classes
│   │   ├── EditActions.java      # Cut, Copy, Paste, Undo, Redo
│   │   ├── FileActions.java      # File operations
│   │   ├── SearchActions.java    # Find and Replace
│   │   └── TerminalAction.java   # Terminal integration
│   ├── ui/                       # User interface components
│   │   ├── EditorFrame.java      # Main application window
│   │   ├── TextAreaPanel.java    # Text editing component
│   │   ├── LineNumberView.java   # Line number display
│   │   ├── StatusBar.java        # Status information
│   │   ├── FindReplaceDialog.java# Search and replace dialog
│   │   ├── MenuBarFactory.java   # Menu creation
│   │   └── ToolBarFactory.java   # Toolbar creation
│   └── utils/                    # Utility classes
│       ├── FileUtils.java        # File I/O operations
│       ├── OSUtils.java          # Operating system utilities
│       ├── SystemIconHelper.java # Enhanced icon system with Ikonli support
│       ├── IkonliIconProvider.java # Professional icon loading
│       └── IconTester.java       # Icon testing utility
├── lib/                          # Professional icon dependencies (optional)
│   ├── ikonli-core-12.3.1.jar
│   ├── ikonli-swing-12.3.1.jar
│   ├── ikonli-fontawesome5-pack-12.3.1.jar
│   └── ikonli-material2-pack-12.3.1.jar
├── bin/                          # Compiled classes (generated)
├── resources/                    # Application resources
├── docs/                         # Documentation
├── build.sh                     # Unix/Linux build script (with Ikonli support)
├── build.bat                    # Windows build script (with Ikonli support)
├── run.sh                       # Unix/Linux run script (with Ikonli support)
├── run.bat                      # Windows run script (with Ikonli support)
├── download-dependencies.sh     # Download professional icons (Unix)
├── download-dependencies.bat    # Download professional icons (Windows)
├── test-icons.sh                # Test icon system
└── README.md                    # This file
```

## Professional Icon Setup (Optional)

For the best visual experience, enable professional FontAwesome and Material Design icons:

### Download Icon Dependencies

#### On Unix/Linux/macOS:
```bash
cd src
chmod +x download-dependencies.sh
./download-dependencies.sh
```

#### On Windows:
```cmd
cd src
download-dependencies.bat
```

This downloads 4 JAR files (~1.1MB total) to the `lib/` directory:
- FontAwesome 5 icon pack
- Material Design 2 icon pack  
- Ikonli core and Swing integration

### Verify Icon Setup
```bash
cd src
chmod +x test-icons.sh
./test-icons.sh
```

Expected output: `✓ Ikonli-Swing loaded successfully - Using professional icons`

**Note**: The application works perfectly without professional icons - they're just a visual enhancement!

## Building and Running

### Prerequisites
- Java Development Kit (JDK) 8 or higher
- Command line access (Terminal/Command Prompt)

### Building the Application

#### On Unix/Linux/macOS:
```bash
cd src
chmod +x build.sh
./build.sh
```

#### On Windows:
```cmd
cd src
build.bat
```

### Running the Application

#### On Unix/Linux/macOS:
```bash
cd src
chmod +x run.sh
./run.sh
```

#### On Windows:
```cmd
cd src
run.bat
```

#### Manual execution:
```bash
cd src
java -cp bin editor.Main
```

## Technical Details

### Architecture
- **MVC Pattern**: Clear separation of model, view, and controller components
- **Event-driven**: Swing-based event handling with property change listeners
- **Modular Design**: Organized into logical packages (actions, ui, utils)

### Key Components

#### TextAreaPanel
- Central text editing component with JTextArea
- Integrated undo/redo support via UndoManager
- Property change events for status updates
- File type detection and read-only status tracking

#### FindReplaceDialog
- Non-modal dialog for search operations
- Support for literal and regex pattern matching
- Case sensitivity and whole word options
- Real-time status feedback and error handling

#### SystemIconHelper
- Enhanced with professional Ikonli icon support
- Three-tier fallback: Professional → System → Geometric
- Dynamic system icon detection with graceful degradation
- Look and Feel integration for native appearance

#### IkonliIconProvider
- Dynamic loading of FontAwesome and Material Design icons
- Reflection-based approach for optional dependency support
- Professional vector icons with consistent design language

#### StatusBar
- Real-time cursor position tracking
- File information display (lines, permissions, type)
- Professional layout with proper spacing

### System Integration
- **Native Look and Feel**: Automatically adapts to system appearance
- **System Icons**: Uses platform-specific icons where available
- **Clipboard Integration**: Full system clipboard support
- **File System**: Proper file encoding and permission handling

## Development

### Code Style
- Clear, descriptive variable and method names
- Comprehensive javadoc comments
- Consistent indentation and formatting
- Proper exception handling

### Error Handling
- Graceful degradation for missing system features
- User-friendly error messages
- Robust file I/O with proper exception handling
- Safe clipboard operations

### Performance
- Efficient text rendering with JTextArea
- Background file operations to prevent UI blocking
- Optimized search algorithms
- Memory-efficient undo/redo implementation

## Dependencies

### Core Dependencies
- Java Standard Library (Swing, AWT)
- No external dependencies required for basic functionality

### Optional Dependencies (Professional Icons)
- Ikonli Core 12.3.1
- Ikonli Swing 12.3.1  
- FontAwesome 5 Pack 12.3.1
- Material Design 2 Pack 12.3.1

## License

This project is open source. Feel free to use, modify, and distribute according to your needs.

## Contributing

1. Ensure all code is in English
2. Follow the existing code style and structure
3. Test thoroughly on different platforms
4. Update documentation as needed

## Version History

- **v1.0** - Initial release with basic editing features
- **v1.1** - Added find and replace functionality
- **v1.2** - Enhanced with system icons and improved UI
- **v1.3** - Added status bar and file information display
- **v1.4** - Professional icon integration with Ikonli-Swing support

## Support

For issues, suggestions, or contributions, please refer to the project documentation or contact the development team.
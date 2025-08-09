# Java Text Editor - Project Structure

## Overview
This document provides a complete overview of the Java Text Editor project structure, build system, and organization.

## Directory Structure

```
src/
├── editor/                          # Main source package
│   ├── Main.java                    # Application entry point
│   │
│   ├── actions/                     # Action handler classes
│   │   ├── EditActions.java         # Cut, Copy, Paste, Undo, Redo actions
│   │   ├── FileActions.java         # File operations (New, Open, Save)
│   │   ├── SearchActions.java       # Find and Replace functionality
│   │   └── TerminalAction.java      # Terminal integration
│   │
│   ├── ui/                          # User interface components
│   │   ├── EditorFrame.java         # Main application window
│   │   ├── TextAreaPanel.java       # Text editing panel with features
│   │   ├── LineNumberView.java      # Line number display component
│   │   ├── StatusBar.java           # Status bar with file information
│   │   ├── FindReplaceDialog.java   # Advanced find/replace dialog
│   │   ├── MenuBarFactory.java      # Application menu creation
│   │   └── ToolBarFactory.java      # Toolbar with system icons
│   │
│   └── utils/                       # Utility classes
│       ├── FileUtils.java           # File I/O operations
│       ├── OSUtils.java             # Operating system integration
│       ├── SystemIconHelper.java    # System icon management
│       └── IconTester.java          # Icon testing utility
│
├── bin/                             # Compiled class files (auto-generated)
│   └── editor/                      # Compiled packages mirror source structure
│       ├── Main.class
│       ├── actions/
│       ├── ui/
│       └── utils/
│
├── resources/                       # Application resources
│   └── demo.txt                     # Sample file for testing features
│
├── docs/                           # Project documentation
│   └── API.md                      # Comprehensive API documentation
│
├── build.sh                        # Unix/Linux build script
├── build.bat                       # Windows build script
├── run.sh                          # Unix/Linux run script
├── run.bat                         # Windows run script
├── README.md                       # Main project documentation
└── PROJECT_STRUCTURE.md            # This file
```

## Build System

### Build Scripts
- **build.sh/build.bat**: Compiles all Java sources to bin/ directory
- **run.sh/run.bat**: Executes the compiled application
- **Clean separation**: Source files remain in src/, compiled files go to bin/

### Build Process
1. Cleans previous build artifacts
2. Creates bin/ directory structure
3. Compiles all Java files with proper classpath
4. Reports compilation status and class count
5. Provides run instructions

## Package Organization

### `editor` (Root Package)
- Contains main application entry point
- Coordinates application startup and Look & Feel setup

### `editor.actions` Package
- **Purpose**: Command pattern implementation for all user actions
- **Features**: Undo/Redo support, keyboard shortcuts, system integration
- **Classes**: 4 action classes with 11 inner action implementations

### `editor.ui` Package
- **Purpose**: All user interface components and factories
- **Features**: Modern Swing UI, system icons, status tracking
- **Classes**: 7 UI classes including main frame, panels, and factories

### `editor.utils` Package
- **Purpose**: System integration and utility functions
- **Features**: File I/O, OS integration, icon management
- **Classes**: 4 utility classes with cross-platform support

## Key Features Implementation

### Text Editing
- **Location**: `TextAreaPanel.java`
- **Features**: Undo/Redo, line numbers, status tracking
- **Integration**: Property change events for UI updates

### Find & Replace
- **Location**: `FindReplaceDialog.java`
- **Features**: Regex support, case sensitivity, whole word matching
- **UI**: Non-modal dialog with real-time feedback

### File Operations
- **Location**: `FileActions.java` + `FileUtils.java`
- **Features**: Background loading/saving, encoding detection
- **Safety**: Unsaved changes confirmation, error handling

### System Integration
- **Location**: `SystemIconHelper.java` + `OSUtils.java`
- **Features**: Native icons, terminal launching, Look & Feel
- **Compatibility**: Windows, macOS, Linux support

## Build Artifacts

### Compiled Classes (36 files)
- Main application: 1 class
- Action classes: 12 classes (including inner classes)
- UI components: 15 classes (including inner classes)
- Utility classes: 8 classes (including inner classes)

### Class File Organization
```
bin/editor/
├── Main.class
├── actions/
│   ├── EditActions.class
│   ├── EditActions$UndoAction.class
│   ├── EditActions$RedoAction.class
│   ├── EditActions$CutAction.class
│   ├── EditActions$CopyAction.class
│   ├── EditActions$PasteAction.class
│   ├── EditActions$SelectAllAction.class
│   ├── FileActions.class
│   ├── SearchActions.class
│   ├── SearchActions$FindAction.class
│   ├── SearchActions$ReplaceAction.class
│   ├── SearchActions$FindNextAction.class
│   └── TerminalAction.class
├── ui/
│   ├── EditorFrame.class
│   ├── EditorFrame$1.class
│   ├── EditorFrame$2.class
│   ├── TextAreaPanel.class
│   ├── TextAreaPanel$1.class
│   ├── TextAreaPanel$FileInfo.class
│   ├── LineNumberView.class
│   ├── StatusBar.class
│   ├── FindReplaceDialog.class
│   ├── FindReplaceDialog$1.class
│   ├── FindReplaceDialog$2.class
│   ├── FindReplaceDialog$3.class
│   ├── MenuBarFactory.class
│   └── ToolBarFactory.class
└── utils/
    ├── FileUtils.class
    ├── OSUtils.class
    ├── SystemIconHelper.class
    ├── SystemIconHelper$1.class
    ├── SystemIconHelper$2.class
    ├── SystemIconHelper$3.class
    ├── IconTester.class
    └── IconTester$IconInfo.class
```

## Development Workflow

### Building
```bash
# Unix/Linux/macOS
./build.sh

# Windows
build.bat
```

### Running
```bash
# Unix/Linux/macOS
./run.sh

# Windows
run.bat

# Manual execution
java -cp bin editor.Main
```

### Development
1. Edit source files in `src/editor/`
2. Run build script to compile
3. Test with run script
4. Documentation in `docs/` and `README.md`

## Code Quality Standards

### Language Requirements
- All code, comments, and documentation in English
- Clear, descriptive naming conventions
- Comprehensive error handling

### Architecture Principles
- Clean separation of concerns (UI, Actions, Utils)
- Event-driven architecture with property change listeners
- Modular design with minimal coupling

### File Organization
- Source files clean (no .class files)
- Compiled files isolated in bin/
- Resources and documentation properly organized
- Build scripts for cross-platform compatibility

## Maintenance

### Adding Features
1. Create new classes in appropriate package
2. Update factory classes for UI integration
3. Add documentation to API.md
4. Test build and run scripts

### Updating Documentation
- **README.md**: User-facing documentation and features
- **API.md**: Technical API documentation
- **PROJECT_STRUCTURE.md**: This organizational overview

### Version Management
- Source code in `src/editor/`
- Build artifacts in `src/bin/` (regenerated)
- Documentation in `src/docs/` and root markdown files

This structure ensures clean separation between source and build artifacts, comprehensive documentation, and cross-platform compatibility.
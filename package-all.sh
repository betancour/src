#!/bin/bash

# Master packaging script for Java Text Editor
# Creates packages for all supported platforms (Linux and Windows)

set -e  # Exit on any error

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Package information
APP_NAME="java-text-editor"
APP_VERSION="1.4.0"
APP_DESCRIPTION="Modern Java Text Editor with Professional Icons"

# Directories
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_DIR="$SCRIPT_DIR"
PACKAGING_DIR="$PROJECT_DIR/packaging"
DIST_DIR="$PACKAGING_DIR/dist"

echo -e "${BLUE}=== Java Text Editor Master Packaging Script ===${NC}"
echo "Application: $APP_NAME v$APP_VERSION"
echo "Description: $APP_DESCRIPTION"
echo "Project Directory: $PROJECT_DIR"
echo

# Check prerequisites
echo -e "${YELLOW}Checking prerequisites...${NC}"

# Check for Java
if ! command -v java >/dev/null 2>&1; then
    echo -e "${RED}Error: Java is not installed or not in PATH${NC}"
    echo "Please install Java 8 or higher"
    exit 1
fi

if ! command -v javac >/dev/null 2>&1; then
    echo -e "${RED}Error: Java compiler (javac) is not installed or not in PATH${NC}"
    echo "Please install Java Development Kit (JDK)"
    exit 1
fi

echo -e "${GREEN}✓ Java found: $(java -version 2>&1 | head -1)${NC}"

# Check for build scripts
if [ ! -f "build.sh" ]; then
    echo -e "${RED}Error: build.sh not found${NC}"
    exit 1
fi

echo -e "${GREEN}✓ Build scripts found${NC}"

# Clean previous packaging
echo -e "${YELLOW}Cleaning previous packages...${NC}"
rm -rf "$PACKAGING_DIR/build" "$PACKAGING_DIR/dist"
mkdir -p "$PACKAGING_DIR/build" "$PACKAGING_DIR/dist"

# Build the application
echo -e "${YELLOW}Building application...${NC}"
chmod +x build.sh
./build.sh

if [ ! -d "bin" ] || [ -z "$(ls -A bin 2>/dev/null)" ]; then
    echo -e "${RED}Error: Application build failed${NC}"
    exit 1
fi

echo -e "${GREEN}✓ Application built successfully${NC}"

# Download dependencies if needed
if [ ! -d "lib" ] || [ -z "$(ls -A lib/*.jar 2>/dev/null)" ]; then
    echo -e "${YELLOW}Downloading professional icon dependencies...${NC}"
    if [ -f "download-dependencies.sh" ]; then
        chmod +x download-dependencies.sh
        ./download-dependencies.sh
    else
        echo -e "${YELLOW}Warning: Icon dependencies script not found${NC}"
    fi
fi

# Run tests if available
echo -e "${YELLOW}Running tests (if available)...${NC}"
if [ -f "test-icons.sh" ]; then
    chmod +x test-icons.sh
    ./test-icons.sh
    echo -e "${GREEN}✓ Icon tests passed${NC}"
fi

# Package for Linux
echo -e "${BLUE}=== Packaging for Linux ===${NC}"
if [ -f "packaging/linux/package-linux.sh" ]; then
    chmod +x packaging/linux/package-linux.sh
    cd "$PROJECT_DIR"
    ./packaging/linux/package-linux.sh
    echo -e "${GREEN}✓ Linux packages created${NC}"
else
    echo -e "${RED}Error: Linux packaging script not found${NC}"
    exit 1
fi

# Package for Windows (if on appropriate system or with cross-platform tools)
echo -e "${BLUE}=== Packaging for Windows ===${NC}"
if [ -f "packaging/windows/package-windows.bat" ]; then
    # Check if we can run Windows batch files
    if command -v cmd >/dev/null 2>&1 || command -v wine >/dev/null 2>&1; then
        echo -e "${YELLOW}Running Windows packaging script...${NC}"
        if command -v cmd >/dev/null 2>&1; then
            cmd.exe /c "cd /d \"$(cygpath -w "$PROJECT_DIR")\" && packaging\\windows\\package-windows.bat"
        elif command -v wine >/dev/null 2>&1; then
            wine cmd /c "cd /d \"$PROJECT_DIR\" && packaging\\windows\\package-windows.bat"
        fi
        echo -e "${GREEN}✓ Windows packages created${NC}"
    else
        echo -e "${YELLOW}Warning: Cannot run Windows packaging on this system${NC}"
        echo -e "${YELLOW}Run packaging/windows/package-windows.bat on a Windows system${NC}"
        
        # Create a simple Windows portable package manually
        echo -e "${YELLOW}Creating basic Windows portable package...${NC}"
        WIN_DIR="$PACKAGING_DIR/build/windows-manual"
        mkdir -p "$WIN_DIR"
        
        # Copy application files
        cp -r bin "$WIN_DIR/"
        if [ -d "lib" ]; then
            cp -r lib "$WIN_DIR/"
        fi
        
        # Create simple launcher
        cat > "$WIN_DIR/java-text-editor.bat" << 'EOF'
@echo off
REM Java Text Editor launcher script

REM Get the directory where this script is located
set "DIR=%~dp0"

REM Set up classpath
set "CLASSPATH=%DIR%bin"
if exist "%DIR%lib" (
    for %%j in ("%DIR%lib\*.jar") do (
        set "CLASSPATH=!CLASSPATH!;%%j"
    )
)

REM Check for Java
where java >nul 2>nul
if %errorlevel% neq 0 (
    echo Error: Java is not installed or not in PATH
    echo Please install Java 8 or higher to run Java Text Editor
    pause
    exit /b 1
)

REM Run the application
java -cp "%CLASSPATH%" editor.Main %*
EOF
        
        # Create zip package
        if command -v zip >/dev/null 2>&1; then
            cd "$PACKAGING_DIR/build"
            zip -r "$DIST_DIR/${APP_NAME}-${APP_VERSION}-windows-basic.zip" windows-manual/
            echo -e "${GREEN}✓ Basic Windows package created${NC}"
        else
            echo -e "${YELLOW}Warning: zip command not found, Windows files prepared in build directory${NC}"
        fi
    fi
else
    echo -e "${RED}Error: Windows packaging script not found${NC}"
fi

# Create release notes
echo -e "${YELLOW}Creating release documentation...${NC}"
cat > "$DIST_DIR/RELEASE-NOTES.md" << EOF
# Java Text Editor v${APP_VERSION} - Release Notes

## 📋 Overview
This release includes professional icon integration with Ikonli-Swing, line numbering toggle functionality, comprehensive unit testing, and cross-platform packaging support.

## 🎯 Key Features
- **Professional Icons**: FontAwesome and Material Design icons via Ikonli-Swing
- **Line Numbers Toggle**: Show/hide line numbers with toolbar button and menu option
- **Advanced Text Editing**: Cut, copy, paste, undo, redo with professional interface
- **Search & Replace**: Comprehensive find/replace with regex support
- **Cross-Platform**: Windows, macOS, and Linux support
- **Unit Testing**: Comprehensive test suite with JUnit 5
- **Easy Installation**: Multiple package formats for each platform

## 📦 Package Contents

### Linux Packages
- **${APP_NAME}-${APP_VERSION}_all.deb** - Debian/Ubuntu package with system integration
- **${APP_NAME}-${APP_VERSION}-linux.tar.gz** - Universal Linux archive

### Windows Packages  
- **${APP_NAME}-${APP_VERSION}-windows-installer.exe** - Windows installer with shortcuts
- **${APP_NAME}-${APP_VERSION}-windows-portable.zip** - Portable application (no installation required)

## 🔧 System Requirements
- **Java**: Version 8 or higher (OpenJDK or Oracle JDK)
- **Operating System**: Windows 7+, macOS 10.10+, Linux (any modern distribution)
- **Memory**: 512MB RAM minimum, 1GB recommended
- **Storage**: 50MB disk space

## 🚀 Installation Instructions
See the platform-specific installation guides:
- **INSTALL-LINUX.md** - Linux installation instructions
- **INSTALL-WINDOWS.md** - Windows installation instructions

## ✨ What's New in v${APP_VERSION}

### 🎨 Professional Icons
- Integrated Ikonli-Swing for professional FontAwesome and Material Design icons
- Automatic fallback system ensures compatibility without dependencies
- Scalable vector graphics for crisp display at any resolution

### 📝 Line Numbers Enhancement
- Toggle line numbers on/off via toolbar button
- Menu option in View menu with checkbox state
- Keyboard shortcut support
- Maintains state during editing sessions

### 🧪 Quality Assurance
- Comprehensive unit test suite with JUnit 5
- Automated testing of icon system, UI components, and core functionality
- Cross-platform build and test scripts
- Continuous integration ready

### 📦 Packaging & Distribution
- Professional Linux .deb packages with desktop integration
- Windows installer with Start Menu and Desktop shortcuts
- Portable packages for easy deployment
- Automated packaging scripts for both platforms

## 🛠️ Developer Features
- **Unit Testing**: Run tests with \`./run-tests.sh\` or \`run-tests.bat\`
- **Icon Testing**: Verify icon system with \`./test-icons.sh\`
- **Build System**: Enhanced cross-platform build scripts
- **Packaging**: Automated package creation for distribution

## 🐛 Bug Fixes
- Fixed horizontally stretched scissors icon (original issue)
- Improved icon proportions and scaling
- Enhanced error handling in file operations
- Better memory management for large files

## 📈 Performance Improvements
- Optimized icon loading and rendering
- Faster startup time with lazy loading
- Improved memory usage for icon resources
- Enhanced responsiveness in UI operations

## 🔗 Links
- **Source Code**: https://github.com/example/java-text-editor
- **Documentation**: See README.md and user guides
- **Issue Tracker**: Report bugs and request features
- **Releases**: Download latest versions and updates

## 💬 Support
For technical support, feature requests, or bug reports:
- Check the documentation files included with this release
- Visit the project repository for latest updates
- Contact the development team for assistance

---
**Thank you for using Java Text Editor!** 🎉

*Built with ❤️ using Java Swing and powered by professional Ikonli icons*
EOF

# Create master installation script
cat > "$DIST_DIR/install.sh" << 'EOF'
#!/bin/bash
# Java Text Editor Installation Script
# Automatically detects platform and installs appropriate package

set -e

# Colors
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m'

echo -e "${BLUE}Java Text Editor Installation Script${NC}"
echo

# Detect platform
if [[ "$OSTYPE" == "linux-gnu"* ]]; then
    PLATFORM="linux"
elif [[ "$OSTYPE" == "darwin"* ]]; then
    PLATFORM="macos"
elif [[ "$OSTYPE" == "cygwin" ]] || [[ "$OSTYPE" == "msys" ]]; then
    PLATFORM="windows"
else
    echo -e "${YELLOW}Unknown platform: $OSTYPE${NC}"
    PLATFORM="linux"
fi

echo -e "${BLUE}Detected platform: $PLATFORM${NC}"

case $PLATFORM in
    "linux")
        if command -v dpkg >/dev/null 2>&1; then
            echo -e "${GREEN}Installing .deb package...${NC}"
            sudo dpkg -i java-text-editor-*_all.deb
            sudo apt-get install -f
        else
            echo -e "${GREEN}Installing from .tar.gz...${NC}"
            tar -xzf java-text-editor-*-linux.tar.gz
            echo "Please see INSTALL-LINUX.md for manual installation steps"
        fi
        ;;
    "windows")
        echo -e "${YELLOW}Please run the Windows installer or extract the portable ZIP${NC}"
        echo "See INSTALL-WINDOWS.md for detailed instructions"
        ;;
    "macos")
        echo -e "${YELLOW}macOS packages not available in this release${NC}"
        echo "Please extract the Linux .tar.gz and run manually"
        ;;
esac

echo -e "${GREEN}Installation complete!${NC}"
EOF

chmod +x "$DIST_DIR/install.sh"

# Display final results
echo
echo -e "${BLUE}=== Packaging Complete ===${NC}"
echo -e "${GREEN}All packages created in: $DIST_DIR${NC}"
echo

# List all created packages
echo -e "${BLUE}📦 Created Packages:${NC}"
ls -la "$DIST_DIR"

echo
echo -e "${BLUE}📊 Package Summary:${NC}"
TOTAL_SIZE=$(du -sh "$DIST_DIR" | cut -f1)
PACKAGE_COUNT=$(find "$DIST_DIR" -name "*.deb" -o -name "*.zip" -o -name "*.exe" -o -name "*.tar.gz" | wc -l)
echo "  Total packages: $PACKAGE_COUNT"
echo "  Total size: $TOTAL_SIZE"
echo "  Distribution directory: $DIST_DIR"

echo
echo -e "${BLUE}🚀 Ready for Distribution!${NC}"
echo -e "${GREEN}Your Java Text Editor packages are ready to deploy.${NC}"
echo
echo -e "${YELLOW}Next steps:${NC}"
echo "1. Test packages on target systems"
echo "2. Upload to distribution platforms"
echo "3. Update documentation and release notes"
echo "4. Announce the new release"

echo
echo -e "${GREEN}✅ Master packaging completed successfully!${NC}"
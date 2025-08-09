#!/bin/bash

# Linux packaging script for Java Text Editor
# Creates .deb package and .tar.gz archive for distribution

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
APP_MAINTAINER="Java Text Editor Team <editor@example.com>"
APP_HOMEPAGE="https://github.com/example/java-text-editor"

# Directories
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_DIR="$(cd "$SCRIPT_DIR/../.." && pwd)"
PACKAGING_DIR="$PROJECT_DIR/packaging"
BUILD_DIR="$PACKAGING_DIR/build"
DIST_DIR="$PACKAGING_DIR/dist"

echo -e "${BLUE}=== Linux Packaging Script ===${NC}"
echo "Application: $APP_NAME v$APP_VERSION"
echo "Project Directory: $PROJECT_DIR"
echo

# Clean and create build directories
echo -e "${YELLOW}Preparing build environment...${NC}"
rm -rf "$BUILD_DIR" "$DIST_DIR"
mkdir -p "$BUILD_DIR" "$DIST_DIR"

# Build the application first
echo -e "${YELLOW}Building application...${NC}"
cd "$PROJECT_DIR"
if [ ! -f "build.sh" ]; then
    echo -e "${RED}Error: build.sh not found in project directory${NC}"
    exit 1
fi

# Ensure build script is executable and run it
chmod +x build.sh
./build.sh

if [ ! -d "bin" ] || [ -z "$(find bin -name "*.class" 2>/dev/null)" ]; then
    echo -e "${RED}Error: Application build failed${NC}"
    exit 1
fi

echo -e "${GREEN}Application built successfully${NC}"

# Download dependencies if not present
if [ ! -d "lib" ] || [ -z "$(ls -A lib/*.jar 2>/dev/null)" ]; then
    echo -e "${YELLOW}Downloading professional icon dependencies...${NC}"
    if [ -f "download-dependencies.sh" ]; then
        chmod +x download-dependencies.sh
        ./download-dependencies.sh
    else
        echo -e "${YELLOW}Warning: Icon dependencies not available${NC}"
    fi
fi

# Create application directory structure
APP_DIR="$BUILD_DIR/$APP_NAME"
echo -e "${YELLOW}Creating application structure...${NC}"
mkdir -p "$APP_DIR"/{bin,lib,share/applications,share/icons/hicolor/48x48/apps,usr/bin}

# Copy application files
echo -e "${YELLOW}Copying application files...${NC}"
cp -r "$PROJECT_DIR/bin" "$APP_DIR/"
if [ -d "$PROJECT_DIR/lib" ]; then
    cp -r "$PROJECT_DIR/lib" "$APP_DIR/"
fi
cp -r "$PROJECT_DIR/resources" "$APP_DIR/" 2>/dev/null || echo "No resources directory"
cp "$PROJECT_DIR/README.md" "$APP_DIR/" 2>/dev/null || echo "No README.md"
cp "$PROJECT_DIR/LICENSE" "$APP_DIR/" 2>/dev/null || echo "No LICENSE file"

# Create launcher script
echo -e "${YELLOW}Creating launcher script...${NC}"
cat > "$APP_DIR/java-text-editor" << 'EOF'
#!/bin/bash
# Java Text Editor launcher script

# Get the directory where this script is located
DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

# Set up classpath
CLASSPATH="$DIR/bin"
if [ -d "$DIR/lib" ]; then
    for jar in "$DIR/lib"/*.jar; do
        if [ -f "$jar" ]; then
            CLASSPATH="$CLASSPATH:$jar"
        fi
    done
fi

# Check for Java
if ! command -v java >/dev/null 2>&1; then
    echo "Error: Java is not installed or not in PATH"
    echo "Please install Java 8 or higher to run Java Text Editor"
    exit 1
fi

# Run the application
exec java -cp "$CLASSPATH" editor.Main "$@"
EOF

chmod +x "$APP_DIR/java-text-editor"

# Create desktop entry
echo -e "${YELLOW}Creating desktop entry...${NC}"
cat > "$APP_DIR/share/applications/java-text-editor.desktop" << EOF
[Desktop Entry]
Version=1.0
Type=Application
Name=Java Text Editor
Comment=$APP_DESCRIPTION
Exec=/usr/share/java-text-editor/java-text-editor %F
Icon=java-text-editor
Terminal=false
StartupWMClass=editor.Main
Categories=Development;TextEditor;Utility;
MimeType=text/plain;text/x-java;text/x-c;text/x-python;text/x-shell;
Keywords=editor;text;programming;development;
EOF

# Create simple icon (if not provided)
echo -e "${YELLOW}Creating application icon...${NC}"
cat > "$APP_DIR/share/icons/hicolor/48x48/apps/java-text-editor.svg" << 'EOF'
<?xml version="1.0" encoding="UTF-8"?>
<svg width="48" height="48" viewBox="0 0 48 48" xmlns="http://www.w3.org/2000/svg">
    <rect width="48" height="48" rx="8" fill="#4A90E2"/>
    <text x="24" y="32" font-family="monospace" font-size="24" font-weight="bold" 
          text-anchor="middle" fill="white">{ }</text>
    <circle cx="12" cy="12" r="2" fill="#FFD700"/>
    <circle cx="20" cy="12" r="2" fill="#FF6B6B"/>
    <circle cx="28" cy="12" r="2" fill="#4ECDC4"/>
</svg>
EOF

# Create system launcher symlink
mkdir -p "$APP_DIR/usr/bin"
cat > "$APP_DIR/usr/bin/java-text-editor" << 'EOF'
#!/bin/bash
exec /usr/share/java-text-editor/java-text-editor "$@"
EOF
chmod +x "$APP_DIR/usr/bin/java-text-editor"

# Create .tar.gz package
echo -e "${YELLOW}Creating .tar.gz package...${NC}"
cd "$BUILD_DIR"
tar -czf "$DIST_DIR/${APP_NAME}-${APP_VERSION}-linux.tar.gz" "$APP_NAME"
echo -e "${GREEN}Created: ${APP_NAME}-${APP_VERSION}-linux.tar.gz${NC}"

# Create .deb package
echo -e "${YELLOW}Creating .deb package...${NC}"

# Create Debian control structure
DEB_DIR="$BUILD_DIR/${APP_NAME}-deb"
mkdir -p "$DEB_DIR"/{DEBIAN,usr/share/java-text-editor,usr/bin,usr/share/applications,usr/share/icons/hicolor/48x48/apps}

# Copy files to proper Debian locations
cp -r "$APP_DIR/bin" "$DEB_DIR/usr/share/java-text-editor/"
if [ -d "$APP_DIR/lib" ]; then
    cp -r "$APP_DIR/lib" "$DEB_DIR/usr/share/java-text-editor/"
fi
cp "$APP_DIR/java-text-editor" "$DEB_DIR/usr/share/java-text-editor/"
cp "$APP_DIR/usr/bin/java-text-editor" "$DEB_DIR/usr/bin/"
cp "$APP_DIR/share/applications/java-text-editor.desktop" "$DEB_DIR/usr/share/applications/"
cp "$APP_DIR/share/icons/hicolor/48x48/apps/java-text-editor.svg" "$DEB_DIR/usr/share/icons/hicolor/48x48/apps/"

# Create control file
cat > "$DEB_DIR/DEBIAN/control" << EOF
Package: $APP_NAME
Version: $APP_VERSION
Section: text
Priority: optional
Architecture: all
Depends: openjdk-8-jre | openjdk-11-jre | openjdk-17-jre | openjdk-21-jre | default-jre
Maintainer: $APP_MAINTAINER
Description: $APP_DESCRIPTION
 A modern, feature-rich text editor built with Java Swing, designed for
 programmers and power users. Features professional icons powered by 
 Ikonli with automatic fallback support.
 .
 Features include:
 - Professional FontAwesome and Material Design icons
 - Advanced find and replace with regex support
 - Undo/redo functionality
 - Line numbers with toggle support
 - Cross-platform compatibility
 - Modern user interface with native look and feel
Homepage: $APP_HOMEPAGE
EOF

# Create postinst script
cat > "$DEB_DIR/DEBIAN/postinst" << 'EOF'
#!/bin/bash
set -e

# Update desktop database
if command -v update-desktop-database >/dev/null 2>&1; then
    update-desktop-database -q /usr/share/applications
fi

# Update icon cache
if command -v gtk-update-icon-cache >/dev/null 2>&1; then
    gtk-update-icon-cache -q /usr/share/icons/hicolor
fi

echo "Java Text Editor has been installed successfully!"
echo "You can launch it from the applications menu or run 'java-text-editor' from terminal."
EOF

# Create prerm script
cat > "$DEB_DIR/DEBIAN/prerm" << 'EOF'
#!/bin/bash
set -e

# Update desktop database
if command -v update-desktop-database >/dev/null 2>&1; then
    update-desktop-database -q /usr/share/applications
fi

# Update icon cache
if command -v gtk-update-icon-cache >/dev/null 2>&1; then
    gtk-update-icon-cache -q /usr/share/icons/hicolor
fi
EOF

chmod +x "$DEB_DIR/DEBIAN/postinst" "$DEB_DIR/DEBIAN/prerm"

# Calculate installed size
INSTALLED_SIZE=$(du -sk "$DEB_DIR" | cut -f1)
echo "Installed-Size: $INSTALLED_SIZE" >> "$DEB_DIR/DEBIAN/control"

# Build .deb package
if command -v dpkg-deb >/dev/null 2>&1; then
    dpkg-deb --build "$DEB_DIR" "$DIST_DIR/${APP_NAME}-${APP_VERSION}_all.deb"
    echo -e "${GREEN}Created: ${APP_NAME}-${APP_VERSION}_all.deb${NC}"
else
    echo -e "${YELLOW}Warning: dpkg-deb not found, skipping .deb creation${NC}"
    echo -e "${YELLOW}Install dpkg-dev to create .deb packages${NC}"
fi

# Create installation instructions
cat > "$DIST_DIR/INSTALL-LINUX.md" << EOF
# Installation Instructions - Linux

## Requirements
- Java 8 or higher (OpenJDK or Oracle JDK)
- Linux distribution with desktop environment

## Installation Options

### Option 1: .deb Package (Ubuntu/Debian)
\`\`\`bash
sudo dpkg -i ${APP_NAME}-${APP_VERSION}_all.deb
sudo apt-get install -f  # Fix dependencies if needed
\`\`\`

### Option 2: .tar.gz Archive (All Linux Distributions)
\`\`\`bash
tar -xzf ${APP_NAME}-${APP_VERSION}-linux.tar.gz
cd ${APP_NAME}
sudo mkdir -p /usr/share/java-text-editor
sudo cp -r * /usr/share/java-text-editor/
sudo cp usr/bin/java-text-editor /usr/bin/
sudo cp share/applications/java-text-editor.desktop /usr/share/applications/
sudo cp share/icons/hicolor/48x48/apps/java-text-editor.svg /usr/share/icons/hicolor/48x48/apps/
sudo update-desktop-database
sudo gtk-update-icon-cache /usr/share/icons/hicolor
\`\`\`

## Usage
- Launch from applications menu: "Java Text Editor"
- Launch from terminal: \`java-text-editor\`
- Open files: \`java-text-editor filename.txt\`

## Uninstallation

### .deb Package
\`\`\`bash
sudo apt-get remove $APP_NAME
\`\`\`

### .tar.gz Installation
\`\`\`bash
sudo rm -rf /usr/share/java-text-editor
sudo rm /usr/bin/java-text-editor
sudo rm /usr/share/applications/java-text-editor.desktop
sudo rm /usr/share/icons/hicolor/48x48/apps/java-text-editor.svg
sudo update-desktop-database
sudo gtk-update-icon-cache /usr/share/icons/hicolor
\`\`\`

## Features
- Professional icons with FontAwesome and Material Design
- Advanced find and replace functionality
- Line numbers with toggle support
- Undo/redo operations
- Cross-platform Java application
- Native system integration

For more information, visit: $APP_HOMEPAGE
EOF

# Display results
echo
echo -e "${BLUE}=== Linux Packaging Complete ===${NC}"
echo -e "${GREEN}Packages created in: $DIST_DIR${NC}"
echo
ls -la "$DIST_DIR"
echo
echo -e "${BLUE}Installation files:${NC}"
if [ -f "$DIST_DIR/${APP_NAME}-${APP_VERSION}_all.deb" ]; then
    echo -e "  📦 ${GREEN}${APP_NAME}-${APP_VERSION}_all.deb${NC} (Debian/Ubuntu package)"
fi
echo -e "  📦 ${GREEN}${APP_NAME}-${APP_VERSION}-linux.tar.gz${NC} (Universal Linux archive)"
echo -e "  📖 ${GREEN}INSTALL-LINUX.md${NC} (Installation instructions)"
echo
echo -e "${BLUE}Ready for distribution! 🚀${NC}"
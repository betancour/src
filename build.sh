#!/bin/bash

# Build script for Java Text Editor
# This script compiles the Java source files and organizes them properly

set -e  # Exit on any error

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Project directories
SRC_DIR="."
BIN_DIR="bin"
EDITOR_PKG="editor"

echo -e "${BLUE}=== Java Text Editor Build Script ===${NC}"
echo -e "${YELLOW}Cleaning previous build...${NC}"

# Clean previous build
if [ -d "$BIN_DIR" ]; then
    rm -rf "$BIN_DIR"
fi

# Create bin directory
mkdir -p "$BIN_DIR"

echo -e "${YELLOW}Compiling Java source files...${NC}"

# Find all Java files and compile them
find "$SRC_DIR/$EDITOR_PKG" -name "*.java" > sources.tmp

if [ ! -s sources.tmp ]; then
    echo -e "${RED}Error: No Java source files found!${NC}"
    rm -f sources.tmp
    exit 1
fi

# Compile all Java files
javac -d "$BIN_DIR" -sourcepath "$SRC_DIR" @sources.tmp

# Check if compilation was successful
if [ $? -eq 0 ]; then
    echo -e "${GREEN}Compilation successful!${NC}"
    
    # Count compiled classes
    CLASS_COUNT=$(find "$BIN_DIR" -name "*.class" | wc -l)
    echo -e "${GREEN}Created $CLASS_COUNT class files${NC}"
    
    echo -e "${BLUE}Build structure:${NC}"
    echo "  src/           - Source files"
    echo "  src/bin/       - Compiled classes"
    echo "  src/resources/ - Resources"
    echo "  src/docs/      - Documentation"
    
    echo -e "${GREEN}To run the application:${NC}"
    echo "  cd src && java -cp bin editor.Main"
    
else
    echo -e "${RED}Compilation failed!${NC}"
    exit 1
fi

# Clean up temporary file
rm -f sources.tmp

echo -e "${GREEN}Build complete!${NC}"
#!/bin/bash

# Run script for Java Text Editor
# This script runs the compiled Java application

set -e  # Exit on any error

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Project directories
BIN_DIR="bin"
MAIN_CLASS="editor.Main"

# Build classpath with Ikonli dependencies (cross-platform)
CLASSPATH="$BIN_DIR"
SEPARATOR=":"

# Detect Windows environment for classpath separator
case "$(uname -s 2>/dev/null || echo 'Windows')" in
    CYGWIN*|MINGW32*|MSYS*|MINGW*) SEPARATOR=";" ;;
esac

if [ -d "lib" ] && [ "$(ls -A lib/*.jar 2>/dev/null)" ]; then
    echo -e "${YELLOW}Including Ikonli dependencies...${NC}"
    for jar in lib/*.jar; do
        CLASSPATH="$CLASSPATH$SEPARATOR$jar"
    done
fi

echo -e "${BLUE}=== Java Text Editor Runner ===${NC}"

# Check if bin directory exists
if [ ! -d "$BIN_DIR" ]; then
    echo -e "${RED}Error: Compiled classes not found!${NC}"
    echo -e "${YELLOW}Please run the build script first:${NC}"
    echo "  ./build.sh"
    exit 1
fi

# Check if Main class exists
if [ ! -f "$BIN_DIR/editor/Main.class" ]; then
    echo -e "${RED}Error: Main class not found!${NC}"
    echo -e "${YELLOW}Please run the build script first:${NC}"
    echo "  ./build.sh"
    exit 1
fi

echo -e "${GREEN}Starting Java Text Editor...${NC}"

# Run the application
java -cp "$CLASSPATH" "$MAIN_CLASS"

echo -e "${BLUE}Application closed.${NC}"
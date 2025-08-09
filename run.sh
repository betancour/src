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
java -cp "$BIN_DIR" "$MAIN_CLASS"

echo -e "${BLUE}Application closed.${NC}"
#!/bin/bash

# Build script for Java Text Editor with Unit Test Support
# This script compiles both main source files and test files

set -e  # Exit on any error

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Detect OS for cross-platform compatibility
OS="$(uname -s 2>/dev/null || echo 'Windows')"
case "$OS" in
    CYGWIN*|MINGW32*|MSYS*|MINGW*) OS='Windows' ;;
    Darwin) OS='macOS' ;;
    Linux) OS='Linux' ;;
esac

# Set classpath separator based on OS
SEPARATOR=":"
if [ "$OS" = "Windows" ]; then
    SEPARATOR=";"
fi

# Project directories
SRC_DIR="."
TEST_SRC_DIR="test"
BIN_DIR="bin"
TEST_BIN_DIR="test-bin"
EDITOR_PKG="editor"
TEST_PKG="test"

echo -e "${BLUE}=== Java Text Editor Build Script (with Tests) ===${NC}"
echo -e "${YELLOW}Cleaning previous build...${NC}"

# Clean previous build
if [ -d "$BIN_DIR" ]; then
    rm -rf "$BIN_DIR"
fi
if [ -d "$TEST_BIN_DIR" ]; then
    rm -rf "$TEST_BIN_DIR"
fi

# Create bin directories
mkdir -p "$BIN_DIR"
mkdir -p "$TEST_BIN_DIR"

echo -e "${YELLOW}Building main application...${NC}"

# Find all Java files and compile them
find "$SRC_DIR/$EDITOR_PKG" -name "*.java" > sources.tmp

if [ ! -s sources.tmp ]; then
    echo -e "${RED}Error: No Java source files found!${NC}"
    rm -f sources.tmp
    exit 1
fi

# Build classpath with Ikonli dependencies (cross-platform)
MAIN_CLASSPATH=""
if [ -d "lib" ] && [ "$(ls -A lib/*.jar 2>/dev/null)" ]; then
    echo -e "${YELLOW}Including Ikonli dependencies...${NC}"
    for jar in lib/*.jar; do
        if [ -z "$MAIN_CLASSPATH" ]; then
            MAIN_CLASSPATH="$jar"
        else
            MAIN_CLASSPATH="$MAIN_CLASSPATH$SEPARATOR$jar"
        fi
    done
    echo -e "${GREEN}Main Classpath: $MAIN_CLASSPATH${NC}"
fi

# Compile main application
if [ -n "$MAIN_CLASSPATH" ]; then
    javac -cp "$MAIN_CLASSPATH" -d "$BIN_DIR" -sourcepath "$SRC_DIR" @sources.tmp
else
    javac -d "$BIN_DIR" -sourcepath "$SRC_DIR" @sources.tmp
fi

# Check if main compilation was successful
if [ $? -eq 0 ]; then
    echo -e "${GREEN}Main application compilation successful!${NC}"
    
    # Count compiled classes
    MAIN_CLASS_COUNT=$(find "$BIN_DIR" -name "*.class" | wc -l)
    echo -e "${GREEN}Created $MAIN_CLASS_COUNT main class files${NC}"
else
    echo -e "${RED}Main compilation failed!${NC}"
    rm -f sources.tmp
    exit 1
fi

# Check for test dependencies
if [ ! -d "test-lib" ]; then
    echo -e "${YELLOW}Warning: test-lib directory not found${NC}"
    echo -e "${YELLOW}Run ./download-test-dependencies.sh to download JUnit 5${NC}"
    echo -e "${GREEN}Main application build complete (tests skipped)${NC}"
    rm -f sources.tmp
    exit 0
fi

echo -e "${YELLOW}Building unit tests...${NC}"

# Find all test Java files
find "$TEST_SRC_DIR" -name "*.java" > test-sources.tmp

if [ ! -s test-sources.tmp ]; then
    echo -e "${YELLOW}Warning: No test source files found!${NC}"
    echo -e "${GREEN}Main application build complete (no tests)${NC}"
    rm -f sources.tmp test-sources.tmp
    exit 0
fi

# Build test classpath
TEST_CLASSPATH="$BIN_DIR"
if [ -n "$MAIN_CLASSPATH" ]; then
    TEST_CLASSPATH="$TEST_CLASSPATH$SEPARATOR$MAIN_CLASSPATH"
fi

# Add test dependencies
if [ -d "test-lib" ] && [ "$(ls -A test-lib/*.jar 2>/dev/null)" ]; then
    echo -e "${YELLOW}Including JUnit 5 test dependencies...${NC}"
    for jar in test-lib/*.jar; do
        TEST_CLASSPATH="$TEST_CLASSPATH$SEPARATOR$jar"
    done
    echo -e "${GREEN}Test Classpath: $TEST_CLASSPATH${NC}"
fi

# Compile test files
javac -cp "$TEST_CLASSPATH" -d "$TEST_BIN_DIR" -sourcepath "$TEST_SRC_DIR" @test-sources.tmp

# Check if test compilation was successful
if [ $? -eq 0 ]; then
    echo -e "${GREEN}Test compilation successful!${NC}"
    
    # Count compiled test classes
    TEST_CLASS_COUNT=$(find "$TEST_BIN_DIR" -name "*.class" | wc -l)
    echo -e "${GREEN}Created $TEST_CLASS_COUNT test class files${NC}"
else
    echo -e "${RED}Test compilation failed!${NC}"
    rm -f sources.tmp test-sources.tmp
    exit 1
fi

echo -e "${BLUE}Build structure:${NC}"
echo "  src/           - Source files"
echo "  src/bin/       - Compiled main classes"
echo "  src/test-bin/  - Compiled test classes"
echo "  src/lib/       - Ikonli dependencies"
echo "  src/test-lib/  - JUnit 5 dependencies"

echo -e "${GREEN}To run the application:${NC}"
if [ -n "$MAIN_CLASSPATH" ]; then
    echo "  cd src && java -cp bin$SEPARATOR$MAIN_CLASSPATH editor.Main"
else
    echo "  cd src && java -cp bin editor.Main"
fi

echo -e "${GREEN}To run the tests:${NC}"
echo "  cd src && ./run-tests.sh"

echo -e "${GREEN}To run icon tests:${NC}"
echo "  cd src && ./test-icons.sh"

# Clean up temporary files
rm -f sources.tmp test-sources.tmp

echo -e "${GREEN}Build complete with tests!${NC}"
#!/bin/bash

# Test runner script for Java Text Editor
# This script runs all unit tests using JUnit 5

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
BIN_DIR="bin"
TEST_BIN_DIR="test-bin"

echo -e "${BLUE}=== Java Text Editor Test Runner ===${NC}"

# Check if test binaries exist
if [ ! -d "$TEST_BIN_DIR" ]; then
    echo -e "${RED}Error: Test classes not found!${NC}"
    echo -e "${YELLOW}Please run the build script first:${NC}"
    echo "  ./build-with-tests.sh"
    exit 1
fi

# Check if main binaries exist
if [ ! -d "$BIN_DIR" ]; then
    echo -e "${RED}Error: Main classes not found!${NC}"
    echo -e "${YELLOW}Please run the build script first:${NC}"
    echo "  ./build-with-tests.sh"
    exit 1
fi

# Check if test dependencies exist
if [ ! -d "test-lib" ] || [ -z "$(ls -A test-lib/*.jar 2>/dev/null)" ]; then
    echo -e "${RED}Error: Test dependencies not found!${NC}"
    echo -e "${YELLOW}Please download test dependencies first:${NC}"
    echo "  ./download-test-dependencies.sh"
    echo "  ./build-with-tests.sh"
    exit 1
fi

echo -e "${YELLOW}Preparing test environment...${NC}"

# Build test classpath
TEST_CLASSPATH="$TEST_BIN_DIR$SEPARATOR$BIN_DIR"

# Add main application dependencies (Ikonli)
if [ -d "lib" ] && [ "$(ls -A lib/*.jar 2>/dev/null)" ]; then
    echo -e "${YELLOW}Including Ikonli dependencies...${NC}"
    for jar in lib/*.jar; do
        TEST_CLASSPATH="$TEST_CLASSPATH$SEPARATOR$jar"
    done
fi

# Add test dependencies (JUnit 5)
if [ -d "test-lib" ] && [ "$(ls -A test-lib/*.jar 2>/dev/null)" ]; then
    echo -e "${YELLOW}Including JUnit 5 dependencies...${NC}"
    for jar in test-lib/*.jar; do
        TEST_CLASSPATH="$TEST_CLASSPATH$SEPARATOR$jar"
    done
fi

echo -e "${GREEN}Running unit tests...${NC}"
echo

# Run tests using JUnit Platform Console Launcher
if [ -f "test-lib/junit-platform-console-standalone-1.10.1.jar" ]; then
    # Use standalone console launcher if available
    java -jar test-lib/junit-platform-console-standalone-1.10.1.jar \
        --classpath="$TEST_CLASSPATH" \
        --scan-classpath \
        --reports-dir=test-reports \
        --details=summary
else
    # Fallback to custom test runner
    java -cp "$TEST_CLASSPATH" test.TestSuite
fi

TEST_RESULT=$?

echo
if [ $TEST_RESULT -eq 0 ]; then
    echo -e "${GREEN}=== All tests passed! ===${NC}"
    echo -e "${BLUE}Your Java Text Editor is working correctly.${NC}"
else
    echo -e "${RED}=== Some tests failed! ===${NC}"
    echo -e "${YELLOW}Please check the test output above for details.${NC}"
fi

echo
echo -e "${BLUE}Additional test options:${NC}"
echo "  ./test-icons.sh          - Test icon system specifically"
echo "  ./run.sh                 - Run the application manually"
echo "  ./build-with-tests.sh    - Rebuild if needed"

echo
if [ $TEST_RESULT -eq 0 ]; then
    echo -e "${GREEN}✅ Test suite completed successfully!${NC}"
else
    echo -e "${RED}❌ Test suite completed with failures.${NC}"
fi

exit $TEST_RESULT
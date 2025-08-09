#!/bin/bash

# Download JUnit 5 testing dependencies for Java Text Editor
# This script downloads the necessary JAR files for unit testing

set -e  # Exit on any error

# Detect OS for cross-platform compatibility
OS="$(uname -s 2>/dev/null || echo 'Windows')"
case "$OS" in
    CYGWIN*|MINGW32*|MSYS*|MINGW*) OS='Windows' ;;
    Darwin) OS='macOS' ;;
    Linux) OS='Linux' ;;
esac

# Colors for output (only on Unix-like systems)
if [ "$OS" != "Windows" ]; then
    RED='\033[0;31m'
    GREEN='\033[0;32m'
    YELLOW='\033[1;33m'
    BLUE='\033[0;34m'
    NC='\033[0m' # No Color
else
    RED=''
    GREEN=''
    YELLOW=''
    BLUE=''
    NC=''
fi

# Directories
LIB_DIR="lib"
TEST_LIB_DIR="test-lib"
MAVEN_BASE="https://repo1.maven.org/maven2"

# JUnit 5 version
JUNIT_VERSION="5.10.1"
JUNIT_PLATFORM_VERSION="1.10.1"

printf "${BLUE}=== Downloading JUnit 5 Testing Dependencies ===${NC}\n"

# Create lib directories if they don't exist
mkdir -p "$LIB_DIR"
mkdir -p "$TEST_LIB_DIR"

# Define test dependencies
declare -a TEST_DEPENDENCIES=(
    "org/junit/jupiter/junit-jupiter-engine/${JUNIT_VERSION}/junit-jupiter-engine-${JUNIT_VERSION}.jar"
    "org/junit/jupiter/junit-jupiter-api/${JUNIT_VERSION}/junit-jupiter-api-${JUNIT_VERSION}.jar"
    "org/junit/jupiter/junit-jupiter-params/${JUNIT_VERSION}/junit-jupiter-params-${JUNIT_VERSION}.jar"
    "org/junit/platform/junit-platform-engine/${JUNIT_PLATFORM_VERSION}/junit-platform-engine-${JUNIT_PLATFORM_VERSION}.jar"
    "org/junit/platform/junit-platform-launcher/${JUNIT_PLATFORM_VERSION}/junit-platform-launcher-${JUNIT_PLATFORM_VERSION}.jar"
    "org/junit/platform/junit-platform-console-standalone/${JUNIT_PLATFORM_VERSION}/junit-platform-console-standalone-${JUNIT_PLATFORM_VERSION}.jar"
    "org/opentest4j/opentest4j/1.3.0/opentest4j-1.3.0.jar"
    "org/apiguardian/apiguardian-api/1.1.2/apiguardian-api-1.1.2.jar"
)

# Cross-platform basename function
get_filename() {
    case "$1" in
        */*) echo "${1##*/}" ;;
        *) echo "$1" ;;
    esac
}

# Download each test dependency
for dep in "${TEST_DEPENDENCIES[@]}"; do
    filename=$(get_filename "$dep")
    url="${MAVEN_BASE}/${dep}"
    
    printf "${YELLOW}Downloading ${filename}...${NC}\n"
    
    # Try different download tools based on availability and OS
    if command -v curl >/dev/null 2>&1; then
        if [ "$OS" = "Windows" ]; then
            curl.exe -L -o "${TEST_LIB_DIR}/${filename}" "$url" 2>/dev/null || curl -L -o "${TEST_LIB_DIR}/${filename}" "$url"
        else
            curl -L -o "${TEST_LIB_DIR}/${filename}" "$url"
        fi
    elif command -v wget >/dev/null 2>&1; then
        if [ "$OS" = "Windows" ]; then
            wget.exe -O "${TEST_LIB_DIR}/${filename}" "$url" 2>/dev/null || wget -O "${TEST_LIB_DIR}/${filename}" "$url"
        else
            wget -O "${TEST_LIB_DIR}/${filename}" "$url"
        fi
    elif [ "$OS" = "Windows" ] && command -v powershell >/dev/null 2>&1; then
        powershell -Command "Invoke-WebRequest -Uri '$url' -OutFile '${TEST_LIB_DIR}/${filename}'"
    elif [ "$OS" = "Windows" ] && command -v powershell.exe >/dev/null 2>&1; then
        powershell.exe -Command "Invoke-WebRequest -Uri '$url' -OutFile '${TEST_LIB_DIR}/${filename}'"
    else
        printf "${RED}Error: No download tool found. Please install curl, wget, or PowerShell.${NC}\n"
        printf "Available options:${NC}\n"
        printf "  - curl (recommended for all platforms)${NC}\n"
        printf "  - wget (Linux/macOS/WSL)${NC}\n"
        printf "  - PowerShell (Windows)${NC}\n"
        exit 1
    fi
    
    if [ $? -eq 0 ] && [ -f "${TEST_LIB_DIR}/${filename}" ]; then
        printf "${GREEN}✓ Downloaded ${filename}${NC}\n"
    else
        printf "${RED}✗ Failed to download ${filename}${NC}\n"
        exit 1
    fi
done

printf "${GREEN}=== All test dependencies downloaded successfully! ===${NC}\n"
printf "${BLUE}Downloaded files:${NC}\n"

# Cross-platform file listing
if [ "$OS" = "Windows" ] && ! command -v ls >/dev/null 2>&1; then
    dir /b "$TEST_LIB_DIR\*.jar" 2>/dev/null || echo "JAR files in $TEST_LIB_DIR directory"
else
    ls -la "$TEST_LIB_DIR"/*.jar 2>/dev/null || echo "JAR files downloaded to $TEST_LIB_DIR"
fi

printf "\n${BLUE}Next steps:${NC}\n"
if [ "$OS" = "Windows" ]; then
    echo "1. Run build-with-tests.bat to compile with test dependencies"
    echo "2. Run run-tests.bat to execute the test suite"
else
    echo "1. Run ./build-with-tests.sh to compile with test dependencies"
    echo "2. Run ./run-tests.sh to execute the test suite"
fi
echo "3. Tests will verify icon system, UI components, and core functionality"
echo "4. Test results will be displayed with pass/fail summary"

printf "\n${GREEN}JUnit 5 testing framework ready!${NC}\n"
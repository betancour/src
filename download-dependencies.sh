#!/bin/bash

# Download Ikonli dependencies for Swing application
# This script downloads the necessary JAR files for icon support

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
MAVEN_BASE="https://repo1.maven.org/maven2"

# Ikonli version
IKONLI_VERSION="12.3.1"

echo -e "${BLUE}=== Downloading Ikonli-Swing Dependencies ===${NC}"

# Create lib directory if it doesn't exist
mkdir -p "$LIB_DIR"

# Define dependencies
declare -a DEPENDENCIES=(
    "org/kordamp/ikonli/ikonli-core/${IKONLI_VERSION}/ikonli-core-${IKONLI_VERSION}.jar"
    "org/kordamp/ikonli/ikonli-swing/${IKONLI_VERSION}/ikonli-swing-${IKONLI_VERSION}.jar"
    "org/kordamp/ikonli/ikonli-fontawesome5-pack/${IKONLI_VERSION}/ikonli-fontawesome5-pack-${IKONLI_VERSION}.jar"
    "org/kordamp/ikonli/ikonli-material2-pack/${IKONLI_VERSION}/ikonli-material2-pack-${IKONLI_VERSION}.jar"
)

# Cross-platform basename function
get_filename() {
    case "$1" in
        */*) echo "${1##*/}" ;;
        *) echo "$1" ;;
    esac
}

# Download each dependency
for dep in "${DEPENDENCIES[@]}"; do
    filename=$(get_filename "$dep")
    url="${MAVEN_BASE}/${dep}"
    
    printf "${YELLOW}Downloading ${filename}...${NC}\n"
    
    # Try different download tools based on availability and OS
    if command -v curl >/dev/null 2>&1; then
        if [ "$OS" = "Windows" ]; then
            curl.exe -L -o "${LIB_DIR}/${filename}" "$url" 2>/dev/null || curl -L -o "${LIB_DIR}/${filename}" "$url"
        else
            curl -L -o "${LIB_DIR}/${filename}" "$url"
        fi
    elif command -v wget >/dev/null 2>&1; then
        if [ "$OS" = "Windows" ]; then
            wget.exe -O "${LIB_DIR}/${filename}" "$url" 2>/dev/null || wget -O "${LIB_DIR}/${filename}" "$url"
        else
            wget -O "${LIB_DIR}/${filename}" "$url"
        fi
    elif [ "$OS" = "Windows" ] && command -v powershell >/dev/null 2>&1; then
        powershell -Command "Invoke-WebRequest -Uri '$url' -OutFile '${LIB_DIR}/${filename}'"
    elif [ "$OS" = "Windows" ] && command -v powershell.exe >/dev/null 2>&1; then
        powershell.exe -Command "Invoke-WebRequest -Uri '$url' -OutFile '${LIB_DIR}/${filename}'"
    else
        printf "${RED}Error: No download tool found. Please install curl, wget, or PowerShell.${NC}\n"
        printf "Available options:${NC}\n"
        printf "  - curl (recommended for all platforms)${NC}\n"
        printf "  - wget (Linux/macOS/WSL)${NC}\n"
        printf "  - PowerShell (Windows)${NC}\n"
        exit 1
    fi
    
    if [ $? -eq 0 ] && [ -f "${LIB_DIR}/${filename}" ]; then
        printf "${GREEN}✓ Downloaded ${filename}${NC}\n"
    else
        printf "${RED}✗ Failed to download ${filename}${NC}\n"
        exit 1
    fi
done

printf "${GREEN}=== All dependencies downloaded successfully! ===${NC}\n"
printf "${BLUE}Downloaded files:${NC}\n"

# Cross-platform file listing
if [ "$OS" = "Windows" ] && ! command -v ls >/dev/null 2>&1; then
    dir /b "$LIB_DIR\*.jar" 2>/dev/null || echo "JAR files in $LIB_DIR directory"
else
    ls -la "$LIB_DIR"/*.jar 2>/dev/null || echo "JAR files downloaded to $LIB_DIR"
fi

printf "\n${BLUE}Next steps:${NC}\n"
if [ "$OS" = "Windows" ]; then
    echo "1. Run build.bat to compile with new dependencies"
    echo "2. Run run.bat to start the application"
else
    echo "1. Run ./build.sh to compile with new dependencies"
    echo "2. Run ./run.sh to start the application"
fi
echo "3. The SystemIconHelper will now have access to professional icons"
echo "4. Available icon packs: FontAwesome5, Material Design 2"

printf "\n${GREEN}Ikonli-Swing integration ready!${NC}\n"
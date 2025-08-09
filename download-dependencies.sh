#!/bin/bash

# Download Ikonli dependencies for Swing application
# This script downloads the necessary JAR files for icon support

set -e  # Exit on any error

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

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

# Download each dependency
for dep in "${DEPENDENCIES[@]}"; do
    filename=$(basename "$dep")
    url="${MAVEN_BASE}/${dep}"
    
    echo -e "${YELLOW}Downloading ${filename}...${NC}"
    
    if command -v curl >/dev/null 2>&1; then
        curl -L -o "${LIB_DIR}/${filename}" "$url"
    elif command -v wget >/dev/null 2>&1; then
        wget -O "${LIB_DIR}/${filename}" "$url"
    else
        echo -e "${RED}Error: Neither curl nor wget found. Please install one of them.${NC}"
        exit 1
    fi
    
    if [ $? -eq 0 ]; then
        echo -e "${GREEN}✓ Downloaded ${filename}${NC}"
    else
        echo -e "${RED}✗ Failed to download ${filename}${NC}"
        exit 1
    fi
done

echo -e "${GREEN}=== All dependencies downloaded successfully! ===${NC}"
echo -e "${BLUE}Downloaded files:${NC}"
ls -la "$LIB_DIR"/*.jar

echo -e "\n${BLUE}Next steps:${NC}"
echo "1. Run ./build.sh to compile with new dependencies"
echo "2. The SystemIconHelper will now have access to professional icons"
echo "3. Available icon packs: FontAwesome5, Material Design 2"

echo -e "\n${GREEN}Ikonli-Swing integration ready!${NC}"
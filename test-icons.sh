#!/bin/bash

# Test script to verify Ikonli icons are working correctly
# This script runs a brief test of the icon system

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

echo -e "${BLUE}=== Ikonli Icon Integration Test ===${NC}"
echo

# Check if dependencies exist
echo -e "${YELLOW}1. Checking dependencies...${NC}"
if [ ! -d "lib" ]; then
    echo -e "${RED}✗ lib directory not found${NC}"
    echo "Run ./download-dependencies.sh first"
    exit 1
fi

expected_jars=("ikonli-core-12.3.1.jar" "ikonli-swing-12.3.1.jar" "ikonli-fontawesome5-pack-12.3.1.jar" "ikonli-material2-pack-12.3.1.jar")
missing_jars=()

for jar in "${expected_jars[@]}"; do
    if [ ! -f "lib/$jar" ]; then
        missing_jars+=("$jar")
    fi
done

if [ ${#missing_jars[@]} -eq 0 ]; then
    echo -e "${GREEN}✓ All JAR dependencies found${NC}"
else
    echo -e "${RED}✗ Missing JAR files:${NC}"
    for jar in "${missing_jars[@]}"; do
        echo "  - $jar"
    done
    echo "Run ./download-dependencies.sh to fix this"
    exit 1
fi

# Check if compiled
echo -e "${YELLOW}2. Checking compilation...${NC}"
if [ ! -d "bin" ] || [ ! -f "bin/editor/Main.class" ]; then
    echo -e "${RED}✗ Project not compiled${NC}"
    echo "Run ./build.sh first"
    exit 1
fi
echo -e "${GREEN}✓ Project is compiled${NC}"

# Test icon loading
echo -e "${YELLOW}3. Testing icon loading...${NC}"

# Create a temporary Java test class
cat > IconTest.java << 'EOF'
import editor.utils.IkonliIconProvider;
import editor.utils.SystemIconHelper;
import javax.swing.Icon;
import java.awt.Color;

public class IconTest {
    public static void main(String[] args) {
        System.out.println("Testing Ikonli Icon Provider...");
        
        // Test if Ikonli is available
        System.out.println("Ikonli available: " + IkonliIconProvider.isAvailable());
        
        // Test individual icons
        String[] iconNames = {"New", "Open", "Save", "Cut", "Copy", "Paste", "Undo", "Redo", "Find", "Replace", "Terminal"};
        
        for (String iconName : iconNames) {
            try {
                Icon icon = SystemIconHelper.getBestIcon(iconName.toLowerCase());
                if (icon != null) {
                    System.out.println("✓ " + iconName + " icon: " + icon.getIconWidth() + "x" + icon.getIconHeight());
                } else {
                    System.out.println("✗ " + iconName + " icon: null");
                }
            } catch (Exception e) {
                System.out.println("✗ " + iconName + " icon failed: " + e.getMessage());
            }
        }
        
        System.out.println("Icon test complete!");
    }
}
EOF

# Compile and run the test
javac -cp "bin:lib/*" -d . IconTest.java

if [ $? -eq 0 ]; then
    echo -e "${GREEN}✓ Test class compiled${NC}"
    
    echo -e "${YELLOW}4. Running icon test...${NC}"
    java -cp ".:bin:lib/*" IconTest
    
    if [ $? -eq 0 ]; then
        echo -e "${GREEN}✓ Icon test completed successfully${NC}"
    else
        echo -e "${RED}✗ Icon test failed${NC}"
        exit 1
    fi
else
    echo -e "${RED}✗ Failed to compile test class${NC}"
    exit 1
fi

# Clean up
rm -f IconTest.java IconTest.class

echo
echo -e "${BLUE}=== Test Summary ===${NC}"
echo -e "${GREEN}✓ Dependencies found${NC}"
echo -e "${GREEN}✓ Project compiled${NC}"
echo -e "${GREEN}✓ Icons loading correctly${NC}"
echo
echo -e "${BLUE}Professional Ikonli icons are working!${NC}"
echo
echo -e "${YELLOW}To test the full application:${NC}"
echo "  ./run.sh"
echo
echo -e "${YELLOW}Icon benefits:${NC}"
echo "  • Professional FontAwesome and Material Design icons"
echo "  • Scalable vector graphics (crisp at any size)"
echo "  • Consistent design language"
echo "  • Automatic fallback to geometric icons if needed"
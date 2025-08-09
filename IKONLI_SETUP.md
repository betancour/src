# Ikonli Integration Setup Guide

This guide explains how to integrate **Ikonli-Swing** professional icons into the Java Text Editor.

## What is Ikonli?

Ikonli is a Java library that provides access to professional icon fonts like FontAwesome, Material Design, and many others. It allows us to use crisp, scalable vector icons instead of custom-drawn geometric shapes.

## Setup Instructions

### 1. Download Dependencies

Run the dependency download script:

```bash
./download-dependencies.sh
```

This will download the following JAR files to the `lib/` directory:
- `ikonli-core-12.3.1.jar` - Core Ikonli functionality
- `ikonli-swing-12.3.1.jar` - Swing integration
- `ikonli-fontawesome5-pack-12.3.1.jar` - FontAwesome 5 icon pack
- `ikonli-material2-pack-12.3.1.jar` - Material Design 2 icon pack

### 2. Build with Dependencies

The build script has been updated to automatically include Ikonli dependencies:

```bash
./build.sh
```

### 3. Run the Application

The run script will automatically include the dependencies in the classpath:

```bash
./run.sh
```

## Icon Mapping

When Ikonli is available, the following professional icons will be used:

| Operation | Fallback Icon | Ikonli Icon | Icon Pack |
|-----------|---------------|-------------|-----------|
| New File | Text "N" | FILE | FontAwesome |
| Open File | Folder | FOLDER_OPEN | FontAwesome |
| Save File | Text "S" | SAVE | FontAwesome |
| Cut | Geometric scissors | CUT (✂️) | FontAwesome |
| Copy | Geometric rectangles | COPY | FontAwesome |
| Paste | Geometric clipboard | PASTE | FontAwesome |
| Undo | Left arrow | UNDO_ALT | FontAwesome |
| Redo | Right arrow | REDO_ALT | FontAwesome |
| Find | Magnifying glass | SEARCH | FontAwesome |
| Replace | Double arrows | SEARCH_REPLACE | Material Design |
| Terminal | Command symbol | TERMINAL | FontAwesome |

## Benefits

✅ **Professional appearance** - Icons look like modern applications
✅ **Scalable vector graphics** - Crisp at any size
✅ **Consistent design** - All icons follow the same design language
✅ **Automatic fallback** - If Ikonli isn't available, geometric icons are used
✅ **No breaking changes** - Works with or without the library

## Troubleshooting

### Icons not loading?

1. Check that JAR files were downloaded:
   ```bash
   ls -la lib/*.jar
   ```

2. Verify console output when starting the application:
   - ✅ Success: `"✓ Ikonli-Swing loaded successfully - Using professional icons"`
   - ❌ Fallback: `"! Ikonli-Swing not found - Using fallback icons"`

### Download script fails?

Make sure you have either `curl` or `wget` installed:
```bash
# Check if curl is available
curl --version

# Or check if wget is available  
wget --version
```

### Build fails with classpath errors?

1. Clean and rebuild:
   ```bash
   rm -rf bin/
   ./build.sh
   ```

2. Check that all JAR files are present in `lib/`

## Manual Download (Alternative)

If the download script doesn't work, you can manually download the JAR files:

1. Go to [Maven Central](https://repo1.maven.org/maven2/org/kordamp/ikonli/)
2. Navigate to each package and version `12.3.1`:
   - `ikonli-core/12.3.1/ikonli-core-12.3.1.jar`
   - `ikonli-swing/12.3.1/ikonli-swing-12.3.1.jar` 
   - `ikonli-fontawesome5-pack/12.3.1/ikonli-fontawesome5-pack-12.3.1.jar`
   - `ikonli-material2-pack/12.3.1/ikonli-material2-pack-12.3.1.jar`
3. Place all JAR files in the `lib/` directory
4. Run `./build.sh` and `./run.sh`

## Architecture

The integration uses a **graceful degradation** approach:

1. **IkonliIconProvider** - Dynamically loads Ikonli classes using reflection
2. **SystemIconHelper** - First tries Ikonli, then falls back to system/geometric icons
3. **No compilation dependencies** - Code compiles whether Ikonli JARs are present or not
4. **Runtime detection** - Icons are chosen at runtime based on library availability

This ensures the application works perfectly with or without professional icons!
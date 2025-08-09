@echo off
rem Build script for Java Text Editor (Windows)
rem This script compiles the Java source files and organizes them properly

setlocal enabledelayedexpansion

rem Project directories
set SRC_DIR=.
set BIN_DIR=bin
set EDITOR_PKG=editor

echo === Java Text Editor Build Script ===
echo Cleaning previous build...

rem Clean previous build
if exist "%BIN_DIR%" (
    rmdir /s /q "%BIN_DIR%"
)

rem Create bin directory
mkdir "%BIN_DIR%"

echo Compiling Java source files...

rem Find all Java files and create sources list
dir /s /b "%SRC_DIR%\%EDITOR_PKG%\*.java" > sources.tmp

rem Check if Java files were found
for /f %%i in ("sources.tmp") do set SIZE=%%~zi
if %SIZE%==0 (
    echo Error: No Java source files found!
    del sources.tmp
    exit /b 1
)

rem Compile all Java files
javac -d "%BIN_DIR%" -sourcepath "%SRC_DIR%" @sources.tmp

rem Check if compilation was successful
if %errorlevel%==0 (
    echo Compilation successful!
    
    rem Count compiled classes
    for /f %%i in ('dir /s /b "%BIN_DIR%\*.class" ^| find /c ".class"') do set CLASS_COUNT=%%i
    echo Created !CLASS_COUNT! class files
    
    echo.
    echo Build structure:
    echo   src\           - Source files
    echo   src\bin\       - Compiled classes
    echo   src\resources\ - Resources
    echo   src\docs\      - Documentation
    echo.
    echo To run the application:
    echo   cd src ^&^& java -cp bin editor.Main
    echo.
    
) else (
    echo Compilation failed!
    del sources.tmp
    exit /b 1
)

rem Clean up temporary file
del sources.tmp

echo Build complete!
pause
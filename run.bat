@echo off
rem Run script for Java Text Editor (Windows)
rem This script runs the compiled Java application

setlocal enabledelayedexpansion

rem Project directories
set BIN_DIR=bin
set MAIN_CLASS=editor.Main

rem Build classpath with Ikonli dependencies
set "CLASSPATH=%BIN_DIR%"
if exist "lib" (
    echo Including Ikonli dependencies...
    for %%f in (lib\*.jar) do (
        set "CLASSPATH=!CLASSPATH!;%%f"
    )
)

echo === Java Text Editor Runner ===

rem Check if bin directory exists
if not exist "%BIN_DIR%" (
    echo Error: Compiled classes not found!
    echo Please run the build script first:
    echo   build.bat
    exit /b 1
)

rem Check if Main class exists
if not exist "%BIN_DIR%\editor\Main.class" (
    echo Error: Main class not found!
    echo Please run the build script first:
    echo   build.bat
    exit /b 1
)

echo Starting Java Text Editor...

rem Run the application
java -cp "%CLASSPATH%" "%MAIN_CLASS%"

echo Application closed.
pause
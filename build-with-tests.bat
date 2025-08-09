@echo off
REM Build script for Java Text Editor with Unit Test Support (Windows)
REM This script compiles both main source files and test files

setlocal enabledelayedexpansion

REM Colors for Windows (using echo with special chars)
set "RED=[91m"
set "GREEN=[92m"
set "YELLOW=[93m"
set "BLUE=[94m"
set "NC=[0m"

REM Project directories
set "SRC_DIR=."
set "TEST_SRC_DIR=test"
set "BIN_DIR=bin"
set "TEST_BIN_DIR=test-bin"
set "EDITOR_PKG=editor"
set "TEST_PKG=test"

echo %BLUE%=== Java Text Editor Build Script (with Tests) ===%NC%
echo %YELLOW%Cleaning previous build...%NC%

REM Clean previous build
if exist "%BIN_DIR%" rmdir /s /q "%BIN_DIR%"
if exist "%TEST_BIN_DIR%" rmdir /s /q "%TEST_BIN_DIR%"

REM Create bin directories
mkdir "%BIN_DIR%"
mkdir "%TEST_BIN_DIR%"

echo %YELLOW%Building main application...%NC%

REM Find all Java files and create sources list
dir /s /b "%SRC_DIR%\%EDITOR_PKG%\*.java" > sources.tmp

REM Check if Java files were found
for /f %%i in ("sources.tmp") do set SIZE=%%~zi
if %SIZE%==0 (
    echo %RED%Error: No Java source files found!%NC%
    del sources.tmp
    exit /b 1
)

REM Build classpath with Ikonli dependencies
set "MAIN_CLASSPATH="
if exist "lib" (
    echo %YELLOW%Including Ikonli dependencies...%NC%
    for %%f in (lib\*.jar) do (
        if defined MAIN_CLASSPATH (
            set "MAIN_CLASSPATH=!MAIN_CLASSPATH!;%%f"
        ) else (
            set "MAIN_CLASSPATH=%%f"
        )
    )
    echo %GREEN%Main Classpath: !MAIN_CLASSPATH!%NC%
)

REM Compile main application
if defined MAIN_CLASSPATH (
    javac -cp "!MAIN_CLASSPATH!" -d "%BIN_DIR%" -sourcepath "%SRC_DIR%" @sources.tmp
) else (
    javac -d "%BIN_DIR%" -sourcepath "%SRC_DIR%" @sources.tmp
)

REM Check if main compilation was successful
if %errorlevel%==0 (
    echo %GREEN%Main application compilation successful!%NC%
    
    REM Count compiled classes
    for /f %%i in ('dir /s /b "%BIN_DIR%\*.class" ^| find /c ".class"') do set MAIN_CLASS_COUNT=%%i
    echo %GREEN%Created !MAIN_CLASS_COUNT! main class files%NC%
) else (
    echo %RED%Main compilation failed!%NC%
    del sources.tmp
    exit /b 1
)

REM Check for test dependencies
if not exist "test-lib" (
    echo %YELLOW%Warning: test-lib directory not found%NC%
    echo %YELLOW%Run download-test-dependencies.bat to download JUnit 5%NC%
    echo %GREEN%Main application build complete (tests skipped)%NC%
    del sources.tmp
    exit /b 0
)

echo %YELLOW%Building unit tests...%NC%

REM Find all test Java files
dir /s /b "%TEST_SRC_DIR%\*.java" > test-sources.tmp

REM Check if test files were found
for /f %%i in ("test-sources.tmp") do set TEST_SIZE=%%~zi
if %TEST_SIZE%==0 (
    echo %YELLOW%Warning: No test source files found!%NC%
    echo %GREEN%Main application build complete (no tests)%NC%
    del sources.tmp test-sources.tmp
    exit /b 0
)

REM Build test classpath
set "TEST_CLASSPATH=%BIN_DIR%"
if defined MAIN_CLASSPATH (
    set "TEST_CLASSPATH=!TEST_CLASSPATH!;!MAIN_CLASSPATH!"
)

REM Add test dependencies
if exist "test-lib" (
    echo %YELLOW%Including JUnit 5 test dependencies...%NC%
    for %%f in (test-lib\*.jar) do (
        set "TEST_CLASSPATH=!TEST_CLASSPATH!;%%f"
    )
    echo %GREEN%Test Classpath: !TEST_CLASSPATH!%NC%
)

REM Compile test files
javac -cp "!TEST_CLASSPATH!" -d "%TEST_BIN_DIR%" -sourcepath "%TEST_SRC_DIR%" @test-sources.tmp

REM Check if test compilation was successful
if %errorlevel%==0 (
    echo %GREEN%Test compilation successful!%NC%
    
    REM Count compiled test classes
    for /f %%i in ('dir /s /b "%TEST_BIN_DIR%\*.class" ^| find /c ".class"') do set TEST_CLASS_COUNT=%%i
    echo %GREEN%Created !TEST_CLASS_COUNT! test class files%NC%
) else (
    echo %RED%Test compilation failed!%NC%
    del sources.tmp test-sources.tmp
    exit /b 1
)

echo.
echo %BLUE%Build structure:%NC%
echo   src\           - Source files
echo   src\bin\       - Compiled main classes
echo   src\test-bin\  - Compiled test classes
echo   src\lib\       - Ikonli dependencies
echo   src\test-lib\  - JUnit 5 dependencies

echo.
echo %GREEN%To run the application:%NC%
if defined MAIN_CLASSPATH (
    echo   cd src ^&^& java -cp "bin;!MAIN_CLASSPATH!" editor.Main
) else (
    echo   cd src ^&^& java -cp bin editor.Main
)

echo.
echo %GREEN%To run the tests:%NC%
echo   cd src ^&^& run-tests.bat

echo.
echo %GREEN%To run icon tests:%NC%
echo   cd src ^&^& test-icons.sh (or manual icon testing)

REM Clean up temporary files
del sources.tmp test-sources.tmp

echo.
echo %GREEN%Build complete with tests!%NC%
pause
@echo off
REM Test runner script for Java Text Editor (Windows)
REM This script runs all unit tests using JUnit 5

setlocal enabledelayedexpansion

REM Colors for Windows (using echo with special chars)
set "RED=[91m"
set "GREEN=[92m"
set "YELLOW=[93m"
set "BLUE=[94m"
set "NC=[0m"

REM Project directories
set "BIN_DIR=bin"
set "TEST_BIN_DIR=test-bin"

echo %BLUE%=== Java Text Editor Test Runner ===%NC%

REM Check if test binaries exist
if not exist "%TEST_BIN_DIR%" (
    echo %RED%Error: Test classes not found!%NC%
    echo %YELLOW%Please run the build script first:%NC%
    echo   build-with-tests.bat
    pause
    exit /b 1
)

REM Check if main binaries exist
if not exist "%BIN_DIR%" (
    echo %RED%Error: Main classes not found!%NC%
    echo %YELLOW%Please run the build script first:%NC%
    echo   build-with-tests.bat
    pause
    exit /b 1
)

REM Check if test dependencies exist
if not exist "test-lib" (
    echo %RED%Error: Test dependencies not found!%NC%
    echo %YELLOW%Please download test dependencies first:%NC%
    echo   download-test-dependencies.bat
    echo   build-with-tests.bat
    pause
    exit /b 1
)

echo %YELLOW%Preparing test environment...%NC%

REM Build test classpath
set "TEST_CLASSPATH=%TEST_BIN_DIR%;%BIN_DIR%"

REM Add main application dependencies (Ikonli)
if exist "lib" (
    echo %YELLOW%Including Ikonli dependencies...%NC%
    for %%f in (lib\*.jar) do (
        set "TEST_CLASSPATH=!TEST_CLASSPATH!;%%f"
    )
)

REM Add test dependencies (JUnit 5)
if exist "test-lib" (
    echo %YELLOW%Including JUnit 5 dependencies...%NC%
    for %%f in (test-lib\*.jar) do (
        set "TEST_CLASSPATH=!TEST_CLASSPATH!;%%f"
    )
)

echo %GREEN%Running unit tests...%NC%
echo.

REM Run tests using JUnit Platform Console Launcher
if exist "test-lib\junit-platform-console-standalone-1.10.1.jar" (
    REM Use standalone console launcher if available
    java -jar test-lib\junit-platform-console-standalone-1.10.1.jar --classpath="!TEST_CLASSPATH!" --scan-classpath --reports-dir=test-reports --details=summary
) else (
    REM Fallback to custom test runner
    java -cp "!TEST_CLASSPATH!" test.TestSuite
)

set TEST_RESULT=%errorlevel%

echo.
if %TEST_RESULT%==0 (
    echo %GREEN%=== All tests passed! ===%NC%
    echo %BLUE%Your Java Text Editor is working correctly.%NC%
) else (
    echo %RED%=== Some tests failed! ===%NC%
    echo %YELLOW%Please check the test output above for details.%NC%
)

echo.
echo %BLUE%Additional test options:%NC%
echo   test-icons.sh          - Test icon system specifically (if available)
echo   run.bat                - Run the application manually
echo   build-with-tests.bat   - Rebuild if needed

echo.
if %TEST_RESULT%==0 (
    echo %GREEN%✅ Test suite completed successfully!%NC%
) else (
    echo %RED%❌ Test suite completed with failures.%NC%
)

pause
exit /b %TEST_RESULT%
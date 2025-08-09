@echo off
REM Download JUnit 5 testing dependencies for Windows
REM This batch file downloads the necessary JAR files for unit testing

setlocal enabledelayedexpansion

REM Colors for Windows (using echo with special chars)
set "RED=[91m"
set "GREEN=[92m"
set "YELLOW=[93m"
set "BLUE=[94m"
set "NC=[0m"

REM Directories
set "LIB_DIR=lib"
set "TEST_LIB_DIR=test-lib"
set "MAVEN_BASE=https://repo1.maven.org/maven2"

REM JUnit 5 version
set "JUNIT_VERSION=5.10.1"
set "JUNIT_PLATFORM_VERSION=1.10.1"

echo %BLUE%=== Downloading JUnit 5 Testing Dependencies ===%NC%

REM Create lib directories if they don't exist
if not exist "%LIB_DIR%" mkdir "%LIB_DIR%"
if not exist "%TEST_LIB_DIR%" mkdir "%TEST_LIB_DIR%"

REM Define test dependencies array (Windows batch style)
set "dep1=org/junit/jupiter/junit-jupiter-engine/%JUNIT_VERSION%/junit-jupiter-engine-%JUNIT_VERSION%.jar"
set "dep2=org/junit/jupiter/junit-jupiter-api/%JUNIT_VERSION%/junit-jupiter-api-%JUNIT_VERSION%.jar"
set "dep3=org/junit/jupiter/junit-jupiter-params/%JUNIT_VERSION%/junit-jupiter-params-%JUNIT_VERSION%.jar"
set "dep4=org/junit/platform/junit-platform-engine/%JUNIT_PLATFORM_VERSION%/junit-platform-engine-%JUNIT_PLATFORM_VERSION%.jar"
set "dep5=org/junit/platform/junit-platform-launcher/%JUNIT_PLATFORM_VERSION%/junit-platform-launcher-%JUNIT_PLATFORM_VERSION%.jar"
set "dep6=org/junit/platform/junit-platform-console-standalone/%JUNIT_PLATFORM_VERSION%/junit-platform-console-standalone-%JUNIT_PLATFORM_VERSION%.jar"
set "dep7=org/opentest4j/opentest4j/1.3.0/opentest4j-1.3.0.jar"
set "dep8=org/apiguardian/apiguardian-api/1.1.2/apiguardian-api-1.1.2.jar"

REM Download each dependency
for /L %%i in (1,1,8) do (
    call :download_file "!dep%%i!"
    if !errorlevel! neq 0 (
        echo %RED%Failed to download test dependency %%i%NC%
        exit /b 1
    )
)

echo.
echo %GREEN%=== All test dependencies downloaded successfully! ===%NC%
echo %BLUE%Downloaded files:%NC%
dir /b "%TEST_LIB_DIR%\*.jar" 2>nul

echo.
echo %BLUE%Next steps:%NC%
echo 1. Run build-with-tests.bat to compile with test dependencies
echo 2. Run run-tests.bat to execute the test suite
echo 3. Tests will verify icon system, UI components, and core functionality
echo 4. Test results will be displayed with pass/fail summary

echo.
echo %GREEN%JUnit 5 testing framework ready!%NC%
goto :eof

:download_file
set "dep_path=%~1"
for %%F in ("%dep_path%") do set "filename=%%~nxF"
set "url=%MAVEN_BASE%/%dep_path%"

echo %YELLOW%Downloading %filename%...%NC%

REM Try different download methods for Windows
where curl >nul 2>&1
if %errorlevel% equ 0 (
    curl -L -o "%TEST_LIB_DIR%\%filename%" "%url%"
    set "download_result=!errorlevel!"
) else (
    where powershell >nul 2>&1
    if !errorlevel! equ 0 (
        powershell -Command "try { Invoke-WebRequest -Uri '%url%' -OutFile '%TEST_LIB_DIR%\%filename%' -UseBasicParsing; exit 0 } catch { exit 1 }"
        set "download_result=!errorlevel!"
    ) else (
        where certutil >nul 2>&1
        if !errorlevel! equ 0 (
            certutil -urlcache -split -f "%url%" "%TEST_LIB_DIR%\%filename%" >nul
            set "download_result=!errorlevel!"
        ) else (
            echo %RED%Error: No download tool found. Please install curl, PowerShell, or ensure certutil is available.%NC%
            echo Available options:
            echo   - curl (recommended, download from https://curl.se/windows/)
            echo   - PowerShell (usually pre-installed on Windows 10+)
            echo   - certutil (built into Windows)
            set "download_result=1"
        )
    )
)

if !download_result! equ 0 (
    if exist "%TEST_LIB_DIR%\%filename%" (
        echo %GREEN%✓ Downloaded %filename%%NC%
        exit /b 0
    ) else (
        echo %RED%✗ Download appeared successful but file not found: %filename%%NC%
        exit /b 1
    )
) else (
    echo %RED%✗ Failed to download %filename%%NC%
    exit /b 1
)
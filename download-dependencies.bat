@echo off
REM Download Ikonli dependencies for Windows
REM This batch file downloads the necessary JAR files for icon support

setlocal enabledelayedexpansion

REM Colors for Windows (using echo with special chars)
set "RED=[91m"
set "GREEN=[92m"
set "YELLOW=[93m"
set "BLUE=[94m"
set "NC=[0m"

REM Directories
set "LIB_DIR=lib"
set "MAVEN_BASE=https://repo1.maven.org/maven2"

REM Ikonli version
set "IKONLI_VERSION=12.3.1"

echo %BLUE%=== Downloading Ikonli-Swing Dependencies ===%NC%

REM Create lib directory if it doesn't exist
if not exist "%LIB_DIR%" mkdir "%LIB_DIR%"

REM Define dependencies array (Windows batch style)
set "dep1=org/kordamp/ikonli/ikonli-core/%IKONLI_VERSION%/ikonli-core-%IKONLI_VERSION%.jar"
set "dep2=org/kordamp/ikonli/ikonli-swing/%IKONLI_VERSION%/ikonli-swing-%IKONLI_VERSION%.jar"
set "dep3=org/kordamp/ikonli/ikonli-fontawesome5-pack/%IKONLI_VERSION%/ikonli-fontawesome5-pack-%IKONLI_VERSION%.jar"
set "dep4=org/kordamp/ikonli/ikonli-material2-pack/%IKONLI_VERSION%/ikonli-material2-pack-%IKONLI_VERSION%.jar"

REM Download each dependency
for /L %%i in (1,1,4) do (
    call :download_file "!dep%%i!"
    if !errorlevel! neq 0 (
        echo %RED%Failed to download dependency %%i%NC%
        exit /b 1
    )
)

echo.
echo %GREEN%=== All dependencies downloaded successfully! ===%NC%
echo %BLUE%Downloaded files:%NC%
dir /b "%LIB_DIR%\*.jar" 2>nul

echo.
echo %BLUE%Next steps:%NC%
echo 1. Run build.bat to compile with new dependencies
echo 2. Run run.bat to start the application
echo 3. The SystemIconHelper will now have access to professional icons
echo 4. Available icon packs: FontAwesome5, Material Design 2

echo.
echo %GREEN%Ikonli-Swing integration ready!%NC%
goto :eof

:download_file
set "dep_path=%~1"
for %%F in ("%dep_path%") do set "filename=%%~nxF"
set "url=%MAVEN_BASE%/%dep_path%"

echo %YELLOW%Downloading %filename%...%NC%

REM Try different download methods for Windows
where curl >nul 2>&1
if %errorlevel% equ 0 (
    curl -L -o "%LIB_DIR%\%filename%" "%url%"
    set "download_result=!errorlevel!"
) else (
    where powershell >nul 2>&1
    if !errorlevel! equ 0 (
        powershell -Command "try { Invoke-WebRequest -Uri '%url%' -OutFile '%LIB_DIR%\%filename%' -UseBasicParsing; exit 0 } catch { exit 1 }"
        set "download_result=!errorlevel!"
    ) else (
        where certutil >nul 2>&1
        if !errorlevel! equ 0 (
            certutil -urlcache -split -f "%url%" "%LIB_DIR%\%filename%" >nul
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
    if exist "%LIB_DIR%\%filename%" (
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
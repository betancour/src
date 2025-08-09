@echo off
REM Windows packaging script for Java Text Editor
REM Creates installer and portable zip package for distribution

setlocal enabledelayedexpansion

REM Package information
set "APP_NAME=java-text-editor"
set "APP_VERSION=1.4.0"
set "APP_DESCRIPTION=Modern Java Text Editor with Professional Icons"
set "APP_PUBLISHER=Java Text Editor Team"
set "APP_URL=https://github.com/example/java-text-editor"

REM Colors for Windows (using echo with special chars)
set "RED=[91m"
set "GREEN=[92m"
set "YELLOW=[93m"
set "BLUE=[94m"
set "NC=[0m"

REM Directories
set "SCRIPT_DIR=%~dp0"
set "PROJECT_DIR=%SCRIPT_DIR%..\.."
set "PACKAGING_DIR=%PROJECT_DIR%\packaging"
set "BUILD_DIR=%PACKAGING_DIR%\build"
set "DIST_DIR=%PACKAGING_DIR%\dist"

echo %BLUE%=== Windows Packaging Script ===%NC%
echo Application: %APP_NAME% v%APP_VERSION%
echo Project Directory: %PROJECT_DIR%
echo.

REM Clean and create build directories
echo %YELLOW%Preparing build environment...%NC%
if exist "%BUILD_DIR%" rmdir /s /q "%BUILD_DIR%"
if exist "%DIST_DIR%" rmdir /s /q "%DIST_DIR%"
mkdir "%BUILD_DIR%" 2>nul
mkdir "%DIST_DIR%" 2>nul

REM Build the application first
echo %YELLOW%Building application...%NC%
cd /d "%PROJECT_DIR%"
if not exist "build.bat" (
    echo %RED%Error: build.bat not found in project directory%NC%
    exit /b 1
)

REM Run build script
call build.bat
if %errorlevel% neq 0 (
    echo %RED%Error: Application build failed%NC%
    exit /b 1
)

if not exist "bin" (
    echo %RED%Error: Application build failed - no bin directory%NC%
    exit /b 1
)

echo %GREEN%Application built successfully%NC%

REM Download dependencies if not present
if not exist "lib" (
    echo %YELLOW%Downloading professional icon dependencies...%NC%
    if exist "download-dependencies.bat" (
        call download-dependencies.bat
    ) else (
        echo %YELLOW%Warning: Icon dependencies not available%NC%
    )
)

REM Create application directory structure
set "APP_DIR=%BUILD_DIR%\%APP_NAME%"
echo %YELLOW%Creating application structure...%NC%
mkdir "%APP_DIR%" 2>nul

REM Copy application files
echo %YELLOW%Copying application files...%NC%
xcopy /e /i /q "%PROJECT_DIR%\bin" "%APP_DIR%\bin\"
if exist "%PROJECT_DIR%\lib" xcopy /e /i /q "%PROJECT_DIR%\lib" "%APP_DIR%\lib\"
if exist "%PROJECT_DIR%\resources" xcopy /e /i /q "%PROJECT_DIR%\resources" "%APP_DIR%\resources\"
if exist "%PROJECT_DIR%\README.md" copy "%PROJECT_DIR%\README.md" "%APP_DIR%\" >nul
if exist "%PROJECT_DIR%\LICENSE" copy "%PROJECT_DIR%\LICENSE" "%APP_DIR%\" >nul

REM Create launcher batch file
echo %YELLOW%Creating launcher script...%NC%
echo @echo off > "%APP_DIR%\java-text-editor.bat"
echo REM Java Text Editor launcher script >> "%APP_DIR%\java-text-editor.bat"
echo. >> "%APP_DIR%\java-text-editor.bat"
echo REM Get the directory where this script is located >> "%APP_DIR%\java-text-editor.bat"
echo set "DIR=%%~dp0" >> "%APP_DIR%\java-text-editor.bat"
echo. >> "%APP_DIR%\java-text-editor.bat"
echo REM Set up classpath >> "%APP_DIR%\java-text-editor.bat"
echo set "CLASSPATH=%%DIR%%bin" >> "%APP_DIR%\java-text-editor.bat"
echo if exist "%%DIR%%lib" ^( >> "%APP_DIR%\java-text-editor.bat"
echo     for %%%%j in ^("%%DIR%%lib\*.jar"^) do ^( >> "%APP_DIR%\java-text-editor.bat"
echo         set "CLASSPATH=!CLASSPATH!;%%%%j" >> "%APP_DIR%\java-text-editor.bat"
echo     ^) >> "%APP_DIR%\java-text-editor.bat"
echo ^) >> "%APP_DIR%\java-text-editor.bat"
echo. >> "%APP_DIR%\java-text-editor.bat"
echo REM Check for Java >> "%APP_DIR%\java-text-editor.bat"
echo where java ^>nul 2^>nul >> "%APP_DIR%\java-text-editor.bat"
echo if %%errorlevel%% neq 0 ^( >> "%APP_DIR%\java-text-editor.bat"
echo     echo Error: Java is not installed or not in PATH >> "%APP_DIR%\java-text-editor.bat"
echo     echo Please install Java 8 or higher to run Java Text Editor >> "%APP_DIR%\java-text-editor.bat"
echo     pause >> "%APP_DIR%\java-text-editor.bat"
echo     exit /b 1 >> "%APP_DIR%\java-text-editor.bat"
echo ^) >> "%APP_DIR%\java-text-editor.bat"
echo. >> "%APP_DIR%\java-text-editor.bat"
echo REM Run the application >> "%APP_DIR%\java-text-editor.bat"
echo java -cp "%%CLASSPATH%%" editor.Main %%* >> "%APP_DIR%\java-text-editor.bat"

REM Create launcher executable using PowerShell (if available)
echo %YELLOW%Creating Windows executable launcher...%NC%
where powershell >nul 2>nul
if %errorlevel% equ 0 (
    powershell -Command "& {
        $batContent = Get-Content '%APP_DIR%\java-text-editor.bat' | Out-String;
        $ps1Script = @'
# PowerShell launcher for Java Text Editor
$DIR = Split-Path -Parent $MyInvocation.MyCommand.Path

# Set up classpath
$CLASSPATH = Join-Path $DIR 'bin'
if (Test-Path (Join-Path $DIR 'lib')) {
    Get-ChildItem (Join-Path $DIR 'lib\*.jar') | ForEach-Object {
        $CLASSPATH += ';' + $_.FullName
    }
}

# Check for Java
if (!(Get-Command java -ErrorAction SilentlyContinue)) {
    [System.Windows.Forms.MessageBox]::Show(
        'Java is not installed or not in PATH. Please install Java 8 or higher.',
        'Java Text Editor - Error',
        [System.Windows.Forms.MessageBoxButtons]::OK,
        [System.Windows.Forms.MessageBoxIcon]::Error
    )
    exit 1
}

# Run the application
Start-Process java -ArgumentList '-cp', \"`$CLASSPATH\", 'editor.Main' -NoNewWindow -Wait
'@;
        Set-Content '%APP_DIR%\java-text-editor.ps1' -Value $ps1Script
    }"
)

REM Create uninstaller
echo %YELLOW%Creating uninstaller...%NC%
echo @echo off > "%APP_DIR%\uninstall.bat"
echo echo Uninstalling Java Text Editor... >> "%APP_DIR%\uninstall.bat"
echo set "INSTALL_DIR=%%~dp0" >> "%APP_DIR%\uninstall.bat"
echo cd /d "%%INSTALL_DIR%%.." >> "%APP_DIR%\uninstall.bat"
echo rmdir /s /q "%%INSTALL_DIR%%" >> "%APP_DIR%\uninstall.bat"
echo echo Java Text Editor has been uninstalled. >> "%APP_DIR%\uninstall.bat"
echo pause >> "%APP_DIR%\uninstall.bat"

REM Create portable zip package
echo %YELLOW%Creating portable ZIP package...%NC%
where powershell >nul 2>nul
if %errorlevel% equ 0 (
    powershell -Command "Compress-Archive -Path '%APP_DIR%\*' -DestinationPath '%DIST_DIR%\%APP_NAME%-%APP_VERSION%-windows-portable.zip' -Force"
    echo %GREEN%Created: %APP_NAME%-%APP_VERSION%-windows-portable.zip%NC%
) else (
    echo %YELLOW%PowerShell not available, skipping ZIP creation%NC%
    echo %YELLOW%You can manually zip the contents of %APP_DIR%%NC%
)

REM Create NSIS installer script
echo %YELLOW%Creating NSIS installer script...%NC%
set "NSI_FILE=%BUILD_DIR%\installer.nsi"

echo ; Java Text Editor NSIS Installer Script > "%NSI_FILE%"
echo ; Generated by package-windows.bat >> "%NSI_FILE%"
echo. >> "%NSI_FILE%"
echo !define APP_NAME "%APP_NAME%" >> "%NSI_FILE%"
echo !define APP_VERSION "%APP_VERSION%" >> "%NSI_FILE%"
echo !define APP_PUBLISHER "%APP_PUBLISHER%" >> "%NSI_FILE%"
echo !define APP_URL "%APP_URL%" >> "%NSI_FILE%"
echo !define APP_DESCRIPTION "%APP_DESCRIPTION%" >> "%NSI_FILE%"
echo. >> "%NSI_FILE%"
echo Name "${APP_NAME}" >> "%NSI_FILE%"
echo OutFile "%DIST_DIR%\%APP_NAME%-%APP_VERSION%-windows-installer.exe" >> "%NSI_FILE%"
echo InstallDir "$PROGRAMFILES\${APP_NAME}" >> "%NSI_FILE%"
echo InstallDirRegKey HKLM "Software\${APP_NAME}" "Install_Dir" >> "%NSI_FILE%"
echo RequestExecutionLevel admin >> "%NSI_FILE%"
echo. >> "%NSI_FILE%"
echo Page components >> "%NSI_FILE%"
echo Page directory >> "%NSI_FILE%"
echo Page instfiles >> "%NSI_FILE%"
echo UninstPage uninstConfirm >> "%NSI_FILE%"
echo UninstPage instfiles >> "%NSI_FILE%"
echo. >> "%NSI_FILE%"
echo Section "Java Text Editor (required)" >> "%NSI_FILE%"
echo   SectionIn RO >> "%NSI_FILE%"
echo   SetOutPath $INSTDIR >> "%NSI_FILE%"
echo   File /r "%APP_DIR%\*" >> "%NSI_FILE%"
echo   WriteRegStr HKLM SOFTWARE\${APP_NAME} "Install_Dir" "$INSTDIR" >> "%NSI_FILE%"
echo   WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${APP_NAME}" "DisplayName" "${APP_NAME}" >> "%NSI_FILE%"
echo   WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${APP_NAME}" "UninstallString" '"$INSTDIR\uninstall.exe"' >> "%NSI_FILE%"
echo   WriteRegDWORD HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${APP_NAME}" "NoModify" 1 >> "%NSI_FILE%"
echo   WriteRegDWORD HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${APP_NAME}" "NoRepair" 1 >> "%NSI_FILE%"
echo   WriteUninstaller "uninstall.exe" >> "%NSI_FILE%"
echo SectionEnd >> "%NSI_FILE%"
echo. >> "%NSI_FILE%"
echo Section "Start Menu Shortcuts" >> "%NSI_FILE%"
echo   CreateDirectory "$SMPROGRAMS\${APP_NAME}" >> "%NSI_FILE%"
echo   CreateShortCut "$SMPROGRAMS\${APP_NAME}\Java Text Editor.lnk" "$INSTDIR\java-text-editor.bat" >> "%NSI_FILE%"
echo   CreateShortCut "$SMPROGRAMS\${APP_NAME}\Uninstall.lnk" "$INSTDIR\uninstall.exe" >> "%NSI_FILE%"
echo SectionEnd >> "%NSI_FILE%"
echo. >> "%NSI_FILE%"
echo Section "Desktop Shortcut" >> "%NSI_FILE%"
echo   CreateShortCut "$DESKTOP\Java Text Editor.lnk" "$INSTDIR\java-text-editor.bat" >> "%NSI_FILE%"
echo SectionEnd >> "%NSI_FILE%"
echo. >> "%NSI_FILE%"
echo Section "Uninstall" >> "%NSI_FILE%"
echo   DeleteRegKey HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${APP_NAME}" >> "%NSI_FILE%"
echo   DeleteRegKey HKLM SOFTWARE\${APP_NAME} >> "%NSI_FILE%"
echo   Delete $INSTDIR\uninstall.exe >> "%NSI_FILE%"
echo   RMDir /r $INSTDIR >> "%NSI_FILE%"
echo   Delete "$SMPROGRAMS\${APP_NAME}\*.*" >> "%NSI_FILE%"
echo   RMDir "$SMPROGRAMS\${APP_NAME}" >> "%NSI_FILE%"
echo   Delete "$DESKTOP\Java Text Editor.lnk" >> "%NSI_FILE%"
echo SectionEnd >> "%NSI_FILE%"

REM Try to compile installer with NSIS
echo %YELLOW%Compiling installer with NSIS...%NC%
where makensis >nul 2>nul
if %errorlevel% equ 0 (
    makensis "%NSI_FILE%"
    if %errorlevel% equ 0 (
        echo %GREEN%Created: %APP_NAME%-%APP_VERSION%-windows-installer.exe%NC%
    ) else (
        echo %YELLOW%NSIS compilation failed%NC%
    )
) else (
    echo %YELLOW%NSIS not found, installer script created but not compiled%NC%
    echo %YELLOW%Install NSIS from https://nsis.sourceforge.io/ to create installer%NC%
    echo %YELLOW%Then run: makensis "%NSI_FILE%"%NC%
)

REM Create installation instructions
echo %YELLOW%Creating installation instructions...%NC%
echo # Installation Instructions - Windows > "%DIST_DIR%\INSTALL-WINDOWS.md"
echo. >> "%DIST_DIR%\INSTALL-WINDOWS.md"
echo ## Requirements >> "%DIST_DIR%\INSTALL-WINDOWS.md"
echo - Java 8 or higher (Oracle JDK or OpenJDK) >> "%DIST_DIR%\INSTALL-WINDOWS.md"
echo - Windows 7 or higher >> "%DIST_DIR%\INSTALL-WINDOWS.md"
echo. >> "%DIST_DIR%\INSTALL-WINDOWS.md"
echo ## Installation Options >> "%DIST_DIR%\INSTALL-WINDOWS.md"
echo. >> "%DIST_DIR%\INSTALL-WINDOWS.md"
echo ### Option 1: Installer (Recommended) >> "%DIST_DIR%\INSTALL-WINDOWS.md"
echo 1. Download `%APP_NAME%-%APP_VERSION%-windows-installer.exe` >> "%DIST_DIR%\INSTALL-WINDOWS.md"
echo 2. Right-click and select "Run as administrator" >> "%DIST_DIR%\INSTALL-WINDOWS.md"
echo 3. Follow the installation wizard >> "%DIST_DIR%\INSTALL-WINDOWS.md"
echo 4. Launch from Start Menu or Desktop shortcut >> "%DIST_DIR%\INSTALL-WINDOWS.md"
echo. >> "%DIST_DIR%\INSTALL-WINDOWS.md"
echo ### Option 2: Portable ZIP >> "%DIST_DIR%\INSTALL-WINDOWS.md"
echo 1. Download `%APP_NAME%-%APP_VERSION%-windows-portable.zip` >> "%DIST_DIR%\INSTALL-WINDOWS.md"
echo 2. Extract to any folder >> "%DIST_DIR%\INSTALL-WINDOWS.md"
echo 3. Run `java-text-editor.bat` >> "%DIST_DIR%\INSTALL-WINDOWS.md"
echo. >> "%DIST_DIR%\INSTALL-WINDOWS.md"
echo ## Usage >> "%DIST_DIR%\INSTALL-WINDOWS.md"
echo - Launch from Start Menu: "Java Text Editor" >> "%DIST_DIR%\INSTALL-WINDOWS.md"
echo - Launch from command line: `java-text-editor` >> "%DIST_DIR%\INSTALL-WINDOWS.md"
echo - Open files: Drag and drop or use File menu >> "%DIST_DIR%\INSTALL-WINDOWS.md"
echo. >> "%DIST_DIR%\INSTALL-WINDOWS.md"
echo ## Uninstallation >> "%DIST_DIR%\INSTALL-WINDOWS.md"
echo ### Installer Version >> "%DIST_DIR%\INSTALL-WINDOWS.md"
echo - Use Windows "Add/Remove Programs" >> "%DIST_DIR%\INSTALL-WINDOWS.md"
echo - Or run uninstaller from Start Menu >> "%DIST_DIR%\INSTALL-WINDOWS.md"
echo. >> "%DIST_DIR%\INSTALL-WINDOWS.md"
echo ### Portable Version >> "%DIST_DIR%\INSTALL-WINDOWS.md"
echo - Simply delete the extracted folder >> "%DIST_DIR%\INSTALL-WINDOWS.md"
echo. >> "%DIST_DIR%\INSTALL-WINDOWS.md"
echo ## Features >> "%DIST_DIR%\INSTALL-WINDOWS.md"
echo - Professional icons with FontAwesome and Material Design >> "%DIST_DIR%\INSTALL-WINDOWS.md"
echo - Advanced find and replace functionality >> "%DIST_DIR%\INSTALL-WINDOWS.md"
echo - Line numbers with toggle support >> "%DIST_DIR%\INSTALL-WINDOWS.md"
echo - Undo/redo operations >> "%DIST_DIR%\INSTALL-WINDOWS.md"
echo - Native Windows integration >> "%DIST_DIR%\INSTALL-WINDOWS.md"
echo - Professional user interface >> "%DIST_DIR%\INSTALL-WINDOWS.md"
echo. >> "%DIST_DIR%\INSTALL-WINDOWS.md"
echo For more information, visit: %APP_URL% >> "%DIST_DIR%\INSTALL-WINDOWS.md"

REM Display results
echo.
echo %BLUE%=== Windows Packaging Complete ===%NC%
echo %GREEN%Packages created in: %DIST_DIR%%NC%
echo.
dir /b "%DIST_DIR%"
echo.
echo %BLUE%Installation files:%NC%
if exist "%DIST_DIR%\%APP_NAME%-%APP_VERSION%-windows-installer.exe" (
    echo   📦 %GREEN%%APP_NAME%-%APP_VERSION%-windows-installer.exe%NC% (Windows Installer)
)
if exist "%DIST_DIR%\%APP_NAME%-%APP_VERSION%-windows-portable.zip" (
    echo   📦 %GREEN%%APP_NAME%-%APP_VERSION%-windows-portable.zip%NC% (Portable ZIP)
)
echo   📖 %GREEN%INSTALL-WINDOWS.md%NC% (Installation instructions)
if exist "%NSI_FILE%" (
    echo   🔧 %GREEN%installer.nsi%NC% (NSIS installer script)
)
echo.
echo %BLUE%Ready for distribution! 🚀%NC%
pause
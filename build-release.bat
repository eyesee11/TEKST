@echo off
REM TEKST Release Build Script for Windows

echo 🔨 Building TEKST Release Package...
echo ==================================
echo.

REM Clean previous builds
echo 🧹 Cleaning previous builds...
call mvn clean
echo.

REM Run tests
echo 🧪 Running tests...
call mvn test
echo.

REM Build with assembly
echo 📦 Building release package...
call mvn package assembly:single

if %errorlevel% equ 0 (
    echo.
    echo ✅ Build successful!
    echo.
    echo 📁 Release packages created:
    dir target\*release*
    echo.
    echo 🚀 Release packages are ready in the target\ directory:
    echo    - text-editor-1.0.0-release.zip
    echo    - text-editor-1.0.0-release.tar.gz
    echo.
    echo 📋 Each package contains:
    echo    - text-editor-1.0.0.jar ^(main application^)
    echo    - tekst.bat ^(Windows launcher^)
    echo    - tekst.sh ^(Linux/Mac launcher^)
    echo    - README.md ^(user guide^)
    echo    - LICENSE ^(license file^)
    echo    - samples/ ^(sample documents^)
    echo.
    echo 🎯 To distribute:
    echo    1. Upload packages to GitHub Releases
    echo    2. Share download links with users
    echo    3. Users extract and run tekst.bat or tekst.sh
) else (
    echo.
    echo ❌ Build failed!
    pause
    exit /b 1
)

echo.
pause

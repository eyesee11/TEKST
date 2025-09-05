@echo off
REM TEKST - Launch Script for Windows

echo 🚀 Starting TEKST - Advanced Text Editor
echo.

REM Check if Java is installed
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo ❌ Java is not installed or not in PATH
    echo 📥 Please install Java 21 or higher from: https://adoptium.net/
    echo.
    pause
    exit /b 1
)

REM Display Java version
echo ☕ Java Version:
java -version
echo.

REM Check if JAR file exists
if not exist "text-editor-1.0.0.jar" (
    echo ❌ text-editor-1.0.0.jar not found in current directory
    echo 📁 Please ensure the JAR file is in the same folder as this script
    echo.
    pause
    exit /b 1
)

REM Launch TEKST
echo 🎯 Launching TEKST...
java -jar text-editor-1.0.0.jar

echo.
echo 👋 TEKST has been closed. Thank you for using TEKST!
pause

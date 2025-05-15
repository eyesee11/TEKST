@echo off
REM Launch script for Text Editor Launcher on Windows

REM Navigate to the script's directory
cd /d "%~dp0"

REM Try python first (most common on Windows)
where python >nul 2>nul
if %ERRORLEVEL% EQU 0 (
    python text_editor_launcher.py
    goto :end
)

REM Try python3 as fallback
where python3 >nul 2>nul
if %ERRORLEVEL% EQU 0 (
    python3 text_editor_launcher.py
    goto :end
)

REM If neither worked, show an error message
echo Python was not found on your system. Please install Python and try again.
echo Visit https://www.python.org/downloads/ to install Python.
pause

:end
pause

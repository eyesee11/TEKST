#!/bin/bash
# Launch script for Text Editor Launcher

# Navigate to the script's directory
SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )"
cd "$SCRIPT_DIR"

# Find the appropriate Python command (try python3 first, then python)
if command -v python3 &> /dev/null; then
    python3 text_editor_launcher.py
elif command -v python &> /dev/null; then
    python text_editor_launcher.py
else
    echo "Python was not found on your system. Please install Python and try again."
    echo "Visit https://www.python.org/downloads/ to install Python."
    read -p "Press Enter to continue..."
fi

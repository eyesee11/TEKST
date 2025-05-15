# Cross-Platform Text Editor Launcher

A professional-looking, beginner-friendly GUI application for launching both CLI-based and GUI-based text editors from a user-friendly interface.

## Overview

Text Editor Launcher is a cross-platform GUI application designed to simplify launching text editors. Whether you prefer CLI-based editors like vim and nano or GUI editors like VS Code and gedit, this application provides a unified interface to access them all.

## Features

- Automatically detect installed text editors (nano, nvim, vim, gedit, notepad, VS Code)
- Cross-platform compatibility (Windows, Linux, WSL)
- Intuitive file/directory browser 
- Editor information display
- Recent files history
- Directory navigation with CD commands
- User-friendly interface with professional design
- Proper path handling for all platforms (Windows, Linux, WSL)
- AI Assistant chatbot integration for help and support

## Requirements

- Python 3.6 or higher
- Tkinter (usually included with Python)
- At least one supported text editor installed:
  - nano, vim, nvim (for CLI editors)
  - gedit, notepad, VS Code (for GUI editors)

## Installation

1. Clone or download this repository
2. Install required Python libraries (only standard libraries are used)
3. Make the script executable (for Linux/WSL):
   ```bash
   chmod +x text_editor_launcher.py
   chmod +x launch.sh
   ```

## Getting Started

### Launching the Application

#### On Windows:
Double-click the `launch.bat` file or the alternative `launch_fixed.bat` file, or run:
```
python text_editor_launcher.py
```

#### On Linux/WSL:
Run one of the launch scripts:
```bash
# Standard launch
chmod +x launch.sh
./launch.sh

# Alternative fixed launch script
chmod +x launch_fixed.sh
./launch_fixed.sh
```

Or directly with Python:
```bash
python3 text_editor_launcher.py
```

## Main Interface Guide

### Editor Selection
- The application automatically detects installed text editors on your system
- Use the dropdown menu to select your preferred editor
- Click "Show Editor Info" to see details about the selected editor including keyboard shortcuts and usage instructions

### File Navigation
1. **Directory Panel**:
   - The central panel shows files and folders in the current directory
   - Double-click on folders to navigate into them
   - Double-click on files to open them with the selected editor

2. **Directory Controls**:
   - Current Directory: Shows your current location
   - Change Dir: Opens a folder browser to change directories
   - CD Command: Enter commands like "cd .." or relative paths

3. **File Browser**:
   - Browse for File: Opens a file selection dialog
   - Browse for Directory: Opens a directory selection dialog

### Recent Files
- The right panel shows recently accessed files
- Double-click any file to open it with the selected editor

### Launching Editors
- Select a file and click "Launch Editor" to open it with the chosen editor
- The application will open terminal-based editors (vim, nano) in an appropriate terminal window
- GUI editors will launch in their normal windows

### Terminal Access
- Click "Open CMD as Guest" to open a terminal/command prompt in the current directory

### AI Assistant
- Click the "AI Assistant" button to open the chatbot interface
- Ask questions about the application, editors, or general programming topics
- The AI can provide help with editor commands and usage tips

## Special Features

### Cross-Platform Path Handling
- The application automatically handles path conversions between:
  - Windows paths (C:\Users\...)
  - Linux paths (/home/user/...)
  - WSL paths (/mnt/c/Users/...)

### WSL Support
- When running on Windows with WSL, the application can launch Linux editors properly
- When running inside WSL, it can interact correctly with both Linux and Windows paths

## Editor Quick Reference

### Common CLI Editors

#### Nano
- Basic navigation: Arrow keys
- Save: Ctrl+O
- Exit: Ctrl+X
- Cut line: Ctrl+K
- Paste: Ctrl+U

#### Vim/NeoVim
- Enter insert mode: i
- Exit insert mode: Esc
- Save: :w (in command mode)
- Quit: :q (in command mode)
- Save and quit: :wq (in command mode)
- Force quit: :q! (in command mode)

### Common GUI Editors

#### VS Code
- Open file: Ctrl+O
- Save: Ctrl+S
- Open terminal: Ctrl+`
- Command palette: Ctrl+Shift+P
- Settings: Ctrl+,

#### Gedit
- Open file: Ctrl+O
- Save: Ctrl+S
- Close: Ctrl+W
- Preferences: Ctrl+,

## Advanced Features

- **Recent Files**: Quick access to recently edited files
- **Path Handling**: Intelligent handling of paths for all platforms
- **Terminal Access**: "Open CMD as Guest" button for quick terminal access
- **File Browsing**: Built-in file browser with folder/file navigation
- **Editor Information**: View details about each editor including usage instructions

---

*Text Editor Launcher - Created May 2025*

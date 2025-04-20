# Terminal Text Editor Launcher

A simple command-line tool that provides a menu-driven interface for choosing between different text editors (nano, vim, and gedit) to open and edit files.

## Features

- Interactive menu to select between available text editors
- Support for nano, vim, and gedit
- Automatic detection of installed editors
- Simple file path input
- Error handling for missing editors and invalid inputs

## Requirements3

- Python 3.6 or higher
- At least one of the following text editors installed:
  - nano
  - vim
  - gedit

## Installation

1. Clone or download this repository
2. Make the script executable:
   ```bash
   chmod +x editor_launcher.py
   ```

## Usage

Run the script:
```bash
./launcher.sh
```

Follow the on-screen prompts to:
1. Select an editor from the available options
2. Enter the path to the file you want to edit
3. Edit your file in the chosen editor

To quit the program, enter 'q' at any prompt.

## Notes

- The script will only show editors that are installed on your system
- If no supported editors are found, the script will display an error message
- You can create new files by entering a path to a non-existent file 
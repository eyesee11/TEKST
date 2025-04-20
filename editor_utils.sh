#!/bin/bash

# Colors for better UI
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Function to check if an editor is installed
check_editor() {
    command -v "$1" >/dev/null 2>&1
    return $?
}

# Function to display editor information
show_editor_info() {
    case "$1" in
        nano)
            echo -e "${BLUE}Nano:${NC} A simple, beginner-friendly editor with commands shown at the bottom of the screen."
            echo "Good for: Quick edits, beginners to command-line editors."
            echo "Key commands: Ctrl+O (save), Ctrl+X (exit), Ctrl+K (cut line), Ctrl+U (paste)"
            ;;
        vim)
            echo -e "${BLUE}Vim:${NC} A powerful modal editor with extensive features and customization."
            echo "Good for: Advanced text manipulation, programming, efficient editing with keyboard."
            echo "Key modes: Normal mode (Esc), Insert mode (i), Command mode (:)"
            echo "Basic commands: :w (save), :q (quit), :wq (save and quit), u (undo)"
            ;;
        gedit)
            echo -e "${BLUE}Gedit:${NC} A graphical text editor with a familiar GUI interface."
            echo "Good for: Users who prefer traditional GUI interfaces over command-line."
            echo "Features: Syntax highlighting, search/replace, plugins"
            ;;
        *)
            echo -e "${RED}Unknown editor: $1${NC}"
            ;;
    esac
}

# Function to show editor help
show_editor_help() {
    clear
    echo -e "${BLUE}=== Editor Help ===${NC}"
    echo -e "\n${YELLOW}Nano:${NC}"
    echo "Ctrl+O: Save file"
    echo "Ctrl+X: Exit"
    echo "Ctrl+W: Search text"
    echo "Ctrl+G: Get help"
    
    echo -e "\n${YELLOW}Vim:${NC}"
    echo "i: Enter insert mode"
    echo "Esc: Return to normal mode"
    echo ":w: Save file"
    echo ":q: Quit"
    echo ":wq: Save and quit"
    
    echo -e "\n${YELLOW}Gedit:${NC}"
    echo "Standard GUI editor"
    echo "Use File menu or Ctrl+S to save"
    echo "Use Ctrl+Q to quit"
    
    echo -e "\n${YELLOW}Press Enter to continue...${NC}"
    read
}

# Function to open file with selected editor
open_with_editor() {
    local editor="$1"
    local file="$2"
    
    # Show preview if file exists
    if [ -f "$file" ]; then
        clear
        echo -e "${BLUE}=== File Preview ===${NC}"
        echo -e "${YELLOW}First few lines of $file:${NC}\n"
        head -n 10 "$file"
        echo -e "\n${YELLOW}Press Enter to edit or Ctrl+C to cancel...${NC}"
        read
    fi
    
    # Open with selected editor
    case "$editor" in
        "nano") nano "$file" ;;
        "vim") vim "$file" ;;
        "gedit") gedit "$file" ;;
    esac
} 
#!/bin/bash
# Terminal Text Editor Launcher CLI
# A simple menu to open files in different text editors

# Colors for better UI
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Store recent files
RECENT_FILES=()
MAX_RECENT=5

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

# Function to display the main menu
show_menu() {
    clear
    echo -e "${BLUE}=== Terminal Text Editor Launcher ===${NC}"
    echo -e "${YELLOW}Available Editors:${NC}"
    
    local i=1
    if check_editor nano; then
        echo -e "${GREEN}$i)${NC} nano - Simple text editor"
        ((i++))
    fi
    if check_editor vim; then
        echo -e "${GREEN}$i)${NC} vim - Advanced text editor"
        ((i++))
    fi
    if check_editor gedit; then
        echo -e "${GREEN}$i)${NC} gedit - Graphical text editor"
        ((i++))
    fi
    
    echo -e "\n${YELLOW}Additional Options:${NC}"
    echo -e "${GREEN}r)${NC} Recent Files"
    echo -e "${GREEN}b)${NC} Browse Files"
    echo -e "${GREEN}h)${NC} Editor Help"
    echo -e "${GREEN}q)${NC} Quit"
    
    echo -e "\n${BLUE}Enter your choice:${NC} "
}

# Function to show recent files
show_recent_files() {
    clear
    echo -e "${BLUE}=== Recent Files ===${NC}"
    if [ ${#RECENT_FILES[@]} -eq 0 ]; then
        echo -e "${YELLOW}No recent files${NC}"
    else
        for i in "${!RECENT_FILES[@]}"; do
            echo -e "${GREEN}$((i+1)))${NC} ${RECENT_FILES[$i]}"
        done
    fi
    echo -e "\n${YELLOW}Press Enter to continue...${NC}"
    read
}

# Function to add file to recent files
add_recent_file() {
    local file="$1"
    # Remove if already exists
    RECENT_FILES=(${RECENT_FILES[@]/$file})
    # Add to beginning
    RECENT_FILES=("$file" "${RECENT_FILES[@]}")
    # Keep only MAX_RECENT files
    RECENT_FILES=("${RECENT_FILES[@]:0:$MAX_RECENT}")
}

# Function to browse files
browse_files() {
    local dir="${1:-.}"
    while true; do
        clear
        echo -e "${BLUE}=== File Browser ===${NC}"
        echo -e "${YELLOW}Current directory:${NC} $(pwd)/$dir"
        echo -e "\n${GREEN}Files and Directories:${NC}"
        
        # List directories first
        for item in "$dir"/*; do
            if [ -d "$item" ]; then
                echo -e "${BLUE}d)${NC} $(basename "$item")/"
            fi
        done
        
        # Then list files
        for item in "$dir"/*; do
            if [ -f "$item" ]; then
                echo -e "${GREEN}f)${NC} $(basename "$item")"
            fi
        done
        
        echo -e "\n${YELLOW}Options:${NC}"
        echo -e "${GREEN}Enter filename${NC} to select"
        echo -e "${GREEN}cd <dir>${NC} to change directory"
        echo -e "${GREEN}b${NC} to go back"
        echo -e "${GREEN}q${NC} to quit browsing"
        
        read -p "Enter choice: " choice
        
        case "$choice" in
            "q") return 1 ;;
            "b") return 0 ;;
            cd\ *) 
                new_dir="${choice#cd }"
                if [ -d "$dir/$new_dir" ]; then
                    dir="$dir/$new_dir"
                else
                    echo -e "${RED}Invalid directory${NC}"
                    sleep 1
                fi
                ;;
            *)
                if [ -f "$dir/$choice" ]; then
                    add_recent_file "$(realpath "$dir/$choice")"
                    return 1
                else
                    echo -e "${RED}Invalid file${NC}"
                    sleep 1
                fi
                ;;
        esac
    done
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

# Main loop
while true; do
    show_menu
    read -p "" choice
    
    case "$choice" in
        [1-3])
            # Map choice to editor
            case "$choice" in
                1) editor="nano" ;;
                2) editor="vim" ;;
                3) editor="gedit" ;;
            esac
            
            if check_editor "$editor"; then
                echo -e "${YELLOW}Enter file path or 'b' to browse:${NC} "
                read file_path
                
                if [ "$file_path" = "b" ]; then
                    if browse_files; then
                        continue
                    fi
                    file_path="${RECENT_FILES[0]}"
                fi
                
                if [ -n "$file_path" ]; then
                    add_recent_file "$(realpath "$file_path")"
                    open_with_editor "$editor" "$file_path"
                fi
            else
                echo -e "${RED}Editor $editor is not installed${NC}"
                sleep 2
            fi
            ;;
        "r")
            show_recent_files
            ;;
        "b")
            if browse_files; then
                continue
            fi
            file_path="${RECENT_FILES[0]}"
            if [ -n "$file_path" ]; then
                show_menu
                read -p "Select editor (1-3): " editor_choice
                case "$editor_choice" in
                    1) editor="nano" ;;
                    2) editor="vim" ;;
                    3) editor="gedit" ;;
                esac
                if check_editor "$editor"; then
                    open_with_editor "$editor" "$file_path"
                fi
            fi
            ;;
        "h")
            show_editor_help
            ;;
        "q")
            echo -e "${GREEN}Goodbye!${NC}"
            exit 0
            ;;
        *)
            echo -e "${RED}Invalid choice${NC}"
            sleep 1
            ;;
    esac
done

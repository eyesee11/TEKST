#!/bin/bash

# Colors for better UI
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

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
                    echo "$(realpath "$dir/$choice")"
                    return 1
                else
                    echo -e "${RED}Invalid file${NC}"
                    sleep 1
                fi
                ;;
        esac
    done
} 
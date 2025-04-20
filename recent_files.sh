#!/bin/bash

# Colors for better UI
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Store recent files
RECENT_FILES=()
MAX_RECENT=5

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

# Function to get a recent file by index
get_recent_file() {
    local index=$1
    if [ $index -ge 0 ] && [ $index -lt ${#RECENT_FILES[@]} ]; then
        echo "${RECENT_FILES[$index]}"
        return 0
    fi
    return 1
} 
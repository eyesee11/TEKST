#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
Icon generator for Text Editor Launcher
This script creates a simple icon for the application
"""

import tkinter as tk
from PIL import Image, ImageDraw

def create_icon():
    # Create a new transparent image
    width, height = 128, 128
    image = Image.new('RGBA', (width, height), (0, 0, 0, 0))
    draw = ImageDraw.Draw(image)
    
    # Define colors (brown theme)
    dark_brown = "#5D4037"
    medium_brown = "#8D6E63"
    light_brown = "#D7CCC8"
    
    # Draw the icon (a simplified text editor icon)
    # Background rectangle
    draw.rectangle([(10, 10), (width-10, height-10)], fill=light_brown, outline=dark_brown, width=3)
    
    # Text lines
    line_y = 30
    for i in range(5):
        line_length = 80 if i == 0 or i == 3 else 60
        draw.rectangle([(24, line_y), (24 + line_length, line_y + 8)], 
                     fill=medium_brown)
        line_y += 16
    
    # Optional: highlight marker (looks like a cursor)
    cursor_height = 30
    draw.rectangle([(90, 40), (94, 40 + cursor_height)], 
                 fill=dark_brown)
    
    # Save the images in different formats
    image.save('editor_icon.ico', sizes=[(32, 32), (64, 64), (128, 128)])
    image.save('editor_icon.png')
    print("Icon created successfully!")

if __name__ == '__main__':
    try:
        from PIL import Image, ImageDraw
        create_icon()
    except ImportError:
        print("Pillow library not found. Installing Pillow is required to create the icon.")
        print("Please install it using: pip install pillow")

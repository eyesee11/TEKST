#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
Cross-Platform GUI-Based Text Editor Launcher
Date: May 9, 2025 (Updated May 11, 2025)

A professional-looking, beginner-friendly application that allows users to
launch CLI-based or GUI-based text editors (such as nano, nvim, and gedit)
from a user-friendly interface.

Now includes an integrated AI chatbot assistant.
"""

import os
import sys
import json
import platform
import subprocess
import shutil
import tkinter as tk
from tkinter import ttk, filedialog, messagebox
from datetime import datetime
import threading
import re

# Import the Chatbot class
try:
    from chatbot import Chatbot  # This will use the chatbot/__init__.py import
except ImportError:
    sys.path.append(os.path.dirname(os.path.abspath(__file__)))
    try:
        from chatbot.chatbot import Chatbot  # Direct import from module
    except ImportError:
        print("Warning: Chatbot functionality will be disabled.")

class TextEditorLauncher:
    def __init__(self, root):
        self.root = root
        self.root.title("Text Editor Launcher")
        self.root.geometry("800x600")
        self.root.minsize(700, 550)
        
        # Initialize variables
        self.editors = {}
        self.editor_paths = {}
        self.selected_editor = tk.StringVar()
        self.selected_file_path = tk.StringVar()
        self.current_directory = tk.StringVar()
        self.current_directory.set(os.path.abspath(os.path.expanduser("~")))
        self.cd_command = tk.StringVar()
        self.recent_files = []
        self.file_list_var = tk.StringVar()
        
        # Load recent files if available
        self.recent_files_path = os.path.join(os.path.dirname(os.path.abspath(__file__)), "recent_files.json")
        self.load_recent_files()
        
        # Color scheme (brown/cream theme)
        self.colors = {
            "dark_brown": "#5D4037",
            "medium_brown": "#8D6E63", 
            "light_brown": "#D7CCC8",
            "cream": "#EFEBE9",
            "accent": "#A1887F",
            "text": "#3E2723"
        }
        
        # Set theme
        self.style = ttk.Style()
        self.style.theme_use('clam')
        self.style.configure("TFrame", background=self.colors["cream"])
        self.style.configure("TLabel", background=self.colors["cream"], foreground=self.colors["text"])
        self.style.configure("TButton", background=self.colors["medium_brown"], foreground="white")
        self.style.map("TButton", background=[('active', self.colors["dark_brown"])])
        self.style.configure("Accent.TButton", background=self.colors["accent"])
        
        # Set window background
        self.root.configure(background=self.colors["cream"])
        
        # Load editor info
        self.load_editor_info()
        
        # Detect installed editors
        self.detect_editors()
          # Create UI
        self.create_widgets()
          # Update directory content on start
        self.update_directory_content()
        
        # Initialize the chatbot assistant if available
        try:
            # Pass False to not create a toggle button in the chatbot (we handle it in our UI)
            self.chatbot = Chatbot(self.root, self.colors, create_toggle_button=False)
            print("Chatbot initialized successfully")
        except Exception as e:
            print(f"Error initializing chatbot: {e}")
            self.chatbot = None
            
    def load_editor_info(self):
        """Load editor information from JSON file"""
        try:
            # First try to load the fixed version, fall back to regular if not available
            info_path = os.path.join(os.path.dirname(os.path.abspath(__file__)), "editor_info_fixed.json")
            if not os.path.exists(info_path):
                info_path = os.path.join(os.path.dirname(os.path.abspath(__file__)), "editor_info.json")
                
            with open(info_path, 'r', encoding='utf-8') as f:
                self.editor_info = json.load(f)
                print(f"Loaded editor info from: {info_path}")
        except Exception as e:
            print(f"Error loading editor info: {e}")
            self.editor_info = {}

    def load_recent_files(self):
        """Load list of recently accessed files"""
        try:
            if os.path.exists(self.recent_files_path):
                with open(self.recent_files_path, 'r', encoding='utf-8') as f:
                    self.recent_files = json.load(f)
        except Exception as e:
            print(f"Error loading recent files: {e}")
            self.recent_files = []

    def save_recent_files(self):
        """Save list of recently accessed files"""
        try:
            # Keep only the last 10 files
            self.recent_files = self.recent_files[:10]
            with open(self.recent_files_path, 'w', encoding='utf-8') as f:
                json.dump(self.recent_files, f)
        except Exception as e:
            print(f"Error saving recent files: {e}")

    def add_recent_file(self, file_path):
        """Add file to recent files list"""
        if not file_path or not os.path.exists(file_path):
            return
            
        # Remove if already exists to avoid duplicates
        if file_path in self.recent_files:
            self.recent_files.remove(file_path)
            
        # Add to the beginning of the list
        self.recent_files.insert(0, file_path)
        
        # Update the recent files display
        self.update_recent_files_display()
        
        # Save the updated list
        self.save_recent_files()

    def update_recent_files_display(self):
        """Update the recent files display in the UI"""
        if hasattr(self, 'recent_files_listbox'):
            self.recent_files_listbox.delete(0, tk.END)
            for file_path in self.recent_files:
                if os.path.exists(file_path):
                    self.recent_files_listbox.insert(tk.END, os.path.basename(file_path))

    def detect_editors(self):
        """Detect installed text editors on the system"""
        editors_to_check = ["nano", "vim", "nvim", "gedit", "notepad", "code"]
        
        # Check if running in WSL
        self.is_wsl = False
        if platform.system() == "Linux" and "microsoft" in platform.uname().release.lower():
            self.is_wsl = True
            print("Running in WSL environment")
        
        print(f"Current platform: {platform.system()}")
        print(f"Checking for editors: {', '.join(editors_to_check)}")
        
        for editor in editors_to_check:
            print(f"Detecting editor: {editor}")
            path = self.get_editor_path(editor)
            if path:
                self.editors[editor] = path
                self.editor_paths[editor] = path
                print(f"Added {editor} to available editors with path: {path}")
            else:
                print(f"Editor {editor} not found")

    def get_editor_path(self, editor):
        """Get the path to an editor executable"""
        try:
            if platform.system() == "Windows":
                # Special handling for Windows-specific editors
                if editor == "notepad":
                    print(f"Adding notepad.exe to editors list")
                    return "notepad.exe"
                elif editor == "code":
                    # Check for VS Code
                    vscode_paths = [
                        os.path.join(os.environ.get('LOCALAPPDATA', ''), 'Programs', 'Microsoft VS Code', 'Code.exe'),
                        os.path.join(os.environ.get('ProgramFiles', ''), 'Microsoft VS Code', 'Code.exe'),
                        os.path.join(os.environ.get('ProgramFiles(x86)', ''), 'Microsoft VS Code', 'Code.exe')
                    ]
                    for path in vscode_paths:
                        if os.path.exists(path):
                            print(f"Found VS Code at: {path}")
                            return path
                    # If we can't find the exact path, try shutil.which
                    code_path = shutil.which("code")
                    if code_path:
                        print(f"Found VS Code using shutil.which: {code_path}")
                        return code_path
                    return None
                elif editor in ["nano", "vim", "nvim", "gedit"]:
                    # For Linux editors on Windows, first check if they're installed directly
                    path = shutil.which(editor)
                    if path:
                        print(f"Found {editor} installed directly on Windows: {path}")
                        return path
                    
                    # Check if they're available via WSL
                    try:
                        # Check if WSL is available first
                        wsl_check = subprocess.run(["wsl", "echo", "WSL Available"], 
                                                 capture_output=True, text=True, timeout=2)
                        if wsl_check.returncode == 0:
                            print(f"WSL is available, checking for {editor} in WSL...")
                            # Try to find the editor in WSL
                            wsl_which = subprocess.run(["wsl", "which", editor], 
                                                     capture_output=True, text=True, timeout=2)
                            if wsl_which.returncode == 0 and wsl_which.stdout.strip():
                                editor_path = wsl_which.stdout.strip()
                                print(f"Found {editor} in WSL at: {editor_path}")
                                return editor  # Return just the editor name for WSL editors
                    except (subprocess.SubprocessError, subprocess.TimeoutExpired, FileNotFoundError) as e:
                        print(f"Error checking WSL for {editor}: {e}")
                    
                    return None
                else:
                    # For other editors, use shutil.which
                    path = shutil.which(editor)
                    if path:
                        print(f"Found {editor} at: {path}")
                    return path
            else:
                # Unix/Linux/Mac
                path = shutil.which(editor)
                if path:
                    print(f"Found {editor} at: {path}")
                return path
        except Exception as e:
            print(f"Error detecting {editor}: {e}")
            return None

    def create_widgets(self):
        """Create all UI widgets"""
        # Main container
        main_frame = ttk.Frame(self.root, padding="20 20 20 20")
        main_frame.pack(fill=tk.BOTH, expand=True)
        
        # Title with stylish font
        title = ttk.Label(main_frame, text="Terminal Text Editor Launcher", 
                            font=("Georgia", 20, "bold"), foreground=self.colors["dark_brown"])
        title.pack(pady=(0, 20))
        
        # Editors section
        editors_frame = ttk.LabelFrame(main_frame, text="Available Editors", padding="10 10 10 10")
        editors_frame.pack(fill=tk.X, pady=(0, 15))
        
        # Create dropdown for editor selection
        self.editor_dropdown = ttk.Combobox(editors_frame, textvariable=self.selected_editor, 
                                          state="readonly", width=30)
        editor_names = []
        dropdown_values = []
        
        # Always add notepad for Windows systems as a fallback
        if platform.system() == "Windows" and "notepad" not in self.editors:
            self.editors["notepad"] = "notepad.exe"
            self.editor_paths["notepad"] = "notepad.exe"
            print("Added notepad as fallback editor")
        
        print(f"Found editors: {list(self.editors.keys())}")
        
        for editor, path in self.editors.items():
            if editor in self.editor_info:
                display_name = f"{self.editor_info[editor]['icon']} {self.editor_info[editor]['name']}"
                editor_names.append(display_name)
                dropdown_values.append(editor)
            else:
                editor_names.append(editor)
                dropdown_values.append(editor)
        
        self.editor_dropdown['values'] = editor_names
        self.editor_dropdown.pack(pady=10, fill=tk.X)
        
        if editor_names:
            # Bind selection event
            self.editor_dropdown.bind("<<ComboboxSelected>>", 
                lambda e: self.on_editor_selected(dropdown_values[self.editor_dropdown.current()]))
            
            # Select first editor by default
            self.editor_dropdown.current(0)
            self.on_editor_selected(dropdown_values[0])
            
            # Editor info button
            info_button = ttk.Button(editors_frame, text="Show Editor Info", 
                                   command=self.show_editor_info)
            info_button.pack(pady=(0, 10), fill=tk.X)
        else:
            no_editors_label = ttk.Label(editors_frame, 
                                       text="No compatible editors found! Adding notepad as fallback.", 
                                       foreground="red")
            no_editors_label.pack(pady=10)
            
            # Still add notepad for Windows
            if platform.system() == "Windows":
                self.editors["notepad"] = "notepad.exe"
                self.editor_paths["notepad"] = "notepad.exe"
                self.selected_editor.set("notepad")
                
                # Refresh the dropdown
                self.editor_dropdown['values'] = ["Notepad"]
                self.editor_dropdown.current(0)
        
        # Directory navigation
        dir_frame = ttk.LabelFrame(main_frame, text="Directory Navigation", padding="10 10 10 10")
        dir_frame.pack(fill=tk.X, pady=(0, 15))
        
        # Current directory display
        dir_label = ttk.Label(dir_frame, text="Current Directory:")
        dir_label.grid(row=0, column=0, sticky=tk.W, pady=5)
        
        dir_entry = ttk.Entry(dir_frame, textvariable=self.current_directory, width=50)
        dir_entry.grid(row=0, column=1, sticky=tk.EW, padx=5, pady=5)
        
        change_dir_btn = ttk.Button(dir_frame, text="Change Dir", 
                                  command=self.change_directory)
        change_dir_btn.grid(row=0, column=2, sticky=tk.E, pady=5)
        
        # CD command
        cd_label = ttk.Label(dir_frame, text="CD Command:")
        cd_label.grid(row=1, column=0, sticky=tk.W, pady=5)
        
        cd_entry = ttk.Entry(dir_frame, textvariable=self.cd_command)
        cd_entry.grid(row=1, column=1, sticky=tk.EW, padx=5, pady=5)
        
        cd_btn = ttk.Button(dir_frame, text="Execute", command=self.execute_cd_command)
        cd_btn.grid(row=1, column=2, sticky=tk.E, pady=5)
        
        # Configure grid
        dir_frame.columnconfigure(1, weight=1)
        
        # Files browser
        files_frame = ttk.Frame(main_frame)
        files_frame.pack(fill=tk.BOTH, expand=True, pady=(0, 15))
        
        # File list with scrollbar
        files_label = ttk.Label(files_frame, text="Files:")
        files_label.pack(anchor=tk.W)
        
        files_listbox_frame = ttk.Frame(files_frame)
        files_listbox_frame.pack(fill=tk.BOTH, expand=True, side=tk.LEFT)
        
        self.files_listbox = tk.Listbox(files_listbox_frame, height=10, 
                                      selectmode=tk.SINGLE, 
                                      bg=self.colors["light_brown"],
                                      fg=self.colors["text"])
        self.files_listbox.pack(fill=tk.BOTH, expand=True, side=tk.LEFT)
        self.files_listbox.bind('<Double-Button-1>', self.on_file_selected)
        
        files_scrollbar = ttk.Scrollbar(files_listbox_frame, orient="vertical", 
                                      command=self.files_listbox.yview)
        files_scrollbar.pack(fill=tk.Y, side=tk.RIGHT)
        self.files_listbox.config(yscrollcommand=files_scrollbar.set)
        
        # Recent files section
        recent_frame = ttk.Frame(files_frame)
        recent_frame.pack(fill=tk.BOTH, expand=True, side=tk.RIGHT, padx=(10, 0))
        
        recent_label = ttk.Label(recent_frame, text="Recent Files:")
        recent_label.pack(anchor=tk.W)
        
        self.recent_files_listbox = tk.Listbox(recent_frame, height=10, 
                                            selectmode=tk.SINGLE,
                                            bg=self.colors["light_brown"],
                                            fg=self.colors["text"])
        self.recent_files_listbox.pack(fill=tk.BOTH, expand=True, side=tk.LEFT)
        self.recent_files_listbox.bind('<Double-Button-1>', self.on_recent_file_selected)
        
        recent_scrollbar = ttk.Scrollbar(recent_frame, orient="vertical", 
                                      command=self.recent_files_listbox.yview)
        recent_scrollbar.pack(fill=tk.Y, side=tk.RIGHT)
        self.recent_files_listbox.config(yscrollcommand=recent_scrollbar.set)
        
        # Update recent files display
        self.update_recent_files_display()
        
        # Action buttons
        action_frame = ttk.Frame(main_frame)
        action_frame.pack(fill=tk.X, pady=(0, 10))
        
        browse_btn = ttk.Button(action_frame, text="Browse for File", 
                              command=self.browse_file)
        browse_btn.pack(side=tk.LEFT, padx=5)
        
        browse_dir_btn = ttk.Button(action_frame, text="Browse for Directory", 
                                  command=self.browse_directory)
        browse_dir_btn.pack(side=tk.LEFT, padx=5)
        
        # Launch button (with accent color)
        launch_btn = ttk.Button(main_frame, text="Launch Editor", 
                              command=self.launch_editor, style="Accent.TButton")
        launch_btn.pack(fill=tk.X, ipady=5)
          # Open CMD as Guest button (positioned at top right)
        guest_btn = ttk.Button(main_frame, text="Open CMD as Guest", 
                             command=self.open_cmd_as_guest,
                             style="Accent.TButton")
        guest_btn.place(relx=1.0, y=0, anchor="ne")
          # AI Assistant button (positioned at bottom right)
        assistant_btn = ttk.Button(
            main_frame, 
            text="💬 AI Assistant", 
            command=self.toggle_chatbot,
            style="Accent.TButton"
        )
        assistant_btn.place(relx=1.0, rely=1.0, anchor="se", x=-10, y=-10)

    def update_directory_content(self):
        """Update the files listbox with content from the current directory"""
        try:
            # Clear the listbox
            self.files_listbox.delete(0, tk.END)
            
            # Add parent directory
            self.files_listbox.insert(tk.END, "../ (Parent Directory)")
            
            # Add subdirectories first with a folder icon
            for item in sorted(os.listdir(self.current_directory.get())):
                full_path = os.path.join(self.current_directory.get(), item)
                if os.path.isdir(full_path):
                    self.files_listbox.insert(tk.END, f"📁 {item}/")
            
            # Then add files with appropriate icons
            for item in sorted(os.listdir(self.current_directory.get())):
                full_path = os.path.join(self.current_directory.get(), item)
                if os.path.isfile(full_path):
                    # You can add more file type indicators here
                    if item.endswith(('.py', '.js', '.html', '.css', '.txt', '.md', '.json')):
                        self.files_listbox.insert(tk.END, f"📄 {item}")
                    else:
                        self.files_listbox.insert(tk.END, f"🗎 {item}")
        except Exception as e:
            messagebox.showerror("Error", f"Failed to read directory: {e}")

    def on_editor_selected(self, editor):
        """Handle editor selection from dropdown"""
        self.selected_editor.set(editor)

    def show_editor_info(self):
        """Show information about the currently selected editor"""
        editor = self.selected_editor.get()
        if not editor:
            messagebox.showinfo("Editor Info", "No editor selected.")
            return
            
        if editor in self.editor_info:
            info = self.editor_info[editor]
            info_text = f"Name: {info['name']}\n\n"
            info_text += f"Description: {info['description']}\n\n"
            info_text += f"Usage: {info['usage']}\n\n"
            info_text += "Key Commands:\n"
            for cmd in info['key_commands']:
                info_text += f"• {cmd}\n"
                
            info_text += f"\nPath: {self.editor_paths[editor]}"
            
            # Create custom dialog
            info_dialog = tk.Toplevel(self.root)
            info_dialog.title(f"{info['name']} Information")
            info_dialog.geometry("500x400")
            info_dialog.configure(background=self.colors["cream"])
            
            # Make dialog modal
            info_dialog.transient(self.root)
            info_dialog.grab_set()
            
            # Add content
            title_label = tk.Label(info_dialog, text=f"{info['icon']} {info['name']} Editor", 
                                font=("Georgia", 16, "bold"),
                                bg=self.colors["cream"], fg=self.colors["dark_brown"])
            title_label.pack(pady=15)
            
            info_text_widget = tk.Text(info_dialog, wrap=tk.WORD, height=15, width=50,
                                    bg=self.colors["light_brown"], fg=self.colors["text"],
                                    bd=0, padx=10, pady=10)
            info_text_widget.insert(tk.END, info_text)
            info_text_widget.config(state="disabled")
            info_text_widget.pack(fill=tk.BOTH, expand=True, padx=20, pady=10)
            
            close_button = tk.Button(info_dialog, text="Close", 
                                   command=info_dialog.destroy,
                                   bg=self.colors["medium_brown"], fg="white",
                                   activebackground=self.colors["dark_brown"],
                                   bd=0, padx=20, pady=5)
            close_button.pack(pady=15)
            
            # Center the dialog on the parent window
            info_dialog.update_idletasks()
            x = self.root.winfo_x() + (self.root.winfo_width() // 2) - (info_dialog.winfo_width() // 2)
            y = self.root.winfo_y() + (self.root.winfo_height() // 2) - (info_dialog.winfo_height() // 2)
            info_dialog.geometry(f"+{x}+{y}")
        else:
            messagebox.showinfo("Editor Info", f"No information available for {editor}.")

    def change_directory(self):
        """Change the current working directory"""
        dir_path = filedialog.askdirectory(initialdir=self.current_directory.get())
        if dir_path:
            self.current_directory.set(dir_path)
            self.update_directory_content()

    def execute_cd_command(self):
        """Execute a change directory command"""
        command = self.cd_command.get().strip()
        if not command:
            return
            
        try:
            # Handle special cases like cd ..
            if command == "..":
                new_dir = os.path.dirname(self.current_directory.get())
            elif command.startswith("/") or (platform.system() == "Windows" and re.match(r'^[a-zA-Z]:\\', command)):
                # Absolute path
                new_dir = command
            else:
                # Relative path
                new_dir = os.path.join(self.current_directory.get(), command)
                
            # Check if directory exists
            if os.path.isdir(new_dir):
                self.current_directory.set(os.path.abspath(new_dir))
                self.update_directory_content()
                self.cd_command.set("")
            else:
                messagebox.showerror("Error", f"Directory not found: {new_dir}")
        except Exception as e:
            messagebox.showerror("Error", f"Failed to change directory: {e}")

    def on_file_selected(self, event):
        """Handle file selection from listbox"""
        selection = self.files_listbox.curselection()
        if not selection:
            return
            
        item = self.files_listbox.get(selection[0])
        
        # Handle parent directory
        if item == "../ (Parent Directory)":
            self.current_directory.set(os.path.dirname(self.current_directory.get()))
            self.update_directory_content()
            return
            
        # Extract name without the icon
        if item.startswith("📁 "):
            # This is a directory
            dir_name = item[2:].strip().rstrip('/')
            new_dir = os.path.join(self.current_directory.get(), dir_name)
            self.current_directory.set(new_dir)
            self.update_directory_content()
        elif item.startswith(("📄 ", "🗎 ")):
            # This is a file
            file_name = item[2:].strip()
            self.selected_file_path.set(os.path.join(self.current_directory.get(), file_name))
            # Launch the editor with the selected file
            self.launch_editor()

    def on_recent_file_selected(self, event):
        """Handle selection from recent files list"""
        selection = self.recent_files_listbox.curselection()
        if not selection:
            return
            
        index = selection[0]
        if index < len(self.recent_files):
            file_path = self.recent_files[index]
            if os.path.exists(file_path):
                self.selected_file_path.set(file_path)
                self.current_directory.set(os.path.dirname(file_path))
                # Launch the editor with the selected file
                self.launch_editor()
            else:
                messagebox.showerror("Error", f"File not found: {file_path}")
                # Remove the file from recent files
                self.recent_files.pop(index)
                self.update_recent_files_display()
                self.save_recent_files()

    def browse_file(self):
        """Open file browser to select a file"""
        file_path = filedialog.askopenfilename(initialdir=self.current_directory.get())
        if file_path:
            self.selected_file_path.set(file_path)
            self.current_directory.set(os.path.dirname(file_path))
            self.update_directory_content()

    def browse_directory(self):
        """Open directory browser"""
        dir_path = filedialog.askdirectory(initialdir=self.current_directory.get())
        if dir_path:
            self.current_directory.set(dir_path)
            self.update_directory_content()

    def launch_editor(self):
        """Launch the selected editor with the selected file"""
        editor = self.selected_editor.get()
        file_path = self.selected_file_path.get()
        
        if not editor:
            messagebox.showerror("Error", "No editor selected.")
            return
            
        if not file_path:
            # If no file is selected, open the editor in the current directory
            file_path = self.current_directory.get()
        
        # Add to recent files if it's a file (not a directory)
        if os.path.isfile(file_path):
            self.add_recent_file(file_path)
        
        # Launch the editor in a separate thread to avoid blocking the UI
        threading.Thread(target=self._launch_editor_thread, args=(editor, file_path)).start()

    def _launch_editor_thread(self, editor, file_path):
        """Thread function to launch the editor"""
        try:
            # Handle path conversions for WSL
            converted_path = self.convert_path_for_platform(file_path)
            
            # Get the editor path
            editor_path = self.editor_paths.get(editor)
            
            if not editor_path:
                raise Exception(f"Editor {editor} not found.")
                
            # Construct and execute the command based on platform
            if platform.system() == "Windows":
                if editor in ["vim", "nvim", "nano", "gedit"]:
                    # If this is a WSL editor, use wsl command
                    cmd = f'wsl {editor} "{converted_path}"'
                    print(f"Launching WSL editor with command: {cmd}")
                    subprocess.Popen(cmd, shell=True)
                else:
                    # For GUI editors, just launch them directly
                    print(f"Launching Windows editor: {editor_path} with file {converted_path}")
                    subprocess.Popen([editor_path, converted_path])
            elif self.is_wsl:
                # For WSL, use wsl.exe if we're on Windows using WSL
                if platform.system() == "Windows":
                    cmd = f'wsl {editor_path} "{converted_path}"'
                    subprocess.Popen(cmd, shell=True)
                else:
                    # We're in WSL already
                    terminal_emulators = ["gnome-terminal", "konsole", "xterm"]
                    terminal = next((t for t in terminal_emulators if shutil.which(t)), None)
                    
                    if terminal:
                        if terminal == "gnome-terminal":
                            cmd = f'{terminal} -- {editor_path} "{converted_path}"'
                        else:
                            cmd = f'{terminal} -e "{editor_path} \'{converted_path}\'"'
                        subprocess.Popen(cmd, shell=True)
                    else:
                        # Direct launch as fallback
                        subprocess.Popen([editor_path, converted_path])
            else:
                # Regular Linux/Unix
                if editor in ["vim", "nvim", "nano"]:
                    # For terminal-based editors, use a terminal emulator
                    terminal_emulators = ["gnome-terminal", "konsole", "xterm"]
                    terminal = next((t for t in terminal_emulators if shutil.which(t)), None)
                    
                    if terminal:
                        if terminal == "gnome-terminal":
                            cmd = f'{terminal} -- {editor_path} "{converted_path}"'
                        else:
                            cmd = f'{terminal} -e "{editor_path} \'{converted_path}\'"'
                        subprocess.Popen(cmd, shell=True)
                    else:
                        # Direct launch as fallback
                        subprocess.Popen([editor_path, converted_path])
                else:
                    # For GUI editors, just launch them directly
                    subprocess.Popen([editor_path, converted_path])
        except Exception as e:
            # We're in a thread, so we need to schedule the error message in the main thread
            self.root.after(0, lambda: messagebox.showerror("Error", f"Failed to launch editor: {e}"))

    def convert_path_for_platform(self, path):
        """Convert file paths between different platforms (Windows, Linux, WSL)"""
        # If we're in WSL or Linux
        if platform.system() == "Linux":
            # If it's a Windows path (e.g. C:\path\to\file), convert to WSL path
            if re.match(r'^[a-zA-Z]:\\', path):
                # Convert Windows path to WSL path
                # E.g., C:\Users\user\file.txt -> /mnt/c/Users/user/file.txt
                drive = path[0].lower()
                wsl_path = f"/mnt/{drive}" + path[2:].replace('\\', '/')
                return wsl_path
            else:
                return path
        # If we're in Windows
        elif platform.system() == "Windows":
            # If it's a WSL path (e.g. /mnt/c/path/to/file), convert to Windows path
            if path.startswith('/mnt/') and len(path) > 6:                # Convert WSL path to Windows path
                # E.g., /mnt/c/Users/user/file.txt -> C:\Users\user\file.txt
                drive = path[5].upper()
                win_path = f"{drive}:" + path[6:].replace('/', '\\')
                return win_path
            else:
                return path
        else:            # On other platforms, return path as is
            return path
            
    def toggle_chatbot(self):
        """Show the AI chatbot assistant"""
        if hasattr(self, 'chatbot') and self.chatbot:
            self.chatbot.show_chatbot()
        else:
            messagebox.showinfo("AI Assistant", "Not available")
    
    def open_cmd_as_guest(self):
        """Open a command prompt as guest"""
        try:
            if platform.system() == "Windows":
                # Detect if PowerShell is the likely shell
                is_powershell = False
                try:
                    # Check if POWERSHELL_VERSION environment variable exists
                    if 'POWERSHELL_VERSION' in os.environ:
                        is_powershell = True
                    # Or try to get process info
                    elif os.environ.get('SHELL', '').lower().endswith('powershell.exe'):
                        is_powershell = True
                except:
                    pass
                
                # Get the properly formatted directory path for Windows
                dir_path = self.current_directory.get()
                
                if is_powershell:
                    # Use PowerShell command
                    subprocess.Popen(f'start powershell.exe -NoExit -Command "Set-Location -Path \'{dir_path}\'"', shell=True)
                else:
                    # Use CMD command
                    subprocess.Popen(f'start cmd.exe /K "cd /d {dir_path}"', shell=True)
            elif self.is_wsl:
                # If we're in WSL, open bash
                terminal_emulators = ["gnome-terminal", "konsole", "xterm"]
                terminal = next((t for t in terminal_emulators if shutil.which(t)), None)
                
                if terminal:
                    if terminal == "gnome-terminal":
                        cmd = f'{terminal} -- bash -c "cd \'{self.current_directory.get()}\'; exec bash"'
                    else:
                        cmd = f'{terminal} -e "bash -c \'cd \"{self.current_directory.get()}\"; exec bash\'"'
                    subprocess.Popen(cmd, shell=True)
                else:
                    # Fallback to xterm if no other terminal found
                    subprocess.Popen(["xterm", "-e", f"cd '{self.current_directory.get()}'; bash"])
            else:
                # Regular Linux/Unix
                terminal_emulators = ["gnome-terminal", "konsole", "xterm"]
                terminal = next((t for t in terminal_emulators if shutil.which(t)), None)
                
                if terminal:
                    if terminal == "gnome-terminal":
                        cmd = f'{terminal} -- bash -c "cd \'{self.current_directory.get()}\'; exec bash"'
                    else:
                        cmd = f'{terminal} -e "bash -c \'cd \"{self.current_directory.get()}\"; exec bash\'"'
                    subprocess.Popen(cmd, shell=True)
                else:
                    # Fallback to xterm if no other terminal found
                    subprocess.Popen(["xterm", "-e", f"cd '{self.current_directory.get()}'; bash"])
        except Exception as e:
            messagebox.showerror("Error", f"Failed to open terminal: {e}")

def main():
    """Main function to run the application"""
    root = tk.Tk()
    app = TextEditorLauncher(root)
    
    # Set window icon if available
    try:
        icon_path = os.path.join(os.path.dirname(os.path.abspath(__file__)), "editor_icon.ico")
        if os.path.exists(icon_path):
            root.iconbitmap(icon_path)
    except:
        pass
        
    root.mainloop()

if __name__ == "__main__":
    main()

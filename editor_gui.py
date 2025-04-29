import os
import subprocess
import tkinter as tk
from tkinter import filedialog, messagebox, ttk

class EditorLauncherGUI:
    def __init__(self, root):
        self.root = root
        self.root.title("Text Editor Launcher")
        self.root.geometry("700x600")
        
        # Brown color palette
        self.colors = {
            "dark_brown": "#5D4037",
            "medium_brown": "#8D6E63",
            "light_brown": "#D7CCC8",
            "cream": "#EFEBE9",
            "accent": "#A1887F"
        }
        
        self.root.configure(bg=self.colors["cream"])
        
        self.editors = []
        self.recent_files = []
        self.max_recent = 5
        self.current_dir = os.getcwd()
        
        # Initialize status_var here
        self.status_var = tk.StringVar()
        self.status_var.set("Ready")
        
        self.check_editors()
        self.create_widgets()
    
    def check_editors(self):
        """Check which editors are installed"""
        editors_to_check = ["vim", "nvim", "nano", "gedit"]
        self.editors = []  # Clear the editors list

        for editor in editors_to_check:
            try:
                # Check if the editor is available
                if os.name == 'nt':  # Windows
                    subprocess.run(["where", editor], check=True, stdout=subprocess.PIPE, stderr=subprocess.PIPE)
                else:  # Unix/Linux/Mac
                    subprocess.run(["which", editor], check=True, stdout=subprocess.PIPE, stderr=subprocess.PIPE)
                self.editors.append(editor)
            except subprocess.CalledProcessError:
                # Editor not found, continue checking others
                pass

        # If no editors are found, show a message
        if not self.editors:
            messagebox.showwarning(
                "No Editors Found",
                "No compatible editors (vim, nvim, nano, gedit) were found on your system. "
                "Please install one of these editors to use this application."
            )
    
    def create_widgets(self):
        """Create all GUI elements"""
        # Main frame
        main_frame = tk.Frame(self.root, bg=self.colors["cream"], bd=2, relief=tk.RIDGE)
        main_frame.pack(fill=tk.BOTH, expand=True, padx=20, pady=20)
        
        # Title with stylish font
        title_label = tk.Label(main_frame, text="Terminal Text Editor Launcher", 
                             font=("Georgia", 20, "bold"), bg=self.colors["cream"],
                             fg=self.colors["dark_brown"])
        title_label.pack(pady=15)
        
        # Shortcut button to open Command Prompt as a guest
        cmd_button = tk.Button(main_frame, text="Open CMD as Guest", command=self.open_cmd_as_guest,
                             bg=self.colors["medium_brown"], fg="white", font=("Georgia", 12), 
                             width=20, bd=0, padx=10, pady=8, activebackground=self.colors["dark_brown"])
        cmd_button.pack(pady=10, anchor="e")  # Align to the right side
        
        # Editors section with improved styling
        editors_frame = tk.LabelFrame(main_frame, text="Available Editors", 
                                    font=("Georgia", 12, "bold"), bg=self.colors["light_brown"],
                                    fg=self.colors["dark_brown"], padx=15, pady=15)
        editors_frame.pack(fill=tk.X, pady=15, padx=10)
        
        self.editor_var = tk.StringVar()
        if self.editors:
            self.editor_var.set(self.editors[0])
            for i, editor in enumerate(self.editors):
                # Format display name based on editor
                display_name = editor
                if editor == "vim" or editor == "nvim":
                    display_name = "Vim/NeoVim"
                elif editor == "nano":
                    display_name = "Nano"
                elif editor == "gedit":
                    display_name = "Gedit"
                
                rb = tk.Radiobutton(editors_frame, text=display_name, variable=self.editor_var, 
                                  value=editor, bg=self.colors["light_brown"], 
                                  fg=self.colors["dark_brown"],
                                  font=("Georgia", 11), selectcolor=self.colors["accent"])
                rb.grid(row=0, column=i, padx=25, pady=5)
        else:
            no_editors = tk.Label(editors_frame, text="No compatible editors found!", 
                                fg="red", bg=self.colors["light_brown"], font=("Georgia", 11))
            no_editors.pack(pady=5)
        
        # Directory navigation section
        dir_frame = tk.LabelFrame(main_frame, text="Directory Navigation", 
                                font=("Georgia", 12, "bold"), bg=self.colors["light_brown"],
                                fg=self.colors["dark_brown"], padx=15, pady=15)
        dir_frame.pack(fill=tk.X, pady=15, padx=10)
        
        # Current directory display
        current_dir_label = tk.Label(dir_frame, text="Current Directory:", 
                                   bg=self.colors["light_brown"], fg=self.colors["dark_brown"],
                                   font=("Georgia", 11))
        current_dir_label.grid(row=0, column=0, padx=5, pady=5, sticky="w")
        
        self.current_dir_var = tk.StringVar()
        self.current_dir_var.set(self.current_dir)
        current_dir_entry = tk.Entry(dir_frame, textvariable=self.current_dir_var, 
                                   width=50, font=("Consolas", 10), bg=self.colors["cream"])
        current_dir_entry.grid(row=0, column=1, padx=5, pady=5, sticky="ew")
        
        # CD command entry
        cd_label = tk.Label(dir_frame, text="CD Command:", 
                          bg=self.colors["light_brown"], fg=self.colors["dark_brown"],
                          font=("Georgia", 11))
        cd_label.grid(row=1, column=0, padx=5, pady=5, sticky="w")
        
        self.cd_entry = tk.Entry(dir_frame, width=40, font=("Consolas", 10), bg=self.colors["cream"])
        self.cd_entry.grid(row=1, column=1, padx=5, pady=5, sticky="ew")
        
        cd_btn = tk.Button(dir_frame, text="Change Dir", command=self.change_directory,
                         bg=self.colors["medium_brown"], fg="white", font=("Georgia", 10), 
                         activebackground=self.colors["dark_brown"], bd=0, padx=10, pady=5)
        cd_btn.grid(row=1, column=2, padx=5, pady=5)
        
        # File listing
        files_label = tk.Label(dir_frame, text="Files:", 
                             bg=self.colors["light_brown"], fg=self.colors["dark_brown"],
                             font=("Georgia", 11))
        files_label.grid(row=2, column=0, padx=5, pady=5, sticky="nw")
        
        # File listbox with scrollbar
        file_frame = tk.Frame(dir_frame, bg=self.colors["light_brown"])
        file_frame.grid(row=2, column=1, columnspan=2, padx=5, pady=5, sticky="nsew")
        
        self.file_listbox = tk.Listbox(file_frame, width=60, height=8, font=("Consolas", 10),
                                     bg=self.colors["cream"], selectbackground=self.colors["medium_brown"])
        self.file_listbox.pack(side=tk.LEFT, fill=tk.BOTH, expand=True)
        
        scrollbar = tk.Scrollbar(file_frame, orient="vertical", command=self.file_listbox.yview)
        scrollbar.pack(side=tk.RIGHT, fill=tk.Y)
        self.file_listbox.config(yscrollcommand=scrollbar.set)
        
        self.file_listbox.bind("<Double-1>", self.select_file)
        
        # Update the file list
        self.update_file_list()
        
        # Recent files section
        recent_frame = tk.LabelFrame(main_frame, text="Recent Files", 
                                   font=("Georgia", 12, "bold"), bg=self.colors["light_brown"],
                                   fg=self.colors["dark_brown"], padx=15, pady=15)
        recent_frame.pack(fill=tk.X, pady=15, padx=10)
        
        self.recent_listbox = tk.Listbox(recent_frame, width=70, height=5, font=("Consolas", 10),
                                       bg=self.colors["cream"], selectbackground=self.colors["medium_brown"])
        self.recent_listbox.pack(fill=tk.X, padx=5, pady=5)
        self.recent_listbox.bind("<Double-1>", self.open_recent_file)
        
        # Action buttons with improved styling
        button_frame = tk.Frame(main_frame, bg=self.colors["cream"])
        button_frame.pack(fill=tk.X, pady=20)
        
        open_btn = tk.Button(button_frame, text="Open File", command=self.open_selected_file,
                          bg=self.colors["dark_brown"], fg="white", font=("Georgia", 12, "bold"), 
                          width=15, bd=0, padx=10, pady=8, activebackground=self.colors["medium_brown"])
        open_btn.pack(side=tk.LEFT, padx=20)
        
        refresh_btn = tk.Button(button_frame, text="Refresh", command=self.update_file_list,
                             bg=self.colors["medium_brown"], fg="white", font=("Georgia", 12),
                             width=10, bd=0, padx=10, pady=8, activebackground=self.colors["dark_brown"])
        refresh_btn.pack(side=tk.LEFT, padx=20)
        
        help_btn = tk.Button(button_frame, text="Editor Help", command=self.show_help,
                          bg=self.colors["medium_brown"], fg="white", font=("Georgia", 12), 
                          width=10, bd=0, padx=10, pady=8, activebackground=self.colors["dark_brown"])
        help_btn.pack(side=tk.LEFT, padx=20)
        
        exit_btn = tk.Button(button_frame, text="Exit", command=self.root.quit,
                          bg=self.colors["dark_brown"], fg="white", font=("Georgia", 12), 
                          width=10, bd=0, padx=10, pady=8, activebackground=self.colors["medium_brown"])
        exit_btn.pack(side=tk.LEFT, padx=20)
        
        # Status bar with style
        self.status_var = tk.StringVar()
        self.status_var.set("Ready")
        status_bar = tk.Label(self.root, textvariable=self.status_var, bd=1, relief=tk.SUNKEN, 
                            anchor=tk.W, font=("Consolas", 10), bg=self.colors["dark_brown"], fg="white")
        status_bar.pack(side=tk.BOTTOM, fill=tk.X)
    
    def update_file_list(self):
        """Update the file list based on current directory"""
        try:
            self.file_listbox.delete(0, tk.END)
            
            # Add parent directory option
            self.file_listbox.insert(tk.END, "../ (Parent Directory)")
            
            # List directories first
            for item in sorted(os.listdir(self.current_dir)):
                full_path = os.path.join(self.current_dir, item)
                if os.path.isdir(full_path):
                    self.file_listbox.insert(tk.END, f"📁 {item}/")
            
            # Then list files
            for item in sorted(os.listdir(self.current_dir)):
                full_path = os.path.join(self.current_dir, item)
                if os.path.isfile(full_path):
                    self.file_listbox.insert(tk.END, f"📄 {item}")
                    
            self.current_dir_var.set(self.current_dir)
            self.status_var.set(f"Directory: {self.current_dir}")
            
        except Exception as e:
            messagebox.showerror("Error", f"Failed to list directory: {str(e)}")
            self.status_var.set("Error listing directory")
    
    def change_directory(self):
        """Change directory based on CD command"""
        cd_command = self.cd_entry.get().strip()
        if not cd_command:
            return
            
        try:
            # Handle relative paths
            if cd_command.startswith("/"):
                # Absolute path
                new_dir = cd_command
            else:
                # Relative path
                new_dir = os.path.join(self.current_dir, cd_command)
                
            # Normalize path
            new_dir = os.path.normpath(new_dir)
            
            if os.path.isdir(new_dir):
                self.current_dir = new_dir
                self.update_file_list()
                self.cd_entry.delete(0, tk.END)
            else:
                messagebox.showerror("Error", f"Directory not found: {new_dir}")
        except Exception as e:
            messagebox.showerror("Error", f"Failed to change directory: {str(e)}")
    
    def select_file(self, event):
        """Handle double-click on file list"""
        if self.file_listbox.curselection():
            index = self.file_listbox.curselection()[0]
            item = self.file_listbox.get(index)
            
            if item == "../ (Parent Directory)":
                # Go to parent directory
                self.current_dir = os.path.dirname(self.current_dir)
                self.update_file_list()
                return
                
            if item.startswith("📁"):
                # It's a directory, change to it
                dir_name = item[2:].strip()[:-1]  # Remove emoji and trailing slash
                new_dir = os.path.join(self.current_dir, dir_name)
                if os.path.isdir(new_dir):
                    self.current_dir = new_dir
                    self.update_file_list()
            elif item.startswith("📄"):
                # It's a file, select it for opening
                file_name = item[2:].strip()
                self.open_file(os.path.join(self.current_dir, file_name))
    
    def open_selected_file(self):
        """Open the currently selected file"""
        if self.file_listbox.curselection():
            index = self.file_listbox.curselection()[0]
            item = self.file_listbox.get(index)
            
            if item.startswith("📄"):
                file_name = item[2:].strip()
                self.open_file(os.path.join(self.current_dir, file_name))
    
    def open_recent_file(self, event):
        """Handle double-click on recent file"""
        if self.recent_listbox.curselection():
            index = self.recent_listbox.curselection()[0]
            file_path = self.recent_files[index]
            
            if os.path.isfile(file_path):
                self.open_file(file_path)
            else:
                messagebox.showerror("Error", f"File no longer exists: {file_path}")
                # Remove from recent files
                self.recent_files.pop(index)
                self.update_recent_files()
    
    def add_recent_file(self, file_path):
        """Add file to recent files list"""
        if file_path in self.recent_files:
            self.recent_files.remove(file_path)
        self.recent_files.insert(0, file_path)
        if len(self.recent_files) > self.max_recent:
            self.recent_files = self.recent_files[:self.max_recent]
        
        # Update listbox
        self.update_recent_files()
    
    def update_recent_files(self):
        """Update the recent files listbox"""
        self.recent_listbox.delete(0, tk.END)
        for file in self.recent_files:
            self.recent_listbox.insert(tk.END, file)
    
    def open_file(self, file_path):
        """Open the selected file with the chosen editor"""
        editor = self.editor_var.get()
        
        if not editor or not self.editors:
            messagebox.showerror("Error", "No editor selected or available!")
            return
        
        try:
            # Preview file if it exists
            if os.path.isfile(file_path):
                self.preview_file(file_path)
            
            # Add to recent files
            self.add_recent_file(file_path)
            
            # Launch editor (different methods based on OS)
            self.status_var.set(f"Opening {file_path} with {editor}...")
            
            if os.name == 'nt':  # Windows
                if editor in ['nano', 'vim', 'nvim']:
                    # For Windows with GitBash
                    subprocess.Popen(["bash", "-c", f"{editor} \"{file_path}\""])
                else:
                    # For regular Windows apps
                    subprocess.Popen([editor, file_path])
            else:
                # For Unix/Linux/Mac
                subprocess.Popen([editor, file_path])
                
            self.status_var.set(f"Opened {file_path} with {editor}")
        except Exception as e:
            messagebox.showerror("Error", f"Failed to open file: {str(e)}")
            self.status_var.set("Error opening file")
    
    def preview_file(self, file_path):
        """Show a preview of the file"""
        try:
            with open(file_path, 'r') as f:
                lines = f.readlines()[:15]  # Get first 15 lines
            
            preview_window = tk.Toplevel(self.root)
            preview_window.title(f"Preview: {os.path.basename(file_path)}")
            preview_window.geometry("700x400")
            preview_window.configure(bg=self.colors["cream"])
            
            # Title frame
            title_frame = tk.Frame(preview_window, bg=self.colors["dark_brown"], padx=10, pady=10)
            title_frame.pack(fill=tk.X)
            
            title_label = tk.Label(title_frame, text=f"File Preview: {os.path.basename(file_path)}", 
                                 font=("Georgia", 14, "bold"), fg="white", bg=self.colors["dark_brown"])
            title_label.pack()
            
            # Preview text
            preview_frame = tk.Frame(preview_window, bg=self.colors["cream"], padx=15, pady=15)
            preview_frame.pack(fill=tk.BOTH, expand=True)
            
            preview_text = tk.Text(preview_frame, wrap=tk.WORD, font=("Consolas", 11), 
                                 bg=self.colors["cream"], fg=self.colors["dark_brown"],
                                 padx=10, pady=10)
            preview_text.pack(fill=tk.BOTH, expand=True)
            
            for line in lines:
                preview_text.insert(tk.END, line)
            preview_text.config(state=tk.DISABLED)
            
            # Button frame
            button_frame = tk.Frame(preview_window, bg=self.colors["cream"], pady=15)
            button_frame.pack()
            
            ok_button = tk.Button(button_frame, text="OK", command=preview_window.destroy,
                               bg=self.colors["medium_brown"], fg="white", font=("Georgia", 12),
                               width=10, bd=0, padx=10, pady=5)
            ok_button.pack()
            
        except Exception as e:
            messagebox.showerror("Preview Error", f"Could not preview file: {str(e)}")
    
    def show_help(self):
        """Show editor help information"""
        help_window = tk.Toplevel(self.root)
        help_window.title("Editor Help")
        help_window.geometry("600x500")
        help_window.configure(bg=self.colors["cream"])
        
        # Title frame
        title_frame = tk.Frame(help_window, bg=self.colors["dark_brown"], padx=10, pady=10)
        title_frame.pack(fill=tk.X)
        
        title_label = tk.Label(title_frame, text="Text Editor Help", 
                             font=("Georgia", 16, "bold"), fg="white", bg=self.colors["dark_brown"])
        title_label.pack()
        
        # Help content
        notebook = ttk.Notebook(help_window)
        notebook.pack(fill=tk.BOTH, expand=True, padx=15, pady=15)
        
        # Configure notebook style
        style = ttk.Style()
        style.configure("TNotebook", background=self.colors["cream"])
        style.configure("TNotebook.Tab", background=self.colors["light_brown"], 
                      font=("Georgia", 11))
        style.map("TNotebook.Tab", background=[("selected", self.colors["medium_brown"])])
        
        editors = {
            "Vim/NeoVim": [
                "i: Enter insert mode",
                "Esc: Return to normal mode",
                ":w: Save file",
                ":q: Quit",
                ":wq: Save and quit",
                "u: Undo",
                "dd: Delete line",
                "/text: Search for 'text'",
                "n: Next search result",
                ":%s/old/new/g: Replace all 'old' with 'new'"
            ],
            "Nano": [
                "Ctrl+O: Save file",
                "Ctrl+X: Exit",
                "Ctrl+W: Search text",
                "Ctrl+G: Get help",
                "Ctrl+K: Cut line",
                "Ctrl+U: Paste",
                "Ctrl+V: Next page",
                "Ctrl+Y: Previous page",
                "Alt+A: Start selection",
                "Ctrl+^: Mark text"
            ],
            "Gedit": [
                "Standard GUI editor",
                "Ctrl+S: Save file",
                "Ctrl+Q: Quit",
                "Ctrl+F: Find text",
                "Ctrl+H: Find and replace",
                "Ctrl+A: Select all",
                "Ctrl+Z: Undo",
                "Ctrl+Shift+Z: Redo",
                "Tab: Indent",
                "Shift+Tab: Unindent"
            ]
        }
        
        for editor, commands in editors.items():
            frame = tk.Frame(notebook, bg=self.colors["cream"], padx=15, pady=15)
            notebook.add(frame, text=editor)
            
            for i, cmd in enumerate(commands):
                label = tk.Label(frame, text=cmd, font=("Consolas", 12), 
                               anchor="w", bg=self.colors["cream"], fg=self.colors["dark_brown"])
                label.pack(fill=tk.X, padx=20, pady=5, anchor="w")

    def open_cmd_as_guest(self):
        """Open Command Prompt as a guest (Windows only)."""
        try:
            if os.name == 'nt':  # Check if the OS is Windows
                subprocess.Popen("cmd.exe")
                self.status_var.set("Opened Command Prompt as Guest")
            else:
                messagebox.showerror("Error", "This feature is only available on Windows.")
        except Exception as e:
            messagebox.showerror("Error", f"Failed to open Command Prompt: {str(e)}")
            self.status_var.set("Error opening Command Prompt")
if __name__ == "__main__":
    root = tk.Tk()
    app = EditorLauncherGUI(root)
    root.mainloop()

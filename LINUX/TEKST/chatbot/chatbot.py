import tkinter as tk
import threading
import json
import urllib.request
import urllib.parse
import datetime
import time

class Chatbot:
    """A chatbot class to handle message interactions with an AI API"""
    def __init__(self, parent, colors, create_toggle_button=True):
        self.parent = parent
        self.colors = colors
        self.messages = []
        self.api_key = "AIzaSyAbbapE0edChzDmtx6aNQ3AO3ZQk_N0iO8"  # Gemini API key
        self.api_url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent"
        
        # Create the chatbot container which will start hidden
        self.create_chatbot_ui(create_toggle_button)
        
    def create_chatbot_ui(self, create_toggle_button=True):
        """Create the chatbot interface elements"""
        # Chatbot floating window
        self.chatbot_window = tk.Toplevel(self.parent)
        self.chatbot_window.title("Rizzard AI Assistant")
        self.chatbot_window.geometry("400x500")
        self.chatbot_window.configure(bg=self.colors["cream"])
        self.chatbot_window.withdraw()  # Start hidden
        self.chatbot_window.protocol("WM_DELETE_WINDOW", self.hide_chatbot)
        
        # Make sure it stays on top but can be minimized
        self.chatbot_window.attributes("-topmost", True)
        
        # Chatbot header
        header_frame = tk.Frame(self.chatbot_window, bg=self.colors["dark_brown"], padx=10, pady=10)
        header_frame.pack(fill=tk.X)
        
        title_label = tk.Label(header_frame, text="Rizzard AI Assistant", 
                             font=("Georgia", 14, "bold"), fg="white", bg=self.colors["dark_brown"])
        title_label.pack(side=tk.LEFT)
        
        close_btn = tk.Button(header_frame, text="×", command=self.hide_chatbot,
                            font=("Arial", 14, "bold"), bg=self.colors["dark_brown"], 
                            fg="white", bd=0, padx=5)
        close_btn.pack(side=tk.RIGHT)
        
        # Messages container with scrollbar
        self.message_frame = tk.Frame(self.chatbot_window, bg=self.colors["cream"])
        self.message_frame.pack(fill=tk.BOTH, expand=True, padx=10, pady=10)
        
        self.canvas = tk.Canvas(self.message_frame, bg=self.colors["cream"], highlightthickness=0)
        self.scrollbar = tk.Scrollbar(self.message_frame, orient="vertical", command=self.canvas.yview)
        self.canvas.configure(yscrollcommand=self.scrollbar.set)
        self.canvas.pack(side=tk.LEFT, fill=tk.BOTH, expand=True)
        self.scrollbar.pack(side=tk.RIGHT, fill=tk.Y)
        
        self.messages_container = tk.Frame(self.canvas, bg=self.colors["cream"])
        self.messages_container.bind(
            "<Configure>",
            lambda e: self.canvas.configure(scrollregion=self.canvas.bbox("all"))
        )
        
        # Replace the mouse wheel binding
        def _on_mousewheel(event):
            self.canvas.yview_scroll(-1 * (event.delta // 120), "units")
        
        self.canvas.bind("<MouseWheel>", _on_mousewheel)
        self.messages_container.bind("<MouseWheel>", _on_mousewheel)
        
        self.canvas.create_window((0, 0), window=self.messages_container, anchor="nw")
        self.canvas.configure(yscrollcommand=self.scrollbar.set)
        
        self.canvas.pack(side=tk.LEFT, fill=tk.BOTH, expand=True)
        self.scrollbar.pack(side=tk.RIGHT, fill=tk.Y)
        
        # Add welcome message
        self.add_message("Hello! I'm your Rizzard AI Assistant. How can I help you today?", "bot")
        
        # Input area
        input_frame = tk.Frame(self.chatbot_window, bg=self.colors["light_brown"], padx=10, pady=10)
        input_frame.pack(fill=tk.X, side=tk.BOTTOM)
        
        self.chat_input = tk.Entry(input_frame, font=("Consolas", 11), bg="white", width=30)
        self.chat_input.pack(side=tk.LEFT, fill=tk.X, expand=True, padx=(0, 10))
        self.chat_input.bind("<Return>", lambda event: self.send_message())
        
        send_btn = tk.Button(input_frame, text="Send", command=self.send_message,
                          bg=self.colors["medium_brown"], fg="white", font=("Georgia", 11),
                          bd=0, padx=10, pady=5)
        send_btn.pack(side=tk.RIGHT)
        
        # Create a frame for the chatbot toggle button (positioned at bottom right)
        self.toggle_frame = tk.Frame(self.parent, bg=self.colors["cream"])
        self.toggle_frame.pack(side=tk.BOTTOM, anchor=tk.SE, padx=20, pady=20)
        
        # Chatbot toggle button with icon at bottom right
        self.toggle_btn = tk.Button(
            self.toggle_frame, 
            text="💬", 
            command=self.show_chatbot,
            bg=self.colors["medium_brown"], 
            fg="white", 
            font=("Arial", 16, "bold"),
            bd=0, 
            width=3, 
            height=2,
            relief=tk.RAISED,
            borderwidth=0,
            highlightthickness=0
        )
        self.toggle_btn.pack(pady=5)
    
    def show_chatbot(self):
        """Show the chatbot window"""
        # Position it at the bottom right of the main window
        x = self.parent.winfo_x() + self.parent.winfo_width() - 420
        y = self.parent.winfo_y() + self.parent.winfo_height() - 550
        self.chatbot_window.geometry(f"400x500+{x}+{y}")
        self.chatbot_window.deiconify()
        self.chat_input.focus()
    
    def hide_chatbot(self):
        """Hide the chatbot window"""
        self.chatbot_window.withdraw()
    
    def add_message(self, content, sender, message_id=None):
        """Add a message to the chat"""
        # Create frame for this message
        msg_frame = tk.Frame(self.messages_container, bg=self.colors["cream"], padx=5, pady=5)
        msg_frame.pack(fill=tk.X, pady=5)
        
        if message_id:
            msg_frame.message_id = message_id
        
        # Calculate timestamp
        timestamp = datetime.datetime.now().strftime("%I:%M %p")
        
        # Style based on sender
        if sender == "user":
            msg_bg = self.colors["medium_brown"]
            msg_fg = "white"
            msg_frame.pack_configure(anchor="e")
        else:  # bot
            msg_bg = self.colors["light_brown"]
            msg_fg = self.colors["dark_brown"]
            msg_frame.pack_configure(anchor="w")
        
        # Message bubble with text wrapping
        msg_bubble = tk.Frame(msg_frame, bg=msg_bg, padx=10, pady=8)
        msg_bubble.pack(side=tk.TOP)
        
        msg_text = tk.Label(msg_bubble, text=content, font=("Consolas", 10), fg=msg_fg, 
                          bg=msg_bg, justify=tk.LEFT, wraplength=250)
        msg_text.pack(fill=tk.X)
        
        # Timestamp
        time_label = tk.Label(msg_frame, text=timestamp, font=("Consolas", 8), 
                           fg=self.colors["medium_brown"], bg=self.colors["cream"])
        time_label.pack(side=tk.BOTTOM, anchor="e" if sender == "user" else "w")
        
        # Auto-scroll to the new message
        self.canvas.update_idletasks()
        self.canvas.yview_moveto(1.0)
        
        return msg_frame
    
    def remove_message(self, message_id):
        """Remove a message by its ID"""
        for widget in self.messages_container.winfo_children():
            if hasattr(widget, "message_id") and widget.message_id == message_id:
                widget.destroy()
                return
    
    def send_message(self):
        """Process and send the user message"""
        # Get the message from the input field
        message = self.chat_input.get().strip()
        if not message:
            return
            
        # Clear the input field
        self.chat_input.delete(0, tk.END)
        
        # Add user message to the chat
        self.add_message(message, "user")
        
        # Show a loading message
        loading_id = str(time.time())
        loading_msg = self.add_message("Thinking...", "bot", loading_id)
        
        # Start a thread for API call to avoid freezing the UI
        threading.Thread(target=self.fetch_ai_response, args=(message, loading_id), daemon=True).start()
    
    def fetch_ai_response(self, message, loading_id):
        """Make an API call to get a response"""
        try:
            # Create request data
            data = {
                "contents": [
                    {
                        "parts": [
                            {"text": message}
                        ]
                    }
                ]
            }
            
            # Prepare the request
            url = f"{self.api_url}?key={self.api_key}"
            headers = {"Content-Type": "application/json"}
            
            # Make the API call
            req = urllib.request.Request(
                url,
                data=json.dumps(data).encode("utf-8"),
                headers=headers,
                method="POST"
            )
            
            # Get the response
            with urllib.request.urlopen(req) as response:
                result = json.loads(response.read().decode("utf-8"))
            
            # Extract the response text
            if (result and "candidates" in result and len(result["candidates"]) > 0 
                and "content" in result["candidates"][0] 
                and "parts" in result["candidates"][0]["content"]):
                response_text = result["candidates"][0]["content"]["parts"][0]["text"]
            else:
                response_text = "Sorry, I couldn't generate a response. Please try again."
                
            # Remove the loading message and add the actual response
            self.parent.after(0, lambda: self.remove_message(loading_id))
            self.parent.after(0, lambda: self.add_message(response_text, "bot"))
            
        except Exception as e:
            # Handle errors
            error_message = f"Sorry, I encountered an error: {str(e)}"
            self.parent.after(0, lambda: self.remove_message(loading_id))
            self.parent.after(0, lambda: self.add_message(error_message, "bot"))
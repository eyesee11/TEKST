![TEKST Banner](https://github.com/user-attachments/assets/your-banner-id-here)

# TEKST-Advanced JAVA Text Editor

A feature-rich, multi-tab Java text editor with rich text formatting capabilities, pixelated themes, and modern UI components built using Swing.

## 🚀 Features

### **Multi-Tab Interface**
- **Tabbed Document Management**: Work with multiple documents simultaneously in separate tabs
- **New Tab Creation**: Press `Ctrl+N` to create new untitled tabs instantly
- **Tab Switching**: Click on tabs or use keyboard shortcuts to switch between documents
- **Unsaved Changes Detection**: Smart detection of modified documents with confirmation dialogs when closing unsaved tabs
- **Individual Document State**: Each tab maintains its own formatting, undo/redo history, and document properties

### **Rich Text Formatting**
- **Advanced Text Styling**: Full rich text support with JTextPane for sophisticated document formatting
- **Interactive Formatting Popup**: Right-click on selected text to access formatting options
- **Text Formatting Options**:
  - **Bold, Italic, Underline**: Standard text styling options
  - **Font Family Selection**: Choose from Consolas, Arial, Times New Roman, Courier New, Verdana, Tahoma
  - **Font Size Control**: Adjustable font sizes from 8pt to 32pt
  - **Text Color**: Full color picker for text color customization
  - **Text Highlighting**: Background color highlighting with color picker
- **Context-Sensitive Formatting**: Formatting popup appears when text is selected via right-click or keyboard shortcuts

### **Pixelated Theme System**
- **Multiple Visual Themes**: 7 unique pixelated themes for retro aesthetics
  - **Normal (Default)**: Clean, modern interface
  - **Dark Retro**: Classic dark theme with retro pixelated fonts
  - **Neon Synthwave**: Vibrant neon colors with synthwave aesthetics
  - **Forest Pixel**: Nature-inspired green theme
  - **Ocean Wave**: Calming blue ocean theme
  - **Sunset Arcade**: Warm sunset colors with arcade vibes
  - **Matrix Hacker**: Green-on-black matrix-style theme
- **Complete UI Theming**: Themes apply to all components including menus, buttons, text areas, and popups
- **Pixelated Fonts**: Authentic retro fonts for non-normal themes
- **Dynamic Theme Switching**: Change themes instantly through the View menu

### **Comprehensive File Operations**
- **Multi-Tab File Handling**: Open, save, and manage files across multiple tabs
- **File Menu Operations**:
  - **New Document** (`Ctrl+N`): Create new untitled documents in new tabs
  - **Open Document** (`Ctrl+O`): Open existing files in new tabs
  - **Save Document** (`Ctrl+S`): Save current tab's document
  - **Save As** (`Ctrl+Shift+S`): Save current document with new name/location
  - **Close Tab** (`Ctrl+W`): Close current tab with unsaved changes confirmation
  - **Exit Application** (`Ctrl+Q`): Exit with confirmation for unsaved changes
- **Smart File Management**: Automatic detection of file modifications and appropriate save prompts

### **Advanced Editing Features**
- **Unlimited Undo/Redo**: Full undo/redo support per tab with `Ctrl+Z`/`Ctrl+Y`
- **Find and Replace** (`Ctrl+F`): Comprehensive text search and replacement functionality
- **Text Selection Operations**: Cut (`Ctrl+X`), Copy (`Ctrl+C`), Paste (`Ctrl+V`), Select All (`Ctrl+A`)
- **Per-Tab Editing State**: Each tab maintains independent undo history and editing state

### **Professional UI Components**
- **Enhanced Menu Bar**: Complete menu system with File, Edit, View, and Help menus
- **Icon-Rich Toolbar**: Visual toolbar with emoji icons and tooltips for quick access
- **Status Bar**: Real-time display of document information and cursor position
- **Themed Dialogs**: All dialogs match the selected pixelated theme
- **Modern Layout**: Professional BorderLayout with integrated components

### **Technical Architecture**
- **MVC Pattern**: Clean separation between UI, controllers, and document models
- **TabManager System**: Sophisticated multi-tab management with individual DocumentManagers
- **DocumentManager Per Tab**: Each tab has its own document state, formatting, and undo history
- **FormattingPopup Per Tab**: Individual formatting popups for each tab's text content
- **Theme Integration**: Comprehensive theming system affecting all UI components
- **Controller Integration**: FileController and EditController work seamlessly with multi-tab interface

## 🎯 Keyboard Shortcuts

### File Operations
- `Ctrl+N` - New Document (creates new tab)
- `Ctrl+O` - Open Document
- `Ctrl+S` - Save Document
- `Ctrl+Shift+S` - Save As
- `Ctrl+W` - Close Current Tab
- `Ctrl+Q` - Exit Application

### Edit Operations
- `Ctrl+Z` - Undo
- `Ctrl+Y` - Redo
- `Ctrl+F` - Find and Replace
- `Ctrl+X` - Cut
- `Ctrl+C` - Copy
- `Ctrl+V` - Paste
- `Ctrl+A` - Select All

### Formatting
- Right-click on selected text - Open formatting popup
- Double-click word selection - Quick word selection for formatting

## 🛠️ Requirements

- **Java 21 LTS** or higher
- **Maven 3.6** or higher
- **Operating System**: Windows, macOS, or Linux with GUI support

## 🚀 Build and Run

1. **Clone the repository**:
   ```bash
   git clone <repository-url>
   cd text_editor
   ```

2. **Build the project**:
   ```bash
   mvn clean compile
   ```

3. **Run the application**:
   ```bash
   mvn exec:java -Dexec.mainClass="com.texteditor.Main"
   ```

4. **Create executable JAR** (optional):
   ```bash
   mvn clean package
   java -jar target/text-editor-1.0-SNAPSHOT.jar
   ```

## 📁 Project Structure

```
src/
├── main/
│   └── java/
│       └── com/
│           └── texteditor/
│               ├── Main.java                    # Application entry point
│               ├── controller/
│               │   ├── FileController.java     # Multi-tab file operations
│               │   └── EditController.java     # Text editing operations
│               ├── model/
│               │   └── DocumentManager.java    # Document state management
│               └── ui/
│                   ├── MainWindow.java         # Main application window
│                   ├── MenuBar.java            # Complete menu system
│                   ├── ToolBar.java            # Icon toolbar
│                   ├── StatusBar.java          # Status information
│                   ├── TabManager.java         # Multi-tab management
│                   ├── FormattingPopup.java    # Rich text formatting
│                   └── themes/
│                       ├── ThemeManager.java       # Theme management system
│                       ├── PixelatedTheme.java     # Base theme class
│                       ├── NormalTheme.java        # Default theme
│                       ├── DarkRetroTheme.java     # Dark pixelated theme
│                       ├── NeonSynthwaveTheme.java # Neon synthwave theme
│                       ├── ForestPixelTheme.java   # Forest theme
│                       ├── OceanWaveTheme.java     # Ocean theme
│                       ├── SunsetArcadeTheme.java  # Sunset theme
│                       ├── MatrixHackerTheme.java  # Matrix theme
│                       └── ThemedDialogs.java      # Themed dialog utilities
└── test/
    └── java/
        └── com/
            └── texteditor/
                └── MainTest.java               # Unit tests
```

## 🎨 Usage Guide

### Getting Started
1. Launch the application
2. Use `Ctrl+N` to create a new document tab
3. Start typing in the rich text editor
4. Select text and right-click to access formatting options

### Working with Multiple Tabs
- Create new tabs with `Ctrl+N`
- Switch between tabs by clicking on tab headers
- Each tab maintains independent document state
- Close tabs with `Ctrl+W` (with unsaved changes confirmation)

### Applying Rich Text Formatting
1. Select text you want to format
2. Right-click to open the formatting popup
3. Choose from bold, italic, underline, font family, size, colors
4. Formatting is applied immediately to selected text

### Changing Themes
1. Go to **View** menu → **Themes**
2. Choose from 7 available pixelated themes
3. Theme applies instantly to entire interface

### File Management
- Save documents with `Ctrl+S`
- Open existing files with `Ctrl+O` (opens in new tab)
- Use "Save As" for creating copies or renaming

## 🔧 Technical Details

- **Architecture**: MVC (Model-View-Controller) pattern with multi-tab support
- **Rich Text Engine**: JTextPane with StyledDocument for advanced formatting
- **Theme System**: Comprehensive theming affecting all UI components
- **Tab Management**: Professional tab interface with individual document states
- **Memory Management**: Efficient handling of multiple documents and formatting states

## 📝 License

This project is open source and available under the [MIT License](LICENSE).

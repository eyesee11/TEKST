package com.texteditor.ui;

import com.texteditor.controller.FileController;
import com.texteditor.controller.EditController;
import com.texteditor.model.DocumentManager;
import com.texteditor.ui.themes.ThemeManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

// main class similar to how React runs on the main thread

public class MainWindow extends JFrame {
    private FileController fileController;
    private EditController editController;

    // here comes the UI components;
    private MenuBar menuBar;
    private ToolBar toolBar;
    private TabManager tabManager; // changed from single textArea to tab manager
    private StatusBar statusBar;

    // constants for windows config

    private static final String APP_TITLE = "Advanced Text Editor";
    private static final int DEFAULT_HEIGHT = 800;
    private static final int DEFAULT_WIDTH = 1200;

    public MainWindow() {
        initializeComponents();
        setupEventHandlers();
        configureWindow();
        setupLayout();

        // Initialize and apply pixelated theme
        initializeTheme();
    }

    // initialize the pixelated theme system
    private void initializeTheme() {
        ThemeManager themeManager = ThemeManager.getInstance();
        themeManager.setMainWindow(this);

        // Start with normal theme - don't auto-apply pixelated theme
        // User can choose from View menu when they want to switch
        SwingUtilities.invokeLater(() -> {
            System.out.println("Theme system initialized. Current theme: "
                    + themeManager.getCurrentTheme().getThemeName());
        });
    }

    // initializing the state components for the GUI
    private void initializeComponents() {
        // Create a temporary DocumentManager for controllers initialization
        DocumentManager tempDocManager = new DocumentManager();

        // Initialize controllers first
        fileController = new FileController(tempDocManager);
        editController = new EditController(tempDocManager);

        // Initialize tab manager (this will create the first tab with its own DocumentManager)
        tabManager = new TabManager(this);

        // Create components with controllers
        menuBar = new MenuBar(fileController, editController);
        toolBar = new ToolBar(fileController, editController);
        statusBar = new StatusBar();
        statusBar.setName("statusBar"); // Add name for ThemeManager to find

        // Update controllers to use the actual current DocumentManager from TabManager
        DocumentManager currentDocManager = tabManager.getCurrentDocumentManager();
        if (currentDocManager != null) {
            // The controllers will be updated via setCurrentComponents when tabs change
            fileController.setCurrentComponents(tabManager.getCurrentTextPane(), currentDocManager);
            editController.setCurrentComponents(tabManager.getCurrentTextPane(), currentDocManager);
            
            // Initialize the undo system for the current tab
            editController.initializeUndoSystem();
        }

        // Set parent component for dialogs
        editController.setParentComponent(this);

        // Set TabManager reference in controllers
        fileController.setTabManager(tabManager);
        editController.setTabManager(tabManager);
    }

    // setting up layout CSS(grid/flex)

    private void setupLayout() {
        setJMenuBar(menuBar);
        setLayout(new BorderLayout());
        add(toolBar, BorderLayout.NORTH);
        add(tabManager, BorderLayout.CENTER); // use tab manager instead of scroll pane
        add(statusBar, BorderLayout.SOUTH);
    }

    // setting up event handlers

    private void setupEventHandlers() {
        // window closing
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // Handle window closing event
                handleApplicationExit();
            }
        });

        // The TabManager will handle text selection and caret events for individual tabs
        // No need to set up listeners here as they are managed per-tab in TabManager
    }

    // configure window properties
    private void configureWindow() {
        setTitle(APP_TITLE);
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(600, 400));

        // Add custom icon (using Unicode emoji for now)
        try {
            // Create a simple icon using text
            BufferedImage icon = new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = icon.createGraphics();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            // Background
            g2d.setColor(new Color(0, 120, 215));
            g2d.fillRoundRect(8, 8, 48, 48, 12, 12);

            // Text icon
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Segoe UI", Font.BOLD, 32));
            FontMetrics fm = g2d.getFontMetrics();
            String text = "T";
            int x = (64 - fm.stringWidth(text)) / 2;
            int y = (64 - fm.getHeight()) / 2 + fm.getAscent();
            g2d.drawString(text, x, y);

            g2d.dispose();
            setIconImage(icon);

        } catch (Exception e) {
            // If icon creation fails, continue without icon
            System.out.println("Could not create window icon: " + e.getMessage());
        }

        // Set window properties for better appearance
        setBackground(Color.WHITE);
    }

    // handling app exit with unsaved changes
    private void handleApplicationExit() {
        if (tabManager.hasUnsavedChanges()) {
            int option = JOptionPane.showConfirmDialog(this,
                    "You have unsaved changes in one or more tabs.", "Unsaved Changes",
                    JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);

            switch (option) {
                case JOptionPane.YES_OPTION:
                    if (tabManager.saveAllModified()) {
                        System.exit(0);
                    }
                    break;
                case JOptionPane.NO_OPTION:
                    System.exit(0);
                    break;
                case JOptionPane.CANCEL_OPTION:
                default:
                    // do nothing, just return to the app
                    return;
            }
        } else {
            System.exit(0);
        }
    }

    // handle text selection changes - delegated to TabManager
    private void handleTextSelection() {
        // TabManager handles text selection per tab
        // This method is kept for compatibility but functionality moved to TabManager
    }

    // show formatting popup if text is selected (for right-click) - delegated to TabManager
    private void showFormattingPopupIfTextSelected(int x, int y) {
        tabManager.showFormattingPopupIfTextSelected(x, y);
    }

    // show formatting popup for current selection - delegated to TabManager
    private void showFormattingPopupForSelection() {
        tabManager.showFormattingPopupForSelection();
    }

    // Getter methods required by TabManager, MenuBar, and ToolBar
    public StatusBar getStatusBar() {
        return statusBar;
    }

    public FileController getFileController() {
        return fileController;
    }

    public EditController getEditController() {
        return editController;
    }

    public TabManager getTabManager() {
        return tabManager;
    }
}

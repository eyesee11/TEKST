package com.texteditor.controller;

import com.texteditor.model.DocumentManager;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

// handles all file operations for the text editor. this is like your API service layer or file
// handling utilities in web apps.
public class FileController {

    private final DocumentManager documentManager;
    private JFileChooser fileChooser;
    private Component parentComponent;
    private com.texteditor.ui.TabManager tabManager; // Reference to TabManager for multi-tab
                                                     // operations

    public FileController(DocumentManager documentManager) {
        this.documentManager = documentManager;
        initializeFileChooser();
    }

    // Initialize file chooser with proper filters (like setting up file input restrictions in HTML)

    private void initializeFileChooser() {
        fileChooser = new JFileChooser();

        // Add file filters (like accept attribute in HTML file inputs)
        fileChooser
                .addChoosableFileFilter(new FileNameExtensionFilter("Text Files (*.txt)", "txt"));
        fileChooser
                .addChoosableFileFilter(new FileNameExtensionFilter("Java Files (*.java)", "java"));
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("All Files", "*"));

        // Set default directory to user's documents
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
    }

    // Set parent component for dialogs (like setting up modal context in React)

    public void setParentComponent(Component parent) {
        this.parentComponent = parent;
    }

    // Set TabManager reference for multi-tab operations
    public void setTabManager(com.texteditor.ui.TabManager tabManager) {
        this.tabManager = tabManager;
    }

    // Get current DocumentManager from TabManager (for multi-tab mode)
    private DocumentManager getCurrentDocumentManager() {
        if (tabManager != null) {
            DocumentManager current = tabManager.getCurrentDocumentManager();
            return current != null ? current : documentManager;
        }
        return documentManager;
    }

    // Create new document (creates a new tab in multi-tab mode)

    public void newDocument() {
        if (tabManager != null) {
            // Create a new untitled tab
            tabManager.createNewUntitledTab();
        } else {
            // Fallback to single document mode
            if (checkUnsavedChanges()) {
                documentManager.setContent("");
                documentManager.setCurrentFile(null);
                updateWindowTitle("Untitled");
            }
        }
    }

    // Open file dialog and load selected file (like handling file upload in web apps)

    public void openDocument() {
        if (!checkUnsavedChanges()) {
            return;
        }

        int result = fileChooser.showOpenDialog(parentComponent);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            loadFile(selectedFile);
        }
    }

    // Save current document (with existing file or show Save As dialog)

    public boolean saveDocument() {
        DocumentManager currentDocManager = getCurrentDocumentManager();
        File currentFile = currentDocManager.getCurrentFile();
        if (currentFile != null) {
            return saveToFile(currentFile, currentDocManager);
        } else {
            return saveDocumentAs();
        }
    }

    // Show Save As dialog and save to selected location

    public boolean saveDocumentAs() {
        int result = fileChooser.showSaveDialog(parentComponent);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();

            // Add .txt extension if no extension provided
            if (!selectedFile.getName().contains(".")) {
                selectedFile = new File(selectedFile.getAbsolutePath() + ".txt");
            }

            DocumentManager currentDocManager = getCurrentDocumentManager();
            return saveToFile(selectedFile, currentDocManager);
        }
        return false;
    }

    // Load content from file (like fetching data from API)

    private void loadFile(File file) {
        try {
            String content = Files.readString(file.toPath());
            DocumentManager currentDocManager = getCurrentDocumentManager();
            currentDocManager.setContent(content);
            currentDocManager.setCurrentFile(file);
            updateWindowTitle(file.getName());

            // Update tab title if in multi-tab mode
            if (tabManager != null) {
                tabManager.updateTabTitle(file.getName());
            }

            showSuccessMessage("File loaded successfully: " + file.getName());

        } catch (IOException e) {
            showErrorMessage("Error loading file: " + e.getMessage());
        }
    }

    // Save content to file (like posting data to API)

    private boolean saveToFile(File file, DocumentManager docManager) {
        try {
            String content = docManager.getContent();
            Files.writeString(file.toPath(), content, StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING);

            docManager.setCurrentFile(file);
            docManager.markAsSaved();
            updateWindowTitle(file.getName());

            // Update tab title if in multi-tab mode
            if (tabManager != null) {
                tabManager.updateTabTitle(file.getName());
            }

            showSuccessMessage("File saved successfully: " + file.getName());
            return true;

        } catch (IOException e) {
            showErrorMessage("Error saving file: " + e.getMessage());
            return false;
        }
    }

    // Legacy method for backward compatibility
    private boolean saveToFile(File file) {
        return saveToFile(file, documentManager);
    }

    // Check for unsaved changes before proceeding (like showing confirmation before navigating away
    // in web apps)

    private boolean checkUnsavedChanges() {
        DocumentManager currentDocManager = getCurrentDocumentManager();
        if (currentDocManager.hasUnsavedChanges()) {
            int choice = JOptionPane.showConfirmDialog(parentComponent,
                    "You have unsaved changes. Do you want to save them?", "Unsaved Changes",
                    JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);

            switch (choice) {
                case JOptionPane.YES_OPTION:
                    return saveDocument();
                case JOptionPane.NO_OPTION:
                    return true;
                default: // CANCEL or close
                    return false;
            }
        }
        return true;
    }

    // Update window title (like updating document.title in web apps)

    private void updateWindowTitle(String filename) {
        if (parentComponent instanceof JFrame) {
            JFrame frame = (JFrame) parentComponent;
            String title = "Advanced Text Editor";
            if (filename != null) {
                title += " - " + filename;
                DocumentManager currentDocManager = getCurrentDocumentManager();
                if (currentDocManager.hasUnsavedChanges()) {
                    title += " *";
                }
            }
            frame.setTitle(title);
        }
    }

    // Show success message (like toast notifications in web apps)

    private void showSuccessMessage(String message) {
        JOptionPane.showMessageDialog(parentComponent, message, "Success",
                JOptionPane.INFORMATION_MESSAGE);
    }

    // Show error message (like error notifications in web apps)

    private void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(parentComponent, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    // Set current components for multi-tab support
    public void setCurrentComponents(JTextPane textPane, DocumentManager documentManager) {
        // This method is called when switching tabs to update the controller's context
        // We need to temporarily work with the passed DocumentManager for this specific operation
        // Note: The main documentManager field remains for compatibility with single-document mode
    }
}

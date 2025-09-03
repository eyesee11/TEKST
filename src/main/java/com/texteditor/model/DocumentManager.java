package com.texteditor.model;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import java.io.File;

/**
 * DocumentManager handles document state and file operations Supports both JTextArea (legacy) and
 * JTextPane (new rich text) components
 */
public class DocumentManager {
    private JTextArea textArea; // Legacy text component
    private JTextPane textPane; // New rich text component
    private File currentFile;
    private boolean hasUnsavedChanges;
    private DocumentListener documentListener;

    public DocumentManager() {
        this.hasUnsavedChanges = false;
        setupDocumentListener();
    }

    // Setup document listener to track changes
    private void setupDocumentListener() {
        documentListener = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                setUnsavedChanges(true);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                setUnsavedChanges(true);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                setUnsavedChanges(true);
            }
        };
    }

    // Set the JTextArea component (legacy mode)
    public void setTextArea(JTextArea textArea) {
        // Remove listener from old component
        if (this.textArea != null) {
            this.textArea.getDocument().removeDocumentListener(documentListener);
        }

        this.textArea = textArea;
        this.textPane = null; // Clear JTextPane when using JTextArea

        // Add listener to new component
        if (textArea != null) {
            textArea.getDocument().addDocumentListener(documentListener);
        }
    }

    // Set the JTextPane component (new rich text mode)
    public void setTextPane(JTextPane textPane) {
        // Remove listener from old component
        if (this.textPane != null) {
            this.textPane.getDocument().removeDocumentListener(documentListener);
        }

        this.textPane = textPane;
        this.textArea = null; // Clear JTextArea when using JTextPane

        // Add listener to new component
        if (textPane != null) {
            textPane.getDocument().addDocumentListener(documentListener);
        }
    }

    // Get current text content (works with both components)
    public String getText() {
        if (textPane != null) {
            return textPane.getText();
        } else if (textArea != null) {
            return textArea.getText();
        }
        return "";
    }

    // Set text content (works with both components)
    public void setText(String text) {
        if (textPane != null) {
            textPane.setText(text);
        } else if (textArea != null) {
            textArea.setText(text);
        }
        setUnsavedChanges(false); // Reset unsaved changes flag after setting text
    }

    // Get the current document (works with both components)
    public Document getDocument() {
        if (textPane != null) {
            return textPane.getDocument();
        } else if (textArea != null) {
            return textArea.getDocument();
        }
        return null;
    }

    // Get the current text component
    public JTextComponent getTextComponent() {
        if (textPane != null) {
            return textPane;
        } else if (textArea != null) {
            return textArea;
        }
        return null;
    }

    // Get JTextPane (if in rich text mode)
    public JTextPane getTextPane() {
        return textPane;
    }

    // Get JTextArea (if in legacy mode)
    public JTextArea getTextArea() {
        return textArea;
    }

    // File management
    public File getCurrentFile() {
        return currentFile;
    }

    public void setCurrentFile(File file) {
        this.currentFile = file;
    }

    // Unsaved changes tracking
    public boolean hasUnsavedChanges() {
        return hasUnsavedChanges;
    }

    public void setUnsavedChanges(boolean hasUnsavedChanges) {
        this.hasUnsavedChanges = hasUnsavedChanges;
    }

    // Clear the document
    public void clear() {
        setText("");
        setCurrentFile(null);
        setUnsavedChanges(false);
    }

    // Check if a file is currently loaded
    public boolean hasFile() {
        return currentFile != null;
    }

    // Get the filename for display
    public String getDisplayName() {
        if (currentFile != null) {
            String name = currentFile.getName();
            return hasUnsavedChanges ? name + "*" : name;
        }
        return hasUnsavedChanges ? "Untitled*" : "Untitled";
    }

    // Compatibility methods for FileController
    public void setContent(String content) {
        setText(content);
    }

    public String getContent() {
        return getText();
    }

    public void markAsSaved() {
        setUnsavedChanges(false);
    }
}

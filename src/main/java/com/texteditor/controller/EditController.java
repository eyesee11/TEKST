package com.texteditor.controller;

import com.texteditor.model.DocumentManager;
import com.texteditor.ui.dialogs.FindReplaceDialog;

import javax.swing.*;
import javax.swing.undo.UndoManager;
import javax.swing.undo.UndoableEdit;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;

// handles all text editing operations like cut, copy, paste, undo, redo, find/replace. this is like
// your editing utilities in web apps.
public class EditController {

    private final DocumentManager documentManager;
    private final UndoManager undoManager;
    private final Clipboard clipboard;
    private Component parentComponent;
    private FindReplaceDialog findReplaceDialog;

    public EditController(DocumentManager documentManager) {
        this.documentManager = documentManager;
        this.undoManager = new UndoManager();
        this.clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        // Note: setupUndoSystem will be called later when text area is ready
    }

    // Initialize the undo system - call this after text area is set up

    public void initializeUndoSystem() {
        setupUndoSystem();
    }

    // Set parent component for dialogs

    public void setParentComponent(Component parent) {
        this.parentComponent = parent;
    }

    // Setup undo/redo system to track document changes

    private void setupUndoSystem() {
        JTextArea textArea = documentManager.getTextArea();
        if (textArea != null) {
            textArea.getDocument().addUndoableEditListener(e -> {
                UndoableEdit edit = e.getEdit();
                undoManager.addEdit(edit);
                updateUndoRedoButtons();
            });
        }
    }

    // Cut selected text to clipboard

    public void cut() {
        JTextArea textArea = documentManager.getTextArea();
        if (textArea != null && textArea.getSelectedText() != null) {
            copy(); // First copy the text
            textArea.replaceSelection(""); // Then delete it
            updateStatus("Text cut to clipboard");
        } else {
            updateStatus("No text selected to cut");
        }
    }

    // Copy selected text to clipboard

    public void copy() {
        JTextArea textArea = documentManager.getTextArea();
        if (textArea != null && textArea.getSelectedText() != null) {
            String selectedText = textArea.getSelectedText();
            StringSelection stringSelection = new StringSelection(selectedText);
            clipboard.setContents(stringSelection, null);
            updateStatus("Text copied to clipboard");
        } else {
            updateStatus("No text selected to copy");
        }
    }

    // Paste text from clipboard

    public void paste() {
        try {
            if (clipboard.isDataFlavorAvailable(DataFlavor.stringFlavor)) {
                String clipboardText = (String) clipboard.getData(DataFlavor.stringFlavor);
                JTextArea textArea = documentManager.getTextArea();
                if (textArea != null) {
                    textArea.replaceSelection(clipboardText);
                    updateStatus("Text pasted from clipboard");
                }
            } else {
                updateStatus("No text in clipboard to paste");
            }
        } catch (Exception e) {
            updateStatus("Error pasting from clipboard: " + e.getMessage());
        }
    }

    // Select all text in the document

    public void selectAll() {
        JTextArea textArea = documentManager.getTextArea();
        if (textArea != null) {
            textArea.selectAll();
            updateStatus("All text selected");
        }
    }

    // Undo last action

    public void undo() {
        if (undoManager.canUndo()) {
            undoManager.undo();
            updateUndoRedoButtons();
            updateStatus("Undid last action");
        } else {
            updateStatus("Nothing to undo");
        }
    }

    // Redo last undone action

    public void redo() {
        if (undoManager.canRedo()) {
            undoManager.redo();
            updateUndoRedoButtons();
            updateStatus("Redid last action");
        } else {
            updateStatus("Nothing to redo");
        }
    }

    // Show find and replace dialog

    public void showFindReplaceDialog() {
        if (findReplaceDialog == null) {
            Window parentWindow = SwingUtilities.getWindowAncestor(parentComponent);
            findReplaceDialog = new FindReplaceDialog(parentWindow, documentManager);
        }
        findReplaceDialog.setVisible(true);
        updateStatus("Find & Replace dialog opened");
    }

    // Find next occurrence of text

    public void findNext(String searchText, boolean caseSensitive) {
        JTextArea textArea = documentManager.getTextArea();
        if (textArea == null || searchText == null || searchText.isEmpty()) {
            updateStatus("No search text provided");
            return;
        }

        String content = textArea.getText();
        String searchContent = caseSensitive ? content : content.toLowerCase();
        String searchTerm = caseSensitive ? searchText : searchText.toLowerCase();

        int startIndex = textArea.getCaretPosition();
        int foundIndex = searchContent.indexOf(searchTerm, startIndex);

        if (foundIndex == -1 && startIndex > 0) {
            // Wrap around to beginning
            foundIndex = searchContent.indexOf(searchTerm, 0);
        }

        if (foundIndex != -1) {
            textArea.setCaretPosition(foundIndex);
            textArea.select(foundIndex, foundIndex + searchText.length());
            updateStatus("Found: " + searchText);
        } else {
            updateStatus("Text not found: " + searchText);
        }
    }

    // Replace current selection with replacement text

    public void replace(String searchText, String replaceText, boolean caseSensitive) {
        JTextArea textArea = documentManager.getTextArea();
        if (textArea == null)
            return;

        String selectedText = textArea.getSelectedText();
        if (selectedText != null) {
            boolean matches = caseSensitive ? selectedText.equals(searchText)
                    : selectedText.equalsIgnoreCase(searchText);

            if (matches) {
                textArea.replaceSelection(replaceText);
                updateStatus("Replaced: " + searchText + " with: " + replaceText);
            }
        }

        // Find next occurrence
        findNext(searchText, caseSensitive);
    }

    // Replace all occurrences of search text

    public void replaceAll(String searchText, String replaceText, boolean caseSensitive) {
        JTextArea textArea = documentManager.getTextArea();
        if (textArea == null || searchText == null || searchText.isEmpty()) {
            updateStatus("No search text provided");
            return;
        }

        String content = textArea.getText();
        String result;
        int replacements;

        if (caseSensitive) {
            result = content.replace(searchText, replaceText);
            replacements = (content.length() - result.length())
                    / Math.max(1, searchText.length() - replaceText.length());
        } else {
            // Case-insensitive replacement
            result = content.replaceAll("(?i)" + java.util.regex.Pattern.quote(searchText),
                    java.util.regex.Matcher.quoteReplacement(replaceText));
            replacements = (content.length() - result.length())
                    / Math.max(1, searchText.length() - replaceText.length());
        }

        textArea.setText(result);
        updateStatus("Replaced " + replacements + " occurrences");
    }

    // Update undo/redo button states (this will be called from UI)

    private void updateUndoRedoButtons() {
        // This method will be enhanced when we connect to toolbar buttons
    }

    // Update status message

    private void updateStatus(String message) {
        // This will update the status bar - we'll connect this properly
        System.out.println("Edit Status: " + message);
    }

    // Getters for UI components to check states
    public boolean canUndo() {
        return undoManager.canUndo();
    }

    public boolean canRedo() {
        return undoManager.canRedo();
    }

    public UndoManager getUndoManager() {
        return undoManager;
    }

    // Set current components for multi-tab support
    public void setCurrentComponents(JTextPane textPane, DocumentManager documentManager) {
        // This method is called when switching tabs to update the controller's context
        // For now, we'll keep the original documentManager but this could be expanded
        // to handle per-tab edit operations differently if needed
    }
}

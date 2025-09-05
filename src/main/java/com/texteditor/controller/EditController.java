package com.texteditor.controller;

import com.texteditor.model.DocumentManager;
import com.texteditor.ui.dialogs.FindReplaceDialog;

import javax.swing.*;
import javax.swing.text.JTextComponent;
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

    private DocumentManager documentManager;
    private final Clipboard clipboard;
    private Component parentComponent;
    private FindReplaceDialog findReplaceDialog;
    private com.texteditor.ui.TabManager tabManager;

    public EditController(DocumentManager documentManager) {
        this.documentManager = documentManager;
        this.clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
    }

    // Set the tab manager reference
    public void setTabManager(com.texteditor.ui.TabManager tabManager) {
        this.tabManager = tabManager;
    }

    // Initialize the undo system - call this after text area is set up
    public void initializeUndoSystem() {
        // Undo system is now managed by TabManager
        // This method is kept for compatibility
    }

    // Set parent component for dialogs
    public void setParentComponent(Component parent) {
        this.parentComponent = parent;
    }

    // Cut selected text to clipboard

    public void cut() {
        JTextComponent textComponent = documentManager.getTextComponent();
        if (textComponent != null && textComponent.getSelectedText() != null) {
            copy(); // First copy the text
            textComponent.replaceSelection(""); // Then delete it
            updateStatus("Text cut to clipboard");
        } else {
            updateStatus("No text selected to cut");
        }
    }

    // Copy selected text to clipboard

    public void copy() {
        JTextComponent textComponent = documentManager.getTextComponent();
        if (textComponent != null && textComponent.getSelectedText() != null) {
            String selectedText = textComponent.getSelectedText();
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
                JTextComponent textComponent = documentManager.getTextComponent();
                if (textComponent != null) {
                    textComponent.replaceSelection(clipboardText);
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
        JTextComponent textComponent = documentManager.getTextComponent();
        if (textComponent != null) {
            textComponent.selectAll();
            updateStatus("All text selected");
        }
    }

    // Undo last action

    public void undo() {
        if (tabManager != null && tabManager.canUndo()) {
            tabManager.undo();
            updateUndoRedoButtons();
            updateStatus("Undid last action");
        } else {
            updateStatus("Nothing to undo");
        }
    }

    // Redo last undone action

    public void redo() {
        if (tabManager != null && tabManager.canRedo()) {
            tabManager.redo();
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
        JTextComponent textComponent = documentManager.getTextComponent();
        if (textComponent == null || searchText == null || searchText.isEmpty()) {
            updateStatus("No search text provided");
            return;
        }

        String content = textComponent.getText();
        String searchContent = caseSensitive ? content : content.toLowerCase();
        String searchTerm = caseSensitive ? searchText : searchText.toLowerCase();

        int startIndex = textComponent.getCaretPosition();
        int foundIndex = searchContent.indexOf(searchTerm, startIndex);

        if (foundIndex == -1 && startIndex > 0) {
            // Wrap around to beginning
            foundIndex = searchContent.indexOf(searchTerm, 0);
        }

        if (foundIndex != -1) {
            textComponent.setCaretPosition(foundIndex);
            textComponent.select(foundIndex, foundIndex + searchText.length());
            updateStatus("Found: " + searchText);
        } else {
            updateStatus("Text not found: " + searchText);
        }
    }

    // Replace current selection with replacement text

    public void replace(String searchText, String replaceText, boolean caseSensitive) {
        JTextComponent textComponent = documentManager.getTextComponent();
        if (textComponent == null)
            return;

        String selectedText = textComponent.getSelectedText();
        if (selectedText != null) {
            boolean matches = caseSensitive ? selectedText.equals(searchText)
                    : selectedText.equalsIgnoreCase(searchText);

            if (matches) {
                textComponent.replaceSelection(replaceText);
                updateStatus("Replaced: " + searchText + " with: " + replaceText);
            }
        }

        // Find next occurrence
        findNext(searchText, caseSensitive);
    }

    // Replace all occurrences of search text

    public void replaceAll(String searchText, String replaceText, boolean caseSensitive) {
        JTextComponent textComponent = documentManager.getTextComponent();
        if (textComponent == null || searchText == null || searchText.isEmpty()) {
            updateStatus("No search text provided");
            return;
        }

        String content = textComponent.getText();
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

        textComponent.setText(result);
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
        return tabManager != null && tabManager.canUndo();
    }

    public boolean canRedo() {
        return tabManager != null && tabManager.canRedo();
    }

    public UndoManager getUndoManager() {
        return tabManager != null ? tabManager.getCurrentUndoManager() : null;
    }

    // Set current components for multi-tab support
    public void setCurrentComponents(JTextPane textPane, DocumentManager documentManager) {
        // Update the document manager reference
        if (documentManager != null) {
            this.documentManager = documentManager;
        }
    }
}

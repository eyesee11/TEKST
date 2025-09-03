package com.texteditor.ui;

import com.texteditor.controller.FileController;
import com.texteditor.controller.EditController;
import com.texteditor.ui.themes.ThemedDialogs;

import javax.swing.*;
import java.awt.*;

// Enhanced toolbar with icons and better styling.

public class ToolBar extends JToolBar {

    private final FileController fileController;
    private final EditController editController;

    public ToolBar(FileController fileController, EditController editController) {
        this.fileController = fileController;
        this.editController = editController;
        initializeToolbar();
        createButtons();
    }

    private void initializeToolbar() {
        setFloatable(false);
        setRollover(true); // Adds hover effects
        setBorderPainted(true);

        // Add some padding
        setBorder(BorderFactory.createCompoundBorder(getBorder(),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
    }

    private void createButtons() {
        // New Document Button
        JButton newButton = createToolbarButton("📄", "New", "Create new document (Ctrl+N)",
                e -> fileController.newDocument());
        add(newButton);

        // Open Document Button
        JButton openButton = createToolbarButton("📁", "Open", "Open document (Ctrl+O)",
                e -> fileController.openDocument());
        add(openButton);

        // Save Document Button
        JButton saveButton = createToolbarButton("💾", "Save", "Save document (Ctrl+S)",
                e -> fileController.saveDocument());
        add(saveButton);

        addSeparator(); // Visual separator

        // Additional useful buttons
        JButton saveAsButton = createToolbarButton("📋", "Save As",
                "Save document with new name (Ctrl+Shift+S)", e -> fileController.saveDocumentAs());
        add(saveAsButton);

        addSeparator();

        // Edit operation buttons
        JButton undoButton = createToolbarButton("↶", "Undo", "Undo last action (Ctrl+Z)",
                e -> editController.undo());
        add(undoButton);

        JButton redoButton = createToolbarButton("↷", "Redo", "Redo last action (Ctrl+Y)",
                e -> editController.redo());
        add(redoButton);

        addSeparator();

        // Find button
        JButton findButton = createToolbarButton("🔍", "Find", "Find and replace text (Ctrl+F)",
                e -> editController.showFindReplaceDialog());
        add(findButton);

        // Add flexible space to push help button to the right
        add(Box.createHorizontalGlue());

        // Help button on the far right
        JButton helpButton =
                createToolbarButton("❓", "Help", "Show help information", e -> showQuickHelp());
        add(helpButton);
    }

    // Creates a styled toolbar button with icon and text
    
    private JButton createToolbarButton(String icon, String text, String tooltip,
            java.awt.event.ActionListener action) {
        JButton button = new JButton();

        // Create a label with icon and text
        String buttonText =
                "<html><center>" + icon + "<br><small>" + text + "</small></center></html>";
        button.setText(buttonText);

        // Styling
        button.setToolTipText(tooltip);
        button.setFocusable(false);
        button.setPreferredSize(new Dimension(70, 55));
        button.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));

        // Add hover effects
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                button.setBackground(new Color(230, 230, 230));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                button.setBackground(UIManager.getColor("Button.background"));
            }
        });

        button.addActionListener(action);
        return button;
    }

    private void showQuickHelp() {
        ThemedDialogs.showQuickHelpDialog(this);
    }
}

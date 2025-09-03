package com.texteditor.ui;

import com.texteditor.controller.FileController;
import com.texteditor.controller.EditController;
import com.texteditor.ui.themes.ThemeManager;
import com.texteditor.ui.themes.PixelatedTheme;
import com.texteditor.ui.themes.ThemedDialogs;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

// creates and manages the application menu bar. this is like your navigation component in React
// apps.
public class MenuBar extends JMenuBar {

    private final FileController fileController;
    private final EditController editController;

    public MenuBar(FileController fileController, EditController editController) {
        this.fileController = fileController;
        this.editController = editController;
        createMenus();
    }

    // create all menus and menu items (like rendering your navigation structure)
    private void createMenus() {
        add(createFileMenu());
        add(createEditMenu());
        add(createViewMenu());
        add(createHelpMenu());
    }

    // Create File menu with all file operations (like grouping related actions in a dropdown)

    private JMenu createFileMenu() {
        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_F); // Alt+F shortcut

        // New Document
        JMenuItem newItem = new JMenuItem("New");
        newItem.setMnemonic(KeyEvent.VK_N);
        newItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK));
        newItem.addActionListener(e -> fileController.newDocument());
        fileMenu.add(newItem);

        // Open Document
        JMenuItem openItem = new JMenuItem("Open");
        openItem.setMnemonic(KeyEvent.VK_O);
        openItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK));
        openItem.addActionListener(e -> fileController.openDocument());
        fileMenu.add(openItem);

        fileMenu.addSeparator(); // Visual separator

        // Save Document
        JMenuItem saveItem = new JMenuItem("Save");
        saveItem.setMnemonic(KeyEvent.VK_S);
        saveItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK));
        saveItem.addActionListener(e -> fileController.saveDocument());
        fileMenu.add(saveItem);

        // Save As
        JMenuItem saveAsItem = new JMenuItem("Save As...");
        saveAsItem.setMnemonic(KeyEvent.VK_A);
        saveAsItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
                KeyEvent.CTRL_DOWN_MASK | KeyEvent.SHIFT_DOWN_MASK));
        saveAsItem.addActionListener(e -> fileController.saveDocumentAs());
        fileMenu.add(saveAsItem);

        fileMenu.addSeparator();

        // Exit Application
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.setMnemonic(KeyEvent.VK_X);
        exitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, KeyEvent.CTRL_DOWN_MASK));
        exitItem.addActionListener(e -> System.exit(0));
        fileMenu.add(exitItem);

        return fileMenu;
    }

    // Create Edit menu with text editing operations

    private JMenu createEditMenu() {
        JMenu editMenu = new JMenu("Edit");
        editMenu.setMnemonic(KeyEvent.VK_E);

        // Undo
        JMenuItem undoItem = new JMenuItem("Undo");
        undoItem.setMnemonic(KeyEvent.VK_U);
        undoItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, KeyEvent.CTRL_DOWN_MASK));
        undoItem.addActionListener(e -> editController.undo());
        editMenu.add(undoItem);

        // Redo
        JMenuItem redoItem = new JMenuItem("Redo");
        redoItem.setMnemonic(KeyEvent.VK_R);
        redoItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, KeyEvent.CTRL_DOWN_MASK));
        redoItem.addActionListener(e -> editController.redo());
        editMenu.add(redoItem);

        editMenu.addSeparator();

        // Cut
        JMenuItem cutItem = new JMenuItem("Cut");
        cutItem.setMnemonic(KeyEvent.VK_T);
        cutItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, KeyEvent.CTRL_DOWN_MASK));
        cutItem.addActionListener(e -> editController.cut());
        editMenu.add(cutItem);

        // Copy
        JMenuItem copyItem = new JMenuItem("Copy");
        copyItem.setMnemonic(KeyEvent.VK_C);
        copyItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.CTRL_DOWN_MASK));
        copyItem.addActionListener(e -> editController.copy());
        editMenu.add(copyItem);

        // Paste
        JMenuItem pasteItem = new JMenuItem("Paste");
        pasteItem.setMnemonic(KeyEvent.VK_P);
        pasteItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, KeyEvent.CTRL_DOWN_MASK));
        pasteItem.addActionListener(e -> editController.paste());
        editMenu.add(pasteItem);

        editMenu.addSeparator();

        // Select All
        JMenuItem selectAllItem = new JMenuItem("Select All");
        selectAllItem.setMnemonic(KeyEvent.VK_A);
        selectAllItem
                .setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, KeyEvent.CTRL_DOWN_MASK));
        selectAllItem.addActionListener(e -> editController.selectAll());
        editMenu.add(selectAllItem);

        editMenu.addSeparator();

        // Find & Replace
        JMenuItem findReplaceItem = new JMenuItem("Find & Replace...");
        findReplaceItem.setMnemonic(KeyEvent.VK_F);
        findReplaceItem
                .setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, KeyEvent.CTRL_DOWN_MASK));
        findReplaceItem.addActionListener(e -> editController.showFindReplaceDialog());
        editMenu.add(findReplaceItem);

        return editMenu;
    }

    // Create View menu with theme selection and UI options

    private JMenu createViewMenu() {
        JMenu viewMenu = new JMenu("View");
        viewMenu.setMnemonic(KeyEvent.VK_V);

        // Themes submenu
        JMenu themesMenu = new JMenu("🎨 Pixelated Themes");
        themesMenu.setMnemonic(KeyEvent.VK_T);

        // Get available themes from ThemeManager
        ThemeManager themeManager = ThemeManager.getInstance();
        ButtonGroup themeGroup = new ButtonGroup();

        for (int i = 0; i < themeManager.getAvailableThemes().size(); i++) {
            PixelatedTheme theme = themeManager.getAvailableThemes().get(i);
            JRadioButtonMenuItem themeItem = new JRadioButtonMenuItem(theme.getThemeName());

            // Mark current theme as selected
            if (theme == themeManager.getCurrentTheme()) {
                themeItem.setSelected(true);
            }

            final int themeIndex = i;
            themeItem.addActionListener(e -> {
                themeManager.switchTheme(themeIndex);
                updateThemeMenuSelection(themesMenu, themeIndex);
            });

            themeGroup.add(themeItem);
            themesMenu.add(themeItem);
        }

        viewMenu.add(themesMenu);
        viewMenu.addSeparator();

        // Apply Current Theme (useful for refreshing)
        JMenuItem applyThemeItem = new JMenuItem("🔄 Refresh Theme");
        applyThemeItem.setMnemonic(KeyEvent.VK_R);
        applyThemeItem.addActionListener(e -> {
            themeManager.applyCurrentTheme();
            ThemedDialogs.showInfoDialog(this,
                    "Theme refreshed: " + themeManager.getCurrentTheme().getThemeName(),
                    "Theme Applied");
        });
        viewMenu.add(applyThemeItem);

        return viewMenu;
    }

    // Update theme menu selection after theme change

    private void updateThemeMenuSelection(JMenu themesMenu, int selectedIndex) {
        for (int i = 0; i < themesMenu.getItemCount(); i++) {
            JMenuItem item = themesMenu.getItem(i);
            if (item instanceof JRadioButtonMenuItem) {
                ((JRadioButtonMenuItem) item).setSelected(i == selectedIndex);
            }
        }
    }

    // Create Help menu

    private JMenu createHelpMenu() {
        JMenu helpMenu = new JMenu("Help");
        helpMenu.setMnemonic(KeyEvent.VK_H);

        // About Dialog
        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.setMnemonic(KeyEvent.VK_A);
        aboutItem.addActionListener(e -> showAboutDialog());
        helpMenu.add(aboutItem);

        return helpMenu;
    }

    // Show About dialog

    private void showAboutDialog() {
        ThemedDialogs.showAboutDialog(this);
    }
}

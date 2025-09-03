package com.texteditor.ui;

import com.texteditor.model.DocumentManager;
import com.texteditor.ui.themes.ThemeManager;
import com.texteditor.ui.themes.PixelatedTheme;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// manages multiple text document tabs like in notepad++
public class TabManager extends JTabbedPane {

    private MainWindow mainWindow;
    private Map<Integer, DocumentManager> documentManagers;
    private Map<Integer, JTextPane> textPanes;
    private Map<Integer, JScrollPane> scrollPanes;
    private Map<Integer, FormattingPopup> formattingPopups;
    private List<String> tabTitles;
    private int nextTabId = 1;

    public TabManager(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        this.documentManagers = new HashMap<>();
        this.textPanes = new HashMap<>();
        this.scrollPanes = new HashMap<>();
        this.formattingPopups = new HashMap<>();
        this.tabTitles = new ArrayList<>();

        setupTabPane();
        createInitialTab();
    }

    // setup the tab pane with proper styling
    private void setupTabPane() {
        setTabPlacement(JTabbedPane.TOP);
        setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

        // add tab change listener
        addChangeListener(e -> {
            int selectedIndex = getSelectedIndex();
            if (selectedIndex >= 0) {
                updateMainWindowComponents();
            }
        });
    }

    // create the initial "untitled" tab
    private void createInitialTab() {
        createNewTab("Untitled-1", null);
    }

    // create a new tab with specified title and file
    public int createNewTab(String title, File file) {
        int tabId = nextTabId++;

        // create new text pane (rich text component)
        JTextPane textPane = new JTextPane();
        setupTextPane(textPane);

        // create scroll pane for text area
        JScrollPane scrollPane = new JScrollPane(textPane);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(0, 0, 0, 0)));

        // create document manager for this tab
        DocumentManager documentManager = new DocumentManager();
        documentManager.setTextPane(textPane);
        if (file != null) {
            documentManager.setCurrentFile(file);
        }

        // create formatting popup for this tab
        FormattingPopup formattingPopup = new FormattingPopup(textPane);

        // store all components
        textPanes.put(tabId, textPane);
        scrollPanes.put(tabId, scrollPane);
        documentManagers.put(tabId, documentManager);
        formattingPopups.put(tabId, formattingPopup);

        // create tab with close button
        JPanel tabPanel = createTabPanel(title, tabId);

        // add tab to tabbed pane
        addTab(null, scrollPane);
        int tabIndex = getTabCount() - 1;
        setTabComponentAt(tabIndex, tabPanel);

        // select the new tab
        setSelectedIndex(tabIndex);

        // update main window components
        updateMainWindowComponents();

        return tabId;
    }

    // setup text pane with proper configuration
    private void setupTextPane(JTextPane textPane) {
        // basic text pane setup
        textPane.setFont(new Font("Consolas", Font.PLAIN, 14));
        textPane.setMargin(new Insets(10, 10, 10, 10));

        // enable drag and drop
        textPane.setDragEnabled(true);

        // setup document listener for status updates
        textPane.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                updateStatusBar();
            }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                updateStatusBar();
            }

            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                updateStatusBar();
            }
        });

        // add caret listener for position updates and text selection
        textPane.addCaretListener(e -> {
            try {
                int caretPos = textPane.getCaretPosition();
                String text = textPane.getText();
                int line = 1;
                int column = 1;

                for (int i = 0; i < caretPos && i < text.length(); i++) {
                    if (text.charAt(i) == '\n') {
                        line++;
                        column = 1;
                    } else {
                        column++;
                    }
                }

                mainWindow.getStatusBar().setPosition(line, column);
            } catch (Exception ex) {
                mainWindow.getStatusBar().setPosition(1, 1);
            }

            // handle text selection for formatting popup
            SwingUtilities.invokeLater(() -> handleTextSelection(textPane));
        });

        // add mouse listener for right-click formatting popup
        textPane.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    showFormattingPopupIfTextSelected(textPane, e.getX(), e.getY());
                }
            }
        });
    }

    // create tab panel with title and close button
    private JPanel createTabPanel(String title, int tabId) {
        JPanel tabPanel = new JPanel(new BorderLayout());
        tabPanel.setOpaque(false);

        // tab title label
        JLabel titleLabel = new JLabel(title);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));

        // close button
        JButton closeButton = new JButton("×");
        closeButton.setPreferredSize(new Dimension(16, 16));
        closeButton.setFont(new Font("Arial", Font.BOLD, 12));
        closeButton.setBorderPainted(false);
        closeButton.setContentAreaFilled(false);
        closeButton.setFocusPainted(false);
        closeButton.setToolTipText("Close tab");

        closeButton.addActionListener(e -> closeTab(tabId));

        tabPanel.add(titleLabel, BorderLayout.CENTER);
        tabPanel.add(closeButton, BorderLayout.EAST);

        return tabPanel;
    }

    // close tab by tab id
    public void closeTab(int tabId) {
        // find tab index
        int tabIndex = -1;
        for (int i = 0; i < getTabCount(); i++) {
            Component tabComponent = getTabComponentAt(i);
            if (tabComponent instanceof JPanel) {
                // would need to store tabId somehow, for now use index
                tabIndex = i;
                break;
            }
        }

        if (tabIndex >= 0) {
            // check for unsaved changes
            DocumentManager docManager = getCurrentDocumentManager();
            if (docManager != null && docManager.hasUnsavedChanges()) {
                int option = JOptionPane.showConfirmDialog(this,
                        "Do you want to save changes before closing this tab?", "Unsaved Changes",
                        JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);

                if (option == JOptionPane.CANCEL_OPTION) {
                    return; // don't close
                }

                if (option == JOptionPane.YES_OPTION) {
                    // save the document
                    mainWindow.getFileController().saveDocument();
                }
            }

            // remove tab
            removeTabAt(tabIndex);

            // remove from maps
            textPanes.remove(tabId);
            scrollPanes.remove(tabId);
            documentManagers.remove(tabId);
            formattingPopups.remove(tabId);

            // if no tabs left, create a new one
            if (getTabCount() == 0) {
                createNewTab("Untitled-" + nextTabId, null);
            }

            updateMainWindowComponents();
        }
    }

    // get current active text pane
    public JTextPane getCurrentTextPane() {
        int selectedIndex = getSelectedIndex();
        if (selectedIndex >= 0) {
            Component component = getComponentAt(selectedIndex);
            if (component instanceof JScrollPane) {
                JScrollPane scrollPane = (JScrollPane) component;
                JViewport viewport = scrollPane.getViewport();
                Component view = viewport.getView();
                if (view instanceof JTextPane) {
                    return (JTextPane) view;
                }
            }
        }
        return null;
    }

    // get current document manager
    public DocumentManager getCurrentDocumentManager() {
        JTextPane currentPane = getCurrentTextPane();
        if (currentPane != null) {
            // find the document manager for this text pane
            for (Map.Entry<Integer, JTextPane> entry : textPanes.entrySet()) {
                if (entry.getValue() == currentPane) {
                    return documentManagers.get(entry.getKey());
                }
            }
        }
        return null;
    }

    // get current formatting popup
    public FormattingPopup getCurrentFormattingPopup() {
        JTextPane currentPane = getCurrentTextPane();
        if (currentPane != null) {
            // find the formatting popup for this text pane
            for (Map.Entry<Integer, JTextPane> entry : textPanes.entrySet()) {
                if (entry.getValue() == currentPane) {
                    return formattingPopups.get(entry.getKey());
                }
            }
        }
        return null;
    }

    // update main window components when tab changes
    private void updateMainWindowComponents() {
        JTextPane currentPane = getCurrentTextPane();
        DocumentManager currentDocManager = getCurrentDocumentManager();

        if (currentPane != null && currentDocManager != null) {
            // update controllers with current components
            mainWindow.getFileController().setCurrentComponents(currentPane, currentDocManager);
            mainWindow.getEditController().setCurrentComponents(currentPane, currentDocManager);

            // update status bar
            updateStatusBar();
        }
    }

    // update status bar with current document info
    private void updateStatusBar() {
        JTextPane currentPane = getCurrentTextPane();
        if (currentPane != null) {
            SwingUtilities.invokeLater(() -> {
                mainWindow.getStatusBar().updateDocumentStats(currentPane.getText());
            });
        }
    }

    // handle text selection for formatting popup
    private void handleTextSelection(JTextPane textPane) {
        String selectedText = textPane.getSelectedText();
        if (selectedText != null && !selectedText.trim().isEmpty() && selectedText.length() > 1) {
            showFormattingPopupForSelection(textPane);
        }
    }

    // show formatting popup if text is selected (for right-click)
    private void showFormattingPopupIfTextSelected(JTextPane textPane, int x, int y) {
        String selectedText = textPane.getSelectedText();
        if (selectedText != null && !selectedText.trim().isEmpty()) {
            FormattingPopup popup = getCurrentFormattingPopup();
            if (popup != null) {
                int selStart = textPane.getSelectionStart();
                int selEnd = textPane.getSelectionEnd();
                popup.showForSelectedText(x, y, selStart, selEnd);
            }
        }
    }

    // show formatting popup for current selection
    private void showFormattingPopupForSelection(JTextPane textPane) {
        String selectedText = textPane.getSelectedText();
        if (selectedText != null && !selectedText.trim().isEmpty()) {
            try {
                int selStart = textPane.getSelectionStart();
                int selEnd = textPane.getSelectionEnd();

                // calculate popup position near the selection
                Rectangle selectionBounds = textPane.modelToView(selStart);
                if (selectionBounds != null) {
                    int x = selectionBounds.x;
                    int y = selectionBounds.y + selectionBounds.height + 5;

                    // make sure popup stays within window bounds
                    Dimension windowSize = mainWindow.getSize();
                    if (x + 450 > windowSize.width) {
                        x = windowSize.width - 450;
                    }
                    if (y + 50 > windowSize.height) {
                        y = selectionBounds.y - 55;
                    }

                    FormattingPopup popup = getCurrentFormattingPopup();
                    if (popup != null) {
                        popup.showForSelectedText(x, y, selStart, selEnd);
                    }
                }
            } catch (Exception e) {
                // if positioning fails, show at a default location
                FormattingPopup popup = getCurrentFormattingPopup();
                if (popup != null) {
                    popup.showForSelectedText(100, 100, textPane.getSelectionStart(),
                            textPane.getSelectionEnd());
                }
            }
        }
    }

    // apply current theme to all tabs
    public void applyTheme() {
        ThemeManager themeManager = ThemeManager.getInstance();
        PixelatedTheme currentTheme = themeManager.getCurrentTheme();

        // apply theme to tab pane itself
        setBackground(currentTheme.getBackgroundColor());
        setForeground(currentTheme.getForegroundColor());

        // apply theme to all text panes
        for (JTextPane textPane : textPanes.values()) {
            currentTheme.applyToTextPane(textPane);
        }

        // apply theme to all formatting popups
        for (FormattingPopup popup : formattingPopups.values()) {
            popup.applyTheme();
        }

        repaint();
    }

    // update tab title when document is saved/modified
    public void updateTabTitle(String newTitle) {
        int selectedIndex = getSelectedIndex();
        if (selectedIndex >= 0) {
            Component tabComponent = getTabComponentAt(selectedIndex);
            if (tabComponent instanceof JPanel) {
                JPanel tabPanel = (JPanel) tabComponent;
                Component[] components = tabPanel.getComponents();
                for (Component component : components) {
                    if (component instanceof JLabel) {
                        JLabel titleLabel = (JLabel) component;
                        titleLabel.setText(newTitle);
                        break;
                    }
                }
            }
        }
    }

    // Check if any tab has unsaved changes
    public boolean hasUnsavedChanges() {
        for (DocumentManager docManager : documentManagers.values()) {
            if (docManager != null && docManager.hasUnsavedChanges()) {
                return true;
            }
        }
        return false;
    }

    // Save all modified tabs
    public boolean saveAllModified() {
        boolean allSaved = true;
        for (Map.Entry<Integer, DocumentManager> entry : documentManagers.entrySet()) {
            DocumentManager docManager = entry.getValue();
            if (docManager != null && docManager.hasUnsavedChanges()) {
                // Find the tab index for this document manager to select it
                Integer tabId = entry.getKey();
                JTextPane textPane = textPanes.get(tabId);
                if (textPane != null) {
                    // Find the tab index by looking for the text pane
                    for (int i = 0; i < getTabCount(); i++) {
                        Component component = getComponentAt(i);
                        if (component instanceof JScrollPane) {
                            JScrollPane scrollPane = (JScrollPane) component;
                            if (scrollPane.getViewport().getView() == textPane) {
                                setSelectedIndex(i);
                                break;
                            }
                        }
                    }
                }

                // Try to save the document
                if (!mainWindow.getFileController().saveDocument()) {
                    allSaved = false;
                }
            }
        }
        return allSaved;
    }

    // Show formatting popup if text is selected (for right-click) - simplified version
    public void showFormattingPopupIfTextSelected(int x, int y) {
        JTextPane currentTextPane = getCurrentTextPane();
        if (currentTextPane != null) {
            showFormattingPopupIfTextSelected(currentTextPane, x, y);
        }
    }

    // Show formatting popup for current selection - simplified version
    public void showFormattingPopupForSelection() {
        JTextPane currentTextPane = getCurrentTextPane();
        if (currentTextPane != null) {
            showFormattingPopupForSelection(currentTextPane);
        }
    }

    // Create a new untitled tab (convenience method for Ctrl+N)
    public void createNewUntitledTab() {
        int nextTabNumber = getTabCount() + 1;
        createNewTab("Untitled-" + nextTabNumber, null);
    }
}

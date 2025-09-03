package com.texteditor.ui.themes;

import com.texteditor.ui.MainWindow;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

// manages pixelated themes for the text editor
public class ThemeManager {

    private static ThemeManager instance;
    private PixelatedTheme currentTheme;
    private List<PixelatedTheme> availableThemes;
    private MainWindow mainWindow;

    private ThemeManager() {
        availableThemes = new ArrayList<>();
        initializeThemes();
        // Start with Normal theme as default (index 0)
        currentTheme = availableThemes.get(0);
    }

    public static ThemeManager getInstance() {
        if (instance == null) {
            instance = new ThemeManager();
        }
        return instance;
    }

    // Initialize all available themes

    private void initializeThemes() {
        availableThemes.add(new NormalTheme()); // Default normal theme
        availableThemes.add(new DarkRetroTheme()); // Dark pixelated retro theme
        availableThemes.add(new NeonSynthwaveTheme()); // Neon synthwave theme
        availableThemes.add(new ForestPixelTheme()); // Forest pixel theme
        availableThemes.add(new OceanWaveTheme()); // Ocean wave theme
        availableThemes.add(new SunsetArcadeTheme()); // Sunset arcade theme
        availableThemes.add(new MatrixHackerTheme()); // Matrix hacker theme
    }

    // Set the main window reference for theme application

    public void setMainWindow(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
    }

    // Get current active theme

    public PixelatedTheme getCurrentTheme() {
        return currentTheme;
    }

    // Get all available themes

    public List<PixelatedTheme> getAvailableThemes() {
        return new ArrayList<>(availableThemes);
    }

    // Switch to a specific theme by name

    public void switchTheme(String themeName) {
        for (PixelatedTheme theme : availableThemes) {
            if (theme.getThemeName().equals(themeName)) {
                currentTheme = theme;
                applyCurrentTheme();
                break;
            }
        }
    }

    // Switch to a specific theme by index

    public void switchTheme(int themeIndex) {
        if (themeIndex >= 0 && themeIndex < availableThemes.size()) {
            currentTheme = availableThemes.get(themeIndex);
            applyCurrentTheme();
        }
    }

    // Apply the current theme to all UI components

    public void applyCurrentTheme() {
        if (mainWindow == null || currentTheme == null) {
            return;
        }

        SwingUtilities.invokeLater(() -> {
            try {
                // Apply to main window
                applyThemeToMainWindow();

                // Refresh the UI
                mainWindow.revalidate();
                mainWindow.repaint();

                System.out.println("Applied theme: " + currentTheme.getThemeName());
            } catch (Exception e) {
                System.err.println("Error applying theme: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }

    // Apply theme to main window and all its components

    private void applyThemeToMainWindow() {
        // Force override FlatLaf defaults by setting UIManager properties
        setUIManagerDefaults();

        // Set main window background
        mainWindow.getContentPane().setBackground(currentTheme.getBackgroundColor());

        // Apply to menu bar
        JMenuBar menuBar = mainWindow.getJMenuBar();
        if (menuBar != null) {
            currentTheme.applyToMenuBar(menuBar);
        }

        // Apply to text area
        JTextArea textArea = getTextAreaFromMainWindow();
        if (textArea != null) {
            currentTheme.applyToTextArea(textArea);
            // Force the text area to use our colors
            forceTextAreaColors(textArea);
        }

        // Apply to toolbar
        applyThemeToToolbar();

        // Apply to status bar
        applyThemeToStatusBar();

        // Apply to scroll pane
        applyThemeToScrollPane();

        // Force repaint of all components
        forceRepaintAll();
    }

    // Set UIManager defaults to override FlatLaf

    private void setUIManagerDefaults() {
        UIManager.put("TextArea.background", currentTheme.getTextAreaBackgroundColor());
        UIManager.put("TextArea.foreground", currentTheme.getTextAreaForegroundColor());
        UIManager.put("TextArea.caretForeground", currentTheme.getCaretColor());
        UIManager.put("TextArea.selectionBackground", currentTheme.getSelectionColor());
        UIManager.put("TextArea.selectionForeground", currentTheme.getBackgroundColor());

        UIManager.put("MenuBar.background", currentTheme.getMenuBackgroundColor());
        UIManager.put("MenuBar.foreground", currentTheme.getMenuForegroundColor());
        UIManager.put("Menu.background", currentTheme.getMenuBackgroundColor());
        UIManager.put("Menu.foreground", currentTheme.getMenuForegroundColor());
        UIManager.put("MenuItem.background", currentTheme.getMenuBackgroundColor());
        UIManager.put("MenuItem.foreground", currentTheme.getMenuForegroundColor());

        UIManager.put("Button.background", currentTheme.getButtonBackgroundColor());
        UIManager.put("Button.foreground", currentTheme.getButtonForegroundColor());

        UIManager.put("Panel.background", currentTheme.getBackgroundColor());
        UIManager.put("Panel.foreground", currentTheme.getForegroundColor());

        UIManager.put("Label.foreground", currentTheme.getForegroundColor());
    }

    // Force text area colors to override FlatLaf

    private void forceTextAreaColors(JTextArea textArea) {
        // Apply colors multiple times to ensure they stick
        textArea.setBackground(currentTheme.getTextAreaBackgroundColor());
        textArea.setForeground(currentTheme.getTextAreaForegroundColor());
        textArea.setCaretColor(currentTheme.getCaretColor());
        textArea.setSelectionColor(currentTheme.getSelectionColor());
        textArea.setSelectedTextColor(currentTheme.getBackgroundColor());

        // Use appropriate font based on theme
        if (currentTheme.getThemeName().equals("Normal (Default)")) {
            textArea.setFont(new Font("Consolas", Font.PLAIN, 14));
        } else {
            textArea.setFont(PixelatedTheme.PIXELATED_FONT_MEDIUM);
        }

        // Force the document to update colors
        textArea.setOpaque(true);
        textArea.revalidate();
        textArea.repaint();

        // Apply to the document as well
        if (textArea.getDocument() != null) {
            textArea.getDocument().putProperty("foreground",
                    currentTheme.getTextAreaForegroundColor());
        }
    }

    // Force repaint of all components

    private void forceRepaintAll() {
        SwingUtilities.invokeLater(() -> {
            mainWindow.invalidate();
            mainWindow.validate();
            mainWindow.repaint();

            // Force update of all child components
            updateComponentTreeUI(mainWindow);
        });
    }

    // Recursively update component tree UI

    private void updateComponentTreeUI(Container container) {
        for (Component component : container.getComponents()) {
            if (component instanceof JComponent) {
                JComponent jcomp = (JComponent) component;
                SwingUtilities.updateComponentTreeUI(jcomp);
                jcomp.revalidate();
                jcomp.repaint();
            }
            if (component instanceof Container) {
                updateComponentTreeUI((Container) component);
            }
        }
    }

    // Get text area from main window

    private JTextArea getTextAreaFromMainWindow() {
        return findTextAreaInContainer(mainWindow);
    }

    // Recursively find JTextArea in container

    private JTextArea findTextAreaInContainer(Container container) {
        for (Component component : container.getComponents()) {
            if (component instanceof JTextArea) {
                return (JTextArea) component;
            } else if (component instanceof Container) {
                JTextArea found = findTextAreaInContainer((Container) component);
                if (found != null) {
                    return found;
                }
            }
        }
        return null;
    }

    // Apply theme to toolbar components

    private void applyThemeToToolbar() {
        JToolBar toolBar = findToolBarInContainer(mainWindow);
        if (toolBar != null) {
            toolBar.setBackground(currentTheme.getMenuBackgroundColor());
            toolBar.setBorder(BorderFactory.createLineBorder(currentTheme.getBorderColor(), 2));

            // Apply to all buttons in toolbar
            for (Component component : toolBar.getComponents()) {
                if (component instanceof AbstractButton) {
                    currentTheme.applyToButton((AbstractButton) component);
                }
            }
        }
    }

    // Apply theme to status bar

    private void applyThemeToStatusBar() {
        JPanel statusBar = findStatusBarInContainer(mainWindow);
        if (statusBar != null) {
            statusBar.setBackground(currentTheme.getMenuBackgroundColor());
            statusBar.setBorder(BorderFactory.createLineBorder(currentTheme.getBorderColor(), 1));

            // Apply to all labels in status bar
            applyThemeToLabelsInContainer(statusBar);
        }
    }

    // Apply theme to scroll pane

    private void applyThemeToScrollPane() {
        JScrollPane scrollPane = findScrollPaneInContainer(mainWindow);
        if (scrollPane != null) {
            scrollPane.setBackground(currentTheme.getBackgroundColor());
            scrollPane.getViewport().setBackground(currentTheme.getTextAreaBackgroundColor());
            scrollPane.setBorder(BorderFactory.createLineBorder(currentTheme.getBorderColor(), 2));
        }
    }

    // Helper method to find toolbar

    private JToolBar findToolBarInContainer(Container container) {
        for (Component component : container.getComponents()) {
            if (component instanceof JToolBar) {
                return (JToolBar) component;
            } else if (component instanceof Container) {
                JToolBar found = findToolBarInContainer((Container) component);
                if (found != null) {
                    return found;
                }
            }
        }
        return null;
    }

    // Helper method to find status bar

    private JPanel findStatusBarInContainer(Container container) {
        for (Component component : container.getComponents()) {
            if (component instanceof JPanel && component.getName() != null
                    && component.getName().toLowerCase().contains("status")) {
                return (JPanel) component;
            } else if (component instanceof Container) {
                JPanel found = findStatusBarInContainer((Container) component);
                if (found != null) {
                    return found;
                }
            }
        }
        return null;
    }

    // Helper method to find scroll pane

    private JScrollPane findScrollPaneInContainer(Container container) {
        for (Component component : container.getComponents()) {
            if (component instanceof JScrollPane) {
                return (JScrollPane) component;
            } else if (component instanceof Container) {
                JScrollPane found = findScrollPaneInContainer((Container) component);
                if (found != null) {
                    return found;
                }
            }
        }
        return null;
    }

    // Apply theme to all labels in a container

    private void applyThemeToLabelsInContainer(Container container) {
        for (Component component : container.getComponents()) {
            if (component instanceof JLabel) {
                JLabel label = (JLabel) component;
                label.setForeground(currentTheme.getMenuForegroundColor());

                if (currentTheme.getThemeName().equals("Normal (Default)")) {
                    label.setFont(new Font("Segoe UI", Font.PLAIN, 11));
                } else {
                    label.setFont(PixelatedTheme.PIXELATED_FONT_SMALL);
                }
            } else if (component instanceof Container) {
                applyThemeToLabelsInContainer((Container) component);
            }
        }
    }
}

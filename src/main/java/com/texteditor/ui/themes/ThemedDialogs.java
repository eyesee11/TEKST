package com.texteditor.ui.themes;

import javax.swing.*;
import java.awt.*;

// Utility class for creating themed dialogs that match the pixelated theme

public class ThemedDialogs {

    private static ThemeManager getThemeManager() {
        return ThemeManager.getInstance();
    }

    // Show a themed information dialog
    
    public static void showInfoDialog(Component parent, String message, String title) {
        showThemedDialog(parent, message, title, JOptionPane.INFORMATION_MESSAGE);
    }

    // Show a themed warning dialog
    
    public static void showWarningDialog(Component parent, String message, String title) {
        showThemedDialog(parent, message, title, JOptionPane.WARNING_MESSAGE);
    }

    // Show a themed error dialog
    
    public static void showErrorDialog(Component parent, String message, String title) {
        showThemedDialog(parent, message, title, JOptionPane.ERROR_MESSAGE);
    }

    // Show a themed question dialog
    
    public static int showQuestionDialog(Component parent, String message, String title,
            String[] options) {
        return showThemedOptionDialog(parent, message, title, options,
                JOptionPane.QUESTION_MESSAGE);
    }

    // Create and show a themed dialog
    
    private static void showThemedDialog(Component parent, String message, String title,
            int messageType) {
        PixelatedTheme theme = getThemeManager().getCurrentTheme();

        // Create custom option pane
        JOptionPane optionPane = new JOptionPane(createThemedMessage(message), messageType);

        // Apply theme to option pane
        applyThemeToOptionPane(optionPane, theme);

        // Create dialog
        JDialog dialog = optionPane.createDialog(parent, title);
        applyThemeToDialog(dialog, theme);

        dialog.setVisible(true);
    }

    // Create and show a themed option dialog
    
    private static int showThemedOptionDialog(Component parent, String message, String title,
            String[] options, int messageType) {
        PixelatedTheme theme = getThemeManager().getCurrentTheme();

        // Create custom option pane
        JOptionPane optionPane = new JOptionPane(createThemedMessage(message), messageType,
                JOptionPane.YES_NO_OPTION, null, options, options[0]);

        // Apply theme to option pane
        applyThemeToOptionPane(optionPane, theme);

        // Create dialog
        JDialog dialog = optionPane.createDialog(parent, title);
        applyThemeToDialog(dialog, theme);

        dialog.setVisible(true);

        Object value = optionPane.getValue();
        if (value == null)
            return -1;

        for (int i = 0; i < options.length; i++) {
            if (options[i].equals(value)) {
                return i;
            }
        }
        return -1;
    }

    // Create a themed message component
    
    private static JComponent createThemedMessage(String message) {
        PixelatedTheme theme = getThemeManager().getCurrentTheme();

        JLabel label = new JLabel(message);
        label.setForeground(theme.getForegroundColor());
        label.setFont(PixelatedTheme.PIXELATED_FONT_MEDIUM);
        label.setOpaque(false);

        // Create a panel with themed background
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(theme.getBackgroundColor());
        panel.setForeground(theme.getForegroundColor());
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(theme.getBorderColor(), 2),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        panel.add(label, BorderLayout.CENTER);

        return panel;
    }

    // Apply theme to JOptionPane
    
    private static void applyThemeToOptionPane(JOptionPane optionPane, PixelatedTheme theme) {
        optionPane.setBackground(theme.getBackgroundColor());
        optionPane.setForeground(theme.getForegroundColor());
        optionPane.setOpaque(true);
        optionPane.setFont(PixelatedTheme.PIXELATED_FONT_MEDIUM);

        // Apply theme to all child components
        applyThemeToContainer(optionPane, theme);
    }

    // Apply theme to dialog
    
    private static void applyThemeToDialog(JDialog dialog, PixelatedTheme theme) {
        dialog.getContentPane().setBackground(theme.getBackgroundColor());
        dialog.setBackground(theme.getBackgroundColor());

        // Apply pixelated border
        dialog.getRootPane()
                .setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(theme.getAccentColor(), 3),
                        BorderFactory.createLineBorder(theme.getBorderColor(), 1)));

        // Apply theme to all components in the dialog
        applyThemeToContainer(dialog.getContentPane(), theme);
    }

    // Recursively apply theme to all components in a container
    
    private static void applyThemeToContainer(Container container, PixelatedTheme theme) {
        for (Component component : container.getComponents()) {
            if (component instanceof JButton) {
                theme.applyToButton((JButton) component);
            } else if (component instanceof JLabel) {
                JLabel label = (JLabel) component;
                label.setForeground(theme.getForegroundColor());
                label.setFont(PixelatedTheme.PIXELATED_FONT_MEDIUM);
            } else if (component instanceof JPanel) {
                JPanel panel = (JPanel) component;
                panel.setBackground(theme.getBackgroundColor());
                panel.setForeground(theme.getForegroundColor());
                panel.setOpaque(true);
            }

            // Recursively apply to child containers
            if (component instanceof Container) {
                applyThemeToContainer((Container) component, theme);
            }
        }
    }

    // Create a themed about dialog specifically for the text editor
    
    public static void showAboutDialog(Component parent) {
        PixelatedTheme theme = getThemeManager().getCurrentTheme();

        // Create custom dialog
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(parent),
                "About Advanced Text Editor", true);

        // Create content
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(theme.getBackgroundColor());
        mainPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(theme.getAccentColor(), 3),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)));

        // Title
        JLabel titleLabel = new JLabel("🎮 Advanced Text Editor v1.0", JLabel.CENTER);
        titleLabel.setFont(PixelatedTheme.PIXELATED_FONT_TITLE);
        titleLabel.setForeground(theme.getAccentColor());
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Content
        String aboutText = """
                <html><div style='text-align: center; color: %s;'>
                <br>This is a highly functional text editor built with<br>
                Java Swing and lots of hopes and dreams 🤓<br><br>

                <b>🎨 Pixelated Features:</b><br>
                • Multiple retro themes<br>
                • File operations (New, Open, Save, Save As)<br>
                • Text editing with modern UI<br>
                • Keyboard shortcuts<br>
                • Find & Replace functionality<br>
                • Undo/Redo support<br><br>

                Built with Java 21 and FlatLaf UI<br>
                Theme: %s
                </div></html>
                """.formatted(toHexString(theme.getForegroundColor()), theme.getThemeName());

        JLabel contentLabel = new JLabel(aboutText);
        contentLabel.setFont(PixelatedTheme.PIXELATED_FONT_SMALL);
        contentLabel.setHorizontalAlignment(JLabel.CENTER);
        mainPanel.add(contentLabel, BorderLayout.CENTER);

        // Close button
        JButton closeButton = new JButton("🚀 Awesome!");
        theme.applyToButton(closeButton);
        closeButton.addActionListener(e -> dialog.dispose());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(theme.getBackgroundColor());
        buttonPanel.add(closeButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        dialog.setContentPane(mainPanel);
        dialog.setSize(450, 400);
        dialog.setLocationRelativeTo(parent);
        dialog.setResizable(false);

        dialog.setVisible(true);
    }

    // Create a themed quick help dialog
    
    public static void showQuickHelpDialog(Component parent) {
        PixelatedTheme theme = getThemeManager().getCurrentTheme();

        // Create custom dialog
        JDialog dialog =
                new JDialog((Frame) SwingUtilities.getWindowAncestor(parent), "Quick Help", true);

        // Create content
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(theme.getBackgroundColor());
        mainPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(theme.getSecondaryColor(), 3),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)));

        // Title
        JLabel titleLabel = new JLabel("⌨️ Quick Help", JLabel.CENTER);
        titleLabel.setFont(PixelatedTheme.PIXELATED_FONT_TITLE);
        titleLabel.setForeground(theme.getSecondaryColor());
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Content
        String helpText = """
                <html><div style='color: %s;'>
                <b>🎹 Keyboard Shortcuts:</b><br>
                • Ctrl+N - New document<br>
                • Ctrl+O - Open document<br>
                • Ctrl+S - Save document<br>
                • Ctrl+Shift+S - Save As<br>
                • Ctrl+A - Select All<br>
                • Ctrl+C - Copy<br>
                • Ctrl+V - Paste<br>
                • Ctrl+X - Cut<br>
                • Ctrl+Z - Undo<br>
                • Ctrl+Y - Redo<br>
                • Ctrl+F - Find & Replace<br><br>

                <b>🎨 Themes:</b><br>
                • View → Pixelated Themes to change theme<br>
                • View → Refresh Theme to reapply current theme
                </div></html>
                """.formatted(toHexString(theme.getForegroundColor()));

        JLabel contentLabel = new JLabel(helpText);
        contentLabel.setFont(PixelatedTheme.PIXELATED_FONT_SMALL);
        mainPanel.add(contentLabel, BorderLayout.CENTER);

        // Close button
        JButton closeButton = new JButton("✨ Got it!");
        theme.applyToButton(closeButton);
        closeButton.addActionListener(e -> dialog.dispose());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(theme.getBackgroundColor());
        buttonPanel.add(closeButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        dialog.setContentPane(mainPanel);
        dialog.setSize(400, 350);
        dialog.setLocationRelativeTo(parent);
        dialog.setResizable(false);

        dialog.setVisible(true);
    }

    // Convert Color to hex string for HTML
    
    private static String toHexString(Color color) {
        return String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());
    }
}

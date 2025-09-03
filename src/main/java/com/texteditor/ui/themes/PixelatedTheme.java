package com.texteditor.ui.themes;

import javax.swing.*;
import java.awt.*;

// base class for pixelated themes with retro aesthetics
public abstract class PixelatedTheme {

    // Common pixelated fonts
    public static final Font PIXELATED_FONT_SMALL = new Font("Courier New", Font.BOLD, 12);
    public static final Font PIXELATED_FONT_MEDIUM = new Font("Courier New", Font.BOLD, 14);
    public static final Font PIXELATED_FONT_LARGE = new Font("Courier New", Font.BOLD, 16);
    public static final Font PIXELATED_FONT_TITLE = new Font("Courier New", Font.BOLD, 18);

    // Abstract methods that each theme must implement
    public abstract Color getBackgroundColor();

    public abstract Color getForegroundColor();

    public abstract Color getAccentColor();

    public abstract Color getSecondaryColor();

    public abstract Color getSelectionColor();

    public abstract Color getBorderColor();

    public abstract Color getMenuBackgroundColor();

    public abstract Color getMenuForegroundColor();

    public abstract Color getButtonBackgroundColor();

    public abstract Color getButtonForegroundColor();

    public abstract Color getTextAreaBackgroundColor();

    public abstract Color getTextAreaForegroundColor();

    public abstract Color getCaretColor();

    public abstract String getThemeName();

    // Apply this theme to a component

    public void applyToComponent(JComponent component) {
        component.setBackground(getBackgroundColor());
        component.setForeground(getForegroundColor());
        component.setFont(PIXELATED_FONT_MEDIUM);

        // Add pixelated border
        component.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(getBorderColor(), 2),
                BorderFactory.createEmptyBorder(4, 4, 4, 4)));
    }

    // Apply theme to text area specifically

    public void applyToTextArea(JTextArea textArea) {
        // Set colors multiple times to override FlatLaf
        textArea.setBackground(getTextAreaBackgroundColor());
        textArea.setForeground(getTextAreaForegroundColor());
        textArea.setCaretColor(getCaretColor());
        textArea.setSelectionColor(getSelectionColor());
        textArea.setSelectedTextColor(getBackgroundColor());
        textArea.setFont(PIXELATED_FONT_MEDIUM);

        // Make sure the component is opaque so background shows
        textArea.setOpaque(true);

        // Pixelated border for text area
        textArea.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(getBorderColor(), 3),
                BorderFactory.createEmptyBorder(8, 8, 8, 8)));

        // Force immediate repaint
        textArea.revalidate();
        textArea.repaint();

        // Set properties on the component to override look and feel
        textArea.putClientProperty("JComponent.background", getTextAreaBackgroundColor());
        textArea.putClientProperty("JComponent.foreground", getTextAreaForegroundColor());
    }

    // Apply theme to menu bar

    public void applyToMenuBar(JMenuBar menuBar) {
        menuBar.setBackground(getMenuBackgroundColor());
        menuBar.setForeground(getMenuForegroundColor());
        menuBar.setBorder(BorderFactory.createLineBorder(getBorderColor(), 2));
        menuBar.setOpaque(true);

        // Apply to all menus
        for (int i = 0; i < menuBar.getMenuCount(); i++) {
            JMenu menu = menuBar.getMenu(i);
            applyToMenu(menu);
        }

        // Force repaint
        menuBar.revalidate();
        menuBar.repaint();
    }

    // Apply theme to individual menu

    public void applyToMenu(JMenu menu) {
        menu.setBackground(getMenuBackgroundColor());
        menu.setForeground(getMenuForegroundColor());
        menu.setFont(PIXELATED_FONT_MEDIUM);
        menu.setOpaque(true);

        // Apply to menu items
        for (int i = 0; i < menu.getItemCount(); i++) {
            JMenuItem item = menu.getItem(i);
            if (item != null) {
                item.setBackground(getMenuBackgroundColor());
                item.setForeground(getMenuForegroundColor());
                item.setFont(PIXELATED_FONT_SMALL);
                item.setOpaque(true);

                // Force immediate update
                item.revalidate();
                item.repaint();
            }
        }

        // Force menu popup colors
        if (menu.getPopupMenu() != null) {
            menu.getPopupMenu().setBackground(getMenuBackgroundColor());
            menu.getPopupMenu().setForeground(getMenuForegroundColor());
            menu.getPopupMenu().setOpaque(true);
        }
    }

    // Apply theme to buttons
    public void applyToButton(AbstractButton button) {
        button.setBackground(getButtonBackgroundColor());
        button.setForeground(getButtonForegroundColor());
        button.setFont(PIXELATED_FONT_SMALL);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(getBorderColor(), 2),
                BorderFactory.createEmptyBorder(4, 8, 4, 8)));
    }

    // Apply theme to text pane (rich text component)
    public void applyToTextPane(JTextPane textPane) {
        textPane.setBackground(getTextAreaBackgroundColor());
        textPane.setForeground(getTextAreaForegroundColor());
        textPane.setCaretColor(getCaretColor());
        textPane.setSelectionColor(getSelectionColor());
        textPane.setSelectedTextColor(getBackgroundColor());

        // Use appropriate font based on theme
        if (getThemeName().equals("Normal (Default)")) {
            textPane.setFont(new Font("Consolas", Font.PLAIN, 14));
        } else {
            textPane.setFont(PIXELATED_FONT_MEDIUM);
        }

        textPane.setOpaque(true);
        textPane.revalidate();
        textPane.repaint();
    }

    // Create pixelated border

    public static javax.swing.border.Border createPixelatedBorder(Color color, int thickness) {
        return BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(color, thickness),
                BorderFactory.createLineBorder(color.brighter(), 1));
    }
}

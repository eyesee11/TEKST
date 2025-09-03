package com.texteditor.ui.themes;

import javax.swing.JComponent;
import java.awt.Color;

// Normal/Default theme that maintains the original FlatLaf appearance This is the default theme
// users see when they first open the application

public class NormalTheme extends PixelatedTheme {

    // Normal theme colors (similar to FlatLaf defaults)
    private static final Color NORMAL_BACKGROUND = new Color(0xFFFFFF); // White
    private static final Color NORMAL_FOREGROUND = new Color(0x000000); // Black
    private static final Color NORMAL_TEXT_BG = new Color(0xFFFFFF); // White
    private static final Color NORMAL_TEXT_FG = new Color(0x000000); // Black
    private static final Color NORMAL_SELECTION = new Color(0x3875D7); // Blue selection
    private static final Color NORMAL_BORDER = new Color(0xCCCCCC); // Light gray border
    private static final Color NORMAL_MENU_BG = new Color(0xF5F5F5); // Light gray menu
    private static final Color NORMAL_BUTTON_BG = new Color(0xE1E1E1); // Button gray
    private static final Color NORMAL_CARET = new Color(0x000000); // Black caret

    @Override
    public String getThemeName() {
        return "Normal (Default)";
    }

    @Override
    public Color getBackgroundColor() {
        return NORMAL_BACKGROUND;
    }

    @Override
    public Color getForegroundColor() {
        return NORMAL_FOREGROUND;
    }

    @Override
    public Color getAccentColor() {
        return NORMAL_SELECTION;
    }

    @Override
    public Color getSecondaryColor() {
        return NORMAL_BORDER;
    }

    @Override
    public Color getSelectionColor() {
        return NORMAL_SELECTION;
    }

    @Override
    public Color getBorderColor() {
        return NORMAL_BORDER;
    }

    @Override
    public Color getMenuBackgroundColor() {
        return NORMAL_MENU_BG;
    }

    @Override
    public Color getMenuForegroundColor() {
        return NORMAL_FOREGROUND;
    }

    @Override
    public Color getButtonBackgroundColor() {
        return NORMAL_BUTTON_BG;
    }

    @Override
    public Color getButtonForegroundColor() {
        return NORMAL_FOREGROUND;
    }

    @Override
    public Color getTextAreaBackgroundColor() {
        return NORMAL_TEXT_BG;
    }

    @Override
    public Color getTextAreaForegroundColor() {
        return NORMAL_TEXT_FG;
    }

    @Override
    public Color getCaretColor() {
        return NORMAL_CARET;
    }

    // Override to apply minimal styling for normal theme
    
    @Override
    public void applyToComponent(JComponent component) {
        // For normal theme, just set basic colors without pixelated styling
        component.setBackground(getBackgroundColor());
        component.setForeground(getForegroundColor());
        // Don't apply pixelated font or borders for normal theme
        component.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 12));
    }

    // Override to apply minimal styling for normal theme text area
    
    @Override
    public void applyToTextArea(javax.swing.JTextArea textArea) {
        textArea.setBackground(getTextAreaBackgroundColor());
        textArea.setForeground(getTextAreaForegroundColor());
        textArea.setCaretColor(getCaretColor());
        textArea.setSelectionColor(getSelectionColor());
        textArea.setSelectedTextColor(getBackgroundColor());
        // Use normal font instead of pixelated font
        textArea.setFont(new java.awt.Font("Consolas", java.awt.Font.PLAIN, 14));

        // Minimal border for normal theme
        textArea.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                javax.swing.BorderFactory.createLineBorder(getBorderColor(), 1),
                javax.swing.BorderFactory.createEmptyBorder(4, 4, 4, 4)));

        textArea.setOpaque(true);
        textArea.revalidate();
        textArea.repaint();
    }

    // Override to apply minimal styling for normal theme buttons
    
    @Override
    public void applyToButton(javax.swing.AbstractButton button) {
        button.setBackground(getButtonBackgroundColor());
        button.setForeground(getButtonForegroundColor());
        // Use normal font instead of pixelated font
        button.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 11));
        button.setFocusPainted(true); // Allow focus painting for normal theme
        // Don't apply custom border for normal theme
        button.setBorder(javax.swing.UIManager.getBorder("Button.border"));
    }
}

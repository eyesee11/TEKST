package com.texteditor.ui.themes;

import java.awt.Color;

// Neon Synthwave theme inspired by 80s cyberpunk aesthetics Color palette: Dark purple/black
// background with hot pink and neon cyan accents

public class NeonSynthwaveTheme extends PixelatedTheme {

    // Synthwave color palette
    private static final Color DARK_PURPLE = new Color(0x0F0F23); // Very dark purple
    private static final Color DEEP_PURPLE = new Color(0x1A1A2E); // Deep purple surface
    private static final Color HOT_PINK = new Color(0xFF006E); // Hot pink/magenta
    private static final Color NEON_CYAN = new Color(0x00F5FF); // Bright cyan
    private static final Color ELECTRIC_PURPLE = new Color(0x8A2BE2); // Electric purple
    private static final Color DARK_BORDER = new Color(0x2E2E5D); // Dark purple border
    private static final Color SYNTHWAVE_WHITE = new Color(0xF8F8FF); // Ghost white
    private static final Color NEON_GREEN = new Color(0x39FF14); // Neon green accent

    @Override
    public String getThemeName() {
        return "🌃 Neon Synthwave";
    }

    @Override
    public Color getBackgroundColor() {
        return DARK_PURPLE;
    }

    @Override
    public Color getForegroundColor() {
        return SYNTHWAVE_WHITE;
    }

    @Override
    public Color getAccentColor() {
        return HOT_PINK;
    }

    @Override
    public Color getSecondaryColor() {
        return NEON_CYAN;
    }

    @Override
    public Color getSelectionColor() {
        return HOT_PINK;
    }

    @Override
    public Color getBorderColor() {
        return DARK_BORDER;
    }

    @Override
    public Color getMenuBackgroundColor() {
        return DEEP_PURPLE;
    }

    @Override
    public Color getMenuForegroundColor() {
        return SYNTHWAVE_WHITE;
    }

    @Override
    public Color getButtonBackgroundColor() {
        return DARK_BORDER;
    }

    @Override
    public Color getButtonForegroundColor() {
        return NEON_CYAN;
    }

    @Override
    public Color getTextAreaBackgroundColor() {
        return DEEP_PURPLE;
    }

    @Override
    public Color getTextAreaForegroundColor() {
        return SYNTHWAVE_WHITE;
    }

    @Override
    public Color getCaretColor() {
        return HOT_PINK;
    }
}

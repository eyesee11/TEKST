package com.texteditor.ui.themes;

import java.awt.Color;

// Dark retro pixelated theme inspired by classic 8-bit games Color palette: Dark grays, neon
// greens, and electric blues

public class DarkRetroTheme extends PixelatedTheme {

    // Dark retro color palette
    private static final Color DARK_BACKGROUND = new Color(0x0D1117); // Very dark blue-gray
    private static final Color DARK_SURFACE = new Color(0x161B22); // Dark surface
    private static final Color NEON_GREEN = new Color(0x39FF14); // Bright neon green
    private static final Color ELECTRIC_BLUE = new Color(0x00BFFF); // Electric blue
    private static final Color RETRO_PURPLE = new Color(0x8A2BE2); // Blue violet
    private static final Color DARK_GRAY = new Color(0x2D333B); // Dark gray for borders
    private static final Color LIGHT_GRAY = new Color(0x484F58); // Lighter gray
    private static final Color NEON_CYAN = new Color(0x00FFFF); // Bright cyan
    private static final Color WHITE_TEXT = new Color(0xF0F6FC); // Off-white text

    @Override
    public String getThemeName() {
        return "Dark Retro Pixelated";
    }

    @Override
    public Color getBackgroundColor() {
        return DARK_BACKGROUND;
    }

    @Override
    public Color getForegroundColor() {
        return WHITE_TEXT;
    }

    @Override
    public Color getAccentColor() {
        return NEON_GREEN;
    }

    @Override
    public Color getSecondaryColor() {
        return ELECTRIC_BLUE;
    }

    @Override
    public Color getSelectionColor() {
        return NEON_CYAN;
    }

    @Override
    public Color getBorderColor() {
        return DARK_GRAY;
    }

    @Override
    public Color getMenuBackgroundColor() {
        return DARK_SURFACE;
    }

    @Override
    public Color getMenuForegroundColor() {
        return WHITE_TEXT;
    }

    @Override
    public Color getButtonBackgroundColor() {
        return DARK_GRAY;
    }

    @Override
    public Color getButtonForegroundColor() {
        return NEON_GREEN;
    }

    @Override
    public Color getTextAreaBackgroundColor() {
        return DARK_SURFACE;
    }

    @Override
    public Color getTextAreaForegroundColor() {
        return WHITE_TEXT;
    }

    @Override
    public Color getCaretColor() {
        return NEON_GREEN;
    }
}

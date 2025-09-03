package com.texteditor.ui.themes;

import java.awt.Color;

// Sunset Arcade theme inspired by retro arcade machines and sunset Color palette: Warm oranges,
// deep purples, and golden yellows

public class SunsetArcadeTheme extends PixelatedTheme {

    // Sunset color palette
    private static final Color DARK_PURPLE = new Color(0x2B1B17); // Dark sunset purple
    private static final Color SUNSET_ORANGE = new Color(0xFF4500); // Bright sunset orange
    private static final Color GOLDEN_YELLOW = new Color(0xFFD700); // Golden yellow
    private static final Color DEEP_RED = new Color(0x8B0000); // Deep red
    private static final Color WARM_PURPLE = new Color(0x4B0082); // Warm purple surface
    private static final Color AMBER_ORANGE = new Color(0xFF8C00); // Amber orange border
    private static final Color CREAM_YELLOW = new Color(0xFFFACD); // Cream yellow text
    private static final Color NEON_PINK = new Color(0xFF1493); // Neon pink accent

    @Override
    public String getThemeName() {
        return "🌅 Sunset Arcade";
    }

    @Override
    public Color getBackgroundColor() {
        return DARK_PURPLE;
    }

    @Override
    public Color getForegroundColor() {
        return CREAM_YELLOW;
    }

    @Override
    public Color getAccentColor() {
        return SUNSET_ORANGE;
    }

    @Override
    public Color getSecondaryColor() {
        return GOLDEN_YELLOW;
    }

    @Override
    public Color getSelectionColor() {
        return NEON_PINK;
    }

    @Override
    public Color getBorderColor() {
        return AMBER_ORANGE;
    }

    @Override
    public Color getMenuBackgroundColor() {
        return WARM_PURPLE;
    }

    @Override
    public Color getMenuForegroundColor() {
        return CREAM_YELLOW;
    }

    @Override
    public Color getButtonBackgroundColor() {
        return DEEP_RED;
    }

    @Override
    public Color getButtonForegroundColor() {
        return GOLDEN_YELLOW;
    }

    @Override
    public Color getTextAreaBackgroundColor() {
        return WARM_PURPLE;
    }

    @Override
    public Color getTextAreaForegroundColor() {
        return CREAM_YELLOW;
    }

    @Override
    public Color getCaretColor() {
        return SUNSET_ORANGE;
    }
}

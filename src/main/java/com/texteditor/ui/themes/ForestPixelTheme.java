package com.texteditor.ui.themes;

import java.awt.Color;

// Forest Pixel theme inspired by nature and retro games Color palette: Forest greens, earth browns,
// and natural tones

public class ForestPixelTheme extends PixelatedTheme {

    // Forest color palette
    private static final Color DARK_FOREST = new Color(0x0D2818); // Dark forest green
    private static final Color FOREST_GREEN = new Color(0x1B4332); // Forest green surface
    private static final Color BRIGHT_GREEN = new Color(0x40916C); // Bright green accent
    private static final Color LIME_GREEN = new Color(0x52B788); // Lime green
    private static final Color EARTH_BROWN = new Color(0x8B4513); // Earth brown
    private static final Color MOSS_GREEN = new Color(0x2D5016); // Moss green border
    private static final Color CREAM_WHITE = new Color(0xF7F3E9); // Cream white text
    private static final Color GOLD_YELLOW = new Color(0xFFD700); // Gold accent

    @Override
    public String getThemeName() {
        return "🌲 Forest Pixel";
    }

    @Override
    public Color getBackgroundColor() {
        return DARK_FOREST;
    }

    @Override
    public Color getForegroundColor() {
        return CREAM_WHITE;
    }

    @Override
    public Color getAccentColor() {
        return BRIGHT_GREEN;
    }

    @Override
    public Color getSecondaryColor() {
        return LIME_GREEN;
    }

    @Override
    public Color getSelectionColor() {
        return GOLD_YELLOW;
    }

    @Override
    public Color getBorderColor() {
        return MOSS_GREEN;
    }

    @Override
    public Color getMenuBackgroundColor() {
        return FOREST_GREEN;
    }

    @Override
    public Color getMenuForegroundColor() {
        return CREAM_WHITE;
    }

    @Override
    public Color getButtonBackgroundColor() {
        return MOSS_GREEN;
    }

    @Override
    public Color getButtonForegroundColor() {
        return BRIGHT_GREEN;
    }

    @Override
    public Color getTextAreaBackgroundColor() {
        return FOREST_GREEN;
    }

    @Override
    public Color getTextAreaForegroundColor() {
        return CREAM_WHITE;
    }

    @Override
    public Color getCaretColor() {
        return GOLD_YELLOW;
    }
}

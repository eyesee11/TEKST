package com.texteditor.ui.themes;

import java.awt.Color;

// Matrix Hacker theme inspired by The Matrix movie and hacker aesthetics Color palette: Black
// background with various shades of green

public class MatrixHackerTheme extends PixelatedTheme {

    // Matrix color palette
    private static final Color MATRIX_BLACK = new Color(0x000000); // Pure black
    private static final Color DARK_GREEN = new Color(0x003300); // Dark green surface
    private static final Color MATRIX_GREEN = new Color(0x00FF00); // Bright matrix green
    private static final Color LIME_GREEN = new Color(0x32CD32); // Lime green
    private static final Color FOREST_GREEN = new Color(0x228B22); // Forest green
    private static final Color DARK_BORDER = new Color(0x004400); // Dark green border
    private static final Color BRIGHT_WHITE = new Color(0xFFFFFF); // Bright white text
    private static final Color NEON_GREEN = new Color(0x39FF14); // Neon green accent

    @Override
    public String getThemeName() {
        return "💚 Matrix Hacker";
    }

    @Override
    public Color getBackgroundColor() {
        return MATRIX_BLACK;
    }

    @Override
    public Color getForegroundColor() {
        return MATRIX_GREEN;
    }

    @Override
    public Color getAccentColor() {
        return NEON_GREEN;
    }

    @Override
    public Color getSecondaryColor() {
        return LIME_GREEN;
    }

    @Override
    public Color getSelectionColor() {
        return FOREST_GREEN;
    }

    @Override
    public Color getBorderColor() {
        return DARK_BORDER;
    }

    @Override
    public Color getMenuBackgroundColor() {
        return DARK_GREEN;
    }

    @Override
    public Color getMenuForegroundColor() {
        return MATRIX_GREEN;
    }

    @Override
    public Color getButtonBackgroundColor() {
        return DARK_BORDER;
    }

    @Override
    public Color getButtonForegroundColor() {
        return NEON_GREEN;
    }

    @Override
    public Color getTextAreaBackgroundColor() {
        return MATRIX_BLACK;
    }

    @Override
    public Color getTextAreaForegroundColor() {
        return MATRIX_GREEN;
    }

    @Override
    public Color getCaretColor() {
        return NEON_GREEN;
    }
}

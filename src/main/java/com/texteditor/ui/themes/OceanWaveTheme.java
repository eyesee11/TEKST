package com.texteditor.ui.themes;

import java.awt.Color;

// Ocean Wave theme inspired by deep ocean and water Color palette: Deep ocean blues, aqua, and sea
// foam

public class OceanWaveTheme extends PixelatedTheme {

    // Ocean color palette
    private static final Color DEEP_OCEAN = new Color(0x003366); // Deep ocean blue
    private static final Color OCEAN_BLUE = new Color(0x0066CC); // Ocean blue surface
    private static final Color AQUA_BLUE = new Color(0x00BFFF); // Bright aqua blue
    private static final Color SEA_FOAM = new Color(0x9FE2BF); // Sea foam green
    private static final Color NAVY_BORDER = new Color(0x002244); // Navy border
    private static final Color PEARL_WHITE = new Color(0xF0F8FF); // Pearl white text
    private static final Color CORAL_ORANGE = new Color(0xFF7F50); // Coral accent
    private static final Color TURQUOISE = new Color(0x40E0D0); // Turquoise highlight

    @Override
    public String getThemeName() {
        return "🌊 Ocean Wave";
    }

    @Override
    public Color getBackgroundColor() {
        return DEEP_OCEAN;
    }

    @Override
    public Color getForegroundColor() {
        return PEARL_WHITE;
    }

    @Override
    public Color getAccentColor() {
        return AQUA_BLUE;
    }

    @Override
    public Color getSecondaryColor() {
        return TURQUOISE;
    }

    @Override
    public Color getSelectionColor() {
        return SEA_FOAM;
    }

    @Override
    public Color getBorderColor() {
        return NAVY_BORDER;
    }

    @Override
    public Color getMenuBackgroundColor() {
        return OCEAN_BLUE;
    }

    @Override
    public Color getMenuForegroundColor() {
        return PEARL_WHITE;
    }

    @Override
    public Color getButtonBackgroundColor() {
        return NAVY_BORDER;
    }

    @Override
    public Color getButtonForegroundColor() {
        return AQUA_BLUE;
    }

    @Override
    public Color getTextAreaBackgroundColor() {
        return OCEAN_BLUE;
    }

    @Override
    public Color getTextAreaForegroundColor() {
        return PEARL_WHITE;
    }

    @Override
    public Color getCaretColor() {
        return CORAL_ORANGE;
    }
}

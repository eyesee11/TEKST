package com.texteditor.ui;

import com.texteditor.ui.themes.ThemeManager;
import com.texteditor.ui.themes.PixelatedTheme;
import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// advanced formatting popup that appears when text is selected provides options for bold, italic,
// font size, and other formatting
public class FormattingPopup extends JPopupMenu {

    private JTextPane textPane; // changed to JTextPane for rich text support
    private int selectionStart;
    private int selectionEnd;
    private String selectedText;

    // Formatting buttons
    private JButton boldButton;
    private JButton italicButton;
    private JButton underlineButton;
    private JComboBox<String> fontSizeCombo;
    private JComboBox<String> fontFamilyCombo;
    private JButton textColorButton;
    private JButton highlightButton;

    // Current formatting state
    private boolean isBold = false;
    private boolean isItalic = false;
    private boolean isUnderline = false;
    private int fontSize = 14;
    private String fontFamily = "Consolas";
    private Color textColor = Color.BLACK;
    private Color highlightColor = Color.YELLOW;

    public FormattingPopup(JTextPane textPane) {
        this.textPane = textPane;
        initializeComponents();
        setupLayout();
        applyTheme();
    }

    // initialize all formatting components
    private void initializeComponents() {
        // Bold button
        boldButton = new JButton("B");
        boldButton.setFont(new Font("Arial", Font.BOLD, 12));
        boldButton.setPreferredSize(new Dimension(35, 30));
        boldButton.setToolTipText("Bold");
        boldButton.addActionListener(e -> toggleBold());

        // Italic button
        italicButton = new JButton("I");
        italicButton.setFont(new Font("Arial", Font.ITALIC, 12));
        italicButton.setPreferredSize(new Dimension(35, 30));
        italicButton.setToolTipText("Italic");
        italicButton.addActionListener(e -> toggleItalic());

        // Underline button
        underlineButton = new JButton("U");
        underlineButton.setFont(new Font("Arial", Font.PLAIN, 12));
        underlineButton.setPreferredSize(new Dimension(35, 30));
        underlineButton.setToolTipText("Underline");
        underlineButton.addActionListener(e -> toggleUnderline());

        // Font size combo
        String[] fontSizes = {"8", "10", "12", "14", "16", "18", "20", "24", "28", "32"};
        fontSizeCombo = new JComboBox<>(fontSizes);
        fontSizeCombo.setSelectedItem("14");
        fontSizeCombo.setPreferredSize(new Dimension(60, 30));
        fontSizeCombo.setToolTipText("Font Size");
        fontSizeCombo.addActionListener(e -> changeFontSize());

        // Font family combo
        String[] fontFamilies =
                {"Consolas", "Arial", "Times New Roman", "Courier New", "Verdana", "Tahoma"};
        fontFamilyCombo = new JComboBox<>(fontFamilies);
        fontFamilyCombo.setSelectedItem("Consolas");
        fontFamilyCombo.setPreferredSize(new Dimension(100, 30));
        fontFamilyCombo.setToolTipText("Font Family");
        fontFamilyCombo.addActionListener(e -> changeFontFamily());

        // Text color button
        textColorButton = new JButton();
        textColorButton.setBackground(Color.BLACK);
        textColorButton.setPreferredSize(new Dimension(35, 30));
        textColorButton.setToolTipText("Text Color");
        textColorButton.addActionListener(e -> chooseTextColor());

        // Highlight button
        highlightButton = new JButton();
        highlightButton.setBackground(Color.YELLOW);
        highlightButton.setPreferredSize(new Dimension(35, 30));
        highlightButton.setToolTipText("Highlight Color");
        highlightButton.addActionListener(e -> chooseHighlightColor());
    }

    // setup the layout of the popup
    private void setupLayout() {
        setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));

        // Add formatting buttons
        add(boldButton);
        add(italicButton);
        add(underlineButton);

        // Add separator
        add(new JSeparator(SwingConstants.VERTICAL));

        // Add font controls
        add(new JLabel("Font:"));
        add(fontFamilyCombo);
        add(new JLabel("Size:"));
        add(fontSizeCombo);

        // Add separator
        add(new JSeparator(SwingConstants.VERTICAL));

        // Add color controls
        add(new JLabel("Color:"));
        add(textColorButton);
        add(new JLabel("Highlight:"));
        add(highlightButton);

        // Set popup size
        setPreferredSize(new Dimension(450, 50));
    }

    // Apply current theme to the popup
    public void applyTheme() {
        ThemeManager themeManager = ThemeManager.getInstance();
        PixelatedTheme currentTheme = themeManager.getCurrentTheme();

        // Apply theme to popup background
        setBackground(currentTheme.getMenuBackgroundColor());
        setBorder(BorderFactory.createLineBorder(currentTheme.getBorderColor(), 2));

        // Apply theme to all buttons
        Component[] components = getComponents();
        for (Component component : components) {
            if (component instanceof JButton) {
                JButton button = (JButton) component;
                currentTheme.applyToButton(button);

                // Special handling for color buttons
                if (button == textColorButton || button == highlightButton) {
                    // Keep the color background but apply theme border
                    button.setBorder(
                            BorderFactory.createLineBorder(currentTheme.getBorderColor(), 1));
                }
            } else if (component instanceof JComboBox) {
                JComboBox<?> combo = (JComboBox<?>) component;
                combo.setBackground(currentTheme.getButtonBackgroundColor());
                combo.setForeground(currentTheme.getButtonForegroundColor());
                combo.setBorder(BorderFactory.createLineBorder(currentTheme.getBorderColor(), 1));

                // Apply theme font
                if (!currentTheme.getThemeName().equals("Normal (Default)")) {
                    combo.setFont(PixelatedTheme.PIXELATED_FONT_SMALL);
                }
            } else if (component instanceof JLabel) {
                JLabel label = (JLabel) component;
                label.setForeground(currentTheme.getMenuForegroundColor());

                // Apply theme font
                if (!currentTheme.getThemeName().equals("Normal (Default)")) {
                    label.setFont(PixelatedTheme.PIXELATED_FONT_SMALL);
                }
            }
        }
    }

    // Show the popup at the specified location with selected text

    public void showForSelectedText(int x, int y, int selStart, int selEnd) {
        this.selectionStart = selStart;
        this.selectionEnd = selEnd;
        this.selectedText = textPane.getSelectedText();

        if (selectedText != null && !selectedText.trim().isEmpty()) {
            // Update current formatting state based on selection
            updateFormattingState();

            // Apply current theme
            applyTheme();

            // Show the popup
            show(textPane, x, y);
        }
    }

    // Update formatting state based on current selection

    private void updateFormattingState() {
        Font currentFont = textPane.getFont();
        if (currentFont != null) {
            isBold = currentFont.isBold();
            isItalic = currentFont.isItalic();
            fontSize = currentFont.getSize();
            fontFamily = currentFont.getFamily();

            // Update UI components
            boldButton.setSelected(isBold);
            italicButton.setSelected(isItalic);
            fontSizeCombo.setSelectedItem(String.valueOf(fontSize));
            fontFamilyCombo.setSelectedItem(fontFamily);
        }

        textColor = textPane.getForeground();
        textColorButton.setBackground(textColor);
    }

    // Toggle bold formatting

    private void toggleBold() {
        isBold = !isBold;
        boldButton.setSelected(isBold);
        applyFormatting();
    }

    // Toggle italic formatting

    private void toggleItalic() {
        isItalic = !isItalic;
        italicButton.setSelected(isItalic);
        applyFormatting();
    }

    // Toggle underline formatting

    private void toggleUnderline() {
        isUnderline = !isUnderline;
        underlineButton.setSelected(isUnderline);
        applyFormatting();
    }

    // Change font size

    private void changeFontSize() {
        try {
            fontSize = Integer.parseInt((String) fontSizeCombo.getSelectedItem());
            applyFormatting();
        } catch (NumberFormatException e) {
            // Invalid font size, ignore
        }
    }

    // Change font family

    private void changeFontFamily() {
        fontFamily = (String) fontFamilyCombo.getSelectedItem();
        applyFormatting();
    }

    // Choose text color

    private void chooseTextColor() {
        Color newColor = JColorChooser.showDialog(this, "Choose Text Color", textColor);
        if (newColor != null) {
            textColor = newColor;
            textColorButton.setBackground(textColor);
            applyFormatting();
        }
    }

    // Choose highlight color

    private void chooseHighlightColor() {
        Color newColor = JColorChooser.showDialog(this, "Choose Highlight Color", highlightColor);
        if (newColor != null) {
            highlightColor = newColor;
            highlightButton.setBackground(highlightColor);
            applyHighlight();
        }
    }

    // Apply current formatting to selected text
    private void applyFormatting() {
        if (selectedText == null || selectedText.trim().isEmpty()) {
            return;
        }

        // Get the styled document from the text pane
        StyledDocument doc = textPane.getStyledDocument();

        // Create attribute set for formatting
        SimpleAttributeSet attrs = new SimpleAttributeSet();

        // Set font attributes
        StyleConstants.setBold(attrs, isBold);
        StyleConstants.setItalic(attrs, isUnderline);
        StyleConstants.setUnderline(attrs, isUnderline);
        StyleConstants.setFontSize(attrs, fontSize);
        StyleConstants.setFontFamily(attrs, fontFamily);
        StyleConstants.setForeground(attrs, textColor);

        // Apply formatting to selected text
        doc.setCharacterAttributes(selectionStart, selectionEnd - selectionStart, attrs, false);

        textPane.revalidate();
        textPane.repaint();

        // Hide popup after applying formatting
        setVisible(false);
    }

    // Apply highlight to selected text
    private void applyHighlight() {
        if (selectedText == null || selectedText.trim().isEmpty()) {
            return;
        }

        // Use StyledDocument for proper highlighting
        StyledDocument doc = textPane.getStyledDocument();
        SimpleAttributeSet attrs = new SimpleAttributeSet();
        StyleConstants.setBackground(attrs, highlightColor);

        // Apply highlight to selected text
        doc.setCharacterAttributes(selectionStart, selectionEnd - selectionStart, attrs, false);

        // Hide popup after applying highlight
        setVisible(false);
    }
}

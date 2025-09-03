package com.texteditor.ui.dialogs;

import com.texteditor.model.DocumentManager;
import com.texteditor.ui.themes.ThemeManager;
import com.texteditor.ui.themes.PixelatedTheme;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

// Themed Find and Replace dialog for searching and replacing text

public class FindReplaceDialog extends JDialog {

    private final DocumentManager documentManager;

    // UI Components
    private JTextField findField;
    private JTextField replaceField;
    private JCheckBox caseSensitiveBox;
    private JCheckBox wholeWordBox;
    private JButton findNextButton;
    private JButton replaceButton;
    private JButton replaceAllButton;
    private JButton closeButton;
    private JLabel statusLabel;

    public FindReplaceDialog(Window parent, DocumentManager documentManager) {
        super(parent, "Find & Replace", ModalityType.MODELESS);
        this.documentManager = documentManager;
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        configureDialog();
    }

    private void initializeComponents() {
        // Search field
        findField = new JTextField(20);
        replaceField = new JTextField(20);

        // Options
        caseSensitiveBox = new JCheckBox("Case sensitive");
        wholeWordBox = new JCheckBox("Whole words only");

        // Buttons
        findNextButton = new JButton("Find Next");
        replaceButton = new JButton("Replace");
        replaceAllButton = new JButton("Replace All");
        closeButton = new JButton("Close");

        // Status label
        statusLabel = new JLabel(" ");
        statusLabel.setForeground(Color.BLUE);
    }

    private void setupLayout() {
        setLayout(new BorderLayout());

        // Main panel
        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // Find row
        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPanel.add(new JLabel("Find:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(findField, gbc);

        // Replace row
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        mainPanel.add(new JLabel("Replace:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(replaceField, gbc);

        // Options
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        mainPanel.add(caseSensitiveBox, gbc);
        gbc.gridy = 3;
        mainPanel.add(wholeWordBox, gbc);

        add(mainPanel, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(findNextButton);
        buttonPanel.add(replaceButton);
        buttonPanel.add(replaceAllButton);
        buttonPanel.add(closeButton);

        add(buttonPanel, BorderLayout.SOUTH);

        // Status
        add(statusLabel, BorderLayout.NORTH);
    }

    private void setupEventHandlers() {
        // Close button
        closeButton.addActionListener(e -> setVisible(false));

        // Escape key to close
        KeyStroke escapeStroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(escapeStroke, "ESCAPE");
        getRootPane().getActionMap().put("ESCAPE", new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                setVisible(false);
            }
        });

        // Find Next
        findNextButton.addActionListener(e -> findNext());

        // Replace
        replaceButton.addActionListener(e -> replace());

        // Replace All
        replaceAllButton.addActionListener(e -> replaceAll());

        // Enter key in find field
        findField.addActionListener(e -> findNext());
    }

    private void configureDialog() {
        setSize(400, 250);
        setResizable(false);
        setDefaultCloseOperation(HIDE_ON_CLOSE);
    }

    @Override
    public void setVisible(boolean visible) {
        if (visible) {
            // Apply current theme when dialog becomes visible
            applyCurrentTheme();

            // Pre-fill with selected text if available
            JTextArea textArea = documentManager.getTextArea();
            if (textArea != null && textArea.getSelectedText() != null) {
                findField.setText(textArea.getSelectedText());
            }
            findField.selectAll();
            findField.requestFocus();

            // Center on parent
            setLocationRelativeTo(getParent());
        }
        super.setVisible(visible);
    }

    // Apply the current theme to all dialog components
    
    private void applyCurrentTheme() {
        ThemeManager themeManager = ThemeManager.getInstance();
        PixelatedTheme theme = themeManager.getCurrentTheme();

        // Apply theme to the dialog
        getContentPane().setBackground(theme.getBackgroundColor());

        // Apply theme to all components
        applyThemeToComponents(this, theme);

        // Special border for pixelated themes
        if (!(theme.getThemeName().equals("Normal (Default)"))) {
            getRootPane().setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(theme.getAccentColor(), 3),
                    BorderFactory.createLineBorder(theme.getBorderColor(), 1)));
        }

        // Revalidate and repaint
        revalidate();
        repaint();
    }

    // Recursively apply theme to all components in a container
    
    private void applyThemeToComponents(Container container, PixelatedTheme theme) {
        for (Component component : container.getComponents()) {
            if (component instanceof JTextField) {
                JTextField field = (JTextField) component;
                field.setBackground(theme.getTextAreaBackgroundColor());
                field.setForeground(theme.getTextAreaForegroundColor());
                field.setCaretColor(theme.getCaretColor());
                field.setSelectionColor(theme.getSelectionColor());
                field.setSelectedTextColor(theme.getBackgroundColor());

                if (theme.getThemeName().equals("Normal (Default)")) {
                    field.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                    field.setBorder(UIManager.getBorder("TextField.border"));
                } else {
                    field.setFont(PixelatedTheme.PIXELATED_FONT_MEDIUM);
                    field.setBorder(BorderFactory.createLineBorder(theme.getBorderColor(), 2));
                }
            } else if (component instanceof JButton) {
                theme.applyToButton((JButton) component);
            } else if (component instanceof JCheckBox) {
                JCheckBox checkBox = (JCheckBox) component;
                checkBox.setBackground(theme.getBackgroundColor());
                checkBox.setForeground(theme.getForegroundColor());
                checkBox.setOpaque(true);

                if (theme.getThemeName().equals("Normal (Default)")) {
                    checkBox.setFont(new Font("Segoe UI", Font.PLAIN, 11));
                } else {
                    checkBox.setFont(PixelatedTheme.PIXELATED_FONT_SMALL);
                }
            } else if (component instanceof JLabel) {
                JLabel label = (JLabel) component;
                label.setForeground(theme.getForegroundColor());

                if (theme.getThemeName().equals("Normal (Default)")) {
                    label.setFont(new Font("Segoe UI", Font.PLAIN, 11));
                } else {
                    label.setFont(PixelatedTheme.PIXELATED_FONT_SMALL);
                }
            } else if (component instanceof JPanel) {
                JPanel panel = (JPanel) component;
                panel.setBackground(theme.getBackgroundColor());
                panel.setOpaque(true);
            }

            // Recursively apply to child containers
            if (component instanceof Container) {
                applyThemeToComponents((Container) component, theme);
            }
        }
    }

    // Find and replace functionality
    private void findNext() {
        String searchText = findField.getText();
        if (searchText.isEmpty()) {
            statusLabel.setText("Enter text to find");
            return;
        }

        JTextArea textArea = documentManager.getTextArea();
        if (textArea == null)
            return;

        String content = textArea.getText();
        String searchContent = caseSensitiveBox.isSelected() ? content : content.toLowerCase();
        String searchTerm = caseSensitiveBox.isSelected() ? searchText : searchText.toLowerCase();

        int startIndex = textArea.getCaretPosition();
        int foundIndex = searchContent.indexOf(searchTerm, startIndex);

        if (foundIndex == -1 && startIndex > 0) {
            // Wrap around to beginning
            foundIndex = searchContent.indexOf(searchTerm, 0);
        }

        if (foundIndex != -1) {
            textArea.setCaretPosition(foundIndex);
            textArea.select(foundIndex, foundIndex + searchText.length());
            statusLabel.setText("Found: " + searchText);
        } else {
            statusLabel.setText("Text not found: " + searchText);
        }
    }

    private void replace() {
        JTextArea textArea = documentManager.getTextArea();
        if (textArea == null)
            return;

        String searchText = findField.getText();
        String replaceText = replaceField.getText();
        String selectedText = textArea.getSelectedText();

        if (selectedText != null) {
            boolean matches = caseSensitiveBox.isSelected() ? selectedText.equals(searchText)
                    : selectedText.equalsIgnoreCase(searchText);

            if (matches) {
                textArea.replaceSelection(replaceText);
                statusLabel.setText("Replaced: " + searchText + " with: " + replaceText);
            }
        }

        // Find next occurrence
        findNext();
    }

    private void replaceAll() {
        String searchText = findField.getText();
        String replaceText = replaceField.getText();

        if (searchText.isEmpty()) {
            statusLabel.setText("Enter text to find");
            return;
        }

        JTextArea textArea = documentManager.getTextArea();
        if (textArea == null)
            return;

        String content = textArea.getText();
        String result;
        int replacements = 0;

        if (caseSensitiveBox.isSelected()) {
            while (content.contains(searchText)) {
                content = content.replace(searchText, replaceText);
                replacements++;
                if (replacements > 1000)
                    break; // Safety limit
            }
            result = content;
        } else {
            // Case-insensitive replacement
            result = content.replaceAll("(?i)" + java.util.regex.Pattern.quote(searchText),
                    java.util.regex.Matcher.quoteReplacement(replaceText));

            // Count replacements (approximate)
            String[] parts = content.split("(?i)" + java.util.regex.Pattern.quote(searchText));
            replacements = parts.length - 1;
        }

        textArea.setText(result);
        statusLabel.setText("Replaced " + replacements + " occurrences");
    }
}

package com.texteditor.ui;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// Enhanced status bar with more information and better styling.

public class StatusBar extends JPanel {

    private JLabel statusLabel;
    private JLabel positionLabel;
    private JLabel documentInfoLabel;
    private JLabel timeLabel;
    private Timer timeUpdateTimer;
    private JPanel rightPanel;

    public StatusBar() {
        initializeComponents();
        setupLayout();
        startTimeUpdater();
    }

    private void initializeComponents() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(0, 25));

        // Create a more sophisticated border
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(3, 8, 3, 8)));

        // Status message (left side)
        statusLabel = new JLabel("Ready");
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        statusLabel.setForeground(new Color(80, 80, 80));

        // Document information (center)
        documentInfoLabel = new JLabel("Lines: 1 | Characters: 0 | Words: 0");
        documentInfoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        documentInfoLabel.setForeground(new Color(80, 80, 80));
        documentInfoLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Position and time (right side)
        rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rightPanel.setOpaque(false);

        positionLabel = new JLabel("Line: 1, Column: 1");
        positionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        positionLabel.setForeground(new Color(80, 80, 80));

        timeLabel = new JLabel();
        timeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        timeLabel.setForeground(new Color(80, 80, 80));
        updateTime();

        rightPanel.add(positionLabel);
        rightPanel.add(createSeparator());
        rightPanel.add(timeLabel);
    }

    private void setupLayout() {
        add(statusLabel, BorderLayout.WEST);
        add(documentInfoLabel, BorderLayout.CENTER);
        add(rightPanel, BorderLayout.EAST);
    }

    private JLabel createSeparator() {
        JLabel separator = new JLabel("|");
        separator.setForeground(Color.LIGHT_GRAY);
        return separator;
    }

    private void startTimeUpdater() {
        // Update time every minute
        timeUpdateTimer = new Timer(60000, e -> updateTime());
        timeUpdateTimer.start();
        updateTime(); // Initial update
    }

    private void updateTime() {
        LocalDateTime now = LocalDateTime.now();
        String timeText = now.format(DateTimeFormatter.ofPattern("HH:mm"));
        timeLabel.setText(timeText);
    }

    public void setStatus(String status) {
        statusLabel.setText(status);
    }

    public void setPosition(int line, int column) {
        positionLabel.setText("Line: " + line + ", Column: " + column);
    }

    public void setDocumentInfo(int lines, int characters, int words) {
        documentInfoLabel
                .setText("Lines: " + lines + " | Characters: " + characters + " | Words: " + words);
    }

    public void updateDocumentStats(String text) {
        if (text == null || text.isEmpty()) {
            setDocumentInfo(1, 0, 0);
            return;
        }

        int lines = text.split("\n").length;
        int characters = text.length();
        int words = text.trim().isEmpty() ? 0 : text.trim().split("\\s+").length;

        setDocumentInfo(lines, characters, words);
    }

    // Clean up timer when component is disposed
    public void dispose() {
        if (timeUpdateTimer != null) {
            timeUpdateTimer.stop();
        }
    }
}

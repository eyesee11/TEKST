package com.texteditor;

import com.formdev.flatlaf.FlatLightLaf;
import com.texteditor.ui.MainWindow;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

// Main app class down here...
public class TextEditor {
    public static void main(String[] args) {
        // setting up CSS for the GUI
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());

            // Disable some FlatLaf defaults to allow our themes to work better
            System.setProperty("flatlaf.useWindowDecorations", "false");

        } catch (UnsupportedLookAndFeelException e) {
            System.err.println("Failed to initialize Look and Feel:  " + e.getMessage());
            // fall back to system default GUI if modern theme fails
        }

        // launching GUI on the event dispatch thread(EDT)
        // similar to how React runs on the main thread
        SwingUtilities.invokeLater(() -> {
            new MainWindow().setVisible(true);
        });
    }
}

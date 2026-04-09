import gui.StockDashboard;

import javax.swing.*;

/**
 * Main entry point for the Stock Market Dashboard application.
 * Demonstrates proper application initialization and threading best practices.
 */
public class Main {
    public static void main(String[] args) {
        // Ensure GUI creation happens on the Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> {
            StockDashboard dashboard = new StockDashboard();
            dashboard.setVisible(true);
        });
    }
}

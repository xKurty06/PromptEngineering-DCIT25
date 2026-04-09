package gui;

import comparators.ChangeComparator;
import comparators.PriceComparator;
import comparators.QuantityComparator;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import models.Stock;
import sorting.BubbleSort;
import sorting.Sorter;

/**
 * Main GUI class for the Stock Market Dashboard.
 * Demonstrates Swing components and visual representation of the Bubble Sort algorithm.
 * 
 * Features:
 * - Real-time visual updates during sorting with color highlighting
 * - Visualization of comparisons (YELLOW) and swaps (RED)
 * - Multiple sorting criteria (Price, Quantity, Price Change)
 * - Adjustable animation speed
 * - Statistics display (swaps and comparisons)
 * - Sample data generation
 * - Thread-safe sorting operations
 */
public class StockDashboard extends JFrame {
    private JTable stockTable;
    private DefaultTableModel tableModel;
    private List<Stock> stocks;
    private JButton sortButton;
    private JButton resetButton;
    private JComboBox<String> sortCriteriaCombo;
    private JLabel statisticsLabel;
    private JLabel statusLabel;
    private JLabel comparisonLabel;
    private JProgressBar sortingProgress;
    private JSlider speedSlider;
    private boolean isSorting = false;
    
    // Tracking indices being compared or swapped
    private int comparingIndex1 = -1;
    private int comparingIndex2 = -1;
    private int swappingIndex1 = -1;
    private int swappingIndex2 = -1;
    private int compareColumnIndex = -1;
    private String compareFieldName = "";
    private boolean isSwapAnimating = false;
    private float swapAnimationProgress = 0f;
    private Stock swapStock1;
    private Stock swapStock2;
    private int swapRow1 = -1;
    private int swapRow2 = -1;
    private javax.swing.Timer swapAnimationTimer;
    private JPanel overlayPanel;
    private long sortDelay = 800; // Milliseconds between operations

    // Column names for the table
    private static final String[] COLUMN_NAMES = {"Symbol", "Company Name", "Price", "Quantity", "Change %"};

    /**
     * Constructor initializing the GUI components.
     */
    public StockDashboard() {
        setTitle("📈 Real-Time Stock Market Dashboard - Bubble Sort Visualizer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 700);
        setLocationRelativeTo(null);
        setResizable(true);

        // Initialize data
        stocks = new ArrayList<>();
        loadSampleData();

        // Create UI
        createUI();
    }

    /**
     * Creates all GUI components and sets up the layout.
     */
    private void createUI() {
        // Main panel with BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Top control panel
        JPanel controlPanel = createControlPanel();
        mainPanel.add(controlPanel, BorderLayout.NORTH);

        // Center table panel
        JPanel tablePanel = createTablePanel();
        mainPanel.add(tablePanel, BorderLayout.CENTER);

        // Bottom statistics panel
        JPanel bottomPanel = createBottomPanel();
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);
        createOverlayPanel();
    }

    /**
     * Creates the overlay panel used for swapping animations.
     */
    private void createOverlayPanel() {
        overlayPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (!isSwapAnimating || swapStock1 == null || swapStock2 == null) {
                    return;
                }

                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                Rectangle rect1 = stockTable.getCellRect(swapRow1, 0, true);
                Rectangle rect2 = stockTable.getCellRect(swapRow2, 0, true);
                Rectangle overlayRect1 = SwingUtilities.convertRectangle(stockTable, rect1, this);
                Rectangle overlayRect2 = SwingUtilities.convertRectangle(stockTable, rect2, this);

                int width = stockTable.getWidth();
                int height = overlayRect1.height;
                int y1 = overlayRect1.y + Math.round((overlayRect2.y - overlayRect1.y) * swapAnimationProgress);
                int y2 = overlayRect2.y + Math.round((overlayRect1.y - overlayRect2.y) * swapAnimationProgress);

                drawAnimatedRow(g2, swapStock1, overlayRect1.x, y1, width, height, new Color(255, 220, 220));
                drawAnimatedRow(g2, swapStock2, overlayRect2.x, y2, width, height, new Color(220, 220, 255));
                g2.dispose();
            }

            @Override
            public boolean contains(int x, int y) {
                return false;
            }
        };

        overlayPanel.setOpaque(false);
        overlayPanel.setBounds(0, 0, getWidth(), getHeight());
        overlayPanel.setVisible(false);
        getLayeredPane().add(overlayPanel, JLayeredPane.DRAG_LAYER);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                overlayPanel.setSize(getWidth(), getHeight());
            }
        });
    }

    private void drawAnimatedRow(Graphics2D g2, Stock stock, int x, int y, int width, int height, Color background) {
        g2.setColor(new Color(background.getRed(), background.getGreen(), background.getBlue(), 180));
        g2.fillRoundRect(x + 2, y + 2, width - 4, height - 4, 12, 12);
        g2.setColor(new Color(100, 100, 100, 200));
        g2.setStroke(new BasicStroke(1.5f));
        g2.drawRoundRect(x + 2, y + 2, width - 4, height - 4, 12, 12);

        g2.setFont(new Font("Monospaced", Font.BOLD, 11));
        g2.setColor(Color.DARK_GRAY);

        int padding = 14;
        int cellWidth = width / COLUMN_NAMES.length;
        int currentX = x + padding;
        int textY = y + height / 2 + 5;

        String[] values = {
            stock.getSymbol(),
            stock.getName(),
            String.format("₱%.2f", stock.getPrice()),
            String.valueOf(stock.getQuantity()),
            String.format("%+.2f%%", stock.getChange())
        };

        for (String text : values) {
            g2.drawString(text, currentX, textY);
            currentX += cellWidth;
        }
    }

    /**
     * Creates the control panel with sorting options.
     */
    private JPanel createControlPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Controls"));
        panel.setBackground(new Color(240, 240, 240));

        // Left: Sort criteria
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        JLabel criteriaLabel = new JLabel("Sort By:");
        String[] sortCriteria = {
                "Price (Ascending)",
                "Quantity (Ascending)",
                "Change % (Descending)"
        };
        sortCriteriaCombo = new JComboBox<>(sortCriteria);
        sortCriteriaCombo.setEnabled(true);
        leftPanel.add(criteriaLabel);
        leftPanel.add(sortCriteriaCombo);

        // Center: Speed slider
        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        JLabel speedLabel = new JLabel("Animation Speed:");
        speedSlider = new JSlider(JSlider.HORIZONTAL, 100, 2000, 800);
        speedSlider.setMajorTickSpacing(500);
        speedSlider.setMinorTickSpacing(100);
        speedSlider.setPaintTicks(true);
        speedSlider.setPaintLabels(true);
        speedSlider.setPreferredSize(new Dimension(200, 40));
        speedSlider.addChangeListener(e -> sortDelay = speedSlider.getValue());
        centerPanel.add(speedLabel);
        centerPanel.add(speedSlider);

        // Right: Buttons
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        sortButton = new JButton("🔄 START SORT");
        sortButton.setFont(new Font("Arial", Font.BOLD, 12));
        sortButton.setBackground(new Color(76, 175, 80));
        sortButton.setForeground(Color.WHITE);
        sortButton.addActionListener(this::handleSort);
        rightPanel.add(sortButton);

        resetButton = new JButton("↻ RESET");
        resetButton.setFont(new Font("Arial", Font.BOLD, 12));
        resetButton.setBackground(new Color(33, 150, 243));
        resetButton.setForeground(Color.WHITE);
        resetButton.addActionListener(this::handleReset);
        rightPanel.add(resetButton);

        panel.add(leftPanel, BorderLayout.WEST);
        panel.add(centerPanel, BorderLayout.CENTER);
        panel.add(rightPanel, BorderLayout.EAST);

        return panel;
    }

    /**
     * Creates the table panel displaying stocks with custom rendering.
     */
    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Stock List - Watch the sorting visualization"));

        // Create table
        tableModel = new DefaultTableModel(COLUMN_NAMES, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        stockTable = new JTable(tableModel);
        stockTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        stockTable.setRowHeight(35);
        stockTable.setFont(new Font("Monospaced", Font.PLAIN, 11));
        stockTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        stockTable.getTableHeader().setBackground(new Color(200, 200, 200));
        
        // Custom cell renderer for highlighting
        stockTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                // Reset background color
                c.setBackground(Color.WHITE);
                c.setForeground(Color.BLACK);
                setFont(new Font("Monospaced", Font.PLAIN, 11));
                
                // Highlight swapped indices (RED - currently being swapped)
                if ((row == swappingIndex1 || row == swappingIndex2) && swappingIndex1 != -1) {
                    c.setBackground(new Color(255, 180, 180));
                    if (column == compareColumnIndex) {
                        c.setBackground(new Color(220, 80, 80));
                    }
                    c.setForeground(Color.BLACK);
                    setFont(new Font("Monospaced", Font.BOLD, 12));
                }
                // Highlight compared indices (YELLOW row and blue compare cell)
                else if ((row == comparingIndex1 || row == comparingIndex2) && comparingIndex1 != -1) {
                    c.setBackground(new Color(255, 255, 160));
                    if (column == compareColumnIndex) {
                        c.setBackground(new Color(156, 204, 255));
                        c.setForeground(Color.BLACK);
                    } else {
                        c.setForeground(Color.BLACK);
                    }
                    setFont(new Font("Monospaced", Font.BOLD, 12));
                }

                return c;
            }
        });

        // Add scroll pane
        JScrollPane scrollPane = new JScrollPane(stockTable);
        scrollPane.setPreferredSize(new Dimension(1000, 350));
        panel.add(scrollPane, BorderLayout.CENTER);

        // Populate table
        updateTable();

        return panel;
    }

    /**
     * Creates the bottom panel with statistics and status.
     */
    private JPanel createBottomPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Status & Statistics"));

        // Left: Statistics
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 5));
        statisticsLabel = new JLabel("Swaps: 0 | Comparisons: 0");
        statisticsLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        leftPanel.add(statisticsLabel);
        
        comparisonLabel = new JLabel("Current: Idle");
        comparisonLabel.setFont(new Font("Arial", Font.BOLD, 11));
        comparisonLabel.setForeground(new Color(255, 152, 0));
        leftPanel.add(comparisonLabel);

        // Center: Progress bar
        sortingProgress = new JProgressBar();
        sortingProgress.setIndeterminate(true);
        sortingProgress.setVisible(false);
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(sortingProgress, BorderLayout.CENTER);

        // Right: Status
        statusLabel = new JLabel("Status: Idle");
        statusLabel.setFont(new Font("Arial", Font.BOLD, 11));
        statusLabel.setForeground(new Color(76, 175, 80));

        JPanel legendPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 5));
        JLabel blueLabel = new JLabel("■ Comparing (column)", JLabel.LEFT);
        blueLabel.setForeground(new Color(33, 150, 243));
        blueLabel.setFont(new Font("Arial", Font.BOLD, 10));
        
        JLabel redLabel = new JLabel("■ Swapping", JLabel.LEFT);
        redLabel.setForeground(new Color(220, 60, 60));
        redLabel.setFont(new Font("Arial", Font.BOLD, 10));
        
        legendPanel.add(blueLabel);
        legendPanel.add(redLabel);
        legendPanel.add(statusLabel);

        panel.add(leftPanel, BorderLayout.WEST);
        panel.add(centerPanel, BorderLayout.CENTER);
        panel.add(legendPanel, BorderLayout.EAST);

        return panel;
    }

    /**
     * Updates the table display with current stock data.
     * Clears highlighting when not sorting.
     */
    private void updateTable() {
        tableModel.setRowCount(0);
        for (Stock stock : stocks) {
            Object[] row = {
                    stock.getSymbol(),
                    stock.getName(),
                    String.format("₱%.2f", stock.getPrice()),
                    stock.getQuantity(),
                    String.format("%+.2f%%", stock.getChange())
            };
            tableModel.addRow(row);
        }
    }

    private void clearHighlights() {
        comparingIndex1 = -1;
        comparingIndex2 = -1;
        swappingIndex1 = -1;
        swappingIndex2 = -1;
        compareColumnIndex = -1;
        compareFieldName = "";
    }

    /**
     * Handles sort button click - initiates sorting in a separate thread.
     */
    private void handleSort(ActionEvent e) {
        if (isSorting) {
            JOptionPane.showMessageDialog(this, "Sorting already in progress!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (stocks.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No stocks to sort!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        compareColumnIndex = getSelectedColumnIndex();
        compareFieldName = getSelectedColumnName();
        isSorting = true;
        sortButton.setEnabled(false);
        resetButton.setEnabled(false);
        sortCriteriaCombo.setEnabled(false);
        speedSlider.setEnabled(false);
        sortingProgress.setVisible(true);
        statusLabel.setText("Status: Sorting...");
        statusLabel.setForeground(new Color(255, 152, 0));

        // Run sorting in separate thread to prevent GUI freezing
        Thread sortThread = new Thread(this::performSort);
        sortThread.start();
    }

    /**
     * Performs the actual sorting with visual updates.
     * Shows comparisons and swaps with color highlighting.
     */
    private void performSort() {
        try {
            Comparator<Stock> comparator = getSelectedComparator();
            Sorter sorter = new BubbleSort(stocks, comparator);

            // Set observer to update GUI on comparisons and swaps
            sorter.setSortObserver(new Sorter.SortObserver() {
                @Override
                public void onComparison(int index1, int index2, List<Stock> data) {
                        SwingUtilities.invokeLater(() -> {
                        comparingIndex1 = index1;
                        comparingIndex2 = index2;
                        comparisonLabel.setText(
                            String.format("Comparing by %s: Index %d (%s) ↔ Index %d (%s)",
                                compareFieldName,
                                index1, data.get(index1).getSymbol(),
                                index2, data.get(index2).getSymbol())
                        );
                        comparisonLabel.setForeground(new Color(33, 150, 243));
                        stockTable.repaint();
                    });

                    try {
                        Thread.sleep(sortDelay);
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }
                }

                @Override
                public void onSwap(int index1, int index2, Stock stock1, Stock stock2) {
                    SwingUtilities.invokeLater(() -> {
                        swappingIndex1 = index1;
                        swappingIndex2 = index2;
                        swapRow1 = index1;
                        swapRow2 = index2;
                        swapStock1 = stock1;
                        swapStock2 = stock2;
                        swapAnimationProgress = 0f;
                        isSwapAnimating = true;
                        overlayPanel.setVisible(true);
                        comparisonLabel.setText(
                            String.format("SWAPPING: Index %d ↔ Index %d",
                                index1, index2)
                        );
                        comparisonLabel.setForeground(new Color(255, 100, 100));
                        updateTable();
                        stockTable.repaint();

                        if (swapAnimationTimer != null && swapAnimationTimer.isRunning()) {
                            swapAnimationTimer.stop();
                        }

                        int frames = 20;
                        int timerDelay = Math.max(15, (int) (sortDelay / frames));
                        swapAnimationTimer = new javax.swing.Timer(timerDelay, event -> {
                            swapAnimationProgress += 1f / frames;
                            if (swapAnimationProgress >= 1f) {
                                swapAnimationProgress = 1f;
                                swapAnimationTimer.stop();
                                isSwapAnimating = false;
                                overlayPanel.setVisible(false);
                                stockTable.repaint();
                            }
                            overlayPanel.repaint();
                        });
                        swapAnimationTimer.start();
                    });

                    try {
                        Thread.sleep(sortDelay);
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }

                    SwingUtilities.invokeLater(() -> {
                        swappingIndex1 = -1;
                        swappingIndex2 = -1;
                        swapRow1 = -1;
                        swapRow2 = -1;
                        swapStock1 = null;
                        swapStock2 = null;
                        comparisonLabel.setForeground(new Color(255, 152, 0));
                        stockTable.repaint();
                    });
                }

                @Override
                public void onSortComplete(int swapCount, int comparisonCount) {
                    SwingUtilities.invokeLater(() -> {
                        clearHighlights();
                        statisticsLabel.setText(
                            String.format("Final Results - Swaps: %d | Comparisons: %d", swapCount, comparisonCount)
                        );
                        comparisonLabel.setText("Sorting Complete! ✓");
                        comparisonLabel.setForeground(new Color(76, 175, 80));
                        stockTable.repaint();
                        resetAfterSort();
                    });
                }
            });

            sorter.sort();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error during sorting: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Resets GUI state after sorting completes.
     */
    private void resetAfterSort() {
        isSorting = false;
        sortButton.setEnabled(true);
        resetButton.setEnabled(true);
        sortCriteriaCombo.setEnabled(true);
        speedSlider.setEnabled(true);
        sortingProgress.setVisible(false);
        statusLabel.setText("Status: Complete ✓");
        statusLabel.setForeground(new Color(76, 175, 80));
    }

    /**
     * Handles reset button click - reloads sample data.
     */
    private void handleReset(ActionEvent e) {
        if (isSorting) {
            JOptionPane.showMessageDialog(this, "Cannot reset while sorting!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        stocks.clear();
        loadSampleData();
        updateTable();
        clearHighlights();
        statisticsLabel.setText("Swaps: 0 | Comparisons: 0");
        comparisonLabel.setText("Current: Idle");
        comparisonLabel.setForeground(new Color(255, 152, 0));
        statusLabel.setText("Status: Reset ✓");
        stockTable.repaint();
    }

    /**
     * Gets the selected comparator based on combo box selection.
     */
    private Comparator<Stock> getSelectedComparator() {
        int selectedIndex = sortCriteriaCombo.getSelectedIndex();
        return switch (selectedIndex) {
            case 0 -> new PriceComparator();
            case 1 -> new QuantityComparator();
            case 2 -> new ChangeComparator();
            default -> new PriceComparator();
        };
    }

    private int getSelectedColumnIndex() {
        return switch (sortCriteriaCombo.getSelectedIndex()) {
            case 0 -> 2; // Price
            case 1 -> 3; // Quantity
            case 2 -> 4; // Change %
            default -> -1;
        };
    }

    private String getSelectedColumnName() {
        return switch (sortCriteriaCombo.getSelectedIndex()) {
            case 0 -> "Price";
            case 1 -> "Quantity";
            case 2 -> "Change %";
            default -> "Value";
        };
    }

    /**
     * Loads sample stock data into the list.
     * Includes various stock prices, quantities, and changes for demonstration.
     */
    private void loadSampleData() {
        stocks.add(new Stock("AAPL", "Apple Inc.", 150.35, 2500, 2.15));
        stocks.add(new Stock("MSFT", "Microsoft Corp.", 380.22, 1200, -0.95));
        stocks.add(new Stock("GOOGL", "Alphabet Inc.", 140.15, 800, 1.43));
        stocks.add(new Stock("AMZN", "Amazon.com Inc.", 170.50, 600, 3.22));
        stocks.add(new Stock("TSLA", "Tesla Inc.", 250.75, 400, -2.15));
        stocks.add(new Stock("META", "Meta Platforms", 480.30, 500, 5.67));
        stocks.add(new Stock("NFLX", "Netflix Inc.", 420.15, 350, -1.20));
        stocks.add(new Stock("NVDA", "NVIDIA Corp.", 875.20, 290, 8.45));
        stocks.add(new Stock("JPM", "JPMorgan Chase", 180.40, 1500, 0.55));
        stocks.add(new Stock("JNJ", "Johnson & Johnson", 155.90, 1100, -0.30));
    }
}

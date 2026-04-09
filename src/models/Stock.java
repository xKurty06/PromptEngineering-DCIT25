package models;

/**
 * Stock Model representing a stock item in the inventory dashboard.
 * Demonstrates encapsulation with private fields and public getters/setters.
 */
public class Stock implements Comparable<Stock> {
    private String symbol;
    private String name;
    private double price;
    private int quantity;
    private double change;

    /**
     * Constructor to initialize a Stock object.
     * 
     * @param symbol      Stock ticker symbol (e.g., "AAPL")
     * @param name        Company name
     * @param price       Current stock price
     * @param quantity    Available quantity
     * @param change      Price change percentage
     */
    public Stock(String symbol, String name, double price, int quantity, double change) {
        this.symbol = symbol;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.change = change;
    }

    // ==================== Getters and Setters (Encapsulation) ====================

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getChange() {
        return change;
    }

    public void setChange(double change) {
        this.change = change;
    }

    // ==================== Comparable Implementation ====================

    @Override
    public int compareTo(Stock other) {
        return Double.compare(this.price, other.price);
    }

    @Override
    public String toString() {
        return String.format("%-6s | %-20s | Price: ₱%-8.2f | Qty: %-5d | Change: %+.2f%%",
                symbol, name, price, quantity, change);
    }

    /**
     * Creates a copy of this Stock object.
     */
    public Stock copy() {
        return new Stock(this.symbol, this.name, this.price, this.quantity, this.change);
    }
}

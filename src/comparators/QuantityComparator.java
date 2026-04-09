package comparators;

import models.Stock;
import java.util.Comparator;

/**
 * Comparator for sorting stocks by quantity in ascending order.
 * Demonstrates polymorphic behavior through the Comparator interface.
 */
public class QuantityComparator implements Comparator<Stock> {
    @Override
    public int compare(Stock stock1, Stock stock2) {
        return Integer.compare(stock1.getQuantity(), stock2.getQuantity());
    }

    @Override
    public String toString() {
        return "Quantity (Ascending)";
    }
}

package comparators;

import models.Stock;
import java.util.Comparator;

/**
 * Comparator for sorting stocks by price in ascending order.
 * Demonstrates polymorphic behavior through the Comparator interface.
 */
public class PriceComparator implements Comparator<Stock> {
    @Override
    public int compare(Stock stock1, Stock stock2) {
        return Double.compare(stock1.getPrice(), stock2.getPrice());
    }

    @Override
    public String toString() {
        return "Price (Ascending)";
    }
}

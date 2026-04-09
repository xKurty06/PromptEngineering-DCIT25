package comparators;

import models.Stock;
import java.util.Comparator;

/**
 * Comparator for sorting stocks by price change percentage in descending order.
 * Demonstrates polymorphic behavior through the Comparator interface.
 */
public class ChangeComparator implements Comparator<Stock> {
    @Override
    public int compare(Stock stock1, Stock stock2) {
        return Double.compare(stock2.getChange(), stock1.getChange());
    }

    @Override
    public String toString() {
        return "Change % (Descending)";
    }
}

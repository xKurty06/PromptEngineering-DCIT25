package sorting;

import java.util.Comparator;
import java.util.List;
import models.Stock;

/**
 * BubbleSort implementation demonstrating the Bubble Sort algorithm.
 * 
 * Algorithm Overview:
 * - Bubble Sort repeatedly steps through the list
 * - Compares adjacent elements and swaps them if they are in the wrong order
 * - Continues until the list is sorted or no swaps occur in a pass
 * 
 * Time Complexity:
 *   Best Case: O(n) - when list is already sorted with early termination
 *   Average Case: O(n²) - typical random arrangement
 *   Worst Case: O(n²) - when list is sorted in reverse order
 * 
 * Space Complexity: O(1) - sorts in-place
 * 
 * Stability: STABLE - maintains relative order of equal elements
 * 
 * Extends: Sorter (abstract class demonstrating inheritance)
 */
public class BubbleSort extends Sorter {

    /**
     * Constructor to initialize BubbleSort with data and comparator.
     * 
     * @param data       The list of stocks to sort
     * @param comparator The comparator defining sort order
     */
    public BubbleSort(List<Stock> data, Comparator<Stock> comparator) {
        super(data, comparator);
    }

    /**
     * Performs the Bubble Sort algorithm with visual notifications.
     * 
     * This implementation:
     * 1. Uses a flag to detect if any swaps occur (optimization)
     * 2. Reduces the range each pass since the largest element "bubbles" to the end
     * 3. Notifies observers of each swap for real-time GUI updates
     * 4. Handles edge cases: empty lists, single element, duplicates
     */
    @Override
    public void sort() {
        if (data == null || data.size() <= 1) {
            notifySortComplete();
            return;
        }

        int n = data.size();
        boolean swapped;

        // Outer loop: number of passes
        for (int i = 0; i < n - 1; i++) {
            swapped = false;

            // Inner loop: compare adjacent elements
            // (n - 1 - i) reduces the range as larger elements move to the end
            for (int j = 0; j < n - 1 - i; j++) {
                // Notify observers of comparison
                notifyComparison(j, j + 1);
                
                // Increment comparison counter and compare
                if (compare(data.get(j), data.get(j + 1)) > 0) {
                    Stock stock1 = data.get(j);
                    Stock stock2 = data.get(j + 1);
                    swap(j, j + 1);
                    notifySwap(j, j + 1, stock1, stock2);
                    swapped = true;
                }
            }

            // If no swaps occurred, list is sorted (optimization)
            if (!swapped) {
                break;
            }
        }

        notifySortComplete();
    }

    /**
     * Alternative implementation: Optimized Bubble Sort with early termination.
     * Call this method instead of sort() for faster sorting without visual delay.
     */
    public void sortFast() {
        if (data == null || data.size() <= 1) {
            notifySortComplete();
            return;
        }

        int n = data.size();

        for (int i = 0; i < n - 1; i++) {
            boolean swapped = false;

            for (int j = 0; j < n - 1 - i; j++) {
                if (compare(data.get(j), data.get(j + 1)) > 0) {
                    Stock stock1 = data.get(j);
                    Stock stock2 = data.get(j + 1);
                    swap(j, j + 1);
                    notifySwap(j, j + 1, stock1, stock2);
                    swapped = true;
                }
            }

            if (!swapped) {
                break;
            }
        }

        notifySortComplete();
    }
}

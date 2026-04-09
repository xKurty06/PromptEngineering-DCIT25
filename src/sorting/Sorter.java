package sorting;

import java.util.Comparator;
import java.util.List;
import models.Stock;

/**
 * Abstract Sorter class providing the framework for sorting implementations.
 * Demonstrates abstraction: defines the contract for sorting without specifying implementation details.
 * Subclasses must implement the actual sorting algorithm.
 */
public abstract class Sorter {
    protected List<Stock> data;
    protected Comparator<Stock> comparator;
    protected SortObserver observer;
    protected int swapCount;
    protected int comparisonCount;

    /**
     * Constructor to initialize the sorter with data and comparator.
     * 
     * @param data       The list of stocks to sort
     * @param comparator The comparator to use for sorting
     */
    public Sorter(List<Stock> data, Comparator<Stock> comparator) {
        this.data = data;
        this.comparator = comparator;
        this.swapCount = 0;
        this.comparisonCount = 0;
    }

    /**
     * Abstract method defining the sorting algorithm contract.
     * Subclasses must implement their specific sorting logic.
     */
    public abstract void sort();

    /**
     * Sets a listener to observe sorting events (for GUI updates).
     * 
     * @param observer The observer to listen to sorting events
     */
    public void setSortObserver(SortObserver observer) {
        this.observer = observer;
    }

    /**
     * Notifies the observer of a comparison event.
     * 
     * @param index1 First index being compared
     * @param index2 Second index being compared
     */
    protected void notifyComparison(int index1, int index2) {
        if (observer != null) {
            observer.onComparison(index1, index2, data);
        }
    }

    /**
     * Notifies the observer of a swap event.
     * 
     * @param index1 First index being swapped
     * @param index2 Second index being swapped
     */
    protected void notifySwap(int index1, int index2, Stock stock1, Stock stock2) {
        if (observer != null) {
            observer.onSwap(index1, index2, stock1, stock2);
        }
        swapCount++;
    }

    /**
     * Notifies the observer that sorting is complete.
     */
    protected void notifySortComplete() {
        if (observer != null) {
            observer.onSortComplete(swapCount, comparisonCount);
        }
    }

    /**
     * Performs a comparison between two elements.
     * 
     * @param stock1 First stock to compare
     * @param stock2 Second stock to compare
     * @return Result of the comparison
     */
    protected int compare(Stock stock1, Stock stock2) {
        comparisonCount++;
        return comparator.compare(stock1, stock2);
    }

    /**
     * Swaps two elements in the data list.
     * 
     * @param i First index
     * @param j Second index
     */
    protected void swap(int i, int j) {
        Stock temp = data.get(i);
        data.set(i, data.get(j));
        data.set(j, temp);
    }

    /**
     * Returns statistics about the sorting process.
     */
    public String getStatistics() {
        return String.format("Swaps: %d | Comparisons: %d", swapCount, comparisonCount);
    }

    /**
     * Interface for observing sorting events (Observer Pattern).
     */
    public interface SortObserver {
        void onComparison(int index1, int index2, List<Stock> data);
        void onSwap(int index1, int index2, Stock stock1, Stock stock2);
        void onSortComplete(int swapCount, int comparisonCount);
    }
}

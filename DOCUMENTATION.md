# Stock Market Dashboard: Bubble Sort Visualizer

## Project Overview

This project demonstrates a **production-grade, object-oriented Java application** that visualizes the Bubble Sort algorithm through a professional Swing GUI. The application simulates a real-time stock market dashboard where users can sort stock data by different criteria (Price, Quantity, Price Change) and observe the sorting process in real-time.

---

## 📂 Project Structure

```
d:\Desktop\Projects\Java\seshh\src\BubbleSort\
├── Main.java                          # Application entry point
├── models/
│   └── Stock.java                     # Stock data model (Encapsulation)
├── comparators/
│   ├── PriceComparator.java          # Sort by price (Polymorphism)
│   ├── QuantityComparator.java       # Sort by quantity (Polymorphism)
│   └── ChangeComparator.java         # Sort by price change (Polymorphism)
├── sorting/
│   ├── Sorter.java                   # Abstract sorter class (Abstraction/Inheritance)
│   └── BubbleSort.java               # Concrete Bubble Sort implementation
└── gui/
    └── StockDashboard.java           # Main GUI class (Swing components)
```

---

## 🎯 How Bubble Sort Works

### Algorithm Definition

Bubble Sort is a simple comparison-based sorting algorithm that repeatedly steps through a list, compares adjacent elements, and swaps them if they are in the wrong order. The process continues until the list is sorted or no swaps occur in a complete pass.

### Step-by-Step Example

```
Initial list:  [5, 2, 8, 1, 9]

PASS 1:
  Compare 5 & 2 → Swap    [2, 5, 8, 1, 9]
  Compare 5 & 8 → No swap [2, 5, 8, 1, 9]
  Compare 8 & 1 → Swap    [2, 5, 1, 8, 9]
  Compare 8 & 9 → No swap [2, 5, 1, 8, 9]
  (Largest element "bubbles" to the end)

PASS 2:
  Compare 2 & 5 → No swap [2, 5, 1, 8, 9]
  Compare 5 & 1 → Swap    [2, 1, 5, 8, 9]
  Compare 5 & 8 → No swap [2, 1, 5, 8, 9]

PASS 3:
  Compare 2 & 1 → Swap    [1, 2, 5, 8, 9]
  Compare 2 & 5 → No swap [1, 2, 5, 8, 9]

PASS 4:
  No swaps occur → List is sorted!

Final list:   [1, 2, 5, 8, 9]
```

### Algorithm Pseudocode

```python
procedure bubbleSort(array):
    n = length(array)
    
    for i from 0 to n-2:
        swapped = false
        
        for j from 0 to n-2-i:
            if array[j] > array[j+1]:
                swap(array[j], array[j+1])
                swapped = true
        
        # Optimization: If no swaps occurred, list is sorted
        if not swapped:
            break
    
    return array
```

### Time & Space Complexity Analysis

| Metric | Complexity | Notes |
|--------|-----------|-------|
| **Best Case** | O(n) | Already sorted list with early termination |
| **Average Case** | O(n²) | Random order with typical comparisons |
| **Worst Case** | O(n²) | Reverse sorted list, maximum swaps |
| **Space Complexity** | O(1) | In-place sorting, no extra storage |
| **Stability** | STABLE | Maintains relative order of equal elements |

### Why Bubble Sort for Learning?

1. **Conceptually Simple**: Easy to understand and implement
2. **Visual Nature**: Swaps are easy to observe and animate
3. **Comparison-Based**: Demonstrates fundamental sorting concepts
4. **Historical Importance**: Foundation for understanding advanced algorithms

---

## 🏗️ Technical Breakdown: OOP Principles

### 1. **Encapsulation** 

Encapsulation hides internal object details and provides controlled access through getters/setters.

**Where It's Used:**
- **Stock.java**: All data fields are `private` with public getter/setter methods
  ```java
  private String symbol;
  private double price;
  private int quantity;
  
  public String getSymbol() { return symbol; }
  public void setSymbol(String symbol) { this.symbol = symbol; }
  // ... more getters/setters
  ```

**Benefits:**
- ✓ Data validation control
- ✓ Internal representation flexibility
- ✓ Protected against unintended modifications
- ✓ Single point of modification for future changes

---

### 2. **Abstraction**

Abstraction defines "what" an object does without specifying "how". It hides complexity behind a simple interface.

**Where It's Used:**
- **Sorter.java**: Abstract class defining the sorting contract
  ```java
  public abstract class Sorter {
      public abstract void sort();  // Subclasses must implement
      
      protected void notifySwap(int i, int j) { /* ... */ }
      protected void swap(int i, int j) { /* ... */ }
      // ... common sorting utilities
  }
  ```

**Benefits:**
- ✓ API consistency across implementations
- ✓ Hiding algorithm complexity
- ✓ Easy to extend with new sorting algorithms
- ✓ Centralized comparison and swap logic
- ✓ Built-in Observer Pattern for GUI updates

**Interface Implementation:**
```java
public interface SortObserver {
    void onSwap(int index1, int index2, List<Stock> data);
    void onSortComplete(int swapCount, int comparisonCount);
}
```

---

### 3. **Inheritance**

Inheritance allows classes to extend the functionality of existing classes, promoting code reuse.

**Where It's Used:**
- **BubbleSort.java**: Extends the `Sorter` abstract class
  ```java
  public class BubbleSort extends Sorter {
      @Override
      public void sort() {
          // Specific Bubble Sort implementation
      }
  }
  ```

**Benefits:**
- ✓ Reuses base sorting utilities from `Sorter`
- ✓ Inherits observer pattern automatically
- ✓ Follows Liskov Substitution Principle (LSP)
- ✓ Easy to add QuickSort, MergeSort, etc. by extending `Sorter`

---

### 4. **Polymorphism**

Polymorphism allows objects to take multiple forms. The same interface invokes different implementations.

**Where It's Used:**

#### a) **Comparator Polymorphism** (Strategy Pattern)
```java
Comparator<Stock> comparator = getSelectedComparator();
// Can be PriceComparator, QuantityComparator, or ChangeComparator

public class PriceComparator implements Comparator<Stock> {
    @Override
    public int compare(Stock s1, Stock s2) {
        return Double.compare(s1.getPrice(), s2.getPrice());
    }
}

public class QuantityComparator implements Comparator<Stock> {
    @Override
    public int compare(Stock s1, Stock s2) {
        return Integer.compare(s1.getQuantity(), s2.getQuantity());
    }
}
```

#### b) **Sorter Polymorphism** (Future Extension)
```java
Sorter sorter = new BubbleSort(stocks, comparator);
// Later: Sorter sorter = new QuickSort(stocks, comparator);
// Later: Sorter sorter = new MergeSort(stocks, comparator);
```

**Benefits:**
- ✓ Extensible sorting criteria without modifying existing code
- ✓ Runtime selection of sorting strategy
- ✓ Open/Closed Principle: Open for extension, closed for modification
- ✓ Easy to add new sorting algorithms or comparison methods

---

## 🖥️ GUI Architecture & Visual Representation

### Key Components

| Component | Purpose | Implementation |
|-----------|---------|-----------------|
| **JTable** | Displays stock data in tabular format | Automatically updated on each swap and comparison |
| **JComboBox** | Sorting criteria selector | Three options: Price, Quantity, Change % |
| **JButton** | Triggers sorting process | Runs in separate thread to prevent freezing |
| **JLabel** | Statistics display | Shows swap and comparison counts and current state |
| **JSlider** | Animation speed control | Adjusts delay between compare/swap steps |
| **JProgressBar** | Visual feedback | Displays indeterminate progress during sorting |

### Visual Sorting Process

```
User clicks "START SORT"
    ↓
GUI thread checks if sorting ongoing
    ↓
Spawn new worker thread for sorting
    ↓
BubbleSort.sort() executes
    ↓
For each comparison:
  - Highlight the two adjacent rows in yellow
  - Highlight the exact comparison field in blue
  - Update the current comparison label
  - Wait for the configured animation delay

For each swap:
  - Highlight the two rows in red
  - Animate the swap with overlapping row movement
  - Update the Stock list order immediately before displaying the animation
  - Call observer.onSwap()
  - Observer invokes SwingUtilities.invokeLater()
  - GUI thread updates JTable and overlay visuals
  - User sees swap animation clearly

    ↓
Sorting complete
  - Update statistics label
  - Reset button states
  - Change status to "Complete"
```

### Thread Safety

The application uses proper threading to ensure GUI responsiveness:

```java
// Sorting runs in worker thread
Thread sortThread = new Thread(this::performSort);
sortThread.start();

// GUI updates dispatch back to EDT (Event Dispatch Thread)
SwingUtilities.invokeLater(() -> {
    updateTable();
    stockTable.repaint();
});
```

---

## 🎓 AI's Reasoning Behind Design Decisions

### 1. **Why Use an Abstract Class Instead of Interface for Sorter?**

**Decision:** Abstract class with concrete methods

**Reasoning:**
- Sorters share common logic: comparison counting, swap notification, statistics
- Abstract class allows both abstract methods (sort) AND concrete implementation (compare, swap, notify)
- Interface would require duplicate code in every sorter implementation
- Encapsulates sorting state (swapCount, comparisonCount) which subclasses shouldn't manage individually

### 2. **Why Observer Pattern for Swap Notifications?**

**Decision:** Implement SortObserver interface in GUI

**Reasoning:**
- **Loose Coupling**: Sorting algorithm doesn't directly reference GUI
- **Single Responsibility**: BubbleSort focuses on sorting, GUI focuses on display
- **Reusability**: Same sorter works with different observers (CLI, Web UI, etc.)
- **Testability**: Sorter can be tested without GUI

### 3. **Why Run Sorting in Separate Thread?**

**Decision:** Use `new Thread(this::performSort)`

**Reasoning:**
- GUI thread (EDT) blocks if sorting takes time
- User can't click buttons, resize window, or interact with UI
- Separate thread allows real-time GUI updates without stuttering
- SwingUtilities.invokeLater() ensures EDT executes updates thread-safely

### 4. **Why Disable Buttons During Sorting?**

**Decision:** Set `sortButton.setEnabled(false)`

**Reasoning:**
- Prevents multiple concurrent sorts (race conditions)
- Prevents data mutation while sorting
- Clear user feedback: "I can't do this right now"
- Better UX than throwing exceptions

### 5. **Why Use Strategy Pattern for Comparators?**

**Decision:** Separate class for each comparison method

**Reasoning:**
- Each comparator is a complete strategy
- Comparators can be unit tested independently
- Easy to add new sorting criteria: create new Comparator class
- Follows Single Responsibility Principle (SRP)
- ComboBox can dynamically load all comparators

### 6. **Why Include 50ms Sleep in Bubble Sort?**

**Original Code:**
```java
try {
    Thread.sleep(50);
} catch (InterruptedException e) {
    Thread.currentThread().interrupt();
}
```

**Reasoning:**
- Makes visualization visible to human eye (sorting would finish too fast otherwise)
- 50ms = 20 swaps per second = understandable visual speed
- Can be removed with `sortFast()` method for benchmarking
- Demonstrates interrupt handling best practice

### 7. **Why Include Sample Data Pre-loaded?**

**Decision:** 10 stocks hardcoded in `loadSampleData()`

**Reasoning:**
- Users don't need to manually enter data to see the demo
- Demonstrates realistic data (real stock names and approximate prices)
- Varied data: different prices, quantities, and changes for sorting visibility
- Reset button quickly returns to initial state

### 8. **Why Use Swing Instead of JavaFX?**

**Decision:** Swing (JFrame, JTable, JButton, etc.)

**Reasoning:**
- Broader compatibility and historical precedent for educational code
- Easier to understand for Java beginners
- Minimal external dependencies
- More examples available online
- (Note: JavaFX would be preferred for new production code)

---

## 🚀 How to Compile and Run

### Compilation

Open PowerShell or Command Prompt in the workspace directory:

```powershell
# Navigate to the BubbleSort directory
cd "d:\Desktop\Projects\Java\seshh\src\BubbleSort"

# Compile all Java files
javac -d "d:\Desktop\Projects\Java\seshh\bin\BubbleSort" *.java models\*.java comparators\*.java sorting\*.java gui\*.java
```

### Execution

```powershell
# Navigate to the bin directory
cd "d:\Desktop\Projects\Java\seshh\bin"

# Run the application
java -cp "BubbleSort" Main
```

### Expected Output

A Swing window should open displaying:
- A table with 10 stocks
- A dropdown to select sort criteria
- START SORT and RESET buttons
- Real-time visual updates when sorting
- Statistics showing swap and comparison counts

---

## 🧪 Testing Scenarios

### Test Case 1: Sort by Price
1. Click Dropdown → Select "Price (Ascending)"
2. Click "START SORT"
3. Watch stocks rearrange by price from lowest to highest
4. Verify statistics: ~45 swaps for 10 items

### Test Case 2: Sort by Quantity
1. Click Dropdown → Select "Quantity (Ascending)"
2. Click "START SORT"
3. Verify stocks sort by quantity
4. Note potentially different swap/comparison counts

### Test Case 3: Already Sorted Data
1. Sort by Price
2. Click "START SORT" again **without** reset
3. Algorithm should detect no swaps needed (early termination)
4. Statistics should show fewer comparisons: ~9 (only first pass)

### Test Case 4: Reset After Sorting
1. Sort by any criteria
2. Click "RESET"
3. Stock list returns to original order
4. Statistics clear

---

## 📊 Complexity Analysis Table

### For n=10 stocks (from sample data):

| Metric | Expected Value | Actual Range |
|--------|---|---|
| **Comparisons (Worst Case)** | n(n-1)/2 = 45 | 45 |
| **Swaps (Worst Case)** | ~n²/2 ≈ 50 | Varies by data |
| **Best Case (Already Sorted)** | n-1 = 9 | 9 |

### Practical Performance:
- **n=100**: ~10,000 operations (~2 seconds visible with 50ms sleep)
- **n=1,000**: ~1,000,000 operations (~slow, noticeable delay)
- **n=10,000**: ~100,000,000 operations (~not recommended, use QuickSort)

---

## 🔧 Extension Points

### Adding a New Sorting Algorithm
1. Create class extending `Sorter`
2. Implement `sort()` method
3. Use `compare()`, `swap()`, and `notifySwap()` from base class

```java
public class QuickSort extends Sorter {
    @Override
    public void sort() {
        quickSortHelper(0, data.size() - 1);
        notifySortComplete();
    }
    
    private void quickSortHelper(int low, int high) {
        // QuickSort implementation
    }
}
```

### Adding a New Comparator
1. Create class implementing `Comparator<Stock>`
2. Add to comboBox in StockDashboard.java

```java
public class SymbolComparator implements Comparator<Stock> {
    @Override
    public int compare(Stock s1, Stock s2) {
        return s1.getSymbol().compareTo(s2.getSymbol());
    }
}
```

---

## 📚 Key Takeaways

1. **OOP Principles in Action**: This project demonstrates all four OOP principles working together harmoniously
2. **Separation of Concerns**: Sorting logic, UI, and data are completely separated
3. **Open/Closed Principle**: Easy to extend with new algorithms without modifying existing code
4. **Thread Safety**: Proper handling of GUI threading prevents freezes
5. **Observer Pattern**: Loose coupling between sorting and visualization
6. **Education Purpose**: Students can modify this to learn sorting algorithms

---

## 📝 License & Attribution

Educational project demonstrating OOP principles and algorithm visualization in Java.

---

**Created for:** Educational Excellence in Software Engineering  
**Specialization:** Bubble Sort Visualization with Real-Time GUI Feedback  
**Target Audience:** CS Students, Java Developers, Algorithm Enthusiasts

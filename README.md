# Stock Market Dashboard - Bubble Sort Visualizer

A **professional-grade, object-oriented Java application** demonstrating the Bubble Sort algorithm through an interactive Swing GUI.

## Quick Start

### Compile
```bash
cd src\BubbleSort
javac -d ..\..\bin\BubbleSort *.java models\*.java comparators\*.java sorting\*.java gui\*.java
```

### Run
```bash
cd bin
java -cp BubbleSort Main
```

## Project Highlights

✅ **Complete OOP Implementation**: Encapsulation, Abstraction, Inheritance, Polymorphism  
✅ **Real-Time Visualization**: Watch Bubble Sort in action with live GUI updates  
✅ **Highlighted Comparisons & Swaps**: Yellow compare rows, blue compare-column highlight, and layered swap animation  
✅ **Animation Speed Control**: Adjustable slider for clearer step-by-step sorting  
✅ **Multiple Sort Criteria**: Price, Quantity, or Price Change  
✅ **Thread-Safe GUI**: Non-blocking sorting with EDT-safe updates  
✅ **Comprehensive Documentation**: 500+ lines explaining every design decision  
✅ **Production-Quality Code**: Organized packages, proper error handling, best practices  

## Architecture

```
models/
  └─ Stock.java              # Data model with encapsulation

comparators/
  ├─ PriceComparator.java    # Sort strategy
  ├─ QuantityComparator.java
  └─ ChangeComparator.java

sorting/
  ├─ Sorter.java             # Abstract base class
  └─ BubbleSort.java         # Concrete implementation

gui/
  └─ StockDashboard.java     # Swing GUI with real-time updates

Main.java                     # Entry point
```

## Key Features

- 🔄 **Visual Sorting**: Real-time table updates during each swap
- 🟨 **Comparison Rows**: Adjacent compare rows are highlighted in yellow
- 🔵 **Compare Column**: The exact field being compared is highlighted in blue
- 🔴 **Swap Animation**: Overlapping row animation makes each swap clearly visible
- ⏱️ **Adjustable Speed**: Slider controls animation speed for step-by-step learning
- 📊 **Multiple Criteria**: Dynamically switch between sort methods
- 📈 **Statistics**: Track swaps and comparisons count
- ⚡ **Thread-Safe**: Non-blocking GUI with worker threads
- 🎯 **OOP Design**: Polymorphism through Strategy pattern
- ♻️ **Reusable**: Easily extensible for new algorithms

## Technical Specifications

- **Language**: Java SE
- **GUI Framework**: Swing
- **Pattern**: Observer + Strategy + Abstract Factory
- **Thread Model**: Event Dispatch Thread + Worker Threads
- **Time Complexity**: O(n²) worst/average case, O(n) best case
- **Space Complexity**: O(1)

## Documentation

See [DOCUMENTATION.md](DOCUMENTATION.md) for:
- Complete algorithm explanation
- OOP principles breakdown
- Design decision rationale
- Complexity analysis
- Extension guidelines

---

**Status**: ✅ Production Ready | **Educational Purpose**: Algorithm Visualization

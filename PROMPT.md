# Project Prompt

Role: You are a Senior Java Developer specialized in Educational Software.

Objective: Create a complex, object-oriented Java program that demonstrates the Bubble Sort algorithm using a Swing GUI.

Scenario: A Real-Time Stock Market Dashboard or Retail Inventory Manager. The GUI must display a list of items (objects) and include a Sort button that triggers the Bubble Sort algorithm.

## Technical Requirements

### Visual Representation

- The GUI must visually reflect the sorting process.
- Show live table updates as comparisons and swaps occur.
- Use row highlighting and animation so the audience can clearly see Bubble Sort in action.

### Strict OOP

- Encapsulation: Data models use private fields with getters/setters.
- Abstraction/Inheritance: Use an abstract `Sorter` class to define sort behavior.
- Polymorphism: Allow users to choose sorting criteria such as Price, Quantity, or Change % through the GUI.

### Data Structure

- Use a logical collection such as `List<Stock>` or `List<Product>`.

### Complexity

- Fully functional.
- Handle edge cases like empty lists, duplicate values, and sorting already-sorted data.

## Deliverables

1. Complete Java code organized into logical packages:
   - `models`
   - `comparators`
   - `sorting`
   - `gui`

2. Markdown documentation that includes:
   - A **How it Works** section for Bubble Sort logic.
   - A **Technical Breakdown** of OOP concepts: Encapsulation, Abstraction, Inheritance, Polymorphism.
   - An **AI's Reasoning** section explaining why specific code structures were chosen.

## Added Features in This Version

- Real-time visual sorting through a `JTable`.
- Comparison highlighting with whole compared rows in yellow.
- Exact comparison field highlighted in blue.
- Swap animation overlay with overlapping row movement.
- Immediate array update before swap animation to keep data accurate.
- Sorting criteria selector for Price, Quantity, and Change %.
- Display prices in Philippine peso format (`₱`).
- Thread-safe GUI updates using Swing `invokeLater`.
- Observer-style notification callbacks from `Sorter` to GUI.
- Updated markdown docs reflect the new animation and visualization behavior.

## Notes

- This prompt is intended to be used as the authoritative project brief for the current implementation.
- The new documentation file is separate from `README.md` and `DOCUMENTATION.md`.

Summary:
In Assignment 2 the ETL logic was implemented in a single procedural `ETLPipeline` class. All parsing, transformation, and writing logic lived in one file and in `main`. For Assignment 3 I reorganized the code into multiple classes with single responsibilities:
- `Product` encapsulates product data.
- `ProductTransformer` performs transformation logic on a `Product`.
- `CSVHandler` handles reading and writing CSV files.
- `ETLPipeline` coordinates extraction, transformation, and loading.

This decomposition separates concerns: parsing and IO are isolated in `CSVHandler`, transformation rules are in `ProductTransformer`, and data is represented by `Product` objects.

How assignment 3 is object-oriented
- **Objects & classes**: The `Product` class models a domain object (a product row), rather than passing raw arrays/strings around.
- **Encapsulation**: Product fields are private with public getters/setters; mutation happens through controlled methods.
- **Single Responsibility**: Each class has one responsibility (CSV IO, transformation, or holding data).
- **Potential for inheritance/polymorphism**: The design supports extension — for example, a `DiscountStrategy` interface or specialized `Product` subclasses could be added if business rules grow. While I did not need inheritance to meet the assignment requirements, the decomposition makes adding polymorphic behavior straightforward.

OO ideas:
- **Object**: `Product` instances represent domain objects.
- **Class**: `Product`, `ProductTransformer`, `CSVHandler`, `ETLPipeline`.
- **Encapsulation**: `Product` fields are private. `CSVHandler` hides file parsing and row validation details.
- **Inheritance & Polymorphism**: Not directly required here, but the transformer can be refactored to a strategy pattern (interface + concrete implementations) in the future. The current design is ready for such extensions.

Testing and Confirmation:
1. **Equivalence tests**: I ran the Assignment 2 implementation and the Assignment 3 implementation using the same sample `data/products.csv` input. I compared the generated `data/transformed_products.csv` files using a text diff to confirm identical output.
2. **Edge cases**:
   - Missing input file: verified the program prints a clear error and exits.
   - Empty input file (header only): verified output contains only the header and summary reports zero transformed rows.
   - Malformed rows (e.g., missing price): verified such rows are skipped and counted as skipped.
3. **Manual checks**: I inspected the transformed file to ensure:
   - Names are UPPERCASE.
   - Electronics prices are discounted by 10% and rounded HALF_UP to two decimals.
   - Electronics over $500 (post-discount) are recategorized to "Premium Electronics".
   - PriceRange values match the final price thresholds.

Lessons learned and next steps:
- Decomposing the solution improves readability and testability.
- Future improvements: add unit tests for `ProductTransformer` and `CSVHandler`, implement a configurable discount/strategy system, and allow pluggable CSV parsing to support quoted fields.


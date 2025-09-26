package org.howard.edu.lsp.assignment3;

import java.io.IOException;
import java.util.List;

/**
 * Main class to run the ETL pipeline for Assignment 3.
 * Coordinates extract, transform, and load steps and prints a summary.
 */
public class ETLPipeline {

    private static final String INPUT_PATH = "data/products.csv";
    private static final String OUTPUT_PATH = "data/transformed_products.csv";

    public static void main(String[] args) {
        CSVHandler csvHandler = new CSVHandler();
        ProductTransformer transformer = new ProductTransformer();

        int rowsRead = 0;
        int rowsTransformed = 0;
        int rowsSkipped = 0;

        try {
            CSVHandler.ReadResult result = csvHandler.readProducts(INPUT_PATH);
            List<Product> products = result.getProducts();
            rowsRead = result.getRowsRead();
            rowsSkipped = result.getRowsSkipped();

            // Transform each product
            for (Product p : products) {
                transformer.transform(p);
            }
            rowsTransformed = products.size();

            // Load (write) the products
            csvHandler.writeProducts(products, OUTPUT_PATH);

            // Print summary
            System.out.println("Summary:");
            System.out.println("Rows read: " + rowsRead);
            System.out.println("Rows transformed: " + rowsTransformed);
            System.out.println("Rows skipped: " + rowsSkipped);
            System.out.println("Output written to: " + OUTPUT_PATH);

        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
            System.err.println("Make sure the input file exists at: " + INPUT_PATH);
        }
    }
}

package org.howard.edu.lsp.assignment3;

import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles reading Product records from a CSV and writing transformed products to a CSV.
 * Assumes no fields contain commas or quotes (simple CSV).
 */
public class CSVHandler {

    private static final String OUTPUT_HEADER = "ProductID,Name,Price,Category,PriceRange";
    private static final String INPUT_HEADER = "ProductID,Name,Price,Category";

    /**
     * Read products from the given input path (relative path expected).
     *
     * @param inputPath path to data/products.csv
     * @return a ReadResult object containing parsed products and stats
     * @throws IOException if reading the file fails (e.g. file missing)
     */
    public ReadResult readProducts(String inputPath) throws IOException {
        File inputFile = new File(inputPath);
        if (!inputFile.exists()) {
            throw new FileNotFoundException("Input file not found: " + inputPath);
        }

        List<Product> products = new ArrayList<>();
        int rowsRead = 0;
        int rowsSkipped = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            String header = reader.readLine();
            if (header == null) {
                // Empty file (no header)
                return new ReadResult(products, rowsRead, rowsSkipped);
            }

            // We accept any header but expect columns in stated order.
            String line;
            while ((line = reader.readLine()) != null) {
                rowsRead++;
                line = line.trim();
                if (line.isEmpty()) {
                    rowsSkipped++;
                    continue;
                }

                String[] parts = line.split(",");
                if (parts.length < 4) {
                    rowsSkipped++;
                    continue;
                }

                try {
                    int id = Integer.parseInt(parts[0].trim());
                    String name = parts[1].trim();
                    BigDecimal price = new BigDecimal(parts[2].trim());
                    String category = parts[3].trim();

                    products.add(new Product(id, name, price, category));
                } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                    rowsSkipped++;
                }
            }
        }

        return new ReadResult(products, rowsRead, rowsSkipped);
    }

    /**
     * Write transformed products to the given output path. Always writes the header.
     *
     * @param products list of transformed products
     * @param outputPath relative output path (e.g., data/transformed_products.csv)
     * @throws IOException on write error
     */
    public void writeProducts(List<Product> products, String outputPath) throws IOException {
        File outFile = new File(outputPath);
        // Ensure parent directories exist
        File parent = outFile.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outFile))) {
            writer.write(OUTPUT_HEADER);
            writer.newLine();
            for (Product p : products) {
                writer.write(p.toCsvRow());
                writer.newLine();
            }
        }
    }

    /**
     * Simple container to return read results and stats.
     */
    public static class ReadResult {
        private final List<Product> products;
        private final int rowsRead;
        private final int rowsSkipped;

        public ReadResult(List<Product> products, int rowsRead, int rowsSkipped) {
            this.products = products;
            this.rowsRead = rowsRead;
            this.rowsSkipped = rowsSkipped;
        }

        public List<Product> getProducts() {
            return products;
        }

        public int getRowsRead() {
            return rowsRead;
        }

        public int getRowsSkipped() {
            return rowsSkipped;
        }
    }
}

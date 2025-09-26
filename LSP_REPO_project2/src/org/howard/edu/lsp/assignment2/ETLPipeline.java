/**
 * Mason Brogden
 */
package org.howard.edu.lsp.assignment2;  

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class ETLPipeline {

    public static void main(String[] args) {
        String inputPath = "data/products.csv";
        String outputPath = "data/transformed_products.csv";

        int rowsRead = 0;
        int rowsTransformed = 0;
        int rowsSkipped = 0;

        File inputFile = new File(inputPath);
        if (!inputFile.exists()) {
            System.err.println("Error: Input file " + inputPath + " does not exist.");
            return;
        }

        try (
            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath))
        ) {
            String header = reader.readLine(); // Read header
            if (header == null) {
                // Empty file, just write header
                writer.write("ProductID,Name,Price,Category,PriceRange");
                writer.newLine();
                System.out.println("Summary:");
                System.out.println("Rows read: 0");
                System.out.println("Rows transformed: 0");
                System.out.println("Rows skipped: 0");
                System.out.println("Output written to: " + outputPath);
                return;
            }

            // Always write the new header
            writer.write("ProductID,Name,Price,Category,PriceRange");
            writer.newLine();

            String line;
            while ((line = reader.readLine()) != null) {
                rowsRead++;

                String[] parts = line.split(",");
                if (parts.length < 4) {
                    rowsSkipped++;
                    continue; // skip invalid row
                }

                try {
                    int productId = Integer.parseInt(parts[0].trim());
                    String name = parts[1].trim().toUpperCase();
                    double price = Double.parseDouble(parts[2].trim());
                    String category = parts[3].trim();

                    // Step 1: Apply discount for Electronics
                    double finalPrice = price;
                    if (category.equalsIgnoreCase("Electronics")) {
                        finalPrice = round(price * 0.9); // 10% discount
                    }

                    // Step 2: Re-categorize Premium Electronics
                    if (category.equalsIgnoreCase("Electronics") && finalPrice > 500.0) {
                        category = "Premium Electronics";
                    }

                    // Step 3: Determine PriceRange
                    String priceRange = getPriceRange(finalPrice);

                    // Write transformed row
                    writer.write(productId + "," + name + "," + String.format("%.2f", finalPrice) + "," +
                            category + "," + priceRange);
                    writer.newLine();
                    rowsTransformed++;

                } catch (NumberFormatException e) {
                    rowsSkipped++;
                }
            }

            // Print summary
            System.out.println("Summary:");
            System.out.println("Rows read: " + rowsRead);
            System.out.println("Rows transformed: " + rowsTransformed);
            System.out.println("Rows skipped: " + rowsSkipped);
            System.out.println("Output written to: " + outputPath);

        } catch (IOException e) {
            System.err.println("Error reading/writing files: " + e.getMessage());
        }
    }

    // Round half up to 2 decimals
    private static double round(double value) {
        BigDecimal bd = new BigDecimal(value).setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    // Determine PriceRange based on final price
    private static String getPriceRange(double price) {
        if (price <= 10.00) {
            return "Low";
        } else if (price <= 100.00) {
            return "Medium";
        } else if (price <= 500.00) {
            return "High";
        } else {
            return "Premium";
        }
    }
}

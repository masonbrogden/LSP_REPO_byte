package org.howard.edu.lsp.assignment3;

import java.math.BigDecimal;

/**
 * Represents a product record from the CSV and holds transformed values.
 * Encapsulates product data and provides getters/setters as needed.
 */
public class Product {

    private int productId;
    private String name;
    private BigDecimal price;
    private String category;

    // Keep originalCategory so transformations that depend on the original value can use it.
    private final String originalCategory;

    private String priceRange;

    /**
     * Construct a Product from parsed values.
     *
     * @param productId the product id
     * @param name the product name
     * @param price the product price as BigDecimal
     * @param category the product category
     */
    public Product(int productId, String name, BigDecimal price, String category) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.category = category;
        this.originalCategory = category;
    }

    /* Getters and setters */

    public int getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    /**
     * Set the product name.
     * @param name new name
     */
    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    /**
     * Set the product price.
     * @param price new price
     */
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    /**
     * Set the product category.
     * @param category new category
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * Returns the original category (unchanged after construction).
     * @return original category string
     */
    public String getOriginalCategory() {
        return originalCategory;
    }

    public String getPriceRange() {
        return priceRange;
    }

    /**
     * Set the price range for this product.
     * @param priceRange one of Low, Medium, High, Premium
     */
    public void setPriceRange(String priceRange) {
        this.priceRange = priceRange;
    }

    /**
     * Return CSV formatted string for output in the required column order:
     * ProductID,Name,Price,Category,PriceRange
     * Price formatted to two decimals.
     *
     * @return CSV row as string
     */
    public String toCsvRow() {
        // Ensure price has exactly two decimal places in string form
        BigDecimal p = price.setScale(2, BigDecimal.ROUND_HALF_UP);
        return String.format("%d,%s,%s,%s,%s", productId, name, p.toPlainString(), category, priceRange);
    }
}


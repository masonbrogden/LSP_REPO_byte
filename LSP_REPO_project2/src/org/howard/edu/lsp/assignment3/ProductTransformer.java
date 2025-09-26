package org.howard.edu.lsp.assignment3;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Responsible for transforming Product instances according to assignment rules:
 * 1) Uppercase the product name
 * 2) Apply 10% discount if original category is "Electronics" (round half up to 2 decimals)
 * 3) If original category was "Electronics" and post-discount price > 500.00, change category to "Premium Electronics"
 * 4) Compute PriceRange based on final price
 */
public class ProductTransformer {

    private static final BigDecimal TEN_PERCENT = new BigDecimal("0.10");

    /**
     * Transform a product in-place following the required transformation order.
     *
     * @param p the product to transform
     */
    public void transform(Product p) {
        if (p == null) {
            return;
        }

        // 1) Uppercase name
        if (p.getName() != null) {
            p.setName(p.getName().toUpperCase());
        }

        // 2) Discount (based on original category)
        BigDecimal price = p.getPrice();
        if (isOriginalElectronics(p)) {
            // apply 10% discount -> price * 0.9
            BigDecimal factor = BigDecimal.ONE.subtract(TEN_PERCENT);
            BigDecimal discounted = price.multiply(factor);
            // Round half up to 2 decimals
            discounted = discounted.setScale(2, RoundingMode.HALF_UP);
            p.setPrice(discounted);
        } else {
            // Even when not discounted, normalize rounding to 2 decimals (HALF_UP)
            p.setPrice(price.setScale(2, RoundingMode.HALF_UP));
        }

        // 3) Recategorize if necessary (use original category to decide)
        if (isOriginalElectronics(p) && p.getPrice().compareTo(new BigDecimal("500.00")) > 0) {
            p.setCategory("Premium Electronics");
        }

        // 4) Compute PriceRange based on final price
        p.setPriceRange(determinePriceRange(p.getPrice()));
    }

    private boolean isOriginalElectronics(Product p) {
        String orig = p.getOriginalCategory();
        return orig != null && orig.equalsIgnoreCase("Electronics");
    }

    private String determinePriceRange(BigDecimal price) {
        // price assumed scaled to 2 decimals already
        if (price.compareTo(new BigDecimal("0.00")) >= 0 && price.compareTo(new BigDecimal("10.00")) <= 0) {
            return "Low";
        } else if (price.compareTo(new BigDecimal("10.01")) >= 0 && price.compareTo(new BigDecimal("100.00")) <= 0) {
            return "Medium";
        } else if (price.compareTo(new BigDecimal("100.01")) >= 0 && price.compareTo(new BigDecimal("500.00")) <= 0) {
            return "High";
        } else { // 500.01 and above
            return "Premium";
        }
    }
}

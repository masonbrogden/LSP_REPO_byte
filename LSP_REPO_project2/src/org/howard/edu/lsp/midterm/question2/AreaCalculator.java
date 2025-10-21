package org.howard.edu.lsp.midterm.question2;

public class AreaCalculator {

    // Circle area
    public static double area(double radius) {
        validatePositive(radius, "radius");
        return Math.PI * radius * radius;
    }

    // Rectangle area
    public static double area(double width, double height) {
        validatePositive(width, "width");
        validatePositive(height, "height");
        return width * height;
    }

    // Triangle (base & height) area
    public static double area(int base, int height) {
        validatePositive(base, "base");
        validatePositive(height, "height");
        return 0.5 * base * height;
    }

    // Square (side length) area
    public static double area(int side) {
        validatePositive(side, "side");
        return (double) side * side;
    }

    private static void validatePositive(double value, String name) {
        if (value <= 0) {
            throw new IllegalArgumentException(name + " must be > 0");
        }
    }
}


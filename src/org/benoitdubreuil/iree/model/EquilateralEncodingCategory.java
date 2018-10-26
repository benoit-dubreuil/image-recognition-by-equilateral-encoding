package org.benoitdubreuil.iree.model;

public enum EquilateralEncodingCategory {
    H,
    S,
    B;

    private static final double MIN_VALUE = 0;
    private static final double MAX_VALUE = 1;
    private static final double RANGE = MAX_VALUE - MIN_VALUE;
    private static final int SIZE = EquilateralEncodingCategory.values().length;
    public static final double[] FILLING_EQUILATERAL_COORDINATES = {0, 0, 1};

    public static double getMinValue() {
        return MIN_VALUE;
    }

    public static double getMaxValue() {
        return MAX_VALUE;
    }

    public static double getRange() {
        return RANGE;
    }

    public static double normalizeDimension(double dimensionData) {
        return (dimensionData - getMinValue()) / getRange() + getMinValue();
    }

    public static int size() {
        return SIZE;
    }
}
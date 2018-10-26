package org.benoitdubreuil.iree.model;

import org.benoitdubreuil.iree.gui.ImageGUIData;
import org.benoitdubreuil.iree.pattern.observer.IObserver;
import org.benoitdubreuil.iree.pattern.observer.Observable;

import java.util.Arrays;

/**
 * The image data for the image recognition by equilateral encoding.
 * <p>
 * See the author Jeff Heaton of this algorithm and his book that contains everything about it :
 * <a href="https://www.heatonresearch.com/book/aifh-vol1-fundamental.html">Artificial Intelligence for Humans, Vol 1: Fundamental Algorithms</a>
 * <p>
 * The base source code for the equilateral encoding algorithm :
 * <a href="https://github.com/jeffheaton/aifh/blob/master/vol1/java-examples/src/main/java/com/heatonresearch/aifh/normalize/Equilateral.java">Equilateral.java</a>
 */
public class EquilateralEncodingTable {

    // The algorithm with only one category does not make sense
    private static final int MIN_CATEGORIES_FOR_ENCODING = 2;

    private int m_categoryCount;
    private int m_dimensionCount;
    private double m_lowestValue;
    private double m_highestValue;
    private double[][] m_categoryDimensionMatrix;

    public EquilateralEncodingTable(int categoryCount, double lowestValue, double highestValue) {
        if (categoryCount < MIN_CATEGORIES_FOR_ENCODING) {
            throw new ArrayIndexOutOfBoundsException("Not enough categories for equilateral encoding");
        }

        this.m_categoryCount = categoryCount;
        this.m_dimensionCount = computeDimensionCount(categoryCount);
        this.m_lowestValue = lowestValue;
        this.m_highestValue = highestValue;
        this.m_categoryDimensionMatrix = computeEquilateralEncodingTable();
    }

    /**
     * Encodes a supplied index and gets the equilaterally encoded coordinates at that index.
     *
     * @param equilateralCoordsIndex The index at which the equilaterally encoded coordinates are.
     *
     * @return The equilaterally encoded coordinates.
     */
    public double[] encode(int equilateralCoordsIndex) {
        return m_categoryDimensionMatrix[equilateralCoordsIndex];
    }

    /**
     * Decodes the supplied coordinates by finding its closest equilaterally encoded coordinates.
     *
     * @param coordinates The coordinates that need to be decoded. It should not be equilateral it doesn't matter, as the goal is simply to find the closest equilaterally encoded
     *                    coordinates.
     *
     * @return The index at which the closest equilaterally encoded coordinates are.
     */
    public int decode(double[] coordinates) {
        double closestDistance = Double.POSITIVE_INFINITY;
        int closestEquilateralCoordsIndex = -1;

        for (int i = 0; i < m_categoryDimensionMatrix.length; ++i) {

            double dist = computeDistance(coordinates, i);

            if (dist < closestDistance) {
                closestDistance = dist;
                closestEquilateralCoordsIndex = i;
            }
        }

        return closestEquilateralCoordsIndex;
    }

    /**
     * Computes the Euclidean distance between the supplied coordinates and the equilaterally encoded coordinates at the supplied index.
     *
     * @param coordinates            Coordinates of the first n-dimensional vector.
     * @param equilateralCoordsIndex Index for the equilaterally encoded coordinates.
     *
     * @return The Euclidean distance between the two vectors.
     */
    public double computeDistance(double[] coordinates, int equilateralCoordsIndex) {
        return computeDistance(coordinates, m_categoryDimensionMatrix[equilateralCoordsIndex]);
    }

    /**
     * Computes the Euclidean distance between the supplied vectors.
     *
     * @param lhsCoordinates Coordinates of the first n-dimensional vector.
     * @param rhsCoordinates Coordinates of the second n-dimensional vector.
     *
     * @return The Euclidean distance between the two vectors.
     */
    public static double computeDistance(double[] lhsCoordinates, double[] rhsCoordinates) {
        double result = 0;

        for (int i = 0; i < rhsCoordinates.length; ++i) {
            result += Math.pow(lhsCoordinates[i] - rhsCoordinates[i], 2);
        }

        return Math.sqrt(result);
    }

    /**
     * Computes the equilateral encoding table, which is used as a look up for table for the data.
     *
     * @return The equilateral encoding table.
     */
    private double[][] computeEquilateralEncodingTable() {
        double negativeReciprocalOfN;
        double scalingFactor;

        final double[][] matrix = new double[m_categoryCount][m_dimensionCount];

        matrix[0][0] = -1;
        matrix[1][0] = 1.0;

        if (m_categoryCount > 2) {
            for (int dimension = 2; dimension < m_categoryCount; ++dimension) {
                // scale the matrix so far
                negativeReciprocalOfN = dimension;
                scalingFactor = Math.sqrt(negativeReciprocalOfN * negativeReciprocalOfN - 1.0) / negativeReciprocalOfN;

                for (int coordinate = 0; coordinate < dimension; ++coordinate) {
                    for (int oldDimension = 0; oldDimension < dimension - 1; ++oldDimension) {
                        matrix[coordinate][oldDimension] *= scalingFactor;
                    }
                }

                negativeReciprocalOfN = -1.0 / negativeReciprocalOfN;

                for (int coordinate = 0; coordinate < dimension; ++coordinate) {
                    matrix[coordinate][dimension - 1] = negativeReciprocalOfN;
                }

                for (int coordinate = 0; coordinate < dimension - 1; ++coordinate) {
                    matrix[dimension][coordinate] = 0.0;
                }

                matrix[dimension][dimension - 1] = 1.0;

                // Scale the matrix
                for (int row = 0; row < matrix.length; ++row) {
                    for (int col = 0; col < matrix[0].length; ++col) {

                        double min = -1;
                        double max = 1;

                        matrix[row][col] = ((matrix[row][col] - min) / (max - min)) * (m_highestValue - m_lowestValue) + m_lowestValue;
                    }
                }
            }
        }

        return matrix;
    }

    public static int computeDimensionCount(int categoryCount) {
        return categoryCount - 1;
    }

    public int getCategoryCount() {
        return m_categoryCount;
    }

    public int getDimensionCount() {
        return m_dimensionCount;
    }

    public double getLowestValue() {
        return m_lowestValue;
    }

    public double getHighestValue() {
        return m_highestValue;
    }

    public double[][] getCategoryDimensionMatrix() {
        return m_categoryDimensionMatrix;
    }
}

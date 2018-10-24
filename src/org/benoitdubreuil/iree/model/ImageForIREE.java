package org.benoitdubreuil.iree.model;

import org.benoitdubreuil.iree.gui.ImageForIREEGUI;
import org.benoitdubreuil.iree.pattern.observer.IObserver;
import org.benoitdubreuil.iree.pattern.observer.Observable;

/**
 * The image data for the image recognition by equilateral encoding.
 *
 * See the author Jeff Heaton of this algorithm and his book that contains everything about it :
 * <a href="https://www.heatonresearch.com/book/aifh-vol1-fundamental.html">Artificial Intelligence for Humans, Vol 1: Fundamental Algorithms</a>
 */
public class ImageForIREE extends Observable<ImageForIREE> implements IObserver<ImageForIREEGUI> {

    public static final int MAXIMUM_WIDTH = 32;
    public static final int MAXIMUM_HEIGHT = 32;
    public static final float MAXIMUM_DIMENSIONS_ASPECT_RATIO = MAXIMUM_WIDTH / (float) MAXIMUM_HEIGHT;

    // Could technically compute the equilateral encoding for all dimensions but the algorithm comes will the two first dimensions already encoded.
    private static final int MIN_PIXELS_FOR_ENCODING = 3;

    private double[][] m_equilateralEncodingMatrix;

    @Override
    public void observableChanged(ImageForIREEGUI newValue) throws ArrayIndexOutOfBoundsException {
        if (newValue.getDownScaledGrayscale() != null) {

            int pixelCount = newValue.getDownScaledGrayscale().getWidth() * newValue.getDownScaledGrayscale().getHeight();

            if (pixelCount < MIN_PIXELS_FOR_ENCODING) {
                throw new ArrayIndexOutOfBoundsException("Not enough pixels for equilateral encoding");
            }

            m_equilateralEncodingMatrix = computeEquilateralEncoding(pixelCount);
            modelChanged(this);
        }
    }

    /**
     * Encodes a supplied index and gets the equilaterally encoded coordinates at that index.
     *
     * @param equilateralCoordsIndex The index at which the equilaterally encoded coordinates are.
     *
     * @return The equilaterally encoded coordinates.
     */
    public double[] encode(int equilateralCoordsIndex) {
        return m_equilateralEncodingMatrix[equilateralCoordsIndex];
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

        for (int i = 0; i < m_equilateralEncodingMatrix.length; ++i) {

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
     * @return The Euclidean distance between the two coordinates.
     */
    public double computeDistance(double[] coordinates, int equilateralCoordsIndex) {
        double result = 0;

        for (int i = 0; i < coordinates.length; ++i) {
            result += Math.pow(coordinates[i] - m_equilateralEncodingMatrix[equilateralCoordsIndex][i], 2);
        }

        return Math.sqrt(result);
    }

    /**
     * Called internally to generate the matrix.
     *
     * @param pixelCount    The number of sets to generate for.
     *
     * @return One row for each set, the columns are the activations for that set.
     */
    private static double[][] computeEquilateralEncoding(int pixelCount) {
        double negativeReciprocalOfN;
        double scalingFactor;
        final double[][] matrix = new double[pixelCount][pixelCount - 1];

        matrix[0][0] = -1;
        matrix[1][0] = 1.0;

        for (int dimensionIndex = 2; dimensionIndex < pixelCount; ++dimensionIndex) {
            // scale the matrix so far
            negativeReciprocalOfN = dimensionIndex;
            scalingFactor = Math.sqrt(negativeReciprocalOfN * negativeReciprocalOfN - 1.0) / negativeReciprocalOfN;
            
            for (int coordinatesIndex = 0; coordinatesIndex < dimensionIndex; ++coordinatesIndex) {
                for (int oldDimensionIndex = 0; oldDimensionIndex < dimensionIndex - 1; ++oldDimensionIndex) {
                    matrix[coordinatesIndex][oldDimensionIndex] *= scalingFactor;
                }
            }

            negativeReciprocalOfN = -1.0 / negativeReciprocalOfN;
            
            for (int coordinatesIndex = 0; coordinatesIndex < dimensionIndex; ++coordinatesIndex) {
                matrix[coordinatesIndex][dimensionIndex - 1] = negativeReciprocalOfN;
            }

            for (int coordinatesIndex = 0; coordinatesIndex < dimensionIndex - 1; ++coordinatesIndex) {
                matrix[dimensionIndex][coordinatesIndex] = 0.0;
            }

            matrix[dimensionIndex][dimensionIndex - 1] = 1.0;
        }

        return matrix;
    }
}

package org.benoitdubreuil.iree.utils;

public final class MathUtils {

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
     * Normalizes the supplied vector.
     *
     * @param vector The vector to normalize.
     *
     * @return The same array, but normalized.
     */
    public static double[] normalizeVector(double[] vector) {

        double squaredLength = 0;

        for (int dimension = 0; dimension < vector.length; ++dimension) {
            squaredLength += vector[dimension] * vector[dimension];
        }

        if (squaredLength != 1.0 && squaredLength != 0.0) {
            double reciprocalLength = 1.0 / Math.sqrt(squaredLength);

            for (int dimension = 0; dimension < vector.length; ++dimension) {
                vector[dimension] *= reciprocalLength;
            }
        }

        return vector;
    }

    /**
     * Negates the vector.
     *
     * @param vector The vector to negate.
     *
     * @return The same array, but each coordinate negated.
     */
    public static double[] negateVector(double[] vector) {

        for (int dimension = 0; dimension < vector.length; ++dimension) {
            vector[dimension] = -vector[dimension];
        }

        return vector;
    }

    /**
     * Multiplies the vector by a scalar.
     *
     * @param vector The vector to multiply.
     * @param scalar      The scalar by which to multiply the vector.
     *
     * @return The same array, but each coordinate multiplied by the scalar.
     */
    public static double[] multVector(double[] vector, double scalar) {

        for (int dimension = 0; dimension < vector.length; ++dimension) {
            vector[dimension] *= scalar;
        }

        return vector;
    }

    /**
     * Computes the dot product of the two supplied points.
     *
     * @param lhsVector The first point.
     * @param rhsVector The second point.
     *
     * @return The dot product of the two points.
     */
    public static double dotProductVector(double[] lhsVector, double[] rhsVector) {
        double dotResult = 0;

        for (int dimension = 0; dimension < lhsVector.length; ++dimension) {
            dotResult += lhsVector[dimension] * rhsVector[dimension];
        }

        return dotResult;
    }

    private MathUtils() {
    }
}

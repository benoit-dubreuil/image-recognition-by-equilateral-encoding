package org.benoitdubreuil.iree.model;

import org.benoitdubreuil.iree.controller.ControllerIREE;
import org.benoitdubreuil.iree.utils.MathUtils;

public final class ImageDataRecognition {

    /**
     * Compares two images and return how similar they are.
     *
     * @param lhs The first image to compare.
     * @param rhs The second image to compare.
     *
     * @return Inclusively from 0, totally different, to 1, the same.
     */
    public static double compareImages(ImageData lhs, ImageData rhs) {

        double meanDistance = 0;
        double[][] lhsEncodedPixelData = lhs.getEncodedPixelData();
        double[][] rhsEncodedPixelData = rhs.getEncodedPixelData();

        if (lhsEncodedPixelData != null && rhsEncodedPixelData != null) {

            EquilateralEncodingTable table = ControllerIREE.getInstance().getEncodingTable();
            int maxPixelCount = Math.max(lhs.getPixelCount(), rhs.getPixelCount());
            double emptyPixelMeanValue = 1.0;

            for (int pixel = 0; pixel < maxPixelCount; ++pixel) {

                double[] lhsVector;
                double[] rhsVector;

                if (pixel < lhs.getPixelCount()) {
                    lhsVector = lhsEncodedPixelData[pixel];
                }
                else {
                    meanDistance += emptyPixelMeanValue;
                    continue;
                }

                if (pixel < rhs.getPixelCount()) {
                    rhsVector = rhsEncodedPixelData[pixel];
                }
                else {
                    meanDistance += emptyPixelMeanValue;
                    continue;
                }

                double rhsDotLhs = MathUtils.dotProductVector(rhsVector, lhsVector);
                double[] rhsProjectedOntoLhs = MathUtils.multVector(lhsVector.clone(), rhsDotLhs);

                meanDistance += MathUtils.computeDistance(lhsVector, rhsProjectedOntoLhs) / table.getValueRange() / maxPixelCount;
            }
        }

        return meanDistance;
    }

    private ImageDataRecognition() {
    }
}

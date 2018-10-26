package org.benoitdubreuil.iree.model;

import org.benoitdubreuil.iree.controller.ControllerIREE;

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

            for (int pixel = 0; pixel < maxPixelCount; ++pixel) {

                double[] lhsCoordinates;
                double[] rhsCoordinates;

                if (pixel < lhs.getPixelCount()) {
                    lhsCoordinates = lhsEncodedPixelData[pixel];
                }
                else {
                    lhsCoordinates = EquilateralEncodingCategory.FILLING_EQUILATERAL_COORDINATES;
                }

                if (pixel < rhs.getPixelCount()) {
                    rhsCoordinates = rhsEncodedPixelData[pixel];
                }
                else {
                    rhsCoordinates = EquilateralEncodingCategory.FILLING_EQUILATERAL_COORDINATES;
                }

                meanDistance += EquilateralEncodingTable.computeDistance(lhsCoordinates, rhsCoordinates) / maxPixelCount;
            }
        }

        return meanDistance;
    }

    private ImageDataRecognition() {
    }
}

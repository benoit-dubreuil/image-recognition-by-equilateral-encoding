package org.benoitdubreuil.iree.model;

import org.benoitdubreuil.iree.controller.ControllerIREE;
import org.benoitdubreuil.iree.gui.ImageGUIData;
import org.benoitdubreuil.iree.pattern.observer.IObserver;
import org.benoitdubreuil.iree.pattern.observer.Observable;

import java.awt.image.BufferedImage;

public class ImageData extends Observable<ImageData> implements IObserver<ImageGUIData> {

    private double[][] m_encodedPixelData;

    /**
     * Encodes the pixel data of the supplied image data.
     *
     * @param newValue The new value of the observed.
     */
    @Override
    public void observableChanged(ImageGUIData newValue) {
        if (newValue.getDownScaled() != null) {
            encodePixelData(newValue);
            modelChanged(this);
        }
    }

    private void encodePixelData(ImageGUIData imageGUIData) {
        double[][] categoryDimensionMatrix = ControllerIREE.getInstance().getEncodingTable().getCategoryDimensionMatrix();
        double[] normalizedPixel = new double[EquilateralEncodingCategory.size()];
        BufferedImage image = imageGUIData.getDownScaled();
        int width = image.getWidth();
        int height = image.getHeight();

        int dimensionCount = EquilateralEncodingTable.computeDimensionCount(EquilateralEncodingCategory.size());

        m_encodedPixelData = new double[width * height][dimensionCount];

        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {

                double[] coordinates = m_encodedPixelData[x * height + y];
                int orignalPixel = image.getRGB(x, height - 1 - y);

                normalizedPixel[EquilateralEncodingCategory.R.ordinal()] = EquilateralEncodingCategory.normalizeDimension((orignalPixel >> 16) & 0xff);
                normalizedPixel[EquilateralEncodingCategory.G.ordinal()] = EquilateralEncodingCategory.normalizeDimension((orignalPixel >> 8) & 0xff);
                normalizedPixel[EquilateralEncodingCategory.B.ordinal()] = EquilateralEncodingCategory.normalizeDimension(orignalPixel & 0xff);

                normalizedPixel[EquilateralEncodingCategory.GRAYSCALE.ordinal()] = EquilateralEncodingCategory.normalizeDimension(
                    (normalizedPixel[EquilateralEncodingCategory.R.ordinal()] + normalizedPixel[EquilateralEncodingCategory.G.ordinal()]
                        + normalizedPixel[EquilateralEncodingCategory.B.ordinal()]) / 3.0);

                for (int dimension = 0; dimension < dimensionCount; ++dimension) {
                    double coordinate = 0;

                    for (int category = 0; category < EquilateralEncodingCategory.size(); ++category) {
                        coordinate += normalizedPixel[category] * categoryDimensionMatrix[category][dimension];
                    }

                    coordinates[dimension] = coordinate;
                }
            }
        }
    }

    public int getPixelCount() {
        return m_encodedPixelData.length;
    }

    double[][] getEncodedPixelData() {
        return m_encodedPixelData;
    }
}

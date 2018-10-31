package org.benoitdubreuil.iree.model;

import org.benoitdubreuil.iree.controller.ControllerIREE;
import org.benoitdubreuil.iree.gui.ImageGUIData;
import org.benoitdubreuil.iree.pattern.observer.IObserver;
import org.benoitdubreuil.iree.pattern.observer.Observable;

import java.awt.*;
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
        EquilateralEncodingTable encodingTable = ControllerIREE.getInstance().getEncodingTable();
        EquilateralEncodingCategory[] categories = EquilateralEncodingCategory.values();
        BufferedImage image = imageGUIData.getDownScaled();
        int width = image.getWidth();
        int height = image.getHeight();

        m_encodedPixelData = new double[width * height][];

        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {

                int orignalPixel = image.getRGB(x, height - 1 - y);

                int r = (orignalPixel >> 16) & 0xff;
                int g = (orignalPixel >> 8) & 0xff;
                int b = orignalPixel & 0xff;

                int minColorDistance = 255 * 3;
                int minColorDistanceCategory = 0;
                for (int category = 0; category < encodingTable.getCategoryCount(); ++category) {

                    Color categoryColor = categories[category].getColor();
                    int colorDistance = Math.abs(r - categoryColor.getRed()) + Math.abs(g - categoryColor.getGreen()) + Math.abs(b - categoryColor.getBlue());

                    if (colorDistance < minColorDistance) {
                        minColorDistance = colorDistance;
                        minColorDistanceCategory = category;
                    }
                }

                m_encodedPixelData[x * height + y] = encodingTable.encode(minColorDistanceCategory).clone();
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

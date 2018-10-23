package org.benoitdubreuil.iree.gui;

import org.benoitdubreuil.iree.pattern.Observable;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;

public class ImageForIREEGUI extends Observable<ImageForIREEGUI> {

    private BufferedImage m_original;
    private BufferedImage m_downScaledGrayscale;

    public ImageForIREEGUI() {
    }

    public ImageForIREEGUI(BufferedImage original) {
        setOriginal(original);
    }

    private static BufferedImage removeTransparency(BufferedImage image) {
        BufferedImage copy = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);

        Graphics2D g2d = copy.createGraphics();
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, copy.getWidth(), copy.getHeight());
        g2d.drawImage(image, 0, 0, null);
        g2d.dispose();

        return copy;
    }

    private static void convertToGrayscaleRGB(BufferedImage image) {
        for (int y = 0; y < image.getHeight(); ++y) {
            for (int x = 0; x < image.getWidth(); ++x) {

                int orignalPixel = image.getRGB(x, y);

                int r = (orignalPixel >> 16) & 0xff;
                int g = (orignalPixel >> 8) & 0xff;
                int b = orignalPixel & 0xff;

                int average = (r + g + b) / 3;

                int grayscalePixel = (average << 16) | (average << 8) | average;

                image.setRGB(x, y, grayscalePixel);
            }
        }
    }

    private static BufferedImage cloneBufferedImage(BufferedImage image) {
        ColorModel colorModel = image.getColorModel();
        boolean isAlphaPremultiplied = colorModel.isAlphaPremultiplied();
        WritableRaster raster = image.copyData(null);

        return new BufferedImage(colorModel, raster, isAlphaPremultiplied, null);
    }

    public BufferedImage getOriginal() {
        return m_original;
    }

    public void setOriginal(BufferedImage original) {
        m_original = removeTransparency(original);

        m_downScaledGrayscale = cloneBufferedImage(m_original);
        convertToGrayscaleRGB(m_downScaledGrayscale);

        modelChanged(this);
    }

    public BufferedImage getDownScaledGrayscale() {
        return m_downScaledGrayscale;
    }
}

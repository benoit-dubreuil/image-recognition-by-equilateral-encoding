package org.benoitdubreuil.iree.utils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;

public final class ImageUtils {

    public static Image fitImage(BufferedImage source, int targetWidth, int targetHeight, int scaleAlgorithm) {
        float imageAspectRatio = source.getWidth() / (float) source.getHeight();
        float targetAspectRatio = targetWidth / (float) targetHeight;

        Image resizedImage;

        if (imageAspectRatio >= targetAspectRatio) {
            resizedImage = source.getScaledInstance(targetWidth, (int) (targetWidth * (1f / imageAspectRatio)), scaleAlgorithm);
        }
        else {
            resizedImage = source.getScaledInstance((int) (targetHeight * imageAspectRatio), targetHeight, scaleAlgorithm);
        }

        return resizedImage;
    }

    public static BufferedImage cloneBufferedImage(BufferedImage image) {
        ColorModel colorModel = image.getColorModel();
        boolean isAlphaPremultiplied = colorModel.isAlphaPremultiplied();
        WritableRaster raster = image.copyData(null);

        return new BufferedImage(colorModel, raster, isAlphaPremultiplied, null);
    }

    public static BufferedImage convertToBufferedImage(Image image) {
        BufferedImage newImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = newImage.createGraphics();

        g.drawImage(image, 0, 0, null);

        g.dispose();
        return newImage;
    }

    public static void convertToGrayscaleRGB(BufferedImage image) {
        for (int y = 0; y < image.getHeight(); ++y) {
            for (int x = 0; x < image.getWidth(); ++x) {

                int orignalPixel = image.getRGB(x, y);

                int r = (orignalPixel >> 16) & 0xff;
                int g = (orignalPixel >> 8) & 0xff;
                int b = orignalPixel & 0xff;

                int average = (r + g + b) / 3;

                int grayscalePixel = (0xFF << 24) | (average << 16) | (average << 8) | average;

                image.setRGB(x, y, grayscalePixel);
            }
        }
    }

    public static BufferedImage removeTransparency(BufferedImage image) {
        BufferedImage copy = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);

        Graphics2D g2d = copy.createGraphics();
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, copy.getWidth(), copy.getHeight());
        g2d.drawImage(image, 0, 0, null);
        g2d.dispose();

        return copy;
    }

    private ImageUtils() {
    }
}

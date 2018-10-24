package org.benoitdubreuil.iree.gui;

import org.benoitdubreuil.iree.controller.ControllerIREE;
import org.benoitdubreuil.iree.pattern.observer.Observable;
import org.benoitdubreuil.iree.utils.ImageUtils;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageForIREEGUI extends Observable<ImageForIREEGUI> {

    private BufferedImage m_original;
    private BufferedImage m_downScaledGrayscale;

    public ImageForIREEGUI() {
    }

    public ImageForIREEGUI(BufferedImage original) {
        setOriginal(original);
    }

    public BufferedImage getOriginal() {
        return m_original;
    }

    public void setOriginal(BufferedImage original) {
        m_original = ImageUtils.removeTransparency(original);

        m_downScaledGrayscale = ImageUtils.convertToBufferedImage(ImageUtils.fitImage(original, ControllerIREE.getInstance().getMaximumImgWidth(), ControllerIREE.getInstance().getMaximumImgHeight(),
            Image.SCALE_FAST));
        ImageUtils.convertToGrayscaleRGB(m_downScaledGrayscale);

        modelChanged(this);
    }

    public BufferedImage getDownScaledGrayscale() {
        return m_downScaledGrayscale;
    }
}

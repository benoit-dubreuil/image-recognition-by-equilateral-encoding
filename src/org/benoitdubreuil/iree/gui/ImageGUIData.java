package org.benoitdubreuil.iree.gui;

import org.benoitdubreuil.iree.controller.ControllerIREE;
import org.benoitdubreuil.iree.pattern.observer.Observable;
import org.benoitdubreuil.iree.utils.ImageUtils;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageGUIData extends Observable<ImageGUIData> {

    private BufferedImage m_original;
    private BufferedImage m_downScaled;

    public ImageGUIData() {
    }

    public ImageGUIData(BufferedImage original) {
        setOriginal(original);
    }

    public BufferedImage getOriginal() {
        return m_original;
    }

    public void setOriginal(BufferedImage original) {
        m_original = ImageUtils.removeTransparency(original);

        m_downScaled = ImageUtils.convertToBufferedImage(ImageUtils.fitImage(m_original,
            ControllerIREE.getInstance().getMaximumImgWidth(),
            ControllerIREE.getInstance().getMaximumImgHeight(),
            Image.SCALE_FAST));

        modelChanged(this);
    }

    public BufferedImage getDownScaled() {
        return m_downScaled;
    }
}

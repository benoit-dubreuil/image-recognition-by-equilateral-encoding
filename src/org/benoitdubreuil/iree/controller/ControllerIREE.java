package org.benoitdubreuil.iree.controller;

import org.benoitdubreuil.iree.gui.ImageForIREEGUI;
import org.benoitdubreuil.iree.gui.MainWindow;
import org.benoitdubreuil.iree.model.ImageForIREE;
import org.benoitdubreuil.iree.pattern.observer.IObserver;

import javax.swing.*;

public class ControllerIREE {

    private static ControllerIREE singletonInstance;

    private boolean m_hasStarted;
    private MainWindow m_mainWindow;
    private ImageForIREE m_imageToCompareData;
    private ImageForIREE m_referenceImageData;

    private ControllerIREE() {
    }

    public static ControllerIREE getInstance() {
        if (singletonInstance == null) {
            singletonInstance = new ControllerIREE();
        }

        return singletonInstance;
    }

    public void start() {
        if (!m_hasStarted) {
            m_hasStarted = true;

            m_imageToCompareData = new ImageForIREE();
            m_referenceImageData = new ImageForIREE();

            ImageForIREEGUI imageToCompare = new ImageForIREEGUI();
            ImageForIREEGUI referenceImage = new ImageForIREEGUI();

            imageToCompare.addObserver(m_imageToCompareData);
            referenceImage.addObserver(m_referenceImageData);

            SwingUtilities.invokeLater(() -> {
                m_mainWindow = new MainWindow(imageToCompare, referenceImage);

                m_imageToCompareData.addObserver((IObserver) m_mainWindow);
                m_referenceImageData.addObserver((IObserver) m_mainWindow);
            });
        }
    }

    public int getMaximumImgWidth() {
        return ImageForIREE.MAXIMUM_WIDTH;
    }

    public int getMaximumImgHeight() {
        return ImageForIREE.MAXIMUM_HEIGHT;
    }

    public float getMaximumImgDimensionsAspectRation() {
        return ImageForIREE.MAXIMUM_DIMENSIONS_ASPECT_RATIO;
    }
}

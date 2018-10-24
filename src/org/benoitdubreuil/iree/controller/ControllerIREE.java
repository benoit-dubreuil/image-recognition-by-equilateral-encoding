package org.benoitdubreuil.iree.controller;

import org.benoitdubreuil.iree.gui.MainWindow;
import org.benoitdubreuil.iree.model.ImageForIREE;

import javax.swing.*;

public class ControllerIREE {

    private static ControllerIREE singletonInstance;

    private boolean m_hasStarted;
    private MainWindow m_mainWindow;

    public ControllerIREE() {
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
            SwingUtilities.invokeLater(() -> m_mainWindow = new MainWindow());
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

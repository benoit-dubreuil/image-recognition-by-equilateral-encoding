package org.benoitdubreuil.iree.controller;

import org.benoitdubreuil.iree.gui.ImageGUIData;
import org.benoitdubreuil.iree.gui.MainWindow;
import org.benoitdubreuil.iree.model.EquilateralEncodingCategory;
import org.benoitdubreuil.iree.model.EquilateralEncodingTable;
import org.benoitdubreuil.iree.model.ImageData;
import org.benoitdubreuil.iree.pattern.observer.IObserver;

import javax.swing.*;

public class ControllerIREE {

    private static ControllerIREE singletonInstance;

    private boolean m_hasStarted;
    private MainWindow m_mainWindow;
    private EquilateralEncodingTable m_encodingTable;
    private ImageData m_imageToCompareData;
    private ImageData m_referenceImageData;

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

            m_encodingTable = new EquilateralEncodingTable(EquilateralEncodingCategory.size(), -1, 1);
            m_imageToCompareData = new ImageData();
            m_referenceImageData = new ImageData();

            ImageGUIData imageToCompare = new ImageGUIData();
            ImageGUIData referenceImage = new ImageGUIData();

            imageToCompare.addObserver(m_imageToCompareData);
            referenceImage.addObserver(m_referenceImageData);

            SwingUtilities.invokeLater(() -> {
                m_mainWindow = new MainWindow(imageToCompare, referenceImage);

                m_imageToCompareData.addObserver((IObserver) m_mainWindow);
                m_referenceImageData.addObserver((IObserver) m_mainWindow);
            });
        }
    }

    public static class ExceptionHandler implements Thread.UncaughtExceptionHandler {

        public void uncaughtException(Thread thread, Throwable thrown) {
            thrown.printStackTrace();
        }
    }

    public boolean hasStarted() {
        return m_hasStarted;
    }

    public MainWindow getMainWindow() {
        return m_mainWindow;
    }

    public ImageData getImageToCompareData() {
        return m_imageToCompareData;
    }

    public ImageData getReferenceImageData() {
        return m_referenceImageData;
    }

    public int getMaximumImgWidth() {
        return 32;
    }

    public int getMaximumImgHeight() {
        return 32;
    }

    public EquilateralEncodingTable getEncodingTable() {
        return m_encodingTable;
    }
}

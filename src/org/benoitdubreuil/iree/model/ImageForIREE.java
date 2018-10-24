package org.benoitdubreuil.iree.model;

import org.benoitdubreuil.iree.gui.ImageForIREEGUI;
import org.benoitdubreuil.iree.pattern.observer.IObserver;
import org.benoitdubreuil.iree.pattern.observer.Observable;

public class ImageForIREE extends Observable<ImageForIREE> implements IObserver<ImageForIREEGUI> {

    public static final int MAXIMUM_WIDTH = 32;
    public static final int MAXIMUM_HEIGHT = 32;
    public static final float MAXIMUM_DIMENSIONS_ASPECT_RATIO = MAXIMUM_WIDTH / (float) MAXIMUM_HEIGHT;

    private float[] m_equilateralEncoding;

    @Override
    public void observableChanged(ImageForIREEGUI newValue) {

    }
}

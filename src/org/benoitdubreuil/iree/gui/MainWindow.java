package org.benoitdubreuil.iree.gui;

import org.benoitdubreuil.iree.controller.ControllerIREE;
import org.benoitdubreuil.iree.model.ImageData;
import org.benoitdubreuil.iree.model.ImageDataRecognition;
import org.benoitdubreuil.iree.pattern.observer.IObserver;
import org.benoitdubreuil.iree.utils.ImageUtils;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.function.BiConsumer;

public class MainWindow extends JFrame implements IObserver<Object> {

    private static final String TITLE = "Image Recognition by Equilateral Encoding";
    private static final int IMAGE_BORDER_SIZE = 10;
    private static final int CENTER_ROW_COUNT = 2;
    private static final int CENTER_COLUMN_COUNT = 2;

    private JLabel m_comparisonValue_label;
    private JLabel m_imageToCompare_label;
    private JLabel m_downScaledImageToCompare_label;
    private JLabel m_referenceImage_label;
    private JLabel m_downScaledReferenceImage_label;

    JFileChooser m_fileChooser;

    private ImageGUIData m_imageToCompare;
    private ImageGUIData m_referenceImage;

    public MainWindow(ImageGUIData imageToCompare, ImageGUIData referenceImage) throws HeadlessException {
        initializeVars(imageToCompare, referenceImage);
        loadConfiguration();
        initializeListeners();
    }

    private void initializeVars(ImageGUIData imageToCompare, ImageGUIData referenceImage) {
        m_imageToCompare = imageToCompare;
        m_referenceImage = referenceImage;

        m_imageToCompare.addObserver((IObserver) this);
        m_referenceImage.addObserver((IObserver) this);
    }

    private void loadConfiguration() {
        setTitle(TITLE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        createFileChooser();
        loadGUI();
        loadSize();

        setVisible(true);
        setLocationRelativeTo(null);
    }

    private void createFileChooser() {
        m_fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Image", ImageIO.getReaderFormatNames());

        m_fileChooser.setFileFilter(filter);
        m_fileChooser.setAcceptAllFileFilterUsed(false);
    }

    private void loadSize() {
        Dimension size = new Dimension();
        size.width = 800;
        size.height = 600;

        setPreferredSize(size);
        setMinimumSize(size);
    }

    private void loadGUI() {
        loadMenuGUI();

        BorderLayout mainLayout = new BorderLayout();
        getContentPane().setLayout(mainLayout);

        loadTopPanelGUI();
        loadCenterPanelGUI();
    }

    private void loadMenuGUI() {
        JMenuBar menuBar = new JMenuBar();

        loadMenuFileGUI(menuBar);
        loadImageFileGUI(menuBar);

        setJMenuBar(menuBar);
    }

    private void loadMenuFileGUI(JMenuBar menuBar) {
        JMenu fileMenu = new JMenu("File");

        JMenuItem exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.setToolTipText("Exit application");

        exitMenuItem.addActionListener((ActionEvent event) -> {
            System.exit(0);
        });

        fileMenu.add(exitMenuItem);
        menuBar.add(fileMenu);
    }

    private void loadImageFileGUI(JMenuBar menuBar) {
        JMenu imageMenu = new JMenu("Image");

        JMenuItem loadRefImgMenuItem = new JMenuItem("Load Ref Image");
        loadRefImgMenuItem.setToolTipText("Load reference image");

        BiConsumer<ActionEvent, ImageGUIData> loadImgAction = (ActionEvent event, ImageGUIData image) -> {
            int result = m_fileChooser.showOpenDialog(this);

            if (result == JFileChooser.APPROVE_OPTION) {
                File file = m_fileChooser.getSelectedFile();

                try {
                    image.setOriginal(ImageIO.read(file));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        loadRefImgMenuItem.addActionListener((ActionEvent event) -> loadImgAction.accept(event, m_referenceImage));

        JMenuItem loadImgMenuItem = new JMenuItem("Load Image");
        loadImgMenuItem.setToolTipText("Load image to compare");

        loadImgMenuItem.addActionListener((ActionEvent event) -> loadImgAction.accept(event, m_imageToCompare));

        imageMenu.add(loadRefImgMenuItem);
        imageMenu.add(loadImgMenuItem);
        menuBar.add(imageMenu);
    }

    private void loadTopPanelGUI() {
        FlowLayout topLayout = new FlowLayout();
        JPanel topPanel = new JPanel(topLayout);
        getContentPane().add(topPanel, BorderLayout.NORTH);

        topPanel.add(m_comparisonValue_label = new JLabel("0"));
        topPanel.add(new JLabel("%"));
    }

    private void loadCenterPanelGUI() {
        GridLayout centerLayout = new GridLayout(CENTER_ROW_COUNT, CENTER_COLUMN_COUNT);
        JPanel centerPanel = new JPanel(centerLayout);
        getContentPane().add(centerPanel, BorderLayout.CENTER);

        m_imageToCompare_label = createImageLabel(IMAGE_BORDER_SIZE);
        m_referenceImage_label = createImageLabel(IMAGE_BORDER_SIZE);
        m_downScaledImageToCompare_label = createImageLabel(IMAGE_BORDER_SIZE);
        m_downScaledReferenceImage_label = createImageLabel(IMAGE_BORDER_SIZE);

        centerPanel.add(m_imageToCompare_label);
        centerPanel.add(m_referenceImage_label);
        centerPanel.add(m_downScaledImageToCompare_label);
        centerPanel.add(m_downScaledReferenceImage_label);
    }

    private void initializeListeners() {
        addComponentListener(new ComponentAdapter() {

            public void componentResized(ComponentEvent componentEvent) {
                observableChanged(m_imageToCompare);
                observableChanged(m_referenceImage);
            }
        });
    }

    private JLabel createImageLabel(int borderSize) {
        JLabel imagePanel = new JLabel();
        imagePanel.setBorder(BorderFactory.createEmptyBorder(borderSize, borderSize, borderSize, borderSize)); // Use grid layout hgap et vgap instead?

        return imagePanel;
    }

    private Image createImageFitToLabel(BufferedImage image, JLabel label) {
        return ImageUtils.fitImage(image, label.getWidth(), label.getHeight(), Image.SCALE_FAST);
    }

    @Override
    public void observableChanged(Object newValue) {
        if (newValue == null) {
            return;
        }

        if (newValue instanceof ImageGUIData) {

            ImageGUIData newValueCasted = (ImageGUIData) newValue;

            if (newValue == m_imageToCompare) {
                if (newValueCasted.getOriginal() != null) {
                    m_imageToCompare_label.setIcon(new ImageIcon(createImageFitToLabel(newValueCasted.getOriginal(), m_imageToCompare_label)));
                    m_downScaledImageToCompare_label.setIcon(new ImageIcon(createImageFitToLabel(newValueCasted.getDownScaled(), m_downScaledImageToCompare_label)));
                }
                else {
                    m_imageToCompare_label.setIcon(null);
                    m_downScaledImageToCompare_label.setIcon(null);
                }
            }
            else if (newValue == m_referenceImage) {
                if (newValueCasted.getOriginal() != null) {
                    m_referenceImage_label.setIcon(new ImageIcon(createImageFitToLabel(newValueCasted.getOriginal(), m_referenceImage_label)));
                    m_downScaledReferenceImage_label.setIcon(new ImageIcon(createImageFitToLabel(newValueCasted.getDownScaled(), m_downScaledReferenceImage_label)));
                }
                else {
                    m_referenceImage_label.setIcon(null);
                    m_downScaledReferenceImage_label.setIcon(null);
                }
            }
        }
        else if (newValue instanceof ImageData && m_imageToCompare.getDownScaled() != null && m_referenceImage.getDownScaled() != null) {
            ControllerIREE controller = ControllerIREE.getInstance();

            double meanDistance = 1 - ImageDataRecognition.compareImages(controller.getImageToCompareData(), controller.getReferenceImageData());

            DecimalFormat decimalFormat = new DecimalFormat("0.00");
            m_comparisonValue_label.setText(decimalFormat.format(meanDistance * 100));
        }
    }
}

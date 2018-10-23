package org.benoitdubreuil.iree.gui;

import org.benoitdubreuil.iree.pattern.IObserver;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.function.BiConsumer;

public class MainWindow extends JFrame implements IObserver<ImageForIREEGUI> {

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

    private ImageForIREEGUI m_imageToCompare;
    private ImageForIREEGUI m_referenceImage;

    public MainWindow() throws HeadlessException {
        initializeVars();
        loadConfiguration();
    }

    private void initializeVars() {
        m_imageToCompare = new ImageForIREEGUI();
        m_referenceImage = new ImageForIREEGUI();

        m_imageToCompare.addObserver(this);
        m_referenceImage.addObserver(this);
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

        BiConsumer<ActionEvent, ImageForIREEGUI> loadImgAction = (ActionEvent event, ImageForIREEGUI image) -> {
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

    private JLabel createImageLabel(int borderSize) {
        JLabel imagePanel = new JLabel();
        imagePanel.setBorder(BorderFactory.createEmptyBorder(borderSize, borderSize, borderSize, borderSize)); // Use grid layout hgap et vgap instead?

        return imagePanel;
    }

    private Image createImageFitToLabel(BufferedImage image, JLabel label) {
        float imageAspectRatio = image.getWidth() / (float) image.getHeight();
        float labelAspectRatio = label.getWidth() / (float) label.getHeight();

        Image resizedImage;

        if (imageAspectRatio >= labelAspectRatio) {
            resizedImage = image.getScaledInstance(label.getWidth(), (int) (label.getWidth() * imageAspectRatio), Image.SCALE_SMOOTH);
        }
        else {
            resizedImage = image.getScaledInstance((int) (label.getHeight() * imageAspectRatio), label.getHeight(), Image.SCALE_SMOOTH);
        }

        return resizedImage;
    }

    @Override
    public void observableChanged(ImageForIREEGUI newValue) {
        if (newValue == m_imageToCompare) {
            m_imageToCompare_label.setIcon(new ImageIcon(createImageFitToLabel(newValue.getOriginal(), m_imageToCompare_label)));
            m_downScaledImageToCompare_label.setIcon(new ImageIcon(createImageFitToLabel(newValue.getDownScaledGrayscale(), m_downScaledImageToCompare_label)));
        }
        else if (newValue == m_referenceImage) {
            m_referenceImage_label.setIcon(new ImageIcon(createImageFitToLabel(newValue.getOriginal(), m_referenceImage_label)));
            m_downScaledReferenceImage_label.setIcon(new ImageIcon(createImageFitToLabel(newValue.getDownScaledGrayscale(), m_downScaledReferenceImage_label)));
        }
    }
}

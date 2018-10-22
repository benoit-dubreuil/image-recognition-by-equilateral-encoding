package org.benoitdubreuil.iree.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class MainWindow extends JFrame {

    private static final String TITLE = "Image Recognition by Equilateral Encoding";
    private static final int IMAGE_BORDER_SIZE = 10;
    private static final int CENTER_ROW_COUNT = 2;
    private static final int CENTER_COLUMN_COUNT = 2;

    private JLabel m_comparisonValue;
    private JLabel m_imageToCompare;
    private JLabel m_downScaledImageToCompare;
    private JLabel m_referenceImage;
    private JLabel m_downScaledReferenceImage;

    public MainWindow() throws HeadlessException {
        loadConfiguration();
    }

    private void loadConfiguration() {
        setTitle(TITLE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        loadGUI();
        loadSize();

        setVisible(true);
        setLocationRelativeTo(null);
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

        loadRefImgMenuItem.addActionListener((ActionEvent event) -> {
        });

        JMenuItem loadImgMenuItem = new JMenuItem("Load Image");
        loadRefImgMenuItem.setToolTipText("Load image to compare");

        loadImgMenuItem.addActionListener((ActionEvent event) -> {
        });

        imageMenu.add(loadRefImgMenuItem);
        menuBar.add(imageMenu);
    }

    private void loadTopPanelGUI() {
        FlowLayout topLayout = new FlowLayout();
        JPanel topPanel = new JPanel(topLayout);
        getContentPane().add(topPanel, BorderLayout.NORTH);

        topPanel.add(m_comparisonValue = new JLabel("0"));
        topPanel.add(new JLabel("%"));
    }

    private void loadCenterPanelGUI() {
        GridLayout centerLayout = new GridLayout(CENTER_ROW_COUNT, CENTER_COLUMN_COUNT);
        JPanel centerPanel = new JPanel(centerLayout);
        getContentPane().add(centerPanel, BorderLayout.CENTER);

        m_imageToCompare = createImageLabel(IMAGE_BORDER_SIZE);
        m_referenceImage = createImageLabel(IMAGE_BORDER_SIZE);
        m_downScaledImageToCompare = createImageLabel(IMAGE_BORDER_SIZE);
        m_downScaledReferenceImage = createImageLabel(IMAGE_BORDER_SIZE);

        centerPanel.add(m_imageToCompare);
        centerPanel.add(m_referenceImage);
        centerPanel.add(m_downScaledImageToCompare);
        centerPanel.add(m_downScaledReferenceImage);
    }

    private JLabel createImageLabel(int borderSize) {
        JLabel imagePanel = new JLabel();
        imagePanel.setBorder(BorderFactory.createEmptyBorder(borderSize, borderSize, borderSize, borderSize)); // Use grid layout hgap et vgap instead?

        return imagePanel;
    }
}

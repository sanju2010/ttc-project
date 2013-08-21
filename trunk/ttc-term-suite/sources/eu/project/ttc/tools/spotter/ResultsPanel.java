// Copyright © 2013 Dictanova SAS
package eu.project.ttc.tools.spotter;

import javax.swing.*;
import java.awt.*;

/**
 * @author Fabien Poulard <fpoulard@dictanova.com>
 * @date 10/07/13
 */
public class ResultsPanel extends JPanel {

    private JPanel viewerGUI;
    private ProcessingResultListener listener;
    private ProcessingResultViewer viewer;
    private JPanel toolbarGUI;
    private AnnotationViewer annotationViewer;

    public ResultsPanel() {
        super();
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        // Prepare components
        createToolbar();
        createViewer();

        // Assemble the GUI
        add(toolbarGUI, BorderLayout.NORTH);
        add(Box.createRigidArea(new Dimension(0,5)));
        add(viewerGUI, BorderLayout.SOUTH);
    }

    private void createViewer() {
        annotationViewer = new AnnotationViewer();

        this.viewer = new ProcessingResultViewer(annotationViewer);
        this.listener = new ProcessingResultListener();
        listener.setViewer(viewer);
        viewer.enableListeners(listener);

//        viewerGUI = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
//        viewerGUI.setBorder(BorderFactory.createLineBorder(Color.BLUE));
//        viewerGUI.setResizeWeight(0.3);
//        viewerGUI.setTopComponent(viewer.getTabs());
//        viewerGUI.setBottomComponent(viewer.getViewer().getComponent());
//        viewerGUI.setPreferredSize(getPreferredSize());
        viewerGUI = new JPanel();
        viewerGUI.setLayout(new BoxLayout(viewerGUI, BoxLayout.LINE_AXIS));
        viewerGUI.add(viewer.getTabs());
        viewerGUI.add(Box.createHorizontalGlue());
        viewerGUI.add(viewer.getViewer().getComponent());
    }

    private void createToolbar() {
        toolbarGUI = new JPanel();
        toolbarGUI.setLayout(new BoxLayout(toolbarGUI, BoxLayout.LINE_AXIS));
        toolbarGUI.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel lbl = new JLabel("You can visualize the result of the processing below...");
        toolbarGUI.add(lbl);
    }

    public void addResult(ProcessingResult result) {
        viewer.getResultModel().addElement(result);
    }

    public DefaultListModel getResultModel() {
        return viewer.getResultModel();
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        viewer.doEnable(enabled);
    }
}

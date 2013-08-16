// Copyright © 2013 Dictanova SAS
package eu.project.ttc.tools.spotter;

import eu.project.ttc.tools.commons.ProcessingResult;
import eu.project.ttc.tools.commons.ProcessingResultListener;
import eu.project.ttc.tools.commons.ProcessingResultViewer;

import javax.swing.*;
import java.awt.*;

/**
 *
 *
 * @author Fabien Poulard <fpoulard@dictanova.com>
 * @date 10/07/13
 */
public class ResultsPanel extends JSplitPane {

    private final ProcessingResultListener listener;
    private ProcessingResultViewer viewer;

    public ResultsPanel() {
        super(JSplitPane.VERTICAL_SPLIT);

        setTopComponent(new Label("You can visualize the result of the processing below..."));

        this.viewer = new ProcessingResultViewer();
        this.listener = new ProcessingResultListener();
        listener.setViewer(viewer);
        viewer.enableListeners(listener);
        setBottomComponent(viewer);
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

// Copyright © 2013 Dictanova SAS
package eu.project.ttc.tools.indexer;

import javax.swing.*;
import java.awt.*;

/**
 * @author grdscarabe
 * @date 16/08/13
 */
public class ResultsPanel extends JSplitPane {

    private TermsBankViewer banker;

    public ResultsPanel() {
        super(JSplitPane.VERTICAL_SPLIT);

        setTopComponent(new Label("You can visualize the result of the processing below..."));

        this.banker = new TermsBankViewer();
        setBottomComponent(banker.getComponent());
    }
}

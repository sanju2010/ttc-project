// Copyright © 2013 Dictanova SAS
package eu.project.ttc.tools.commons;

import javax.swing.*;
import java.beans.PropertyChangeEvent;

/**
 * @author grdscarabe
 * @date 09/07/13
 */
public interface ToolView {

    /**
     * Method called when a new run is about to start. If necessary some elements in the
     * view should be reset (results...).
     */
    public abstract void runStarts();

    /**
     * Method called when the run has ended. If necessary some elements in the view
     * should be displayed (results...).
     */
    public abstract void runEnds();

}

// Copyright © 2013 Dictanova SAS
package eu.project.ttc.tools.spotter;

import eu.project.ttc.tools.ToolView;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.FileNotFoundException;

/**
 * Main view of the Spotter tool.
 *
 * @author Fabien Poulard <fpoulard@dictanova.com>
 */
public class SpotterView extends JTabbedPane implements ToolView {

    private final ConfigPanel compConfig;
    private final ResultsPanel compResults;

    public SpotterView() {
        super(JTabbedPane.TOP);

        // Prepare components
        compConfig = new ConfigPanel();
        compConfig.setPreferredSize(this.getPreferredSize());
        compResults = new ResultsPanel();

        compConfig.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                if ( evt.getPropertyName().startsWith("spotter.") )
                    firePropertyChange(evt.getPropertyName(), evt.getOldValue(), evt.getNewValue());
            }
        });

        // Set up tabs
        JScrollPane scrollConfig = new JScrollPane(compConfig,
               JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
               JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        addTab(" Configure ", scrollConfig);
        addTab(" Processing Results ", compResults);
    }

    public void addLanguageChangeListener(PropertyChangeListener listener) {
        addPropertyChangeListener("spotter.language", listener);
    }

    public void addInputDirectoryChangeListener(PropertyChangeListener listener) {
        addPropertyChangeListener("spotter.inputdirectory", listener);
    }

    public void addOutputDirectoryChangeListener(PropertyChangeListener listener) {
        addPropertyChangeListener("spotter.outputdirectory", listener);
    }

    public void addTtgDirectoryChangeListener(PropertyChangeListener listener) {
        addPropertyChangeListener("spotter.ttgdirectory", listener);
    }

    public void setLanguage(String language) {
        compConfig.setLanguage(language);
    }
}

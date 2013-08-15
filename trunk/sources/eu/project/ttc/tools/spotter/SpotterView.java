// Copyright © 2013 Dictanova SAS
package eu.project.ttc.tools.spotter;

import eu.project.ttc.tools.commons.ToolView;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.FileNotFoundException;

/**
 * Main view of the Spotter tool.
 *
 * @author Fabien Poulard <fpoulard@dictanova.com>
 */
public class SpotterView extends JTabbedPane implements ToolView, SpotterBinding {

    private final ConfigPanel compConfig;
    private final ResultsPanel compResults;
    private IllegalArgumentException inputDirectoryError;

    /**
     * Create the view.
     * Instantiate both panels:
     * <ul>
     *     <li>the configuration panel, responsible for exposing the
     *     tool parameters to the user,</li>
     *     <li>the result panel, responsible for exposing the results
     *     of the processing (this one or another) to the user.</li>
     * </ul>
     *
     * Most
     */
    public SpotterView() {
        super(JTabbedPane.TOP);

        // Prepare components
        compConfig = new ConfigPanel();
        compConfig.setPreferredSize(this.getPreferredSize());
        compResults = new ResultsPanel();

        compConfig.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                if ( evt.getPropertyName().startsWith(EVT_PREFIX) )
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

    //////////////////////////////////////////////////////// SPOTTER BINDINGS

    @Override
    public void addLanguageChangeListener(PropertyChangeListener listener) {
        addPropertyChangeListener(EVT_LANGUAGE, listener);
    }

    @Override
    public void addInputDirectoryChangeListener(PropertyChangeListener listener) {
        addPropertyChangeListener(EVT_INPUT, listener);
    }

    @Override
    public void addOutputDirectoryChangeListener(PropertyChangeListener listener) {
        addPropertyChangeListener(EVT_OUTPUT, listener);
    }

    @Override
    public void addTtgDirectoryChangeListener(PropertyChangeListener listener) {
        addPropertyChangeListener(EVT_TTGHOME, listener);
    }

    @Override
    public void setLanguage(String language) {
        compConfig.setLanguage(language);
    }

    @Override
    public String getLanguage() {
        return compConfig.getLanguage();
    }

    public void setLanguageError(IllegalArgumentException e) {
        compConfig.setLanguageError(e);
    }

    public void unsetLanguageError() {
        compConfig.unsetLanguageError();
    }

    @Override
    public void setInputDirectory(String inputDirectory) {
        compConfig.setInputDirectory(inputDirectory);
    }

    @Override
    public String getInputDirectory() {
        return compConfig.getInputDirectory();
    }

    public void setInputDirectoryError(IllegalArgumentException e) {
        compConfig.setInputDirectoryError(e);
    }

    public void unsetInputDirectoryError() {
        compConfig.unsetInputDirectoryError();
    }

    @Override
    public void setOutputDirectory(String outputDirectory) {
        compConfig.setOutputDirectory(outputDirectory);
    }

    @Override
    public String getOutputDirectory() {
        return compConfig.getOutputDirectory();
    }

    public void setOutputDirectoryError(IllegalArgumentException e) {
        compConfig.setOutputDirectoryError(e);
    }

    public void unsetOutputDirectoryError() {
        compConfig.unsetOutputDirectoryError();
    }

    @Override
    public void setTreetaggerHome(String treetaggerHome) {
        compConfig.setTreetaggerHome(treetaggerHome);
    }

    @Override
    public String getTreetaggerHome() {
        return compConfig.getTreetaggerHome();
    }

    public void setTreetaggerHomeError(IllegalArgumentException e) {
        compConfig.setTreetaggerHomeError(e);
    }

    public void unsetTreetaggerHomeError() {
        compConfig.unsetTreetaggerHomeError();
    }
}

package eu.project.ttc.tools.commons;

import org.apache.uima.resource.ResourceConfigurationException;
import org.apache.uima.resource.metadata.NameValuePair;
import org.apache.uima.resource.metadata.ResourceMetaData;
import org.xml.sax.SAXException;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Model part of the MVC paradigm used for the application.
 * The TermSuite application is made of tools (Spotter, Indexer, Aligner) that are each designed
 * according to the MVC paradigm and so made of a view, a controller and a model. This class
 * is the abstract implementation of the model part of the tools.
 *
 * In our implementation, the model does not know about the controller or the views. It simply
 * informs all PropertyChangeListener of anything going on.
 * Each tool is responsible of its own configuration persistence. Therefore the ToolModel must
 * be able to persist any data related to the configuration. Most likely this data is persisted
 * as UIMA parameters.
 *
 * @author Fabien Poulard <fpoulard@dictanova.com>
 */
public abstract class ToolModel {

    // Watch property changes in the bean
    protected PropertyChangeSupport propertyChangeSupport;
    // File where the model is serialized (UIMA configuration file)
    protected File persistedCfg;

    /**
     * Constructor.
     * Create a new ToolModel and initialize the support of property changes
     * as well as the saving the file
     */
    public ToolModel(File cfg) {
        propertyChangeSupport = new PropertyChangeSupport(this);
        persistedCfg = cfg;
    }

    /**
     * Access all the parameters registered in the system.
     *
     * @param includeLanguage
     *      if set, then the Language parameter is included as well which will
     *      generate an error if you try to run the engine from here as the
     *      corresponding parameter does not exist
     */
    public abstract NameValuePair[] getParameterSettings(boolean includeLanguage);

    /**
     * Provides the means to set or reset the model to a default state.
     */
    public abstract void initDefault() throws FileNotFoundException, InvalidTermSuiteConfiguration;

    /**
     * Load the configuration persisted if any.
     */
    public abstract void load() throws IOException, InvalidTermSuiteConfiguration;

    /**
     * Persists the configuration.
     */
    public abstract void save() throws IOException;

    public void addPropertyChangeListener(String property, PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(property, listener);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(String property, PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(property, listener);
    }

    protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
        propertyChangeSupport.firePropertyChange(propertyName, oldValue, newValue);
    }

    public File getConfigurationFile() {
        return persistedCfg;
    }

    /**
     * Validate the current configuration.
     */
    public abstract void validate() throws ResourceConfigurationException;

    /**
     * Method called when a new run is about to start. If necessary some elements in the
     * model should be reset (stats...).
     */
    public abstract void runStarts();

    /**
     * Method called when the run has ended. If necessary some elements in the model
     * should be computed (stats...).
     */
    public abstract void runEnds();

    // FIXME remove all these
//
//	public abstract void doUpdate();
//
//	public abstract void validate() throws ResourceConfigurationException;
//
//	public abstract void doSave() throws IOException, SAXException;
//
//	public abstract ResourceMetaData getMetaData();
	
}
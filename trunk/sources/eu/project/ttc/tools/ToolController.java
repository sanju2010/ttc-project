package eu.project.ttc.tools;

import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.cas.CAS;
import org.apache.uima.resource.metadata.ConfigurationParameterSettings;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.lang.reflect.Method;

/**
 * Controller part of the MVC paradigm used for the application.
 * The TermSuite application is made of tools (Spotter, Indexer, Aligner) that are each designed
 * according to the MVC paradigm and so made of a view, a controller and a model. This class
 * is the abstract implementation of the controller part of the tools.
 *
 * In our implementation, the controller is the bound between views and controllers. It is the
 * one responsible for binding them together and flowing the data from one another.
 * This controller implements the {@link PropertyChangeListener} interface so that it can listen
 * for changing properties both in the models and in the views so that it can reflect the changes.
 *
 * @author Fabien Poulard <fpoulard@dictanova.com>
 */
public abstract class ToolController implements PropertyChangeListener {

    // Default encoding for TermSuite analysis engines
    public static final String DEFAULT_ENCODING = "utf-8";

    // All the registered views this controller controls
    protected ToolView registeredView;
    // There is only one registered model
    protected ToolModel registeredModel;

    /**
     * Constructor.
     * Register the model controlled by this controller as well as the view
     * exposing the model.
     */
    public ToolController(ToolModel model, ToolView view) {
        registeredView = view;

        registeredModel = model;
        registeredModel.addPropertyChangeListener(this);
    }

    public JComponent getView() {
        return (JComponent) registeredView;
    }

    /**
     * (re)Load the persisted configuration for the model.
     */
    public void loadConfiguration() throws IOException {
        // Proxy for ToolModel#load()
        registeredModel.load();
    }

    /**
     * Persist the configuration for the model.
     */
    public void saveConfiguration() throws IOException {
        // Proxy for ToolModel#save()
        registeredModel.save();
    }

    /**
     * Build the analysis engine settings from the model so that the resulting settings
     * can be directly passed to the corresponding engine.
     */
    public abstract ConfigurationParameterSettings getAESettings();

    /**
     * Build the analysis engine description from the model so that it can be
     * run directly or through a CPE.
     */
    public abstract AnalysisEngineDescription getAEDescription() throws Exception;

    /**
     * Compute the description of where and what the files to be processed are.
     */
    public abstract InputSource getInputSource();

    /**
     * Method called once a CAS has been processed so that the result can be
     * shared with the model and the views.
     */
    public abstract void processingCallback(CAS cas) throws Exception;

    /** Getter for the language parameter */
    public abstract String getLanguage();

    /** Getter for the encoding parameter */
    public String getEncoding() {
        return DEFAULT_ENCODING;
    }

    /**
     * Use this to observe property changes from registered models
     * and propagate them on to the view.
     *
     * @param evt
     *      the PropertyChangeEvent to share with the view
     */
    public void propertyChange(PropertyChangeEvent evt) {
        registeredView.modelPropertyChange(evt);
    }

    /**
     * This is a convenience method that subclasses can call upon
     * to fire property changes back to the models. This method
     * uses reflection to inspect each of the model classes
     * to determine whether it is the owner of the property
     * in question. If it isn't, a NoSuchMethodException is thrown,
     * which the method ignores.
     *
     * @param propertyName
     *      The name of the property.
     * @param newValue
     *      An object that represents the new value of the property.
     */
    protected void setModelProperty(String propertyName, Object newValue) {
        try {
            Method method = registeredModel.getClass().
                    getMethod("set" + propertyName, new Class[]{newValue.getClass()});
            method.invoke(registeredModel, newValue);
        } catch (Exception ex) {
            //  Handle exception.
        }
    }

    // FIXME remove all these

	public abstract void setParent(TermSuite parent);

	public abstract TermSuite getParent();

	public abstract ToolModel getSettings();
	
//	public abstract Component getView();

}
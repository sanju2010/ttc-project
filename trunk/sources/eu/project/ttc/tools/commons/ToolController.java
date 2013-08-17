package eu.project.ttc.tools.commons;

import eu.project.ttc.tools.TermSuite;
import eu.project.ttc.tools.spotter.SpotterModel;
import eu.project.ttc.tools.spotter.SpotterView;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.cas.CAS;
import org.apache.uima.resource.ResourceConfigurationException;
import org.apache.uima.resource.metadata.ConfigurationParameterSettings;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
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
//public abstract class ToolController implements PropertyChangeListener {
public abstract class ToolController {

    // Default encoding for TermSuite analysis engines
    public static final String DEFAULT_ENCODING = "utf-8";

    // All the registered views this controller controls
    private ToolView registeredView;
    // There is only one registered model
    private ToolModel registeredModel;

    /**
     * Constructor.
     * Register the model controlled by this controller as well as the view
     * exposing the model.
     */
    public ToolController(ToolModel model, ToolView view) {
        registeredView = view;

        registeredModel = model;
//        registeredModel.addPropertyChangeListener(this);
    }

    /**
     * (re)Load the persisted configuration for the model.
     */
    public void loadConfiguration() throws IOException, InvalidTermSuiteConfiguration {
        // Proxy for ToolModel#load()
        registeredModel.load();
    }

    /**
     * Persist the configuration for the model.
     */
    public void validateAndSaveConfiguration() throws IOException, InvalidTermSuiteConfiguration {
        try {
            registeredModel.validate();
            registeredModel.save();
        } catch (ResourceConfigurationException e) {
            throw new InvalidTermSuiteConfiguration("The configuration is invalid in UIMA terms.", e);
        }
    }

    /**
     * Build the analysis engine settings from the model so that the resulting settings
     * can be directly passed to the corresponding engine.
     */
    public abstract ConfigurationParameterSettings getAESettings();

    /**
     * Compute the name of the resource corresponding to the descriptor to use
     * to run the corresponding engine.
     */
    public abstract String getAEDescriptor();

    /**
     * Specify the kind of files to be processed by the tool.
     */
    public abstract InputSource.InputSourceTypes getInputSourceType();

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
     * Getter to the model
     */
    protected ToolModel getToolModel() {
        return registeredModel;
    }

    /**
     * Getter to the view
     */
    protected ToolView getToolView() {
        return registeredView;
    }

    /**
     * Accessor to the tool configuration file where its model is persisted.
     */
    public File getConfigurationFile() {
        return registeredModel.getConfigurationFile();
    }

    /**
     * Method called when a new run is about to start. If necessary some elements in the
     * tool should be reset (some parts of the views like results, some stats too).
     */
    public void runStarts() {
        getToolView().runStarts();
        getToolModel().runStarts();
    }

    /**
     * Method called when the run has ended. If necessary some elements in the tool
     * should be displayed or computed (parts of the views like results, stats...).
     */
    public void runEnds() {
        getToolView().runEnds();
        getToolModel().runEnds();
    }

    // FIXME remove all these
//
//
//    /**
//     * Use this to observe property changes from registered models
//     * and propagate them on to the view.
//     *
//     * @param evt
//     *      the PropertyChangeEvent to share with the view
//     */
//    public void propertyChange(PropertyChangeEvent evt) {
////        registeredView.modelPropertyChange(evt);
//    }
//
//    /**
//     * This is a convenience method that subclasses can call upon
//     * to fire property changes back to the models. This method
//     * uses reflection to inspect each of the model classes
//     * to determine whether it is the owner of the property
//     * in question. If it isn't, a NoSuchMethodException is thrown,
//     * which the method ignores.
//     *
//     * @param propertyName
//     *      The name of the property.
//     * @param newValue
//     *      An object that represents the new value of the property.
//     */
//    protected void setModelProperty(String propertyName, Object newValue) {
//        try {
//            Method method = registeredModel.getClass().
//                    getMethod("set" + propertyName, new Class[]{newValue.getClass()});
//            method.invoke(registeredModel, newValue);
//        } catch (Exception ex) {
//            //  Handle exception.
//        }
//    }
//
//	public abstract void setParent(TermSuite parent);
//
//	public abstract TermSuite getParent();
//
//	public abstract ToolModel getSettings();

}
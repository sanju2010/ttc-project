package eu.project.ttc.tools.config;

import eu.project.ttc.tools.ToolModel;
import org.apache.uima.resource.metadata.ConfigurationParameter;
import org.apache.uima.resource.metadata.ConfigurationParameterDeclarations;

import javax.swing.*;

// FIXME
// public class AlignerSettings extends TermSuiteToolConfiguration implements ToolModel {
public class AlignerSettings extends TermSuiteToolConfiguration {
	
	/** Name of the parameter that must be set to the output dir path */
	public static final String P_OUTPUT_DIRECTORY = "AlignerOutputDirectory";
	
	/** Name of the parameter that must be set to the source language */
	public static final String P_SOURCE_LANGUAGE = "SourceLanguage";
	
	/** Name of the parameter that must be set to the target language */
	public static final String P_TARGET_LANGUAGE = "TargetLanguage";
	
	/** Name of the parameter that must be set to the source terminology XMI file */
	public static final String P_SOURCE_TERMINOLOGY = "SourceTerminologyFile";
	
	/** Name of the parameter that must be set to the target terminology XMI file */
	public static final String P_TARGET_TERMINOLOGY = "TargetTerminologyFile";
	
	/** Name of the parameter that must be set to the bilingual dictionary */
	public static final String P_BILINGUAL_DICTIONARY = "BilingualDictionaryFile";
	
	/** Name of the parameter that must be set to the bilingual dictionary */
	public static final String P_EVALUATION_DIRECTORY = "EvaluationInputOutputTranslationDirectory";

    /**
     * Name of the parameter that must be set to enable the compositional method
     */
    public static final String P_METHOD_COMPOSITIONAL = "CompositionalMethod";

    /**
     * Name of the parameter that must be set to enable the distributional
     * method
     */
    public static final String P_METHOD_DISTRIBUTIONAL = "DistributionalMethod";

    /**
     * Name of the parameter that must be set to enable the distributional
     * method
     */
    public static final String P_SIMILARITY_DISTANCE = "SimilarityDistanceClassName";

    /** Name of the parameter that must be set to limit the number of translation candidates */
    public static final String P_MAX_CANDIDATES = "MaxTranslationCandidates";
	
	public AlignerSettings(String resource) {
		super(resource);
        getComponent().setBorder(BorderFactory.createTitledBorder("Settings"));
	}

    @Override
    protected GroupOfParameters[] getParameters() {
        return new GroupOfParameters[0];  //To change body of implemented methods use File | Settings | File Templates.
    }

    protected void setMetaData(ConfigurationParameterDeclarations declarations) {
		this.addParameter(declarations, P_EVALUATION_DIRECTORY,
                ConfigurationParameter.TYPE_STRING, false, true);
		this.addParameter(declarations, P_SOURCE_LANGUAGE,
                ConfigurationParameter.TYPE_STRING, false, true, "values:en|fr|de|es|ru|da|lv|zh");
		this.addParameter(declarations, P_SOURCE_TERMINOLOGY,
                ConfigurationParameter.TYPE_STRING, false, true);
		this.addParameter(declarations, P_TARGET_LANGUAGE,
                ConfigurationParameter.TYPE_STRING, false, true, "values:en|fr|de|es|ru|da|lv|zh");
		this.addParameter(declarations, P_TARGET_TERMINOLOGY,
                ConfigurationParameter.TYPE_STRING, false, true);
		this.addParameter(declarations, P_BILINGUAL_DICTIONARY,
                ConfigurationParameter.TYPE_STRING, false, false);
		this.addParameter(declarations, P_OUTPUT_DIRECTORY,
                ConfigurationParameter.TYPE_STRING, false, true);
        this.addParameter(declarations, P_METHOD_COMPOSITIONAL,
                ConfigurationParameter.TYPE_BOOLEAN, false, false);
        this.addParameter(declarations, P_METHOD_DISTRIBUTIONAL,
                ConfigurationParameter.TYPE_BOOLEAN, false, false);
        this.addParameter(declarations, P_SIMILARITY_DISTANCE,
                ConfigurationParameter.TYPE_STRING, false, false,
                "values:eu.project.ttc.metrics.Jaccard|eu.project.ttc.metrics.Cosine");
        this.addParameter(declarations, P_MAX_CANDIDATES,
                ConfigurationParameter.TYPE_INTEGER, false, false);
	}

	protected static String[][] params = new String[][] {
			// Input parameters
			new String[] { " Input parameters  ", P_EVALUATION_DIRECTORY, "false" },
			new String[] { P_SOURCE_LANGUAGE, P_SOURCE_TERMINOLOGY,
					P_TARGET_LANGUAGE, P_TARGET_TERMINOLOGY,
					P_BILINGUAL_DICTIONARY },
            // Advanced parameters
            new String[] { " Advanced parameters  ", P_METHOD_COMPOSITIONAL, "false" },
            new String[] { P_METHOD_DISTRIBUTIONAL, P_SIMILARITY_DISTANCE, P_MAX_CANDIDATES }
    };

	protected String[][] getParameterGroups() {
		return null;
	}

	@Override
	protected String[][] getButtonGroups() {
		return null;
	}
		
}

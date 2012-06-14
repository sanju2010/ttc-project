package eu.project.ttc.tools.aligner;

import org.apache.uima.resource.metadata.ConfigurationParameter;
import org.apache.uima.resource.metadata.ConfigurationParameterDeclarations;

import eu.project.ttc.tools.TermSuiteSettings;
import eu.project.ttc.tools.utils.Parameters;

public class AlignerSettings extends Parameters implements TermSuiteSettings {	
	
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
	
	public AlignerSettings(String resource) {
		super(resource);
	}

	protected void setMetaData(ConfigurationParameterDeclarations declarations) {
		this.addParameter(declarations, P_EVALUATION_DIRECTORY, ConfigurationParameter.TYPE_STRING, false, true);
		this.addParameter(declarations, P_SOURCE_LANGUAGE, ConfigurationParameter.TYPE_STRING, false, true, "values:en|fr|de|es|ru|da|lv|zh");
		this.addParameter(declarations, P_SOURCE_TERMINOLOGY, ConfigurationParameter.TYPE_STRING, false, true);
		this.addParameter(declarations, P_TARGET_LANGUAGE, ConfigurationParameter.TYPE_STRING, false, true, "values:en|fr|de|es|ru|da|lv|zh");
		this.addParameter(declarations, P_TARGET_TERMINOLOGY, ConfigurationParameter.TYPE_STRING, false, true);
		this.addParameter(declarations, P_BILINGUAL_DICTIONARY, ConfigurationParameter.TYPE_STRING, false, false);
		this.addParameter(declarations, P_OUTPUT_DIRECTORY, ConfigurationParameter.TYPE_STRING, false, true);

	}

	protected static String[][] params = new String[][] {
			// Input parameters
			new String[] { " Input parameters  ", P_EVALUATION_DIRECTORY, "false" },
			new String[] { P_SOURCE_LANGUAGE, P_SOURCE_TERMINOLOGY,
					P_TARGET_LANGUAGE, P_TARGET_TERMINOLOGY,
					P_BILINGUAL_DICTIONARY } };
	
	@Override
	protected String[][] getParameterGroups() {
		return null;
	}

	@Override
	protected String[][] getButtonGroups() {
		return null;
	}
		
}

package eu.project.ttc.tools.config;

import eu.project.ttc.tools.TermSuiteConfigurationFile;
import org.apache.uima.resource.metadata.ConfigurationParameter;
import org.apache.uima.resource.metadata.ConfigurationParameterDeclarations;

public class SpotterSettings extends UIMAParameters implements TermSuiteConfigurationFile {

	/** Name of the parameter that must be set to the output dir path */
	public static final String P_OUTPUT_DIRECTORY = "OutputDirectory";
	
	/** Name of the parameter that must be set to the output dir path */
	public static final String P_INPUT_DIRECTORY = "InputDirectory";
	
	/** Name of the parameter that must be set to the source language */
	public static final String P_SOURCE_LANGUAGE = "Language";
	
	/** Name of the parameter that must be set to the target language */
	public static final String P_TREETAGGER_HOME_DIRECTORY = "TreeTaggerHomeDirectory";
	
	public SpotterSettings(String resource) {
		super(resource);
	}

	protected void setMetaData(ConfigurationParameterDeclarations declarations) {
		this.addParameter(declarations, P_SOURCE_LANGUAGE, ConfigurationParameter.TYPE_STRING, false, true, "values:en|fr|de|es|ru|da|lv|zh");
		this.addParameter(declarations, P_INPUT_DIRECTORY, ConfigurationParameter.TYPE_STRING, false, true);
		this.addParameter(declarations, P_OUTPUT_DIRECTORY, ConfigurationParameter.TYPE_STRING, false, true);
		this.addParameter(declarations, P_TREETAGGER_HOME_DIRECTORY, ConfigurationParameter.TYPE_STRING, false, true);
		/*
		this.addParameter(declarations, "TreeTaggerParameterFile", ConfigurationParameter.TYPE_STRING, false, true);
		this.addParameter(declarations, "SegmentFile", ConfigurationParameter.TYPE_STRING, false, true);
		this.addParameter(declarations, "CategoryMappingFile", ConfigurationParameter.TYPE_STRING, false, true);
		this.addParameter(declarations, "SubcategoryMappingFile", ConfigurationParameter.TYPE_STRING, false, true);
		this.addParameter(declarations, "MoodMappingFile", ConfigurationParameter.TYPE_STRING, false, true);
		this.addParameter(declarations, "TenseMappingFile", ConfigurationParameter.TYPE_STRING, false, true);
		this.addParameter(declarations, "GenderMappingFile", ConfigurationParameter.TYPE_STRING, false, true);
		this.addParameter(declarations, "NumberMappingFile", ConfigurationParameter.TYPE_STRING, false, true);
		this.addParameter(declarations, "CaseMappingFile", ConfigurationParameter.TYPE_STRING, false, true);
		*/
	}

	@Override
	protected String[][] getParameterGroups() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String[][] getButtonGroups() {
		return null;
	}
		
}

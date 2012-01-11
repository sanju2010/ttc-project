package eu.project.ttc.tools.tagger.treetagger;

import org.apache.uima.resource.metadata.ConfigurationParameter;
import org.apache.uima.resource.metadata.ConfigurationParameterDeclarations;

import eu.project.ttc.tools.tagger.TaggerSettings;
import eu.project.ttc.tools.utils.Parameters;

public class TreeTaggerSettings extends Parameters implements TaggerSettings {	
	
	public TreeTaggerSettings(String resource) {
		super(resource);
	}

	protected void setMetaData(ConfigurationParameterDeclarations declarations) {
		this.addParameter(declarations, "Language", ConfigurationParameter.TYPE_STRING, false, true, "values:en|fr|de|es|da|ru|zh");
		this.addParameter(declarations, "InputDirectory", ConfigurationParameter.TYPE_STRING, false, true);
		this.addParameter(declarations, "OutputDirectory", ConfigurationParameter.TYPE_STRING, false, true);
		this.addParameter(declarations, "TreeTaggerHomeDirectory", ConfigurationParameter.TYPE_STRING, false, true);
		this.addParameter(declarations, "TreeTaggerParameterFile", ConfigurationParameter.TYPE_STRING, false, true);
		this.addParameter(declarations, "SegmentFile", ConfigurationParameter.TYPE_STRING, false, true);
		this.addParameter(declarations, "CategoryMappingFile", ConfigurationParameter.TYPE_STRING, false, true);
		this.addParameter(declarations, "SubcategoryMappingFile", ConfigurationParameter.TYPE_STRING, false, true);
		this.addParameter(declarations, "MoodMappingFile", ConfigurationParameter.TYPE_STRING, false, true);
		this.addParameter(declarations, "TenseMappingFile", ConfigurationParameter.TYPE_STRING, false, true);
	}
		
}

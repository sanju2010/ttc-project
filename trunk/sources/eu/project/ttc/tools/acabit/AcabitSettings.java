package eu.project.ttc.tools.acabit;

import org.apache.uima.resource.metadata.ConfigurationParameter;
import org.apache.uima.resource.metadata.ConfigurationParameterDeclarations;

import eu.project.ttc.tools.utils.Parameters;

public class AcabitSettings extends Parameters {	
	
	public AcabitSettings(String resource) {
		super(resource);
	}

	protected void setMetaData(ConfigurationParameterDeclarations declarations) {
		this.addParameter(declarations, "Language", ConfigurationParameter.TYPE_STRING, false, true, "values:en|fr|de|es|ru|da|lv|zh");
		this.addParameter(declarations, "InputDirectory", ConfigurationParameter.TYPE_STRING, false, true);
		this.addParameter(declarations, "OutputDirectory", ConfigurationParameter.TYPE_STRING, false, true);
		this.addParameter(declarations, "TerminologyFile", ConfigurationParameter.TYPE_STRING, false, true);
		this.addParameter(declarations, "TermAnnotationType", ConfigurationParameter.TYPE_STRING, false, true, "values:eu.project.ttc.types.TermAnnotation|eu.project.ttc.types.SingleWordTermAnnotation|eu.project.ttc.types.MultiWordTermAnnotation|eu.project.ttc.types.NeoClassicalCompoundTermAnnotation");
		this.addParameter(declarations, "MultiWordPatternRuleFile", ConfigurationParameter.TYPE_STRING, false, true);
		this.addParameter(declarations, "TermVariationRuleFile", ConfigurationParameter.TYPE_STRING, false, true);
		this.addParameter(declarations, "NeoclassicalElementFile", ConfigurationParameter.TYPE_STRING, false, true);
		this.addParameter(declarations, "EditDistanceClassName", ConfigurationParameter.TYPE_STRING, false, true, "values:eu.project.ttc.metrics.LevenshteinDistance");
		this.addParameter(declarations, "EnableTermGathering", ConfigurationParameter.TYPE_BOOLEAN, false, true);
	}
		
}

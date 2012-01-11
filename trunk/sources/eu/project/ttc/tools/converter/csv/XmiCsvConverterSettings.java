package eu.project.ttc.tools.converter.csv;

import org.apache.uima.resource.metadata.ConfigurationParameter;
import org.apache.uima.resource.metadata.ConfigurationParameterDeclarations;

import eu.project.ttc.tools.converter.ConverterSettings;
import eu.project.ttc.tools.utils.Parameters;

public class XmiCsvConverterSettings extends Parameters implements ConverterSettings {	
	
	public XmiCsvConverterSettings(String resource) {
		super(resource);
	}

	protected void setMetaData(ConfigurationParameterDeclarations declarations) {
		this.addParameter(declarations, "InputFile", ConfigurationParameter.TYPE_STRING, false, true);
		this.addParameter(declarations, "OutputFile", ConfigurationParameter.TYPE_STRING, false, true);
		this.addParameter(declarations, "TermAnnotationType", ConfigurationParameter.TYPE_STRING, false, true, "values:eu.project.ttc.types.TermAnnotation|eu.project.ttc.types.SingleWordTermAnnotation|eu.project.ttc.types.MultiWordTermAnnotation|eu.project.ttc.types.NeoClassicalCompoundTermAnnotation");
		this.addParameter(declarations, "SortBy", ConfigurationParameter.TYPE_STRING, false, true, "values:Specificity|Frequency|Name");
	}
		
}

package eu.project.ttc.tools.converter.tsv;

import org.apache.uima.resource.metadata.ConfigurationParameter;
import org.apache.uima.resource.metadata.ConfigurationParameterDeclarations;

import eu.project.ttc.tools.converter.ConverterSettings;
import eu.project.ttc.tools.utils.Parameters;

public class XmiTsvConverterSettings extends Parameters implements ConverterSettings {	
	
	public XmiTsvConverterSettings(String resource) {
		super(resource);
	}

	protected void setMetaData(ConfigurationParameterDeclarations declarations) {
		this.addParameter(declarations, "InputDirectory", ConfigurationParameter.TYPE_STRING, false, true);
		this.addParameter(declarations, "OutputDirectory", ConfigurationParameter.TYPE_STRING, false, true);
	}
		
}

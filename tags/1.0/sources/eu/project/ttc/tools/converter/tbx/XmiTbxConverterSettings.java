package eu.project.ttc.tools.converter.tbx;

import org.apache.uima.resource.metadata.ConfigurationParameter;
import org.apache.uima.resource.metadata.ConfigurationParameterDeclarations;

import eu.project.ttc.tools.TermSuiteSettings;
import eu.project.ttc.tools.utils.Parameters;

public class XmiTbxConverterSettings extends Parameters implements TermSuiteSettings {	
	
	public XmiTbxConverterSettings(String resource) {
		super(resource);
	}

	protected void setMetaData(ConfigurationParameterDeclarations declarations) {
		this.addParameter(declarations, "InputFile", ConfigurationParameter.TYPE_STRING, false, true);
		this.addParameter(declarations, "OutputFile", ConfigurationParameter.TYPE_STRING, false, true);
	}
		
}

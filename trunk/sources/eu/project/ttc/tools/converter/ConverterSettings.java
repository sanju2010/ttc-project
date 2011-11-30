package eu.project.ttc.tools.converter;

import org.apache.uima.resource.metadata.ConfigurationParameter;
import org.apache.uima.resource.metadata.ConfigurationParameterDeclarations;

import eu.project.ttc.tools.utils.Parameters;

public class ConverterSettings extends Parameters {	
	
	public ConverterSettings(String resource) {
		super(resource);
	}

	protected void setMetaData(ConfigurationParameterDeclarations declarations) {
		this.addParameter(declarations, "AnnotatorClassName", ConfigurationParameter.TYPE_STRING, false, true, "values:eu.project.ttc.all.engines.TsvCasConsumer|eu.project.ttc.all.engines.FlxCasConsumer|eu.project.ttc.all.engines.ZigCasConsumer");
		this.addParameter(declarations, "InputFileOrDirectory", ConfigurationParameter.TYPE_STRING, false, true);
		this.addParameter(declarations, "OutputFileOrDirectory", ConfigurationParameter.TYPE_STRING, false, true);
	}
		
}

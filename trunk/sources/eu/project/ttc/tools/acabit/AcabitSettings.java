package eu.project.ttc.tools.acabit;

import org.apache.uima.resource.metadata.ConfigurationParameter;
import org.apache.uima.resource.metadata.ConfigurationParameterDeclarations;

import eu.project.ttc.tools.utils.Parameters;

public class AcabitSettings extends Parameters {	
	
	public AcabitSettings(String resource) {
		super(resource);
	}

	protected void setMetaData(ConfigurationParameterDeclarations declarations) {
		this.addParameter(declarations, "Language", ConfigurationParameter.TYPE_STRING, false, true);
		this.addParameter(declarations, "InputDirectory", ConfigurationParameter.TYPE_STRING, false, true);
		this.addParameter(declarations, "OutputDirectory", ConfigurationParameter.TYPE_STRING, false, true);
		this.addParameter(declarations, "TerminologyFile", ConfigurationParameter.TYPE_STRING, false, true);
		this.addParameter(declarations, "TermAnnotationType", ConfigurationParameter.TYPE_STRING, false, true);
	}
		
}

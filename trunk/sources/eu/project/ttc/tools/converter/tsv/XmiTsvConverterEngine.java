package eu.project.ttc.tools.converter.tsv;

import org.apache.uima.UIMAFramework;
import org.apache.uima.cas.CAS;
import org.apache.uima.resource.metadata.ConfigurationParameterSettings;

import eu.project.ttc.tools.TermSuiteEngine;
import eu.project.ttc.tools.TermSuiteRunner;
import eu.project.ttc.tools.TermSuiteTool;

public class XmiTsvConverterEngine implements TermSuiteEngine {


	private TermSuiteTool termSuiteTool;
	
	public void setTermSuiteTool(TermSuiteTool termSuiteTool) {
		this.termSuiteTool = termSuiteTool;
	}
	
	private TermSuiteTool getTermSuiteTool() {
		return this.termSuiteTool;
	}

	@Override
	public ConfigurationParameterSettings settings() throws Exception {
		ConfigurationParameterSettings parameters = this.getTermSuiteTool().getSettings().getMetaData().getConfigurationParameterSettings();
        ConfigurationParameterSettings settings = UIMAFramework.getResourceSpecifierFactory().createConfigurationParameterSettings();
        settings.setParameterValue("Directory", (String) parameters.getParameterValue("OutputDirectory"));
		return settings;
	}

	@Override
	public String data() {
		ConfigurationParameterSettings parameters = this.getTermSuiteTool().getSettings().getMetaData().getConfigurationParameterSettings();
		return (String) parameters.getParameterValue("InputDirectory");
	}

	@Override
	public int input() {
		return TermSuiteRunner.XMI;
	}

	@Override
	public String language() {
		return null;
	}

	@Override
	public String encoding() {
		return null;
	}

	@Override
	public String get() throws Exception {
		return "eu/project/ttc/all/engines/XmiTsvConverter.xml";
	}

	@Override
	public void callBack(CAS cas) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void callBack() {
		// TODO Auto-generated method stub
		
	}

}

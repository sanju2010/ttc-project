package eu.project.ttc.tools.aligner;

import org.apache.uima.UIMAFramework;
import org.apache.uima.cas.CAS;
import org.apache.uima.resource.metadata.ConfigurationParameterSettings;

import eu.project.ttc.tools.TermSuiteEngine;
import eu.project.ttc.tools.TermSuiteRunner;
import eu.project.ttc.tools.TermSuiteTool;

public class AlignerEngine implements TermSuiteEngine {

	private TermSuiteTool tool;
	
	public void setTool(TermSuiteTool tool) {
		this.tool = tool;
	}
	
	private TermSuiteTool getTool() {
		return this.tool;
	}

	@Override
	public String get() throws Exception {
		return "eu/project/ttc/all/engines/aligner/Aligner.xml";
	}

	@Override
	public ConfigurationParameterSettings settings() throws Exception {
		ConfigurationParameterSettings parameters = this.getTool().getSettings().getMetaData().getConfigurationParameterSettings();
		ConfigurationParameterSettings settings = UIMAFramework.getResourceSpecifierFactory().createConfigurationParameterSettings();
		settings.setParameterValue("SourceLanguage",parameters.getParameterValue("SourceLanguage"));
		settings.setParameterValue("TargetLanguage",parameters.getParameterValue("TargetLanguage"));
		settings.setParameterValue("SimilarityDistanceClassName",parameters.getParameterValue("SimilarityDistanceClassName"));
		settings.setParameterValue("DictionaryFile",parameters.getParameterValue("DictionaryFile"));
		settings.setParameterValue("SourceTerminologyFile",parameters.getParameterValue("SourceTerminologyFile"));
		settings.setParameterValue("TargetTerminologyFile",parameters.getParameterValue("TargetTerminologyFile"));
		settings.setParameterValue("DistributionalMethod",parameters.getParameterValue("DistributionalMethod"));
		settings.setParameterValue("CompositionalMethod",parameters.getParameterValue("CompositionalMethod"));
		settings.setParameterValue("Action", "drop");
		return settings;
	}

	@Override
	public String data() {
		ConfigurationParameterSettings parameters = this.getTool().getSettings().getMetaData().getConfigurationParameterSettings();
		return (String) parameters.getParameterValue("InputDirectory");
	}

	@Override
	public int input() {
		return TermSuiteRunner.TXT;
	}

	@Override
	public String language() {
		return null;
	}

	@Override
	public String encoding() {
		return TermSuiteRunner.UTF8;
	}

	@Override
	public void callBack(CAS cas) throws Exception {
		this.getTool().getParent().getMixer().doLoad(cas.getJCas());
	}

	@Override
	public void callBack() { }
	
}

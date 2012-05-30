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
		ConfigurationParameterSettings advancedParameters = this.getTool().getAdvancedSettings().getMetaData().getConfigurationParameterSettings();
		ConfigurationParameterSettings settings = UIMAFramework.getResourceSpecifierFactory().createConfigurationParameterSettings();
		settings.setParameterValue("SourceLanguage",parameters.getParameterValue("SourceLanguage"));
		settings.setParameterValue("TargetLanguage",parameters.getParameterValue("TargetLanguage"));
		settings.setParameterValue("DictionaryFile",parameters.getParameterValue("BilingualDictionaryFile"));
		settings.setParameterValue("SourceTerminologyFile",parameters.getParameterValue("InputSourceTerminologyFile"));
		settings.setParameterValue("TargetTerminologyFile",parameters.getParameterValue("OutputTargetTerminologyFile"));
		settings.setParameterValue("Directory", parameters.getParameterValue("EvaluationInputOutputTranslationDirectory"));
		settings.setParameterValue("Action", "drop");
		settings.setParameterValue("SimilarityDistanceClassName",advancedParameters.getParameterValue("SimilarityDistanceClassName"));
		settings.setParameterValue("DistributionalMethod",advancedParameters.getParameterValue("DistributionalMethod") == null ? Boolean.TRUE : advancedParameters.getParameterValue("DistributionalMethod"));
		settings.setParameterValue("CompositionalMethod",advancedParameters.getParameterValue("CompositionalMethod") == null ? Boolean.TRUE : advancedParameters.getParameterValue("CompositionalMethod"));
		return settings;
	}

	@Override
	public String data() {
		ConfigurationParameterSettings parameters = this.getTool().getSettings().getMetaData().getConfigurationParameterSettings();
		return (String) parameters.getParameterValue("EvaluationInputOutputTranslationDirectory");
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
	
}

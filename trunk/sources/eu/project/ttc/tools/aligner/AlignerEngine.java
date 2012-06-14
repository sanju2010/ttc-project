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
		
		settings.setParameterValue(AlignerSettings.P_SOURCE_LANGUAGE,
				parameters.getParameterValue(AlignerSettings.P_SOURCE_LANGUAGE));
		settings.setParameterValue(AlignerSettings.P_TARGET_LANGUAGE,
				parameters.getParameterValue(AlignerSettings.P_TARGET_LANGUAGE));
		settings.setParameterValue("DictionaryFile", parameters
				.getParameterValue(AlignerSettings.P_BILINGUAL_DICTIONARY));
		settings.setParameterValue(AlignerSettings.P_SOURCE_TERMINOLOGY,
				parameters.getParameterValue(AlignerSettings.P_SOURCE_TERMINOLOGY));
		settings.setParameterValue(AlignerSettings.P_TARGET_TERMINOLOGY,
				parameters.getParameterValue(AlignerSettings.P_TARGET_TERMINOLOGY));
		settings.setParameterValue("Directory", parameters
				.getParameterValue(AlignerSettings.P_EVALUATION_DIRECTORY));
		settings.setParameterValue("Action", "drop");
		settings.setParameterValue(AlignerSettings.P_OUTPUT_DIRECTORY,
				parameters.getParameterValue(AlignerSettings.P_OUTPUT_DIRECTORY));

		settings.setParameterValue(
				AlignerAdvancedSettings.P_SIMILARITY_DISTANCE,
				advancedParameters
						.getParameterValue(AlignerAdvancedSettings.P_SIMILARITY_DISTANCE));
		settings.setParameterValue(
				AlignerAdvancedSettings.P_METHOD_DISTRIBUTIONAL,
				Boolean.TRUE.equals(advancedParameters
						.getParameterValue(AlignerAdvancedSettings.P_METHOD_DISTRIBUTIONAL)));
		settings.setParameterValue(
				AlignerAdvancedSettings.P_METHOD_COMPOSITIONAL,
				Boolean.TRUE.equals(advancedParameters
						.getParameterValue(AlignerAdvancedSettings.P_METHOD_COMPOSITIONAL)));

		return settings;
	}

	@Override
	public String data() {
		ConfigurationParameterSettings parameters = this.getTool().getSettings().getMetaData().getConfigurationParameterSettings();
		return (String) parameters.getParameterValue(AlignerSettings.P_EVALUATION_DIRECTORY);
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

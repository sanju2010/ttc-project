package eu.project.ttc.tools.indexer;

import java.util.Locale;

import org.apache.uima.UIMAFramework;
import org.apache.uima.cas.CAS;
import org.apache.uima.resource.metadata.ConfigurationParameterSettings;

import eu.project.ttc.tools.TermSuiteEngine;
import eu.project.ttc.tools.TermSuiteRunner;
import eu.project.ttc.tools.TermSuiteTool;

public class IndexerEngine implements TermSuiteEngine {

	private TermSuiteTool tool;
	
	public void setTool(TermSuiteTool tool) {
		this.tool = tool;
	}
	
	private TermSuiteTool getTool() {
		return this.tool;
	}
		
	public String get() throws Exception {
		ConfigurationParameterSettings parameters = this.getTool().getSettings().getMetaData().getConfigurationParameterSettings();
		String code = (String) parameters.getParameterValue("Language");
		String language = new Locale (code).getDisplayLanguage(Locale.ENGLISH);
		return "eu/project/ttc/" + language.toLowerCase() + "/engines/indexer/" + language + "Indexer.xml";
	}

	public ConfigurationParameterSettings settings() { 
		ConfigurationParameterSettings parameters = this.getTool().getSettings().getMetaData().getConfigurationParameterSettings();
		ConfigurationParameterSettings advancedParameters = this.getTool().getAdvancedSettings().getMetaData().getConfigurationParameterSettings();
		String code = (String) parameters.getParameterValue("Language");
        ConfigurationParameterSettings settings = UIMAFramework.getResourceSpecifierFactory().createConfigurationParameterSettings();
        settings.setParameterValue("Language", code);
        settings.setParameterValue("Directory", parameters.getParameterValue("OutputDirectory"));
        // settings.setParameterValue("File", (String) parameters.getParameterValue("TerminologyFile"));
        settings.setParameterValue("Action", "drop");
        /*
        settings.setParameterValue("MultiWordPatternRuleFile", (String) parameters.getParameterValue("MultiWordPatternRuleFile"));
        settings.setParameterValue("TermVariationRuleFile", (String) parameters.getParameterValue("TermVariationRuleFile"));
        settings.setParameterValue("NeoclassicalElementFile", (String) parameters.getParameterValue("NeoclassicalElementFile"));
        */
        settings.setParameterValue("Threshold", advancedParameters.getParameterValue("HapaxFilteringThreshold"));
        settings.setParameterValue("AssociationRateClassName", advancedParameters.getParameterValue("AssociationRateClassName"));
        settings.setParameterValue("EnableTermGathering", advancedParameters.getParameterValue("EnableTermConflating") == null ? Boolean.FALSE : advancedParameters.getParameterValue("EnableTermConflating"));
        settings.setParameterValue("EditDistanceClassName", advancedParameters.getParameterValue("EditDistanceClassName"));
        settings.setParameterValue("EditDistanceThreshold", advancedParameters.getParameterValue("EditDistanceThreshold"));
        settings.setParameterValue("EditDistanceNgrams", advancedParameters.getParameterValue("EditDistanceNgrams"));
        return settings;
	}

	@Override
	public void callBack(CAS cas) throws Exception {
		// this.getTool().getParent().getBanker().doLoad(cas.getJCas());	
	}

	@Override
	public String data() {
		ConfigurationParameterSettings parameters = this.getTool().getSettings().getMetaData().getConfigurationParameterSettings();
		return (String) parameters.getParameterValue("InputDirectory");
	}

	@Override
	public int input() {
		return TermSuiteRunner.XMI;
	}

	@Override
	public String language() {
		ConfigurationParameterSettings parameters = this.getTool().getSettings().getMetaData().getConfigurationParameterSettings();
		return (String) parameters.getParameterValue("Language");
	}

	@Override
	public String encoding() {
		return TermSuiteRunner.UTF8;
	}
	
}

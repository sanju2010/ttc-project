package eu.project.ttc.tools.indexer;

import java.util.Locale;

import org.apache.uima.UIMAFramework;
import org.apache.uima.cas.CAS;
import org.apache.uima.resource.metadata.ConfigurationParameterSettings;

import eu.project.ttc.tools.TermSuiteEngine;
import eu.project.ttc.tools.TermSuiteRunner;

public class IndexerEngine implements TermSuiteEngine {

	private Indexer acabit;
	
	public void setAcabit(Indexer acabit) {
		this.acabit = acabit;
	}
	
	private Indexer getAcabit() {
		return this.acabit;
	}
		
	public String get() throws Exception {
		ConfigurationParameterSettings parameters = this.getAcabit().getSettings().getMetaData().getConfigurationParameterSettings();
		String code = (String) parameters.getParameterValue("Language");
		String language = new Locale (code).getDisplayLanguage(Locale.ENGLISH);
		return "eu/project/ttc/" + language.toLowerCase() + "/engines/indexer/" + language + "Indexer.xml";
	}

	public ConfigurationParameterSettings settings() { 
		ConfigurationParameterSettings parameters = this.getAcabit().getSettings().getMetaData().getConfigurationParameterSettings();
		String code = (String) parameters.getParameterValue("Language");
        ConfigurationParameterSettings settings = UIMAFramework.getResourceSpecifierFactory().createConfigurationParameterSettings();
        settings.setParameterValue("Language", code);
        settings.setParameterValue("Directory", (String) parameters.getParameterValue("OutputDirectory"));
        // settings.setParameterValue("File", (String) parameters.getParameterValue("TerminologyFile"));
        settings.setParameterValue("Type", (String) parameters.getParameterValue("TermAnnotationType"));
        settings.setParameterValue("Action", "drop");
        /*
        settings.setParameterValue("MultiWordPatternRuleFile", (String) parameters.getParameterValue("MultiWordPatternRuleFile"));
        settings.setParameterValue("TermVariationRuleFile", (String) parameters.getParameterValue("TermVariationRuleFile"));
        settings.setParameterValue("NeoclassicalElementFile", (String) parameters.getParameterValue("NeoclassicalElementFile"));
        */
        settings.setParameterValue("AssociationRateClassName", (String) parameters.getParameterValue("AssociationRateClassName"));
        settings.setParameterValue("EditDistanceClassName", (String) parameters.getParameterValue("EditDistanceClassName"));
        settings.setParameterValue("EnableTermGathering", (Boolean) parameters.getParameterValue("EnableTermGathering"));
        settings.setParameterValue("EditDistanceThreshold", (Float) parameters.getParameterValue("EditDistanceThreshold"));
        settings.setParameterValue("EditDistanceNgrams", (Integer) parameters.getParameterValue("EditDistanceNgrams"));
        return settings;
	}

	@Override
	public void callBack(CAS cas) throws Exception {
		this.getAcabit().getParent().getBanker().doLoad(cas.getJCas());	
	}

	@Override
	public void callBack() { }

	@Override
	public String data() {
		ConfigurationParameterSettings parameters = this.getAcabit().getSettings().getMetaData().getConfigurationParameterSettings();
		return (String) parameters.getParameterValue("InputDirectory");
	}

	@Override
	public int input() {
		return TermSuiteRunner.XMI;
	}

	@Override
	public String language() {
		ConfigurationParameterSettings parameters = this.getAcabit().getSettings().getMetaData().getConfigurationParameterSettings();
		return (String) parameters.getParameterValue("Language");
	}

	@Override
	public String encoding() {
		return TermSuiteRunner.UTF8;
	}
	
}

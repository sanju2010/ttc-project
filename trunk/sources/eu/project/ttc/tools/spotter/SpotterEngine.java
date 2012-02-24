package eu.project.ttc.tools.spotter;

import java.util.Locale;

import org.apache.uima.UIMAFramework;
import org.apache.uima.cas.CAS;
import org.apache.uima.resource.metadata.ConfigurationParameterSettings;

import eu.project.ttc.tools.TermSuiteEngine;
import eu.project.ttc.tools.TermSuiteRunner;
import fr.free.rocheteau.jerome.dunamis.models.ProcessingResult;

public class SpotterEngine implements TermSuiteEngine {

	private Spotter treeTagger;
	
	public void setTagger(Spotter treeTagger) {
		this.treeTagger = treeTagger;
	}
	
	private Spotter getTreeTagger() {
		return this.treeTagger;
	}
		
	@Override
	public void callBack(CAS cas) throws Exception {
		ProcessingResult result = new ProcessingResult();
		result.setCas(cas);
		this.getTreeTagger().getParent().getViewer().getResultModel().addElement(result);
	}

	@Override
	public void callBack() { }

	@Override
	public String get() throws Exception {
		ConfigurationParameterSettings parameters = this.getTreeTagger().getSettings().getMetaData().getConfigurationParameterSettings();
		String code = (String) parameters.getParameterValue("Language");
		String language = new Locale (code).getDisplayLanguage(Locale.ENGLISH);
		return "eu/project/ttc/" + language.toLowerCase() + "/engines/spotter/" + language + "Spotter.xml";
	}

	@Override
	public ConfigurationParameterSettings settings() throws Exception {
		ConfigurationParameterSettings parameters = this.getTreeTagger().getSettings().getMetaData().getConfigurationParameterSettings();
        ConfigurationParameterSettings settings = UIMAFramework.getResourceSpecifierFactory().createConfigurationParameterSettings();
		settings.setParameterValue("TreeTaggerHomeDirectory", (String) parameters.getParameterValue("TreeTaggerHomeDirectory"));
        settings.setParameterValue("Directory", (String) parameters.getParameterValue("OutputDirectory"));
        /*
        settings.setParameterValue("TreeTaggerParameterFile", (String) parameters.getParameterValue("TreeTaggerParameterFile"));
        settings.setParameterValue("SegmentFile", (String) parameters.getParameterValue("SegmentFile"));
        settings.setParameterValue("CategoryMappingFile", (String) parameters.getParameterValue("CategoryMappingFile"));
        settings.setParameterValue("SubcategoryMappingFile", (String) parameters.getParameterValue("SubcategoryMappingFile"));
        settings.setParameterValue("MoodMappingFile", (String) parameters.getParameterValue("MoodMappingFile"));
        settings.setParameterValue("TenseMappingFile", (String) parameters.getParameterValue("TenseMappingFile"));
        settings.setParameterValue("GenderMappingFile", (String) parameters.getParameterValue("GenderMappingFile"));
        settings.setParameterValue("NumberMappingFile", (String) parameters.getParameterValue("NumberMappingFile"));
        settings.setParameterValue("CaseMappingFile", (String) parameters.getParameterValue("CaseMappingFile"));
        */
        return settings;
	}

	@Override
	public String data() {
		ConfigurationParameterSettings parameters = this.getTreeTagger().getSettings().getMetaData().getConfigurationParameterSettings();
		return (String) parameters.getParameterValue("InputDirectory");
	}

	@Override
	public int input() {
		return TermSuiteRunner.TXT;
	}

	@Override
	public String language() {
		ConfigurationParameterSettings parameters = this.getTreeTagger().getSettings().getMetaData().getConfigurationParameterSettings();
		return (String) parameters.getParameterValue("Language");
	}

	@Override
	public String encoding() {
		return TermSuiteRunner.UTF8;
	}

}

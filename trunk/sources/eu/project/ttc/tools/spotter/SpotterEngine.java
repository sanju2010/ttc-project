package eu.project.ttc.tools.spotter;

import java.util.Locale;

import org.apache.uima.UIMAFramework;
import org.apache.uima.cas.CAS;
import org.apache.uima.resource.metadata.ConfigurationParameterSettings;

import eu.project.ttc.tools.TermSuiteEngine;
import eu.project.ttc.tools.TermSuiteRunner;
import eu.project.ttc.tools.TermSuiteTool;
import fr.free.rocheteau.jerome.dunamis.models.ProcessingResult;

public class SpotterEngine implements TermSuiteEngine {

	private TermSuiteTool tool;
	
	public void setTool(TermSuiteTool tool) {
		this.tool = tool;
	}
	
	private TermSuiteTool getTool() {
		return this.tool;
	}
		
	@Override
	public void callBack(CAS cas) throws Exception {
		ProcessingResult result = new ProcessingResult();
		result.setCas(cas);
		this.getTool().getParent().getViewer().getResultModel().addElement(result);
	}

	@Override
	public void callBack() { }

	@Override
	public String get() throws Exception {
		ConfigurationParameterSettings parameters = this.getTool().getSettings().getMetaData().getConfigurationParameterSettings();
		String code = (String) parameters.getParameterValue("Language");
		String language = new Locale (code).getDisplayLanguage(Locale.ENGLISH);
		return "eu/project/ttc/" + language.toLowerCase() + "/engines/spotter/" + language + "Spotter.xml";
	}

	@Override
	public ConfigurationParameterSettings settings() throws Exception {
		ConfigurationParameterSettings parameters = this.getTool().getSettings().getMetaData().getConfigurationParameterSettings();
        ConfigurationParameterSettings settings = UIMAFramework.getResourceSpecifierFactory().createConfigurationParameterSettings();
		settings.setParameterValue("TreeTaggerHomeDirectory", (String) parameters.getParameterValue("TreeTaggerHomeDirectory"));
        settings.setParameterValue("Directory", (String) parameters.getParameterValue("OutputDirectory"));
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
		ConfigurationParameterSettings parameters = this.getTool().getSettings().getMetaData().getConfigurationParameterSettings();
		return (String) parameters.getParameterValue("Language");
	}

	@Override
	public String encoding() {
		return TermSuiteRunner.UTF8;
	}

}

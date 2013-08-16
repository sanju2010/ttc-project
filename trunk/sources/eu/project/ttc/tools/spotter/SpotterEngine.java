package eu.project.ttc.tools.spotter;

import eu.project.ttc.tools.commons.ToolController;
import org.apache.uima.cas.CAS;

import eu.project.ttc.tools.commons.TermSuiteEngine;

@Deprecated
public class SpotterEngine implements TermSuiteEngine {

	private ToolController tool;
	
	public void setTool(ToolController tool) {
		this.tool = tool;
	}
	
	private ToolController getTool() {
		return this.tool;
	}

	public void callBack(CAS cas) throws Exception {
		ProcessingResult result = new ProcessingResult();
		result.setCas(cas);
		// FIXME this.getTool().getParent().getViewer().getResultModel().addElement(result);
	}

//	public String getEngineDescriptor() throws Exception {
//		ConfigurationParameterSettings parameters = this.getTool().getSettings().getMetaData().getConfigurationParameterSettings();
//		String code = (String) parameters.getParameterValue(SpotterModel.P_SOURCE_LANGUAGE);
//		String language = new Locale (code).getDisplayLanguage(Locale.ENGLISH);
//		return "eu/project/ttc/" + language.toLowerCase() + "/engines/spotter/" + language + "Spotter.xml";
//	}
//
//	public ConfigurationParameterSettings getAESettings() throws Exception {
//		ConfigurationParameterSettings parameters = this.getTool().getSettings().getMetaData().getConfigurationParameterSettings();
//        ConfigurationParameterSettings settings = UIMAFramework.getResourceSpecifierFactory().createConfigurationParameterSettings();
//
//		File treeTaggerHomeDirectory = new File((String)(parameters.getParameterValue(SpotterModel.P_TREETAGGER_HOME_DIRECTORY)));
//        if (treeTaggerHomeDirectory.exists()) {
//	        settings.setParameterValue(SpotterModel.P_TREETAGGER_HOME_DIRECTORY,
//	        	(String) parameters.getParameterValue(SpotterModel.P_TREETAGGER_HOME_DIRECTORY));
//		} else {
//			throw new FileNotFoundException("unable to find TreeTagger home directory : "+
//					parameters.getParameterValue(SpotterModel.P_TREETAGGER_HOME_DIRECTORY));
//		}
//        settings.setParameterValue("Directory", (String) parameters.getParameterValue(SpotterModel.P_OUTPUT_DIRECTORY));
//        return settings;
//	}
//
//	public InputSource getInputSource() {
//		ConfigurationParameterSettings parameters = this.getTool().getSettings().getMetaData().getConfigurationParameterSettings();
//        String dir = (String) parameters.getParameterValue(SpotterModel.P_INPUT_DIRECTORY);
//        return new InputSource(dir, InputSource.InputSourceTypes.TXT);
//	}

    // FIXME
    public String language() {
        return "fr";
    }
//	public String language() {
//		ConfigurationParameterSettings parameters = this.getTool().getSettings().getMetaData().getConfigurationParameterSettings();
//		return (String) parameters.getParameterValue(SpotterModel.P_SOURCE_LANGUAGE);
//	}

	public String encoding() {
		return TermSuiteEngine.DEFAULT_ENCODING;
	}

}

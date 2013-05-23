package eu.project.ttc.tools.indexer;

import java.util.Locale;

import eu.project.ttc.tools.config.IndexerSettings;
import org.apache.uima.UIMAFramework;
import org.apache.uima.cas.CAS;
import org.apache.uima.resource.metadata.ConfigurationParameterSettings;

import eu.project.ttc.tools.InputSourceTypes;
import eu.project.ttc.tools.TermSuiteEngine;
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
		Indexer indexer=(Indexer)this.getTool();
		
		ConfigurationParameterSettings parameters = indexer.getSettings().getMetaData().getConfigurationParameterSettings();
		//ConfigurationParameterSettings advancedParameters = indexer.getSettings().getMetaData().getConfigurationParameterSettings();
		String code = (String) parameters.getParameterValue("Language");


		ConfigurationParameterSettings settings = UIMAFramework.getResourceSpecifierFactory().createConfigurationParameterSettings();
        settings.setParameterValue("Language", code);
        settings.setParameterValue("Directory", parameters.getParameterValue("OutputDirectory"));
		settings.setParameterValue("Action", "drop");

		
//		// TBX settings
//		ConfigurationParameterSettings tbxParameters = indexer.getTBXSettings().getMetaData().getConfigurationParameterSettings();
		settings.setParameterValue(IndexerSettings.P_KEEP_VERBS,
                Boolean.TRUE.equals(parameters.getParameterValue(IndexerSettings.P_KEEP_VERBS)));
		settings.setParameterValue(IndexerSettings.P_FILTER_RULE,
                parameters.getParameterValue(IndexerSettings.P_FILTER_RULE));
		Object threshold = parameters.getParameterValue(IndexerSettings.P_FILTERING_THRESHOLD);
		settings.setParameterValue(IndexerSettings.P_FILTERING_THRESHOLD, threshold == null ? 0.0 : threshold);
		settings.setParameterValue(IndexerSettings.P_ENABLE_TSV,
				Boolean.TRUE.equals(parameters.getParameterValue(IndexerSettings.P_ENABLE_TSV)));

        // settings.setParameterValue("File", (String) parameters.getParameterValue("TerminologyFile"));
        // settings.setParameterValue("MultiWordPatternRuleFile", (String) parameters.getParameterValue("MultiWordPatternRuleFile"));
        // settings.setParameterValue("TermVariationRuleFile", (String) parameters.getParameterValue("TermVariationRuleFile"));
        // settings.setParameterValue("NeoclassicalElementFile", (String) parameters.getParameterValue("NeoclassicalElementFile"));
        
        
		// Context vector settings
		settings.setParameterValue(IndexerSettings.P_FREQUENCY_THRESHOLD,
                parameters.getParameterValue(IndexerSettings.P_FREQUENCY_THRESHOLD));
        settings.setParameterValue("AssociationRateClassName",
                parameters.getParameterValue(IndexerSettings.P_ASSOCIATION_MEASURE));
       
        // Conflation parameters
        settings.setParameterValue("EnableTermGathering",
                Boolean.TRUE.equals(parameters.getParameterValue(IndexerSettings.P_TERM_VARIANT_DETECTION)));
        settings.setParameterValue(IndexerSettings.P_EDIT_DISTANCE_CLASS,
                parameters.getParameterValue(IndexerSettings.P_EDIT_DISTANCE_CLASS));
        settings.setParameterValue(IndexerSettings.P_EDIT_DISTANCE_THRESHOLD,
                parameters.getParameterValue(IndexerSettings.P_EDIT_DISTANCE_THRESHOLD));
        settings.setParameterValue(IndexerSettings.P_EDIT_DISTANCE_NGRAMS,
                parameters.getParameterValue(IndexerSettings.P_EDIT_DISTANCE_NGRAMS));

        // Addendum Sebastian Peña
        settings.setParameterValue(IndexerSettings.P_IGNORE_DIACRITICS,
        		Boolean.valueOf(Boolean.TRUE.equals(parameters.getParameterValue(IndexerSettings.P_IGNORE_DIACRITICS))));
        
        return settings;
	}

	public void callBack(CAS cas) throws Exception {
		// this.getTool().getParent().getBanker().doLoad(cas.getJCas());	
	}

	public String data() {
		ConfigurationParameterSettings parameters = this.getTool().getSettings().getMetaData().getConfigurationParameterSettings();
		return (String) parameters.getParameterValue("InputDirectory");
	}

	public InputSourceTypes input() {
		return InputSourceTypes.XMI;
	}

	public String language() {
		ConfigurationParameterSettings parameters = this.getTool().getSettings().getMetaData().getConfigurationParameterSettings();
		return (String) parameters.getParameterValue("Language");
	}

	public String encoding() {
		return TermSuiteEngine.DEFAULT_ENCODING;
	}
	
}

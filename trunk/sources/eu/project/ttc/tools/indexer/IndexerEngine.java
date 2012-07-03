package eu.project.ttc.tools.indexer;

import java.util.Locale;

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
		ConfigurationParameterSettings advancedParameters = indexer.getAdvancedSettings().getMetaData().getConfigurationParameterSettings();
		String code = (String) parameters.getParameterValue("Language");


		ConfigurationParameterSettings settings = UIMAFramework.getResourceSpecifierFactory().createConfigurationParameterSettings();
        settings.setParameterValue("Language", code);
        settings.setParameterValue("Directory", parameters.getParameterValue("OutputDirectory"));
		settings.setParameterValue("Action", "drop");

		
		// TBX settings
		ConfigurationParameterSettings tbxParameters = indexer.getTBXSettings().getMetaData().getConfigurationParameterSettings();
		settings.setParameterValue(TBXSettings.P_KEEP_VERBS, Boolean.TRUE.equals(tbxParameters.getParameterValue(TBXSettings.P_KEEP_VERBS)));
		settings.setParameterValue(TBXSettings.P_FILTER_RULE, tbxParameters.getParameterValue(TBXSettings.P_FILTER_RULE));
		Object threshold = tbxParameters.getParameterValue(TBXSettings.P_FILTERING_THRESHOLD);
		settings.setParameterValue(TBXSettings.P_FILTERING_THRESHOLD, threshold == null ? 0 : threshold);

        // settings.setParameterValue("File", (String) parameters.getParameterValue("TerminologyFile"));
        // settings.setParameterValue("MultiWordPatternRuleFile", (String) parameters.getParameterValue("MultiWordPatternRuleFile"));
        // settings.setParameterValue("TermVariationRuleFile", (String) parameters.getParameterValue("TermVariationRuleFile"));
        // settings.setParameterValue("NeoclassicalElementFile", (String) parameters.getParameterValue("NeoclassicalElementFile"));
        
        
		// Context vector settings
		settings.setParameterValue(IndexerAdvancedSettings.P_FREQUENCY_THRESHOLD, advancedParameters.getParameterValue(IndexerAdvancedSettings.P_FREQUENCY_THRESHOLD));
        settings.setParameterValue("AssociationRateClassName", advancedParameters.getParameterValue(IndexerAdvancedSettings.P_ASSOCIATION_MEASURE));
       
        // Conflation parameters
        settings.setParameterValue("EnableTermGathering", Boolean.TRUE.equals(advancedParameters.getParameterValue(IndexerAdvancedSettings.P_TERM_VARIANT_DETECTION)));
        settings.setParameterValue(IndexerAdvancedSettings.P_EDIT_DISTANCE_CLASS, advancedParameters.getParameterValue(IndexerAdvancedSettings.P_EDIT_DISTANCE_CLASS));
        settings.setParameterValue(IndexerAdvancedSettings.P_EDIT_DISTANCE_THRESHOLD, advancedParameters.getParameterValue(IndexerAdvancedSettings.P_EDIT_DISTANCE_THRESHOLD));
        settings.setParameterValue(IndexerAdvancedSettings.P_EDIT_DISTANCE_NGRAMS, advancedParameters.getParameterValue(IndexerAdvancedSettings.P_EDIT_DISTANCE_NGRAMS));

        // Addendum Sebastian Peña
        settings.setParameterValue(IndexerAdvancedSettings.P_IGNORE_DIACRITICS, 
        		Boolean.valueOf(Boolean.TRUE.equals(advancedParameters.getParameterValue(IndexerAdvancedSettings.P_IGNORE_DIACRITICS))));
        
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
	public InputSourceTypes input() {
		return InputSourceTypes.XMI;
	}

	@Override
	public String language() {
		ConfigurationParameterSettings parameters = this.getTool().getSettings().getMetaData().getConfigurationParameterSettings();
		return (String) parameters.getParameterValue("Language");
	}

	@Override
	public String encoding() {
		return TermSuiteEngine.DEFAULT_ENCODING;
	}
	
}

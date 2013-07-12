package eu.project.ttc.tools.aligner;

import java.io.File;
import java.io.FileNotFoundException;

import eu.project.ttc.tools.InputSource;
import eu.project.ttc.tools.InputSource.InputSourceTypes;
import eu.project.ttc.tools.ToolController;
import eu.project.ttc.tools.config.AlignerSettings;
import org.apache.uima.UIMAFramework;
import org.apache.uima.cas.CAS;
import org.apache.uima.resource.metadata.ConfigurationParameterSettings;

import eu.project.ttc.tools.TermSuiteEngine;

public class AlignerEngine implements TermSuiteEngine {

	/** Default candidate count */
	public static final int DEFAULT_MAX_TRANSLATION_CANDIDATES = 100;
	
	private ToolController tool;
	
	public void setTool(ToolController tool) {
		this.tool = tool;
	}
	
	private ToolController getTool() {
		return this.tool;
	}

	public String getEngineDescriptor() throws Exception {
		return "eu/project/ttc/all/engines/aligner/Aligner.xml";
	}

//	public ConfigurationParameterSettings getAESettings() throws Exception {
//		ConfigurationParameterSettings parameters = this.getTool().getSettings().getMetaData().getConfigurationParameterSettings();
//		ConfigurationParameterSettings settings = UIMAFramework.getResourceSpecifierFactory().createConfigurationParameterSettings();
//
//		settings.setParameterValue(AlignerSettings.P_SOURCE_LANGUAGE,
//				parameters.getParameterValue(AlignerSettings.P_SOURCE_LANGUAGE));
//		settings.setParameterValue(AlignerSettings.P_TARGET_LANGUAGE,
//				parameters.getParameterValue(AlignerSettings.P_TARGET_LANGUAGE));
//
//		File dictionaryFile = new File((String)(parameters.getParameterValue(AlignerSettings.P_BILINGUAL_DICTIONARY)));
//		if (dictionaryFile.exists()) {
//			settings.setParameterValue("DictionaryFile", parameters
//				.getParameterValue(AlignerSettings.P_BILINGUAL_DICTIONARY));
//		} else {
//			throw new FileNotFoundException("unable to find dictionary file : "+
//					parameters.getParameterValue(AlignerSettings.P_BILINGUAL_DICTIONARY));
//		}
//
//		File sourceTerminologyFile = new File((String)(parameters.getParameterValue(AlignerSettings.P_SOURCE_TERMINOLOGY)));
//		if (sourceTerminologyFile.exists()) {
//		settings.setParameterValue(AlignerSettings.P_SOURCE_TERMINOLOGY,
//				parameters.getParameterValue(AlignerSettings.P_SOURCE_TERMINOLOGY));
//		} else {
//			throw new FileNotFoundException("unable to find source terminology file : "+
//					parameters.getParameterValue(AlignerSettings.P_SOURCE_TERMINOLOGY));
//		}
//
//		File targetTerminologyFile = new File((String)(parameters.getParameterValue(AlignerSettings.P_TARGET_TERMINOLOGY)));
//		if (targetTerminologyFile.exists()) {
//		settings.setParameterValue(AlignerSettings.P_TARGET_TERMINOLOGY,
//				parameters.getParameterValue(AlignerSettings.P_TARGET_TERMINOLOGY));
//		} else {
//			throw new FileNotFoundException("unable to find target terminology file : "+
//					parameters.getParameterValue(AlignerSettings.P_TARGET_TERMINOLOGY));
//		}
//
//
//		settings.setParameterValue("Directory", parameters
//				.getParameterValue(AlignerSettings.P_EVALUATION_DIRECTORY));
//		settings.setParameterValue("Action", "drop");
//
//		File outputDirectory = new File((String)(parameters.getParameterValue(AlignerSettings.P_OUTPUT_DIRECTORY)));
//		if (outputDirectory.exists()) {
//		settings.setParameterValue(AlignerSettings.P_OUTPUT_DIRECTORY,
//				parameters.getParameterValue(AlignerSettings.P_OUTPUT_DIRECTORY));
//		} else {
//			throw new FileNotFoundException("unable to find outputDirectory : "+
//					parameters.getParameterValue(AlignerSettings.P_OUTPUT_DIRECTORY));
//		}
//
//		settings.setParameterValue(
//				AlignerSettings.P_SIMILARITY_DISTANCE,
//				parameters.getParameterValue(AlignerSettings.P_SIMILARITY_DISTANCE));
//		settings.setParameterValue(
//				AlignerSettings.P_METHOD_DISTRIBUTIONAL,
//				Boolean.TRUE.equals(parameters.getParameterValue(AlignerSettings.P_METHOD_DISTRIBUTIONAL)));
//		settings.setParameterValue(
//				AlignerSettings.P_METHOD_COMPOSITIONAL,
//				Boolean.TRUE.equals(parameters.getParameterValue(AlignerSettings.P_METHOD_COMPOSITIONAL)));
//
//		Object candidates = parameters.getParameterValue(AlignerSettings.P_MAX_CANDIDATES);
//		settings.setParameterValue(AlignerSettings.P_MAX_CANDIDATES,
//				candidates == null ? DEFAULT_MAX_TRANSLATION_CANDIDATES : candidates);
//
//		return settings;
//	}
//
//	public InputSource getInputSource() {
//		ConfigurationParameterSettings parameters = this.getTool().getSettings().getMetaData().getConfigurationParameterSettings();
//		return (String) parameters.getParameterValue(AlignerSettings.P_EVALUATION_DIRECTORY);
//	}

	public InputSourceTypes input() {
		return InputSourceTypes.TXT;
	}

	public String language() {
		return null;
	}

	public String encoding() {
		return TermSuiteEngine.DEFAULT_ENCODING;
	}

	public void callBack(CAS cas) throws Exception {
		this.getTool().getParent().getMixer().doLoad(cas.getJCas());
	}
	
}


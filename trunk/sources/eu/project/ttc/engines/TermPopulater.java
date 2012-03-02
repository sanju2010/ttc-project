package eu.project.ttc.engines;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.jcas.JCas;

import org.apache.uima.resource.ResourceInitializationException;

import eu.project.ttc.types.TermAnnotation;
import eu.project.ttc.types.TranslationAnnotation;

public class TermPopulater extends JCasAnnotator_ImplBase {
	
	private String sourceLanguage;
	
	private void setSourceLanguage(String language) {
		this.sourceLanguage = language;
	}
	
	private String getSourceLanguage() {
		return this.sourceLanguage;
	}
	
	private String targetLanguage;
	
	private void setTargetLanguage(String language) {
		this.targetLanguage = language;
	}
	
	private String getTargetLanguage() {
		return this.targetLanguage;
	}
	
	@Override
	public void initialize(UimaContext context) throws ResourceInitializationException {
		super.initialize(context);
		try {
			if (this.getSourceLanguage() == null) {
				String language = (String) context.getConfigParameterValue("SourceLanguage");
				this.setSourceLanguage(language);
			}
			if (this.getTargetLanguage() == null) {
				String language = (String) context.getConfigParameterValue("TargetLanguage");
				this.setTargetLanguage(language);
			}
		} catch (Exception e) {
			throw new ResourceInitializationException(e);
		}
	}
	
	@Override
	public void process(JCas cas) throws AnalysisEngineProcessException {
		try {
			cas.setDocumentLanguage(this.getSourceLanguage());
			String text = cas.getDocumentText();
			Pattern pattern = Pattern.compile("\\t");
			Matcher matcher = pattern.matcher(text);
			boolean index = true;
			int begin = 0;
			int end = 0;
			while (matcher.find()) {
				end = matcher.start();
				if (index) {
					index = false;
					TermAnnotation annotation = new TermAnnotation(cas, begin, end);
					annotation.addToIndexes();
				} else {
					TranslationAnnotation annotation = new TranslationAnnotation(cas, begin, end);
					annotation.setLanguage(this.getTargetLanguage());
					annotation.addToIndexes();
				}
				begin = matcher.end();
			}
		} catch (Exception e) {
			throw new AnalysisEngineProcessException(e);
		}
	}
	
}

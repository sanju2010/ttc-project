package eu.project.ttc.engines;

import java.util.Scanner;

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
			Scanner scanner = new Scanner(text);
			String delimiter = System.getProperty("line.separator");
			scanner.useDelimiter(delimiter);
			int begin = 0;
			int end = 0;
			StringBuilder builder = new StringBuilder();
			while (scanner.hasNext()) {
				String line = scanner.next();
				builder.append(line);
				builder.append(delimiter);
				String[] items = line.split("\t");
				TermAnnotation term = null;
				for (int index = 0; index < items.length; index++) {
					String word = items[index];
					end = begin + word.length();
					if (index == 0) {
						TermAnnotation annotation = new TermAnnotation(cas, begin, end);
						annotation.addToIndexes();
						term = annotation;
					} else {
						TranslationAnnotation annotation = new TranslationAnnotation(cas, begin, end);
						annotation.setTerm(term);
						annotation.setLanguage(this.getTargetLanguage());
						annotation.addToIndexes();
					}
					begin = end + 1;
				}
				begin = builder.length();
			}
		} catch (Exception e) {
			throw new AnalysisEngineProcessException(e);
		}
	}
	
}

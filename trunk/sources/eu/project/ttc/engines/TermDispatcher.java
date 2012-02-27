package eu.project.ttc.engines;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;

import org.apache.uima.UIMAFramework;
import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.JCasMultiplier_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.AbstractCas;
import org.apache.uima.cas.CAS;
import org.apache.uima.cas.Feature;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.text.AnnotationIndex;
import org.apache.uima.examples.SourceDocumentInformation;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;

import org.apache.uima.resource.ResourceInitializationException;

import org.apache.uima.util.Level;

import eu.project.ttc.types.TranslationAnnotation;

public class TermDispatcher extends JCasMultiplier_ImplBase {

	private Iterator<Entry<String, Set<String>>> iterator;
		
	private void setIterator() {
		this.iterator = this.getEntries().entrySet().iterator();
	}
	
	private void scan(String text) throws Exception {
		Scanner scanner = new Scanner(text);
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			String[] items = line.split("\t");
			String source = items[0];
			String target = items[1];
			if (source != null) {
				Set<String> targets = this.getEntries().get(source);
				if (targets == null) {
					targets = new HashSet<String>();
					this.getEntries().put(source, targets);
				}		
				if (target != null) {
					targets.add(target);
				}
			}
		}
	}
	
	private Iterator<Entry<String, Set<String>>> getIterator() {
		return this.iterator;
	}
	
	private Map<String, Set<String>> entries;
	
	private void setEntries() {
		this.entries = new HashMap<String, Set<String>>();
	}

	private Map<String, Set<String>> getEntries() {
		return this.entries;
	}
	
	private String feature;
	
	private void setFeature(String feature) {
		this.feature = feature;
	}
	
	private String getFeature() {
		return this.feature;
	}
	
	private Feature getFeature(JCas cas) throws Exception {
		String[] items = this.feature.split(":");
		if (items.length == 2) {
			Type type = cas.getRequiredType(items[0]); 
			return cas.getRequiredFeature(type, items[1]);
		} else {
			throw new Exception(this.feature);
		}
	}
	
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
			if (this.getEntries() == null) {
				this.setEntries();
			}
			if (this.getFeature() == null) {
				String feature = (String) context.getConfigParameterValue("Feature");
				this.setFeature(feature);
			}
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
	
	private boolean retrieve(JCas cas) throws Exception {
		Feature feature = this.getFeature(cas);
		String range = feature.getRange().getName();
		String bool = CAS.TYPE_NAME_BOOLEAN;
		if (range.equals(bool)) {
			AnnotationIndex<Annotation> index = cas.getAnnotationIndex(feature.getDomain());
			Iterator<Annotation> iterator = index.iterator();
			if (iterator.hasNext()) {
				Annotation annotation = iterator.next();
				return annotation.getBooleanValue(feature);
			} else {
				throw new Exception("No annotation of type " + feature.getDomain().getName());
			}
		} else {
			throw new Exception(this.feature + " should rande over " + bool + " but ranges over " + range);
		}
	}
	
	@Override
	public void process(JCas cas) throws AnalysisEngineProcessException {
		try {
			String text = cas.getDocumentText();
			this.scan(text);
			if (this.retrieve(cas)) {
				this.setIterator();
			}
		} catch (Exception e) {
			throw new AnalysisEngineProcessException(e);
		}
	}

	@Override
	public boolean hasNext() {
		if (this.getIterator() == null) {
			return false;
		} else {
			return this.getIterator().hasNext();
		}
	}
	
	@Override
	public AbstractCas next() throws AnalysisEngineProcessException {
		try {
			JCas cas = this.getEmptyJCas();
			Entry<String, Set<String>> entry = this.getIterator().next();
			cas.setDocumentLanguage(this.getSourceLanguage());
			cas.setDocumentText(entry.getKey());
			int begin = 0;
			int end = cas.getDocumentText().length();

			for (String value : entry.getValue()) {
				TranslationAnnotation annotation = new TranslationAnnotation(cas, begin, end);
				annotation.setTerm(value);
				annotation.setLanguage(this.getTargetLanguage());
				annotation.addToIndexes();
			}
			
			SourceDocumentInformation sdi = new SourceDocumentInformation(cas, begin, end);
			sdi.setDocumentSize(end);
			sdi.setLastSegment(!this.hasNext());
			sdi.setOffsetInSource(0);
			sdi.setUri("http://" + cas.getDocumentText() + ".term");
			sdi.addToIndexes();
			UIMAFramework.getLogger().log(Level.INFO,"Processing " + cas.getDocumentText());
			return cas;
		} catch (Exception e) {
			throw new AnalysisEngineProcessException(e);
		}
	}
	
}

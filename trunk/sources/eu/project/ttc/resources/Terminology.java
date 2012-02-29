package eu.project.ttc.resources;

import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.uima.UIMAFramework;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.cas.CAS;
import org.apache.uima.cas.CASException;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.cas.impl.XmiCasDeserializer;
import org.apache.uima.cas.text.AnnotationIndex;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.DataResource;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.SharedResourceObject;
import org.apache.uima.util.CasCreationUtils;
import org.apache.uima.util.Level;
import org.apache.uima.util.XMLInputSource;
import org.apache.uima.util.XMLParser;

import eu.project.ttc.models.Context;
import eu.project.ttc.types.TermAnnotation;

public class Terminology implements SharedResourceObject {

	private Map<String, TermAnnotation> annotations;
	
	private void annotations() throws Exception {
		this.annotations = new HashMap<String, TermAnnotation>();
		this.occurences = new HashMap<String, Integer>();
		AnnotationIndex<Annotation> index = this.terminology.getJCas().getAnnotationIndex(TermAnnotation.type);
		FSIterator<Annotation> iterator = index.iterator();
		while (iterator.hasNext()) {
			TermAnnotation annotation = (TermAnnotation) iterator.next();
			this.annotations.put(annotation.getCoveredText(), annotation);
			this.occurences.put(annotation.getCoveredText(), annotation.getOccurrences());
		}
	}
	
	public TermAnnotation get(String term) {
		return this.annotations.get(term);
	}
	
	private Set<String> identifiers;
	
	private CAS terminology;

	public void load(String path) throws Exception {
		if (this.terminology != null && !this.identifiers.contains(path) && this.annotations == null) {
			this.identifiers.add(path);
			UIMAFramework.getLogger().log(Level.INFO, "Loading " + path);
			InputStream stream = new FileInputStream(path);
			XmiCasDeserializer.deserialize(stream, this.terminology);
			this.annotations();
			this.contexts();
		}
	}

	public JCas getJCas() throws CASException {
		return this.terminology.getJCas();
	}
	
	public boolean exists(String term) {
		return this.annotations.get(term) != null;
	}
	
	public Set<String> terms() {
		return this.contexts.keySet();
	}
	
	private Map<String, Context> contexts;
	
	private void contexts() {
		this.contexts = new HashMap<String, Context>();
		Iterator<CAS> iterator = this.terminology.getViewIterator();
		while (iterator.hasNext()) {
			CAS cas = iterator.next();
			String term = cas.getViewName();
			if (term.startsWith("_")) {
				continue;
			} else {
				Context context = new Context();
				this.contexts.put(term, context);
				context.fromString(cas.getDocumentText());				
			}
		}
	}
	
	public Context context(String term) {
		return this.contexts.get(term);
	}
	
	private Map<String, Integer> occurences;
	
	public Integer occurrences(String term) {
		return this.occurences.get(term);
	}
		
	@Override
	public void load(DataResource aData) throws ResourceInitializationException {
		try {
			if (this.identifiers == null) {
				this.identifiers = new HashSet<String>();
			}
			if (this.terminology == null) {
				URL url = aData.getUrl();
				XMLInputSource source = new XMLInputSource(url);
				XMLParser parser = UIMAFramework.getXMLParser();
				AnalysisEngineDescription ae = parser.parseAnalysisEngineDescription(source);
				this.terminology = CasCreationUtils.createCas(ae);
			}
		} catch (Exception e) {
			throw new ResourceInitializationException(e);
		}
	}
	
}

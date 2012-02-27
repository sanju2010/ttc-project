package eu.project.ttc.resources;

import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.uima.UIMAFramework;
import org.apache.uima.cas.CAS;
import org.apache.uima.cas.CASException;
import org.apache.uima.cas.CASRuntimeException;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.cas.impl.XmiCasDeserializer;
import org.apache.uima.cas.text.AnnotationIndex;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.DataResource;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.SharedResourceObject;
import org.apache.uima.resource.metadata.FsIndexDescription;
import org.apache.uima.resource.metadata.TypeSystemDescription;
import org.apache.uima.util.CasCreationUtils;
import org.apache.uima.util.XMLInputSource;
import org.apache.uima.util.XMLParser;

import eu.project.ttc.models.Context;
import eu.project.ttc.types.TermAnnotation;

public class Terminology implements SharedResourceObject {

	private CAS terminology;

	public void load(String path) throws Exception {
		if (this.terminology == null) {
			InputStream stream = new FileInputStream(path);
			XmiCasDeserializer.deserialize(stream, this.terminology);
			this.setContexts();
		}
	}

	public JCas getJCas() throws CASException {
		return this.terminology.getJCas();
	}
	
	private Set<String> terms;
	
	public boolean exists(String term) {
		try {
			this.terminology.getView(term);
			return true;
		} catch (CASRuntimeException e) {
			return false;
		}
	}
	
	private void setContexts() {
		this.terms = new HashSet<String>();
		Iterator<CAS> iterator = this.terminology.getViewIterator();
		while (iterator.hasNext()) {
			CAS cas = iterator.next();
			String term = cas.getViewName();
			if (!term.startsWith("_")) {
				this.terms.add(term);
			}
		}
	}
	
	public Set<String> getContexts() {
		return this.terms;
	}
	
	public Context getContext(String term) {
		try {
			CAS cas = this.terminology.getView(term);
			Context context = new Context();
			String text = cas.getDocumentText();
			String[] pairs = text.split(":");
			for (String pair : pairs) {
				String[] keyValue = pair.split("#");
				String key = keyValue[0];
				String value = keyValue[1];
				context.setCoOccurrences(key, Double.valueOf(value), Context.ADD_MODE);
			}
			return context;
		} catch (CASRuntimeException e) {
			return null;
		}
	}
	
	public int getOccurrences(String term) {
		try {
			CAS cas = this.terminology.getView(term);
			AnnotationIndex<Annotation> index = cas.getJCas().getAnnotationIndex(TermAnnotation.type);
			FSIterator<Annotation> iterator = index.iterator();
			if (iterator.hasNext()) {
				TermAnnotation annotation = (TermAnnotation) iterator.next();
				return annotation.getOccurrences();
			} else {
				return 0;
			}
		} catch (CASException e) {
			return 0;
		} catch (CASRuntimeException e) {
			return 0;
		}
	}
	
	@Override
	public void load(DataResource aData) throws ResourceInitializationException {
		try {
			URL url = aData.getUrl();
			XMLInputSource source = new XMLInputSource(url);
			XMLParser parser = UIMAFramework.getXMLParser();
			TypeSystemDescription ts = parser.parseTypeSystemDescription(source); 
			this.terminology = CasCreationUtils.createCas(ts,null,new FsIndexDescription[0]);
		} catch (Exception e) {
			throw new ResourceInitializationException(e);
		}
	}
	
}

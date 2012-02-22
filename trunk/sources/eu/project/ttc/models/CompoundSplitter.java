package eu.project.ttc.models;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.uima.UimaContext;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.cas.text.AnnotationIndex;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.DataResource;
import org.apache.uima.resource.ResourceInitializationException;

import eu.project.ttc.types.CompoundTermAnnotation;
import eu.project.ttc.types.SingleWordTermAnnotation;
import eu.project.ttc.types.TermComponentAnnotation;
import fr.univnantes.lina.uima.dictionaries.Dictionary;

import uima.sandbox.indexer.resources.IndexListener;

public class CompoundSplitter implements IndexListener {
		
	private Dictionary dictionary;
	
	private void setDictionary(Dictionary dictionary) {
		this.dictionary = dictionary;
	}
	
	private Dictionary getDictionary() {
		return this.dictionary;
	}
	
	private String path;
	
	private void setPath(String path) throws Exception {
		this.path = path;
		URI uri = URI.create(path);
		this.getDictionary().doLoad(uri);
	}
	
	private String getPath() {
		return this.path;
	}
	
	@Override 
	public void configure(UimaContext context) throws ResourceInitializationException {
		try {
			Dictionary dictionary = (Dictionary) context.getResourceObject("Dictionary");
			this.setDictionary(dictionary);

			String path = (String) context.getConfigParameterValue("File");
			if (path != null && this.getPath() == null) {
				this.setPath(path);
			}
		} catch (Exception e) {
			throw new ResourceInitializationException(e);
		}
	}
	
	@Override
	public void load(DataResource data) throws ResourceInitializationException { }

	@Override
	public void index(Annotation annotation) { }
	
	private boolean done = false;

	@Override
	public void release(JCas cas) {
		if (!this.done) {
			this.done = true;
			this.dash(cas);
		}
	}
	
	private void dash(JCas cas) {
		AnnotationIndex<Annotation> index = cas.getAnnotationIndex(SingleWordTermAnnotation.type);
		FSIterator<Annotation> iterator = index.iterator();
		while (iterator.hasNext()) {
			SingleWordTermAnnotation annotation = (SingleWordTermAnnotation) iterator.next();
			List<TermComponentAnnotation> components = new ArrayList<TermComponentAnnotation>();
			int begin = annotation.getBegin();
			int end = annotation.getEnd();
			this.detect(cas, annotation.getCoveredText(), begin, end, components, false);
			if (!components.isEmpty()) {
				CompoundTermAnnotation compound = new CompoundTermAnnotation(cas, begin, end);
				compound.setComplexity("compound");
				compound.setCategory(annotation.getCategory());
				compound.setLemma(annotation.getLemma());
				compound.setOccurrences(annotation.getOccurrences());
				compound.setFrequency(annotation.getFrequency());
				compound.setSpecificity(annotation.getSpecificity());
				compound.addToIndexes();
			}
		}
	}

	private void detect(JCas cas, String text, int begin, int end, List<TermComponentAnnotation> components, boolean allowed) {
		int first = text.indexOf('-');
		if (first == -1) { 
			if (allowed && begin < end) {
				this.add(cas, text, begin, end, components);
			}
		} else {
			this.add(cas, text, begin, begin + first, components);
			this.detect(cas, text.substring(first + 1), begin + first + 1, end, components, true);
		}
	}

	private void add(JCas cas, String text, int begin, int end, List<TermComponentAnnotation> components) {
		TermComponentAnnotation component = new TermComponentAnnotation(cas, begin, end);
		component.setLemma(component.getCoveredText());
		component.addToIndexes();
		components.add(component);
	}
	
}

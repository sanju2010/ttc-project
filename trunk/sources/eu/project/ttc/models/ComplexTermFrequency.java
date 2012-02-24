package eu.project.ttc.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.uima.UIMAFramework;
import org.apache.uima.cas.CASException;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.cas.text.AnnotationIndex;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.util.Level;

import eu.project.ttc.types.MultiWordTermAnnotation;
import eu.project.ttc.types.WordAnnotation;

public class ComplexTermFrequency extends SimpleTermFrequency {

	public ComplexTermFrequency() {
		super();
		this.setComponents();
	}
	
	private Map<String, List<Component>> components;
	
	private void setComponents() {
		this.components = new HashMap<String, List<Component>>();
	}
	
	public Map<String, List<Component>> getComponents() {
		return this.components;
	}

	public void addEntry(MultiWordTermAnnotation annotation) {
		String term = this.add(annotation);
		if (term == null) {
			return;
		} else {
			this.addComponents(annotation, term);
		}
	}
	
	private void addComponents(MultiWordTermAnnotation annotation, String term) {
		try {
			JCas cas = annotation.getCAS().getJCas();
			AnnotationIndex<Annotation> index = cas.getAnnotationIndex(WordAnnotation.type);
			FSIterator<Annotation> iterator = index.subiterator(annotation);
			List<Component> components = new ArrayList<Component>();
			while (iterator.hasNext()) {
				WordAnnotation component = (WordAnnotation) iterator.next();
				if (component.getBegin() == annotation.getBegin() && component.getEnd() == annotation.getEnd()) {
					continue;
				} else {
					Component c = new Component();
					c.update(component, annotation.getBegin());
					components.add(c);
				}
			}
			this.getComponents().put(term, components);
		} catch (CASException e) {
			UIMAFramework.getLogger().log(Level.WARNING,e.getMessage());
		}
	}
	
}

package eu.project.ttc.resources;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.uima.UIMAFramework;
import org.apache.uima.cas.CASException;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.cas.text.AnnotationIndex;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.util.Level;

import eu.project.ttc.models.Component;
import eu.project.ttc.types.MultiWordTermAnnotation;
import eu.project.ttc.types.TermComponentAnnotation;
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
			if (annotation.getComponents() == null) {
				this.addSubComponents(annotation, term);
			} else {
				this.addComponents(annotation, term);
			}
		}
	}
	
	private void addForm(MultiWordTermAnnotation annotation, Set<String> forms) {
		String coveredText = annotation.getCoveredText().toLowerCase().replaceAll("\\s+", " ");
		int size = annotation.getComponents().size();
		StringBuilder form = new StringBuilder();
		for (int index = 0; index < size; index++) {
			TermComponentAnnotation component = annotation.getComponents(index);
			if (component.getBegin() == annotation.getBegin() && component.getEnd() == annotation.getEnd()) {
				continue;
			} else {
				form.append(component.getCoveredText());
				if (index < size - 1) {
					form.append(' ');
				}
			}
		}
		if (coveredText.length() < form.length()) {
			forms.add(coveredText);
		} else {
			forms.add(form.toString());
		}
	}

	private void add(MultiWordTermAnnotation annotation, String term) {
		Integer frequency = this.getFrequencies().get(term);
		int freq = frequency == null ? 1 : frequency.intValue() + 1;
		this.getFrequencies().put(term, new Integer(freq));
		this.getCategories().put(term, annotation.getCategory());
		Set<String> forms = this.getForms().get(term);
		if (forms == null) {
			forms = new HashSet<String>();
			this.getForms().put(term, forms);
		}
		this.addForm(annotation, forms);
	}
	
	private String add(MultiWordTermAnnotation annotation) {
		if (annotation.getComponents() == null) {
			return super.add(annotation);
		} else {
			int size = annotation.getComponents().size();
			StringBuilder builder = new StringBuilder();
			for (int index = 0; index < size; index++) {
				TermComponentAnnotation component = annotation.getComponents(index);
				builder.append(component.getLemma());
				if (index < size - 1) {
					builder.append(' ');
				}
			}
			String term = builder.toString();
			this.add(annotation, term);
			return term;
		}
	}
	
	private void addComponents(MultiWordTermAnnotation annotation, String term) {
		int size = annotation.getComponents().size();
		List<Component> components = new ArrayList<Component>();
		for (int index = 0; index < size; index++) {
			TermComponentAnnotation component = annotation.getComponents(index);
			if (component.getBegin() == annotation.getBegin() && component.getEnd() == annotation.getEnd()) {
				continue;
			} else {
				Component c = new Component();
				c.update(component, annotation.getBegin());
				components.add(c);
			}
		}
		this.getComponents().put(term, components);
	}
	
	private void addSubComponents(MultiWordTermAnnotation annotation, String term) {
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

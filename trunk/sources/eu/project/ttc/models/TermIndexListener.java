package eu.project.ttc.models;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.uima.UIMAFramework;
import org.apache.uima.UimaContext;
import org.apache.uima.cas.CASException;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.cas.text.AnnotationIndex;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.DataResource;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.Level;

import eu.project.ttc.types.MultiWordTermAnnotation;
import eu.project.ttc.types.NeoClassicalCompoundTermAnnotation;
import eu.project.ttc.types.SingleWordTermAnnotation;
import eu.project.ttc.types.TermAnnotation;
import eu.project.ttc.types.TermComponentAnnotation;

import fr.free.rocheteau.jerome.models.IndexListener;

public class TermIndexListener implements IndexListener {

	private Map<String, Integer> frequencies;
	
	private void setFrequencies() {
		this.frequencies = new HashMap<String, Integer>();
	}
	
	private Map<String, Integer> getFrequencies() {
		return this.frequencies;
	}
	
	private Map<String, List<Component>> components;
	
	private void setComponents() {
		this.components = new HashMap<String, List<Component>>();
	}
	
	private Map<String, List<Component>> getComponents() {
		return this.components;
	}
	
	private Map<String, String> categories;
	
	private void setCategories() {
		this.categories = new HashMap<String, String>();
	}
	
	private Map<String, String> getCategories() {
		return this.categories;
	}
	
	private Map<String, String> complexities;
	
	private void setComplexities() {
		this.complexities = new HashMap<String, String>();
	}
	
	private Map<String, String> getComplexities() {
		return this.complexities;
	}
	
	private String add(TermAnnotation annotation, boolean normalized) {
		String term = normalized ? annotation.getLemma() : annotation.getCoveredText();
		if (term == null) { 
			return null;
		} else {
			term = term.toLowerCase().trim();
			if (term.length() <= 2) {
				return null;
			} else {
				Integer frequency = this.getFrequencies().get(term);
				int freq = frequency == null ? 1 : frequency.intValue() + 1;
				this.getFrequencies().put(term, new Integer(freq));
				this.getCategories().put(term, annotation.getCategory());
				this.getComplexities().put(term, annotation.getComplexity());
			}
		}
		return term;
	}
		
	private void addEntry(SingleWordTermAnnotation annotation) {
		this.add(annotation, true);
	}

	private void addEntry(MultiWordTermAnnotation annotation) {
		String term = this.add(annotation, false);
		if (term == null) {
			return;
		} else {
			this.addComponents(annotation, term);
		}
	}

	private void addEntry(NeoClassicalCompoundTermAnnotation annotation) {
		String term = this.add(annotation, false);
		if (term == null) {
			return;
		} else {
			this.addComponents(annotation, term);
		}
	}
	
	private void addComponents(TermAnnotation annotation, String term) {
		try {
			JCas cas = annotation.getCAS().getJCas();
			AnnotationIndex<Annotation> index = cas.getAnnotationIndex(TermComponentAnnotation.type);
			FSIterator<Annotation> iterator = index.subiterator(annotation);
			List<Component> components = new ArrayList<Component>();
			while (iterator.hasNext()) {
				TermComponentAnnotation component = (TermComponentAnnotation) iterator.next();
				Component c = new Component();
				c.update(component, annotation.getBegin());
				components.add(c);
			}
			this.getComponents().put(term, components);
		} catch (CASException e) {
			UIMAFramework.getLogger().log(Level.WARNING,e.getMessage());
		}
	}
	
	public TermIndexListener() {
		this.setFrequencies();
		this.setComponents();
		this.setCategories();
		this.setComplexities();
		this.setComparator();
	}
	
	private FrequencyComparator comparator;
	
	private void setComparator() {
		this.comparator = new FrequencyComparator(this.getFrequencies());
	}
	
	private FrequencyComparator getComparator() {
		return this.comparator;
	}
	
	private String language;
	
	private void setLanguage(String language) {
		this.language = language;
	}
	
	private String getLanguage() {
		return this.language;
	}
	
	@Override 
	public void configure(UimaContext context){ 
		String language = (String) context.getConfigParameterValue("Language");
		this.setLanguage(language);
	}
	
	@Override
	public void load(DataResource data) throws ResourceInitializationException { }

	@Override
	public void update(Annotation annotation) {
		if (annotation instanceof SingleWordTermAnnotation) {
			this.addEntry((SingleWordTermAnnotation) annotation);							
		} else if (annotation instanceof MultiWordTermAnnotation) {
			this.addEntry((MultiWordTermAnnotation) annotation);			
		} else if (annotation instanceof NeoClassicalCompoundTermAnnotation) {
			this.addEntry((NeoClassicalCompoundTermAnnotation) annotation);			
		}
	}
	
	private boolean done = false;
	
	@Override
	public void release(JCas cas) {
		if (!this.done) {
			this.done = true;
			cas.setDocumentLanguage(this.getLanguage());
			StringBuilder builder = new StringBuilder();
			Map<String, Integer> entries = new TreeMap<String, Integer>(this.getComparator());
			entries.putAll(this.getFrequencies());
			for (String entry : entries.keySet()) {
				int frequency = this.getFrequencies().get(entry).intValue();
				String category = this.getCategories().get(entry);
				String complexity = this.getComplexities().get(entry);
				List<Component> components = this.getComponents().get(entry);
				if (frequency > 1) {
					int begin = builder.length();
					builder.append(entry);
					int end = builder.length();
					builder.append('\n');
					TermAnnotation annotation = null;
					if (complexity.equals(Term.SINGLE_WORD)) {
						annotation = new SingleWordTermAnnotation(cas,begin,end);
					} else if (complexity.equals(Term.MULTI_WORD)) {
						annotation = new MultiWordTermAnnotation(cas,begin,end);
					} else if (complexity.equals(Term.NEO_CLASSICAL_COMPOUND)) {
						annotation = new NeoClassicalCompoundTermAnnotation(cas,begin,end);
					} else {
						annotation = new TermAnnotation(cas,begin,end);
					}
					annotation.setFrequency(frequency);
					annotation.setCategory(category);
					annotation.setComplexity(complexity);
					annotation.addToIndexes();
					if (components != null && !components.isEmpty()) {
						for (Component component : components) {
							TermComponentAnnotation c = new TermComponentAnnotation(cas);
							component.release(c, annotation.getBegin());
							c.addToIndexes();
						}						
					}
				}
			}
			cas.setDocumentText(builder.toString());
		}
	}
	
	@Override
	public double priority() {
		return 1;
	}

	private class Component {
		
		private String category;
		private String lemma;
		private int begin;
		private int end;
		
		public void release(TermComponentAnnotation annotation, int offset) {
			annotation.setBegin(offset + this.begin);
			annotation.setEnd(offset + this.end);
			annotation.setCategory(this.category);
			annotation.setLemma(this.lemma);
		}
		
		public void update(TermComponentAnnotation annotation,int offset) {
			this.category = annotation.getCategory();
			this.lemma = annotation.getLemma();
			this.begin = annotation.getBegin() - offset;
			this.end = annotation.getEnd() - offset;
		}
		
	}
	
	private class FrequencyComparator implements Comparator<String> {

		private Map<String, Integer> frequencies;
		
		public FrequencyComparator(Map<String, Integer> frequencies) {
			this.frequencies = frequencies;
		}
		
		@Override
		public int compare(String sourceKey, String targetKey) {
			Integer sourceValue = this.frequencies.get(sourceKey);
			Integer targetValue = this.frequencies.get(targetKey);
			double source = sourceValue == null ? 0.0 : sourceValue.doubleValue();
			double target = targetValue == null ? 0.0 : targetValue.doubleValue();
			if (source < target) {
				return 1;
			} else if (source > target) {
				return -1;
			} else {
				return sourceKey.compareTo(targetKey);
			}
		}
		
	}
	
}

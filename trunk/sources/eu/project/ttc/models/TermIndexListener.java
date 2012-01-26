package eu.project.ttc.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import uima.sandbox.indexer.resources.IndexListener;

import eu.project.ttc.types.MultiWordTermAnnotation;
import eu.project.ttc.types.NeoClassicalCompoundTermAnnotation;
import eu.project.ttc.types.SingleWordTermAnnotation;
import eu.project.ttc.types.TermAnnotation;
import eu.project.ttc.types.TermComponentAnnotation;
import eu.project.ttc.types.WordAnnotation;


public class TermIndexListener implements IndexListener {

	private class SimpleTermFrequency {
		
		public SimpleTermFrequency() {
			this.setFrequencies();
			this.setCategories();
		}
		
		private Map<String, Integer> frequencies;
		
		private void setFrequencies() {
			this.frequencies = new HashMap<String, Integer>();
		}
		
		public Map<String, Integer> getFrequencies() {
			return this.frequencies;
		}

		private Map<String, String> categories;
		
		private void setCategories() {
			this.categories = new HashMap<String, String>();
		}
		
		public Map<String, String> getCategories() {
			return this.categories;
		}

		protected String add(TermAnnotation annotation, boolean normalized) {
			String term = normalized ? annotation.getLemma().toLowerCase() : annotation.getCoveredText().toLowerCase();
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
					return term;
				}
			}
		}

		public void addEntry(SingleWordTermAnnotation annotation) {
			this.add(annotation, true);
		}

	}
	
	private class ComplexTermFrequency extends SimpleTermFrequency {

		public ComplexTermFrequency() {
			super();
			this.setComponents();
		}
		
		private Map<String, List<Component>> components;
		
		private void setComponents() {
			this.components = new HashMap<String, List<Component>>();
		}
		
		private Map<String, List<Component>> getComponents() {
			return this.components;
		}
	
		public void addEntry(MultiWordTermAnnotation annotation) {
			String term = this.add(annotation, false);
			if (term == null) {
				return;
			} else {
				this.addComponents(annotation, term);
			}
		}
	
		public void addEntry(NeoClassicalCompoundTermAnnotation annotation) {
			String term = this.add(annotation, false);
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

		private void addComponents(NeoClassicalCompoundTermAnnotation annotation, String term) {
			try {
				JCas cas = annotation.getCAS().getJCas();
				AnnotationIndex<Annotation> index = cas.getAnnotationIndex(TermComponentAnnotation.type);
				FSIterator<Annotation> iterator = index.subiterator(annotation);
				List<Component> components = new ArrayList<Component>();
				while (iterator.hasNext()) {
					TermComponentAnnotation component = (TermComponentAnnotation) iterator.next();
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
	
	private SimpleTermFrequency singleWordTermFrequency;
	
	private void setSingleWordTermFrequency() {
		this.singleWordTermFrequency = new SimpleTermFrequency();
	}
	
	private SimpleTermFrequency getSingleWordTermFrequency() {
		return this.singleWordTermFrequency;
	}
	
	private ComplexTermFrequency multiWordTermFrequency;
	
	private void setMultiWordTermFrequency() {
		this.multiWordTermFrequency = new ComplexTermFrequency();
	}
	
	private ComplexTermFrequency getMultiWordTermFrequency() {
		return this.multiWordTermFrequency;
	}
	
	private ComplexTermFrequency neoClassicalCompoundFrequency;
	
	private void setNeoClassicalCompoundFrequency() {
		this.neoClassicalCompoundFrequency = new ComplexTermFrequency();
	}
	
	private ComplexTermFrequency getNeoClassicalCompoundFrequency() {
		return this.neoClassicalCompoundFrequency;
	}
	
	public TermIndexListener() {
		this.setSingleWordTermFrequency();
		this.setMultiWordTermFrequency();
		this.setNeoClassicalCompoundFrequency();
	}
	
	private String language;
	
	private void setLanguage(String language) {
		this.language = language;
	}
	
	private String getLanguage() {
		return this.language;
	}
	
	private String type;
	
	private void setType(String type) {
		this.type = type;
	}
	
	private boolean isAllTermEnabled() {
		return this.type.equals(TermAnnotation.class.getCanonicalName()); 
	}
	
	private boolean isSingleWordTermEnabled() {
		return this.isAllTermEnabled() || this.type.equals(SingleWordTermAnnotation.class.getCanonicalName()); 
	}
	
	private boolean isMultiWordTermEnabled() {
		return this.isAllTermEnabled() || this.type.equals(MultiWordTermAnnotation.class.getCanonicalName()); 
	}
	
	private boolean isNeoClassicalCompoundEnabled() {
		return this.isAllTermEnabled() || this.type.equals(NeoClassicalCompoundTermAnnotation.class.getCanonicalName()); 
	}
	
	@Override 
	public void configure(UimaContext context){ 
		String language = (String) context.getConfigParameterValue("Language");
		this.setLanguage(language);
		String type = (String) context.getConfigParameterValue("Type");
		this.setType(type);
	}
	
	@Override
	public void load(DataResource data) throws ResourceInitializationException { }

	@Override
	public void index(Annotation annotation) {
		if (annotation instanceof SingleWordTermAnnotation && this.isSingleWordTermEnabled()) {
			this.getSingleWordTermFrequency().addEntry((SingleWordTermAnnotation) annotation);					
		} else if (annotation instanceof MultiWordTermAnnotation && this.isMultiWordTermEnabled()) {
			this.getMultiWordTermFrequency().addEntry((MultiWordTermAnnotation) annotation);
		} else if (annotation instanceof NeoClassicalCompoundTermAnnotation && this.isNeoClassicalCompoundEnabled()) {
			this.getNeoClassicalCompoundFrequency().addEntry((NeoClassicalCompoundTermAnnotation) annotation);
		}
	}
	
	@Override
	public void release(JCas cas) {
		cas.setDocumentLanguage(this.getLanguage());
		StringBuilder builder = new StringBuilder();
		this.release(cas, builder, this.getSingleWordTermFrequency());
		this.release(cas, builder, this.getMultiWordTermFrequency(), Term.MULTI_WORD);
		this.release(cas, builder, this.getNeoClassicalCompoundFrequency(), Term.NEO_CLASSICAL_COMPOUND);
		cas.setDocumentText(builder.toString());
	}

	private void release(JCas cas, StringBuilder builder, SimpleTermFrequency frequency) {
		for (String entry : frequency.getFrequencies().keySet()) {
			int freq = frequency.getFrequencies().get(entry).intValue();
			String category = frequency.getCategories().get(entry);
			if (freq > 1) {
				int begin = builder.length();
				builder.append(entry);
				int end = builder.length();
				builder.append('\n');
				TermAnnotation annotation = new SingleWordTermAnnotation(cas,begin,end);
				annotation.setFrequency(freq);
				annotation.setCategory(category);
				annotation.setLemma(entry);
				annotation.setComplexity(Term.SINGLE_WORD);
				annotation.addToIndexes();
			}
		}
	}
	
	private void release(JCas cas, StringBuilder builder, ComplexTermFrequency frequency, String complexity) {
		for (String entry : frequency.getFrequencies().keySet()) {
			int freq = frequency.getFrequencies().get(entry).intValue();
			String category = frequency.getCategories().get(entry);
			List<Component> components = frequency.getComponents().get(entry);
			if (freq > 1) {
				int begin = builder.length();
				builder.append(entry);
				int end = builder.length();
				builder.append('\n');
				TermAnnotation annotation = null;
				if (complexity.equals(Term.MULTI_WORD)) {
					annotation = new MultiWordTermAnnotation(cas,begin,end);
				} else if (complexity.equals(Term.NEO_CLASSICAL_COMPOUND)) {
					annotation = new NeoClassicalCompoundTermAnnotation(cas,begin,end);
				} else {
					continue;
				}
				annotation.setFrequency(freq);
				annotation.setCategory(category);
				annotation.setComplexity(complexity);
				annotation.addToIndexes();
				if (components != null) {
					for (Component component : components) {
						TermComponentAnnotation c = new TermComponentAnnotation(cas);
						component.release(c, annotation.getBegin());
						c.addToIndexes();
					}						
				}
			}
		}
	}

	private class Component {
		
		private String category;
		private String lemma;
		private String stem;
		private int begin;
		private int end;
		
		public void release(TermComponentAnnotation annotation, int offset) {
			annotation.setBegin(offset + this.begin);
			annotation.setEnd(offset + this.end);
			annotation.setCategory(this.category);
			annotation.setLemma(this.lemma);
			annotation.setStem(this.stem);
		}
		
		public void update(WordAnnotation annotation,int offset) {
			this.category = annotation.getCategory();
			this.lemma = annotation.getLemma();
			this.stem = annotation.getStem();
			this.begin = annotation.getBegin() - offset;
			this.end = annotation.getEnd() - offset;
		}

		public void update(TermComponentAnnotation annotation,int offset) {
			this.category = annotation.getCategory();
			this.lemma = annotation.getLemma();
			this.stem = annotation.getStem();
			this.begin = annotation.getBegin() - offset;
			this.end = annotation.getEnd() - offset;
		}

		
	}
		
}

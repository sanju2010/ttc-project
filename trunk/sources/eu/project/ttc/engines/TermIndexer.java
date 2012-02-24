package eu.project.ttc.engines;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.uima.UIMAFramework;
import org.apache.uima.cas.CASException;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.cas.text.AnnotationIndex;
import org.apache.uima.examples.SourceDocumentInformation;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.cas.FSArray;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.util.Level;

import uima.sandbox.indexer.engines.Indexer;

import eu.project.ttc.metrics.AssociationRate;
import eu.project.ttc.models.CrossTable;
import eu.project.ttc.models.Term;
import eu.project.ttc.models.Context;
import eu.project.ttc.types.MultiWordTermAnnotation;
import eu.project.ttc.types.SingleWordTermAnnotation;
import eu.project.ttc.types.TermAnnotation;
import eu.project.ttc.types.TermComponentAnnotation;
import eu.project.ttc.types.WordAnnotation;

public class TermIndexer extends Indexer {

	private class SimpleTermFrequency {
		
		public SimpleTermFrequency() {
			this.setFrequencies();
			this.setCategories();
			this.setContexts();
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

		protected String add(TermAnnotation annotation) {
			String term = annotation.getLemma().toLowerCase().trim(); // : annotation.getCoveredText().toLowerCase();
			if (term == null) { 
				return null;
			} else if (term.length() <= 2) {
				return null;
			} else {
				Integer frequency = this.getFrequencies().get(term);
				int freq = frequency == null ? 1 : frequency.intValue() + 1;
				this.getFrequencies().put(term, new Integer(freq));
				this.getCategories().put(term, annotation.getCategory());
				return term;
			}
		}

		public void addEntry(SingleWordTermAnnotation annotation) {
			this.add(annotation);
		}

		private Map<String, Context> contexts;
		
		private void setContexts() {
			this.contexts = new HashMap<String, Context>();
		}
		
		public Map<String,Context> getContexts() {
			return this.contexts;
		}
		
		public void setCoOccurrences(String term,String context,Double coOccurrences,int mode) {
			Context termContext = this.getContexts().get(term);
			if (termContext == null) {
				termContext = new Context();
				this.getContexts().put(term,termContext);
			}
			termContext.setCoOccurrences(context, coOccurrences, mode);
		}
		
		public void addCoOccurrences(String term,String context) {
			Context termContext = this.getContexts().get(term);
			if (termContext == null) {
				termContext = new Context();
				this.getContexts().put(term, termContext);
			}
			termContext.setCoOccurrences(context, new Double(1), Context.ADD_MODE);
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
	
	private String language;
	
	private void setLanguage(String language) {
		this.language = language;
	}
	
	private String getLanguage() {
		return this.language;
	}
	
	private AssociationRate associationRate;
	
	private void setAssociationRate(String name) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		if (this.associationRate == null) {
			Class<?> cl = Class.forName(name);
			Object obj = cl.newInstance();
			if (obj instanceof AssociationRate) {
				this.associationRate = (AssociationRate) obj;
				UIMAFramework.getLogger().log(Level.INFO,"Setting Association Rate: " + this.associationRate.getClass().getSimpleName());
			} else {
				throw new ClassCastException("Class name '" + name + "' doesn't comply " + AssociationRate.class.getCanonicalName());
			}
		}
	}
	
	private AssociationRate getAssociationRate() {
		return this.associationRate;
	}
		
	@Override 
	public void initialize() throws Exception { 
		this.setSingleWordTermFrequency();
		this.setMultiWordTermFrequency();
		String language = (String) this.getContext().getConfigParameterValue("Language");
		this.setLanguage(language);
		String className = (String) this.getContext().getConfigParameterValue("AssociationRateClassName");
		this.setAssociationRate(className);
	}
	
	@Override
	public void update(JCas cas) throws Exception {
		AnnotationIndex<Annotation> index = cas.getAnnotationIndex(TermAnnotation.type);
		FSIterator<Annotation> iterator = index.iterator();
		while (iterator.hasNext()) {
			TermAnnotation annotation = (TermAnnotation) iterator.next();
			if (annotation instanceof SingleWordTermAnnotation) {
				this.getSingleWordTermFrequency().addEntry((SingleWordTermAnnotation) annotation);					
			} else if (annotation instanceof MultiWordTermAnnotation) {
				this.getMultiWordTermFrequency().addEntry((MultiWordTermAnnotation) annotation);
			} 
			this.update(annotation);
		}
	}
	
	private void update(TermAnnotation annotation) throws Exception {
		FSArray array = annotation.getContext();
		if (array == null) {
			return;
		} else {
			String term = annotation.getLemma().toLowerCase();
			Integer freq = this.getSingleWordTermFrequency().getFrequencies().get(term);
			if (freq == null || freq.intValue() < 2) {
				return;
			} else {
				for (int index = 0; index < array.size(); index++) {
					TermAnnotation entry = annotation.getContext(index);
					String context = entry.getLemma().toLowerCase();
					this.getSingleWordTermFrequency().addCoOccurrences(term, context);
				}
			}
		}
	}
	
	@Override
	public void release(JCas cas) throws Exception {
		cas.setDocumentLanguage(this.getLanguage());
		StringBuilder builder = new StringBuilder();
		this.release(cas, builder, this.getSingleWordTermFrequency());
		this.release(cas, builder, this.getMultiWordTermFrequency(), Term.MULTI_WORD);
		cas.setDocumentText(builder.toString());
		SourceDocumentInformation sdi = new SourceDocumentInformation(cas);
		sdi.setUri("http://terminology." + this.getLanguage().toLowerCase());
		sdi.setBegin(0);
		sdi.setEnd(builder.length());
		sdi.setOffsetInSource(0);
		sdi.addToIndexes();
		this.normalize(this.getAssociationRate());
		this.annotate(cas);
	}
	
	private void annotate(JCas cas) throws Exception {
		Map<String, Context> contexts = new TreeMap<String, Context>();
		contexts.putAll(this.getSingleWordTermFrequency().getContexts());
		for (String item : contexts.keySet()) {
			JCas jcas = cas.createView(item);
			jcas.setDocumentLanguage(this.getLanguage());
			StringBuilder builder = new StringBuilder();
			Context context = contexts.get(item);
			builder.append(context.toString());
			builder.append('\n');
			jcas.setDocumentText(builder.toString());
		}
	}
	
	private void normalize(AssociationRate rate) throws Exception {
		CrossTable crossTable = new CrossTable();
		Map<String, Context> contexts = this.getSingleWordTermFrequency().getContexts();
		crossTable.setContexts(contexts);
		for (String term : contexts.keySet()) {
			Context context = contexts.get(term);
			crossTable.setTerm(term);
			for (String coTerm : context.getCoOccurrences().keySet()) {
				crossTable.setCoTerm(coTerm);	
				crossTable.compute();
				int a = crossTable.getA();
				int b = crossTable.getB();
				int c = crossTable.getC();
				int d = crossTable.getD();
				double norm = rate.getValue(a, b, c, d);
				this.getSingleWordTermFrequency().setCoOccurrences(term, coTerm, new Double(norm), Context.DEL_MODE);
			}
		}
	}

	private void release(JCas cas, StringBuilder builder, SimpleTermFrequency frequency) {
		for (String entry : frequency.getFrequencies().keySet()) {
			int freq = frequency.getFrequencies().get(entry).intValue();
			String category = frequency.getCategories().get(entry);
			int begin = builder.length();
			builder.append(entry);
			int end = builder.length();
			builder.append('\n');
			TermAnnotation annotation = new SingleWordTermAnnotation(cas,begin,end);
			annotation.setOccurrences(freq);
			annotation.setFrequency(freq);
			annotation.setCategory(category);
			annotation.setLemma(entry);
			annotation.setComplexity(Term.SINGLE_WORD);
			annotation.addToIndexes();
		}
	}
	
	private void release(JCas cas, StringBuilder builder, ComplexTermFrequency frequency, String complexity) {
		for (String entry : frequency.getFrequencies().keySet()) {
			int freq = frequency.getFrequencies().get(entry).intValue();
			String category = frequency.getCategories().get(entry);
			List<Component> components = frequency.getComponents().get(entry);
			int begin = builder.length();
			builder.append(entry);
			int end = builder.length();
			builder.append('\n');
			TermAnnotation annotation = new MultiWordTermAnnotation(cas,begin,end);
			annotation.setOccurrences(freq);
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
		
	}
		
}

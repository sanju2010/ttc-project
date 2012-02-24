package eu.project.ttc.engines;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.uima.UIMAFramework;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.cas.text.AnnotationIndex;
import org.apache.uima.examples.SourceDocumentInformation;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.cas.FSArray;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.util.Level;

import uima.sandbox.indexer.engines.Indexer;

import eu.project.ttc.metrics.AssociationRate;
import eu.project.ttc.models.ComplexTermFrequency;
import eu.project.ttc.models.Component;
import eu.project.ttc.models.CrossTable;
import eu.project.ttc.models.Context;
import eu.project.ttc.models.SimpleTermFrequency;
import eu.project.ttc.types.MultiWordTermAnnotation;
import eu.project.ttc.types.SingleWordTermAnnotation;
import eu.project.ttc.types.TermAnnotation;
import eu.project.ttc.types.TermComponentAnnotation;

public class TermIndexer extends Indexer {
	
	private SimpleTermFrequency singleWordTermFrequency;
	
	private void setSingleWordTermFrequency(SimpleTermFrequency termFrequency) {
		this.singleWordTermFrequency = termFrequency;
	}
	
	private SimpleTermFrequency getSingleWordTermFrequency() {
		return this.singleWordTermFrequency;
	}
	
	private ComplexTermFrequency multiWordTermFrequency;
	
	private void setMultiWordTermFrequency(ComplexTermFrequency termFrequency) {
		this.multiWordTermFrequency = termFrequency;
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
		SimpleTermFrequency singleWordTermFrequency = (SimpleTermFrequency) this.getContext().getResourceObject("SimpleTermFrequency");
		this.setSingleWordTermFrequency(singleWordTermFrequency);
		ComplexTermFrequency multiWordTermFrequency = (ComplexTermFrequency) this.getContext().getResourceObject("ComplexTermFrequency");
		this.setMultiWordTermFrequency(multiWordTermFrequency);
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
		this.release(cas, builder, this.getMultiWordTermFrequency());
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
			annotation.addToIndexes();
		}
	}
	
	private void release(JCas cas, StringBuilder builder, ComplexTermFrequency frequency) {
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

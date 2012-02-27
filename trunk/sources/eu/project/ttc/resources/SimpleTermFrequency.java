package eu.project.ttc.resources;

import java.util.HashMap;
import java.util.Map;

import org.apache.uima.resource.DataResource;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.SharedResourceObject;

import eu.project.ttc.models.Context;
import eu.project.ttc.types.SingleWordTermAnnotation;
import eu.project.ttc.types.TermAnnotation;

public class SimpleTermFrequency implements SharedResourceObject {

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

	@Override
	public void load(DataResource aData) throws ResourceInitializationException {
		// TODO Auto-generated method stub
		
	}

}

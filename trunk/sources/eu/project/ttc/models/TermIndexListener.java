package eu.project.ttc.models;

import java.util.HashMap;
import java.util.Map;

import org.apache.uima.UimaContext;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.DataResource;
import org.apache.uima.resource.ResourceInitializationException;

import eu.project.ttc.types.TermAnnotation;
import eu.project.ttc.types.TermEntryAnnotation;
import fr.free.rocheteau.jerome.models.IndexListener;

public class TermIndexListener implements IndexListener {

	private Map<String, Integer> entries;
	
	private void setEntries() {
		this.entries = new HashMap<String, Integer>();
	}
	
	private Map<String, Integer> getEntries() {
		return this.entries;
	}
	
	private Map<String, String> categories;
	
	private void setCategories() {
		this.categories = new HashMap<String, String>();
	}
	
	private Map<String, String> getCategories() {
		return this.categories;
	}
	
	private void addEntry(TermAnnotation annotation) {
		String text = annotation.getLemma();
		if (text == null) { 
			return;
		} else {
			text = text.toLowerCase().trim();
			if (text.length() <= 2) {
				return;
			} else {
				Integer frequency = this.getEntries().get(text);
				int freq = frequency == null ? 1 : frequency.intValue() + 1;
				this.getEntries().put(text, new Integer(freq));
				this.getCategories().put(text, annotation.getCategory());
			}
		}
	}
		
	public TermIndexListener() {
		this.setEntries();
		this.setCategories();
	}
	
	@Override 
	public void configure(UimaContext context){ }
	
	@Override
	public void load(DataResource data) throws ResourceInitializationException { }

	@Override
	public void update(Annotation annotation) {
		if (annotation instanceof TermAnnotation) {
			this.addEntry((TermAnnotation) annotation);			
		}
	}
	
	private boolean done = false;
	
	@Override
	public void release(JCas cas) {
		if (!this.done) {
			this.done = true;
			for (String entry : this.getEntries().keySet()) {
				int frequency = this.getEntries().get(entry).intValue();
				if (frequency > 1) {
					TermEntryAnnotation annotation = new TermEntryAnnotation(cas);
					annotation.setTerm(entry);
					annotation.setFrequency(frequency);
					annotation.setCategory(this.getCategories().get(entry));
					annotation.addToIndexes();				
				}
			}
		}
	}
	
	@Override
	public double priority() {
		return 1;
	}

}

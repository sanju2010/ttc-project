package eu.project.ttc.models;

import org.apache.uima.UimaContext;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.cas.text.AnnotationIndex;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.DataResource;
import org.apache.uima.resource.ResourceAccessException;
import org.apache.uima.resource.ResourceInitializationException;

import eu.project.ttc.types.TermEntryAnnotation;
import fr.free.rocheteau.jerome.models.IndexListener;

public class TermFrequencyListener implements IndexListener {
	
	private double specializedFrequency;
	
	private void setSpecializedFrequency() {
		this.specializedFrequency = 0.0;
	}
	
	private double getSpecializedFrequency() {
		return this.specializedFrequency;
	}
	
	private GeneralLanguage generalLanguage;
	
	private void setGeneralLanguage(GeneralLanguage generalLanguage) {
		this.generalLanguage = generalLanguage;
	}
	
	private GeneralLanguage getGeneralLanguage() {
		return this.generalLanguage;
	}
	
	@Override 
	public void configure(UimaContext context) throws ResourceInitializationException {
		try {
			this.setSpecializedFrequency();
			GeneralLanguage generalLanguage = (GeneralLanguage) context.getResourceObject("GeneralLanguage");
			this.setGeneralLanguage(generalLanguage);
		} catch (ResourceAccessException e) {
			throw new ResourceInitializationException(e);
		}
	}
	
	@Override
	public void load(DataResource data) throws ResourceInitializationException { }

	@Override
	public void update(Annotation annotation) { }
	
	private boolean done = false;

	private void set(JCas cas) {
		AnnotationIndex<Annotation> index = cas.getAnnotationIndex(TermEntryAnnotation.type);
		FSIterator<Annotation> iterator = index.iterator();
		while (iterator.hasNext()) {
			TermEntryAnnotation annotation = (TermEntryAnnotation) iterator.next();
			double frequency = annotation.getFrequency();
			this.specializedFrequency += frequency;
		}
	}
	
	private void get(JCas cas) {
		AnnotationIndex<Annotation> index = cas.getAnnotationIndex(TermEntryAnnotation.type);
		FSIterator<Annotation> iterator = index.iterator();
		while (iterator.hasNext()) {
			TermEntryAnnotation annotation = (TermEntryAnnotation) iterator.next();
			String entry = annotation.getTerm().toLowerCase();
			double frequency = annotation.getFrequency() / this.getSpecializedFrequency();
			double generalFrequency = this.getGeneralLanguage().get(entry);
			if (generalFrequency != 0.0) { 
				annotation.setSpecificity(frequency / generalFrequency);				
			}
		}
	}
	
	@Override
	public void release(JCas cas) { 
		if (!this.done) {
			this.done = true;
			this.set(cas);
			this.get(cas);
		}
	}

	@Override
	public double priority() {
		return 0.5;
	}

}

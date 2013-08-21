package eu.project.ttc.engines;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.cas.text.AnnotationIndex;
import org.apache.uima.examples.SourceDocumentInformation;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.cas.FSArray;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.ResourceAccessException;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.Level;

import eu.project.ttc.resources.GeneralLanguage;
import eu.project.ttc.types.TermAnnotation;

public class TermFrequencyComputer extends JCasAnnotator_ImplBase {
	
	private Double specializedFrequency;
	
	private void setSpecializedFrequency() {
		this.specializedFrequency = new Double(0.0);
	}
	
	private Double getSpecializedFrequency() {
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
	public void initialize(UimaContext context) throws ResourceInitializationException {
		super.initialize(context);
		try {
			if (this.getGeneralLanguage() == null) {
				GeneralLanguage generalLanguage = (GeneralLanguage) context.getResourceObject("GeneralLanguage");
				this.setGeneralLanguage(generalLanguage);
			}
			if (this.getSpecializedFrequency() == null) {
				this.setSpecializedFrequency();
			}
		} catch (ResourceAccessException e) {
			throw new ResourceInitializationException(e);
		}
	}
	
	private void set(JCas cas) {
		AnnotationIndex<Annotation> index = cas.getAnnotationIndex(TermAnnotation.type);
		FSIterator<Annotation> iterator = index.iterator();
		while (iterator.hasNext()) {
			TermAnnotation annotation = (TermAnnotation) iterator.next();
			fixOccurrences(annotation);
			this.specializedFrequency += annotation.getFrequency();
		}
	}
	
	private void fixOccurrences(TermAnnotation annotation) {
		FSArray forms = annotation.getForms();
		int count = 0;
		if (forms != null) {
			for (int i = 0; i < forms.size(); i++) {
				count += annotation.getForms(i).getOccurrences();
			}
			annotation.setOccurrences(count);
			annotation.setFrequency(count);
		}
		
	}

	private void get(JCas cas) {
		AnnotationIndex<Annotation> index = cas.getAnnotationIndex(TermAnnotation.type);
		FSIterator<Annotation> iterator = index.iterator();
		while (iterator.hasNext()) {
			TermAnnotation annotation = (TermAnnotation) iterator.next();
			String entry = annotation.getCoveredText(); 
			double frequency = annotation.getFrequency() / this.getSpecializedFrequency();
			annotation.setFrequency(frequency);
			double generalFrequency = this.getGeneralLanguage().get(entry);
			if (generalFrequency != 0.0) { 
				annotation.setSpecificity(frequency / generalFrequency);				
			}
		}
	}
	
	private void display(JCas cas) {
		AnnotationIndex<Annotation> index = cas.getAnnotationIndex(SourceDocumentInformation.type);
		FSIterator<Annotation> iterator = index.iterator();
		if (iterator.hasNext()) {
			SourceDocumentInformation sdi = (SourceDocumentInformation) iterator.next();
			this.getContext().getLogger().log(Level.INFO, "Computing frequency of " + sdi.getUri());
		}
	}
	
	@Override
	public void process(JCas cas) { 
		this.display(cas);
		this.set(cas);
		this.get(cas);
	}

}

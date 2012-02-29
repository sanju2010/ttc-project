package eu.project.ttc.engines;

import java.util.HashSet;
import java.util.Set;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.cas.text.AnnotationIndex;
import org.apache.uima.examples.SourceDocumentInformation;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.Level;

import eu.project.ttc.types.TermAnnotation;

public class TermCleaner extends JCasAnnotator_ImplBase {
		
	@Override 
	public void initialize(UimaContext context) throws ResourceInitializationException {
		super.initialize(context);
		try {
			if (this.getAnnotations() == null) {
				this.setAnnotations();
			}
		} catch (Exception e) {
			throw new ResourceInitializationException(e);
		}
	}
		
	private Set<Annotation> annotations;
	
	private void setAnnotations() {
		this.annotations = new HashSet<Annotation>();
	}
	
	private Set<Annotation> getAnnotations() {
		return this.annotations;
	}
	
	private void display(JCas cas) {
		AnnotationIndex<Annotation> index = cas.getAnnotationIndex(SourceDocumentInformation.type);
		FSIterator<Annotation> iterator = index.iterator();
		if (iterator.hasNext()) {
			SourceDocumentInformation sdi = (SourceDocumentInformation) iterator.next();
			this.getContext().getLogger().log(Level.INFO, "Cleaning terms of " + sdi.getUri());
		}
	}
	
	@Override
	public void process(JCas cas) throws AnalysisEngineProcessException {
		this.display(cas);
		this.select(cas);
		this.remove();
		this.adjust(cas);
	}

	private void select(JCas cas) {
		AnnotationIndex<Annotation> index = cas.getAnnotationIndex(TermAnnotation.type);
		FSIterator<Annotation> iterator = index.iterator();
		while (iterator.hasNext()) {
			TermAnnotation annotation = (TermAnnotation) iterator.next();
			if (annotation.getOccurrences() < 2) {
				this.getAnnotations().add(annotation);
			}
		}
	}
	
	private void remove() {
		for (Annotation annotation : this.getAnnotations()) {
			annotation.removeFromIndexes();
		}
	}
	
	private void adjust(JCas cas) {
		AnnotationIndex<Annotation> index = cas.getAnnotationIndex(TermAnnotation.type);
		FSIterator<Annotation> iterator = index.iterator();
		while (iterator.hasNext()) {
			TermAnnotation annotation = (TermAnnotation) iterator.next();
			if (annotation.getVariants() != null) {
				int occ = annotation.getOccurrences();
				double freq = annotation.getFrequency();
				double spec = annotation.getSpecificity();
				for (int i = 0; i < annotation.getVariants().size(); i++) {
					occ += annotation.getVariants(i).getOccurrences();
					freq += annotation.getVariants(i).getFrequency();
					spec += annotation.getVariants(i).getSpecificity();
				}
				annotation.setOccurrences(occ);
				annotation.setFrequency(freq);
				annotation.setSpecificity(spec);
			} 
		}
	}
	
}

package eu.project.ttc.engines;

import java.util.HashSet;
import java.util.Set;

import org.apache.uima.UIMAFramework;
import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.CASException;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.text.AnnotationIndex;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.Level;

import eu.project.ttc.types.TermAnnotation;

public class TermCleaner extends JCasAnnotator_ImplBase {
	
	private String type;
	
	private void setType(String type) {
		this.type = type;
	}
	
	private boolean isAllowed(JCas cas, Type type) {
		try {
			Type allowed = cas.getRequiredType(this.type);
			return cas.getTypeSystem().subsumes(allowed, type);
		} catch (CASException e) {
			UIMAFramework.getLogger().log(Level.WARNING, e.getMessage());
			return false;
		}		
	}
		
	@Override 
	public void initialize(UimaContext context) throws ResourceInitializationException {
		super.initialize(context);
		try {
			String type = (String) context.getConfigParameterValue("Type");
			this.setType(type);
			this.setAnnotations();
			context.getLogger().log(Level.INFO, "Term Annotation Type: " + type);
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
	
	@Override
	public void process(JCas cas) throws AnalysisEngineProcessException {
		this.select(cas);
		this.correct(cas);
		this.remove();
		this.adjust(cas);
	}

	private void select(JCas cas) {
		AnnotationIndex<Annotation> index = cas.getAnnotationIndex(TermAnnotation.type);
		FSIterator<Annotation> iterator = index.iterator();
		while (iterator.hasNext()) {
			TermAnnotation annotation = (TermAnnotation) iterator.next();
			if (this.isAllowed(cas, annotation.getType())) {
				if (annotation.getOccurrences() < 2) {
					this.getAnnotations().add(annotation);
				}
			} else {
				this.getAnnotations().add(annotation);
			}
		}
	}
		
	private void correct(JCas cas) {
		AnnotationIndex<Annotation> index = cas.getAnnotationIndex(TermAnnotation.type);
		FSIterator<Annotation> iterator = index.iterator();
		while (iterator.hasNext()) {
			TermAnnotation annotation = (TermAnnotation) iterator.next();
			if (this.isAllowed(cas, annotation.getType())) {
				if (annotation.getVariants() != null) {
					if (annotation.getOccurrences() < 2) {
						this.getAnnotations().remove(annotation);
					}
					for (int i = 0; i < annotation.getVariants().size(); i++) {
						TermAnnotation variant = annotation.getVariants(i);
						if (this.getAnnotations().contains(variant)) {
							this.getAnnotations().remove(variant);
						}
					}
				}
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
			if (this.isAllowed(cas, annotation.getType())) {
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
	
}

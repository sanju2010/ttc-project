package eu.project.ttc.engines;

import org.apache.uima.LemmaAnnotation;
import org.apache.uima.TreeTaggerAnnotation;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.cas.text.AnnotationIndex;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;

public class TreeTaggerLemmaAnnotator extends JCasAnnotator_ImplBase {
	
	@Override
	public void process(JCas cas) throws AnalysisEngineProcessException {
		AnnotationIndex<Annotation> index = cas.getAnnotationIndex(TreeTaggerAnnotation.type);
		FSIterator<Annotation> iter = index.iterator();
		while (iter.hasNext()) {
			Annotation annotation = iter.next();
			if (annotation instanceof TreeTaggerAnnotation) {
				TreeTaggerAnnotation tt = (TreeTaggerAnnotation) annotation;
				this.doAnnotate(cas,tt);
			}
		}
	}
	
	private void doAnnotate(JCas cas,TreeTaggerAnnotation annotation) {
		String lemma = annotation.getLemma();
		int index = lemma.indexOf("\\|");
		if (index == -1) {
			LemmaAnnotation lemmaAnnotation = new LemmaAnnotation(cas);
			lemmaAnnotation.setBegin(annotation.getBegin());
			lemmaAnnotation.setEnd(annotation.getEnd());
			lemmaAnnotation.setValue(lemma);
			lemmaAnnotation.addToIndexes();	
		} else {
			String[] values = lemma.split("\\|");
			int length = values.length;
			String value = values[length - 1];
			LemmaAnnotation lemmaAnnotation = new LemmaAnnotation(cas);
			lemmaAnnotation.setBegin(annotation.getBegin());
			lemmaAnnotation.setEnd(annotation.getEnd());
			lemmaAnnotation.setValue(value);
			lemmaAnnotation.addToIndexes();	
		}
	}

}

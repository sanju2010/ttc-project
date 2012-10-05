package eu.project.ttc.engines;

import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.cas.text.AnnotationIndex;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;

import eu.project.ttc.types.WordAnnotation;

public class ChineseNormalizer extends JCasAnnotator_ImplBase {

	@Override
	public void process(JCas cas) throws AnalysisEngineProcessException {
		try {
			AnnotationIndex<Annotation> index = cas.getAnnotationIndex(WordAnnotation.type);
			FSIterator<Annotation> iterator = index.iterator();
			while (iterator.hasNext()) {
				WordAnnotation annotation = (WordAnnotation) iterator.next();
				String norm = annotation.getCoveredText();
				annotation.setLemma(norm);
				annotation.setStem(norm);
			}
		} catch (Exception e) {
			throw new AnalysisEngineProcessException(e);
		}
	}

}

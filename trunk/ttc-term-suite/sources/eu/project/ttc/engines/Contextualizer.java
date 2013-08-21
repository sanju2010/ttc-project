package eu.project.ttc.engines;

import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.cas.FSArray;
import org.apache.uima.jcas.tcas.Annotation;

import eu.project.ttc.types.TermAnnotation;

public class Contextualizer extends uima.sandbox.contextualizer.engines.Contextualizer {

	@Override
	protected void annotate(JCas cas, Annotation annotation, Annotation[] annotations) {
		// System.out.println(annotation.getCoveredText());
		if (annotation instanceof TermAnnotation) {
			TermAnnotation term = (TermAnnotation) annotation;
			int length = annotations.length;
			FSArray array = new FSArray(cas,length);
			term.setContext(array);
			for (int index = 0; index < length; index ++) {
				TermAnnotation coTerm = (TermAnnotation) annotations[index];
				term.setContext(index, coTerm);
			}
		}
	}

}

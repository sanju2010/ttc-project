package eu.project.ttc.engines;

import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.cas.FSArray;
import org.apache.uima.jcas.tcas.Annotation;

import eu.project.ttc.types.TermAnnotation;
import eu.project.ttc.types.TermContextAnnotation;
import fr.free.rocheteau.jerome.engines.ContextBuilder;

public class TermContextBuilder extends ContextBuilder {

	@Override
	protected void annotate(JCas cas, Annotation annotation,Annotation[] annotations) {
		// System.out.println(annotation.getCoveredText() + " " + annotation.getBegin() + " " + annotation.getEnd() + " " + annotations.length);
		TermContextAnnotation vector = new TermContextAnnotation(cas);
		vector.setTerm((TermAnnotation) annotation);
		int length = annotations.length;
		FSArray array = new FSArray(cas,length);
		vector.setContext(array);
		for (int index = 0; index < length; index ++) {
			Annotation a = annotations[index];
			vector.setContext(index, (TermAnnotation) a);
		}
		vector.setBegin(annotation.getBegin());
		vector.setEnd(annotation.getEnd());
		vector.addToIndexes();
	}

}

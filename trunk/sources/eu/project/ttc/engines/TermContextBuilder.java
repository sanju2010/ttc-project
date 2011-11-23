package eu.project.ttc.engines;

import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.cas.FSArray;
import org.apache.uima.jcas.tcas.Annotation;

import eu.project.ttc.types.TermContextEntryAnnotation;
import eu.project.ttc.types.TermContextVectorAnnotation;
import fr.free.rocheteau.jerome.engines.ContextBuilder;

public class TermContextBuilder extends ContextBuilder {

	@Override
	protected void annotate(JCas cas, Annotation annotation,Annotation[] annotations) {
		// System.out.println(annotation.getCoveredText() + " " + annotation.getBegin() + " " + annotation.getEnd() + " " + annotations.length);
		TermContextVectorAnnotation vector = new TermContextVectorAnnotation(cas);
		vector.setTerm((TermContextEntryAnnotation) annotation);
		int length = annotations.length;
		FSArray array = new FSArray(cas,length);
		vector.setContextVector(array);
		for (int index = 0; index < length; index ++) {
			Annotation a = annotations[index];
			vector.setContextVector(index, (TermContextEntryAnnotation) a);
		}
		vector.setBegin(annotation.getBegin());
		vector.setEnd(annotation.getEnd());
		vector.addToIndexes();
	}

}

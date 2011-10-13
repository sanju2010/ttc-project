package fr.univnantes.lina.uima.engines;

import org.apache.uima.TermContextEntryAnnotation;
import org.apache.uima.TermContextVectorAnnotation;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.cas.FSArray;
import org.apache.uima.jcas.tcas.Annotation;

import fr.free.rocheteau.jerome.engines.ContextBuilder;

public class TermContextBuilder extends ContextBuilder {

	@Override
	protected void annotate(JCas cas, Annotation annotation,Annotation[] annotations) {
		TermContextVectorAnnotation vector = new TermContextVectorAnnotation(cas);
		vector.setTerm((TermContextEntryAnnotation) annotation);
		int length = annotations.length;
		FSArray array = new FSArray(cas,length);
		vector.setContextVector(array);
		int begin = -1;
		int end = -1;
		for (int index = 0; index < length; index ++) {
			Annotation a = annotations[index];
			if (begin == -1 || a.getBegin() < begin) {
				begin = a.getBegin();
			}
			if (end == -1 || end < a.getEnd()) {
				end = a.getEnd();
			} 
			vector.setContextVector(index, (TermContextEntryAnnotation) a);
		}
		vector.setBegin(begin);
		vector.setEnd(end);
		vector.addToIndexes();
	}

}

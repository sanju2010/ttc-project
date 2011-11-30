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
		TermContextAnnotation context = new TermContextAnnotation(cas);
		context.setTerm((TermAnnotation) annotation);
		int length = annotations.length;
		FSArray array = new FSArray(cas,length);
		context.setContext(array);
		for (int index = 0; index < length; index ++) {
			Annotation a = annotations[index];
			context.setContext(index, (TermAnnotation) a);
		}
		context.setBegin(annotation.getBegin());
		context.setEnd(annotation.getEnd());
		context.addToIndexes();
	}

}

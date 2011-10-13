package eu.project.ttc.engines;

import org.apache.uima.MultiWordTermAnnotation;
import org.apache.uima.SingleWordTermAnnotation;
import org.apache.uima.TermAnnotation;
import org.apache.uima.TermComponentAnnotation;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.cas.text.AnnotationIndex;
import org.apache.uima.examples.SourceDocumentInformation;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.cas.FSArray;
import org.apache.uima.jcas.tcas.Annotation;

import eu.project.ttc.types.WordAnnotation;
import fr.free.rocheteau.jerome.engines.PatternMatcher;

public class RuleBasedTermDetector extends PatternMatcher {
	
	private void setComponents(JCas cas,MultiWordTermAnnotation term, Annotation[] annotations) {
		int length = annotations.length;
		FSArray array = new FSArray(cas,length);
		term.setComponents(array);
		String lemma = "";
		String category = "";
		for (int index = 0; index < length; index++) {
			WordAnnotation a = (WordAnnotation) annotations[index];
			int begin = a.getBegin();
			int end = a.getEnd();
			TermComponentAnnotation component = new TermComponentAnnotation(cas, begin, end);
			component.setCategory(a.getCategory());
			category += " " + a.getCategory();
			component.setLemma(a.getLemma());
			lemma += " " + a.getLemma();
			component.addToIndexes();
			term.setComponents(index,component);
		}
		term.setCategory(category);
		term.setLemma(lemma);
	}
		
	@Override
	protected void annotate(JCas cas, int begin, int end,Annotation[] annotations) {
		TermAnnotation term = null;
		if (annotations.length == 1) {
			term = new SingleWordTermAnnotation(cas, begin, end);
			WordAnnotation word = (WordAnnotation) annotations[0];
			term.setCategory(word.getCategory());
			term.setLemma(word.getLemma());
			term.setComplexity("single-word");
		} else {
			term = new MultiWordTermAnnotation(cas, begin, end);
			this.setComponents(cas,(MultiWordTermAnnotation) term, annotations);
			term.setComplexity("multi-word");
		}
		term.setLanguage(cas.getDocumentLanguage());
		term.setDocument(this.getDocument(cas));
		term.addToIndexes();
	}
	
	private String getDocument(JCas cas) {
		AnnotationIndex<Annotation> index = cas.getAnnotationIndex(SourceDocumentInformation.type);
		FSIterator<Annotation> iterator = index.iterator();
		if (iterator.hasNext()) {
			SourceDocumentInformation annotation = (SourceDocumentInformation) iterator.next();
			return annotation.getUri();
		} else {
			return "";
		}
	}
	
}

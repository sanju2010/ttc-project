package eu.project.ttc.engines;

import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;

import eu.project.ttc.types.MultiWordTermAnnotation;
import eu.project.ttc.types.SingleWordTermAnnotation;
import eu.project.ttc.types.TermAnnotation;
import eu.project.ttc.types.WordAnnotation;
import fr.free.rocheteau.jerome.engines.PatternMatcher;

public class RuleBasedTermDetector extends PatternMatcher {
			
	@Override
	protected void annotate(JCas cas, String id, int begin, int end,Annotation[] annotations) {
		if (0 <= begin && begin < end && end <= cas.getDocumentText().length()) {
			TermAnnotation term = null;
			if (annotations.length == 1) {
				term = new SingleWordTermAnnotation(cas, begin, end);
				WordAnnotation word = (WordAnnotation) annotations[0];
				term.setCategory(word.getCategory());
				term.setLemma(word.getLemma());
				term.setComplexity("single-word");
			} else {
				term = new MultiWordTermAnnotation(cas, begin, end);
				/*
				for (int index = 0; index < annotations.length; index++) {
					WordAnnotation a = (WordAnnotation) annotations[index];
					
					TermComponentAnnotation component = new TermComponentAnnotation(cas, a.getBegin(), a.getEnd());
					component.setCategory(a.getCategory());
					component.setLemma(a.getLemma());
					component.addToIndexes();
					
				}
				*/
				term.setComplexity("multi-word");
				term.setCategory(id);
				term.setLemma(cas.getDocumentText().substring(begin, end).toLowerCase().trim());
			}
			term.addToIndexes();
		}
	}
		
}

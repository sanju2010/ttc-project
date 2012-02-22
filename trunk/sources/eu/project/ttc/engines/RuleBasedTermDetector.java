package eu.project.ttc.engines;

import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;

import uima.sandbox.matcher.engines.Matcher;

import eu.project.ttc.models.Term;
import eu.project.ttc.types.MultiWordTermAnnotation;
import eu.project.ttc.types.SingleWordTermAnnotation;
import eu.project.ttc.types.TermAnnotation;
import eu.project.ttc.types.WordAnnotation;

public class RuleBasedTermDetector extends Matcher {
			
	@Override
	protected void annotate(JCas cas, String id, int begin, int end,Annotation[] annotations) {
		if (0 <= begin && begin < end && end <= cas.getDocumentText().length()) {
			TermAnnotation term = null;
			if (annotations.length == 1) {
				term = new SingleWordTermAnnotation(cas, begin, end);
				WordAnnotation word = (WordAnnotation) annotations[0];
				term.setCategory(word.getCategory());
				term.setLemma(word.getLemma());
				term.setComplexity(Term.SINGLE_WORD);
			} else {
				term = new MultiWordTermAnnotation(cas, begin, end);
				String lemma = term.getCoveredText();
				/*
				for (int index = 0; index < annotations.length; index++) {
					WordAnnotation a = (WordAnnotation) annotations[index];
					lemma += " " + a.getCoveredText();
					
					TermComponentAnnotation component = new TermComponentAnnotation(cas, a.getBegin(), a.getEnd());
					component.setCategory(a.getCategory());
					component.setLemma(a.getLemma());
					component.addToIndexes();
					
				}
				*/
				term.setComplexity(Term.MULTI_WORD);
				term.setCategory(id);
				term.setLemma(lemma.trim());
			}
			term.addToIndexes();
			System.out.println(term.getLemma());
		}
	}
		
}

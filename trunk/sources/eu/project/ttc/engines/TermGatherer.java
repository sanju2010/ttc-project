package eu.project.ttc.engines;

import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.util.Level;

import uima.sandbox.catcher.engines.Catcher;

public class TermGatherer extends Catcher {

	@Override
	protected void release(JCas cas, String id, Annotation[] annotations) {
		String message = "Found " + annotations.length + " annotations with the rule: " + id;
		for (int index = 0; index < annotations.length; index++) {
			message += "\n\t" + annotations[index].getCoveredText();
		}
		this.getContext().getLogger().log(Level.INFO, message);
	}

}

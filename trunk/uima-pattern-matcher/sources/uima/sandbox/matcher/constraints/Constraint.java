package uima.sandbox.matcher.constraints;

import org.apache.uima.cas.FSMatchConstraint;
import org.apache.uima.jcas.JCas;

public interface Constraint extends Cloneable {
		
	public abstract FSMatchConstraint getConstraint(JCas cas);

}
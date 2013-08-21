package uima.sandbox.catcher.resources.constraint;

import java.util.Map;

import org.apache.uima.cas.Type;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;

import uima.sandbox.catcher.resources.Constraint;
import uima.sandbox.catcher.resources.Element;

public class True implements Constraint {

	@Override
	public int compareTo(Constraint constraint) {
		if (constraint instanceof Eq) {
			return -1;
		} else if (constraint instanceof Neq) {
			return -1;
		} else if (constraint instanceof Lt) {
			return -1;
		} else if (constraint instanceof Leq) {
			return -1;
		} else if (constraint instanceof Gt) {
			return -1;
		} else if (constraint instanceof Geq) {
			return -1;
		} else if (constraint instanceof True) {
			return 0;
		} else {
			return 1;
		}
	}

	@Override
	public boolean equals(Constraint constraint) {
		return this.compareTo(constraint) == 0;
	}

	@Override
	public Constraint subst(Map<String, Element> values) {
		return this;
	}

	@Override
	public boolean check(JCas cas, Map<String, Type> parameters) {
		return true;
	}

	@Override
	public boolean match(JCas cas, Map<String, Annotation> values) {
		return true;
	}
	
	@Override
	public String toString() {
		return "true";
	}
	
}

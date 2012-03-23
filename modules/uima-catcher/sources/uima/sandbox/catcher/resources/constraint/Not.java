package uima.sandbox.catcher.resources.constraint;

import java.util.Map;

import org.apache.uima.cas.Type;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;

import uima.sandbox.catcher.resources.Constraint;
import uima.sandbox.catcher.resources.Element;

public class Not implements Constraint {

	private Constraint constraint;
	
	public Constraint constraint() {
		return this.constraint;
	}
	
	public  Not(Constraint constraint) {
		this.constraint = constraint;
	}
	
	@Override
	public int compareTo(Constraint constraint) {
		if (constraint instanceof Exists) {
			return 1;
		} else if (constraint instanceof ForAll) {
			return 1;
		} else if (constraint instanceof Equiv) {
			return 1;
		} else if (constraint instanceof Imply) {
			return 1;
		} else if (constraint instanceof Or) {
			return 1;
		} else if (constraint instanceof And) {
			return 1;
		} else if (constraint instanceof Not) {
			Not not = (Not) constraint;
			return this.constraint.compareTo(not.constraint());
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
		Constraint constraint = this.constraint.subst(values);
		return new Not(constraint);
	}

	@Override
	public boolean check(JCas cas, Map<String, Type> parameters) {
		return this.constraint.check(cas, parameters);
	}

	@Override
	public boolean match(JCas cas, Map<String, Annotation> values) {
		return !this.constraint.match(cas, values);
	}
	
	@Override
	public String toString() {
		return "not " + this.constraint.toString();
	}
	
}

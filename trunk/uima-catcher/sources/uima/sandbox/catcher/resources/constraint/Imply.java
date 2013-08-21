package uima.sandbox.catcher.resources.constraint;

import java.util.Map;

import org.apache.uima.cas.Type;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;

import uima.sandbox.catcher.resources.Constraint;
import uima.sandbox.catcher.resources.Element;

public class Imply implements Constraint {
	
	private Constraint left;
	
	public Constraint left() {
		return this.left;
	}
	
	private Constraint right;
	
	public Constraint right() {
		return this.right;
	}
	
	public  Imply(Constraint left, Constraint right) {
		this.left = left;
		this.right = right;
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
			Imply imply = (Imply) constraint;
			int diff = this.left.compareTo(imply.left());
			if (diff == 0) {
				return this.right.compareTo(imply.right());
			} else {
				return diff;
			}
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
		Constraint left = this.left.subst(values);
		Constraint right = this.right.subst(values);
		return new Imply(left, right);
	}

	@Override
	public boolean check(JCas cas, Map<String, Type> parameters) {
		return this.left.check(cas, parameters) && this.right.check(cas, parameters);
	}

	@Override
	public boolean match(JCas cas, Map<String, Annotation> values) {
		return !this.left.match(cas, values) || this.right.match(cas, values);
	}	
	
	@Override
	public String toString() {
		return this.left.toString() + " => " + this.right.toString();
	}
	
}


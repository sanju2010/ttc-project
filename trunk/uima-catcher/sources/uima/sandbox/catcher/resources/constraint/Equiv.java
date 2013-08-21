package uima.sandbox.catcher.resources.constraint;

import java.util.Map;

import org.apache.uima.cas.Type;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;

import uima.sandbox.catcher.resources.Constraint;
import uima.sandbox.catcher.resources.Element;

public class Equiv implements Constraint {
	
	private Constraint left;
	
	public Constraint left() {
		return this.left;
	}
	
	private Constraint right;
	
	public Constraint right() {
		return this.right;
	}
	
	public  Equiv(Constraint left, Constraint right) {
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
			Equiv equiv = (Equiv) constraint;
			int diff = this.left.compareTo(equiv.left());
			if (diff == 0) {
				return this.right.compareTo(equiv.right());
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
		return new Equiv(left, right);
	}

	@Override
	public boolean check(JCas cas, Map<String, Type> parameters) {
		return this.left.check(cas, parameters) && this.right.check(cas, parameters);
	}

	@Override
	public boolean match(JCas cas, Map<String, Annotation> values) {
		return (this.left.match(cas, values) && this.right.match(cas, values)) 
				|| (!this.left.match(cas, values) && !this.right.match(cas, values));
	}	

	@Override
	public String toString() {
		return this.left.toString() + " <=> " + this.right.toString();
	}
	
}

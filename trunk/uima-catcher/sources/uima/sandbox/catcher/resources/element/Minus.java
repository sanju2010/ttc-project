package uima.sandbox.catcher.resources.element;

import java.util.Map;

import org.apache.uima.cas.Type;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;

import uima.sandbox.catcher.resources.Element;

public class Minus implements Element {

	private Element left;
	
	public Element left() {
		return this.left;
	}
	
	private Element right;
	
	public Element right() {
		return this.right;
	}
	
	public Minus(Element left,Element right) {
		this.left = left;
		this.right = right;
	}

	@Override
	public int compareTo(Element term) {
		if (term instanceof Variable) {
			return 1;
		} else if (term instanceof Integer) {
			return 1;
		} else if (term instanceof String) {
			return 1;
		} else if (term instanceof Feature) {
			return 1;
		} else if (term instanceof Plus) {
			return 1;
		} else if (term instanceof Minus) {
			Minus plus = (Minus) term;
			int diff = this.left.compareTo(plus.left());
			if (diff == 0) {
				return this.right.compareTo(plus.right());
			} else {
				return diff;
			}
		} else {
			return -1;
		}
	}

	@Override
	public boolean equals(Element term) {
		return this.compareTo(term) == 0;
	}

	@Override
	public Element subst(Map<java.lang.String, Element> values) {
		Element left = this.left.subst(values);
		Element right = this.right.subst(values);
		return new Minus(left,right);
	}

	@Override
	public boolean check(JCas cas, Map<java.lang.String, Type> variables, Type type) {
		return this.left.check(cas, variables, type) && this.right.check(cas, variables, type);
	}

	@Override
	public Object match(JCas cas, Map<java.lang.String, Annotation> values) {
		Object left = this.left.match(cas, values);
		Object right = this.right.match(cas, values);
		if (left instanceof java.lang.Integer && right instanceof java.lang.Integer) {
			java.lang.Integer first = (java.lang.Integer) left;
			java.lang.Integer second = (java.lang.Integer) right;
			return new java.lang.Integer(first - second);
		} else {
			return null;
		}
	}
	
	@Override
	public java.lang.String toString() {
		return this.left.toString() + " - " + this.right.toString();
	}
	
}

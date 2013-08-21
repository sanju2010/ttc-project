package uima.sandbox.catcher.resources.element;

import java.util.Map;

import org.apache.uima.cas.ArrayFS;
import org.apache.uima.cas.CAS;
import org.apache.uima.cas.Type;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;

import uima.sandbox.catcher.resources.Element;

public class Get implements Element {

	private Element left;
	
	public Element left() {
		return this.left;
	}
	
	private Element right;
	
	public Element right() {
		return this.right;
	}
	
	public Get(Element left,Element right) {
		this.left = left;
		this.right = right;
	}

	@Override
	public int compareTo(Element term) {
		if (term instanceof Sub) {
			return -1;
		} else if (term instanceof Get) {
			Get plus = (Get) term;
			int diff = this.left.compareTo(plus.left());
			if (diff == 0) {
				return this.right.compareTo(plus.right());
			} else {
				return diff;
			}
		} else {
			return 1;
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
		return new Get(left,right);
	}

	@Override
	public boolean check(JCas cas, Map<java.lang.String, Type> variables, Type type) {
		// System.out.println("check: " + this + " vs " + type.getName());
		Type integer = cas.getTypeSystem().getType(CAS.TYPE_NAME_INTEGER);
		if (this.right.check(cas, variables, integer)) {
			Type array = cas.getTypeSystem().getArrayType(type);
			return this.left.check(cas, variables, array);
		} else {
			return false;
		}
	}

	@Override
	public Object match(JCas cas, Map<java.lang.String, Annotation> values) {
		Object left = this.left.match(cas, values);
		if (left instanceof ArrayFS) {
			ArrayFS array = (ArrayFS) left;
			Object right = this.right.match(cas, values);
			if (right instanceof java.lang.Integer) {
				java.lang.Integer integer = (java.lang.Integer) right;
				// System.out.println("match " + this + "\t\t" + array.get(integer.intValue()));
				return array.get(integer.intValue());
			} else {
				// System.out.println("match " + this + "\t\t" + null);
				return null;
			}
		} else {
			// System.out.println("match " + this + "\t\t" + null);
			return null;
		}
	}
	
	@Override
	public java.lang.String toString() {
		return "get(" + this.left.toString() + " , " + this.right.toString() + ")";
	}
	
}

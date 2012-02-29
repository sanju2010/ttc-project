package uima.sandbox.catcher.resources.constraint;

import java.util.Iterator;
import java.util.Map;

import org.apache.uima.cas.Type;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;

import uima.sandbox.catcher.resources.Constraint;
import uima.sandbox.catcher.resources.Element;

public class Neq implements Constraint {

	private Element left;
	
	public Element left() {
		return this.left;
	}
	
	private Element right;
	
	public Element right() {
		return this.right;
	}
	
	public Neq(Element left,Element right) {
		this.left = left;
		this.right = right;
	}

	@Override
	public int compareTo(Constraint constraint) {
		if (constraint instanceof Eq) {
			Eq eq = (Eq) constraint;
			int diff = this.left.compareTo(eq.left());
			if (diff == 0) {
				return this.right.compareTo(eq.right());
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
		Element left = this.left.subst(values);
		Element right = this.right.subst(values);
		return new Eq(left, right);
	}

	@Override
	public boolean check(JCas cas, Map<String, Type> parameters) {
		Iterator<Type> iterator = cas.getTypeSystem().getTypeIterator();
		while (iterator.hasNext()) {
			Type type = iterator.next();
			if (type.isPrimitive()) {
				if (this.left.check(cas, parameters, type) && this.right.check(cas, parameters, type)) {
					// System.out.println("check OK: " + this);
					return true;
				}
			} 
		}
		return false;
	}

	@Override
	public boolean match(JCas cas, Map<String, Annotation> values) {
		Object left = this.left.match(cas, values);
		Object right = this.right.match(cas, values);
		if (left == null) {
			System.out.println("Null " + this.left);
			return false;
		} else if (right == null) {
			System.out.println("Null " + this.right);
			return false;
		} else if (left instanceof Type && right instanceof Type) {
			Type first = (Type) left;
			Type second = (Type) right;
			return first.getName().equals(second.getName());
		} else if (left instanceof Integer && right instanceof Integer) {
			Integer first = (Integer) left;
			Integer second = (Integer) right;
			return first.compareTo(second) != 0;
		} else 	if (left instanceof String && right instanceof String) {
			// System.out.println("match " + this + "\t\t" + left + " == " + right);
			String first = (String) left;
			String second = (String) right;
			return first.compareTo(second) != 0;
		} else {
			System.out.println("Not implemented " + left.getClass()  + " " + right.getClass());
			return false;
		}
	}
	
	@Override
	public String toString() {
		return this.left.toString() + " == " + this.right.toString();
	}
	
}

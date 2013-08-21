package uima.sandbox.catcher.resources.constraint;

import java.util.Iterator;
import java.util.Map;

import org.apache.uima.cas.Type;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;

import uima.sandbox.catcher.resources.Constraint;
import uima.sandbox.catcher.resources.Element;

public class Lt implements Constraint {

	private Element left;
	
	public Element left() {
		return this.left;
	}
	
	private Element right;
	
	public Element right() {
		return this.right;
	}
	
	public Lt(Element left,Element right) {
		this.left = left;
		this.right = right;
	}

	@Override
	public int compareTo(Constraint constraint) {
		if (constraint instanceof Eq) {
			return -1;
		} else if (constraint instanceof Lt) {
			Lt lt = (Lt) constraint;
			int diff = this.left.compareTo(lt.left());
			if (diff == 0) {
				return this.right.compareTo(lt.right());
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
		return new Lt(left, right);
	}

	@Override
	public boolean check(JCas cas, Map<String, Type> parameters) {
		Iterator<Type> iterator = cas.getTypeSystem().getTypeIterator();
		while (iterator.hasNext()) {
			Type type = iterator.next();
			if (this.left.check(cas, parameters, type) && this.right.check(cas, parameters, type)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean match(JCas cas, Map<String, Annotation> values) {
		Object left = this.left.match(cas, values);
		Object right = this.right.match(cas, values);
		if (left instanceof Type && right instanceof Type) {
			Type first = (Type) left;
			Type second = (Type) right;
			// FIXME: subsumes(ty,ty) ?
			return cas.getTypeSystem().subsumes(first, second);
		} else if (left instanceof Integer && right instanceof Integer) {
			Integer first = (Integer) left;
			Integer second = (Integer) right;
			return first.compareTo(second) < 0;
		} else 	if (left instanceof String && right instanceof String) {
			String first = (String) left;
			String second = (String) right;
			return first.compareTo(second) < 0;
		} else {
			return false;
		}
	}
	
	@Override
	public String toString() {
		return this.left.toString() + " < " + this.right.toString();
	}
	
}

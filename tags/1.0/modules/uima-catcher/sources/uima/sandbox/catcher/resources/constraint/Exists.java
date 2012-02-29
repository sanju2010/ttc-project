package uima.sandbox.catcher.resources.constraint;

import java.util.HashMap;
import java.util.Map;

import org.apache.uima.cas.CASException;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.text.AnnotationIndex;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;

import uima.sandbox.catcher.resources.Constraint;
import uima.sandbox.catcher.resources.Element;
import uima.sandbox.catcher.resources.element.Variable;

public class Exists implements Constraint {

	private String variable;
	
	public String variable() {
		return this.variable;
	}
	
	private String type;
	
	public String type() {
		return this.type;
	}
	
	private Constraint constraint;
	
	public Constraint constraint() {
		return this.constraint;
	}
	
	public Exists(String variable, String type, Constraint constraint) {
		this.variable = variable;
		this.type = type;
		this.constraint = constraint;
	}
	
	@Override
	public int compareTo(Constraint constraint) {
		if (constraint instanceof Exists) {
			Exists exists = (Exists) constraint;
			int diff = this.type.compareTo(exists.type());
			if (diff == 0) {
				Map<String, Element> values = new HashMap<String, Element>();
				String name = this.variable + "+" + exists.variable();
				values.put(name, new Variable(name));
				Constraint first = this.constraint.subst(values);
				Constraint second = exists.constraint().subst(values);
				return first.compareTo(second);
			} else {
				return diff;
			}
		} else {
			return -1;
		}
	}

	@Override
	public boolean equals(Constraint constraint) {
		return this.compareTo(constraint) == 0;
	}

	@Override
	public Constraint subst(Map<String, Element> values) {
		values.remove(this.variable);
		Constraint constraint = this.constraint.subst(values);
		return new Exists(this.variable, this.type, constraint);
	}

	@Override
	public boolean check(JCas cas, Map<String, org.apache.uima.cas.Type> parameters) {
		try {
			Type type = cas.getRequiredType(this.type);
			if (type.isPrimitive()) {
				return false;
			} else {
				parameters.put(this.variable, type);
				return this.constraint.check(cas, parameters);
			}
		} catch (CASException e) {
			return false;			
		}
	}

	@Override
	public boolean match(JCas cas, Map<String, Annotation> values) {
		try {
			Type type = cas.getRequiredType(this.type);
			AnnotationIndex<Annotation> index = cas.getAnnotationIndex(type);
			FSIterator<Annotation> iterator = index.iterator().copy();
			while(iterator.hasNext()) {
				Annotation annotation = iterator.next();
				values.put(this.variable, annotation);
				if (this.constraint.match(cas, values)) {
					return true;
				}
			}
			return false;
		} catch (CASException e) {
			return false;
		}
	}
	
	@Override
	public String toString() {
		return "exists "  + this.variable + " : " + this.type + " . " + this.constraint.toString();
	}

}

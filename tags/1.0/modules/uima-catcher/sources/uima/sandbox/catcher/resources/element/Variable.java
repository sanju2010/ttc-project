package uima.sandbox.catcher.resources.element;

import java.util.Map;

import org.apache.uima.cas.Type;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;

import uima.sandbox.catcher.resources.Element;

public class Variable implements Element {

	private java.lang.String name;
	
	public java.lang.String name() {
		return this.name;
	}
		
	public Variable(java.lang.String name) {
		this.name = name;
	}
	
	@Override
	public java.lang.String toString() {
		return this.name;
	}
	
	@Override
	public int compareTo(Element term) {
		if (term instanceof Variable) {
			Variable variable = (Variable) term;
			return this.name().compareTo(variable.name());	
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
		Element term = values.get(this.name);
		if (term == null) {
			return this;
		} else {
			return term;
		}
	}

	@Override
	public boolean check(JCas cas, Map<java.lang.String, Type> variables, Type type) {
		Type ty = variables.get(this.name);
		if (ty == null) {
			return false;
		} else {
			// TODO array, etc
			// System.out.println("check: " + this + " vs " + ty.getName() + " < " + type.getName() + " " + cas.getTypeSystem().subsumes(type, ty));
			return cas.getTypeSystem().subsumes(type, ty);
			// return ty.getName().equals(type.getName());
		}
	}

	@Override
	public Object match(JCas cas, Map<java.lang.String, Annotation> values) {
		// System.out.println("match " + this + "\t\t" + values.get(this.name).getCoveredText());
		return values.get(this.name);
	}
	
}

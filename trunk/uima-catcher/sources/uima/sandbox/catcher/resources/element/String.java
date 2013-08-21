package uima.sandbox.catcher.resources.element;

import java.util.Map;

import org.apache.uima.cas.Type;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;

import uima.sandbox.catcher.resources.Element;

public class String implements Element {

	private java.lang.String value;
	
	public java.lang.String value() {
		return this.value;
	}
	
	public String(java.lang.String value) {
		this.value = value;
	}
	
	@Override
	public java.lang.String toString() {
		return this.value;
	}
	
	@Override
	public boolean equals(Element term) {
		return this.compareTo(term) == 0;
	}
	
	@Override
	public int compareTo(Element term) {
		if (term instanceof Variable) {
			return 1;
		} else if (term instanceof Integer) {
			return 1;
		} else if (term instanceof String) {
			String string = (String) term;
			return this.value.compareTo(string.value());
		} else {
			return -1;
		}
	}

	@Override
	public Element subst(Map<java.lang.String, Element> values) {
		return this;
	}

	@Override
	public boolean check(JCas cas, Map<java.lang.String, Type> variables, Type type) {
		return type.getName().equals("uima.cas.String");
	}

	@Override
	public Object match(JCas cas, Map<java.lang.String, Annotation> values) {
		// System.out.println("match " + this + "\t\t" + this.value);
		return this.value;
	}
	
}

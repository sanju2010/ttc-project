package uima.sandbox.catcher.resources.element;

import java.math.BigInteger;
import java.util.Map;

import org.apache.uima.cas.CAS;
import org.apache.uima.cas.Type;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;

import uima.sandbox.catcher.resources.Element;

public class Integer implements Element {

	private java.lang.Integer value;
	
	public java.lang.Integer value() {
		return this.value;
	}
	
	public Integer(java.lang.Integer value) {
		this.value = value;
	}
	
	public Integer(int value) {
		this(new java.lang.Integer(value));
	}

	public Integer(BigInteger value) {
		this(value.intValue());
	}
	
	@Override
	public java.lang.String toString() {
		return this.value.toString();
	}
	
	@Override
	public boolean equals(Element term) {
		return this.compareTo(term) == 0;
	}
	
	@Override
	public int compareTo(Element term) {
		if (term instanceof Variable || term instanceof String) {
			return 1;
		} else if (term instanceof Integer) {
			Integer integer = (Integer) term;
			return this.value.compareTo(integer.value());
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
		// System.out.println("check " + this + " " + type.getName() + " == "+ CAS.TYPE_NAME_INTEGER);
		return type.getName().equals(CAS.TYPE_NAME_INTEGER);
	}

	@Override
	public Object match(JCas cas, Map<java.lang.String, Annotation> values) {
		// System.out.println("match " + this + "\t\t" + this.value);
		return this.value;
	}
	
}

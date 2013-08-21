package uima.sandbox.catcher.resources;

import java.util.Map;

import org.apache.uima.cas.Type;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;

public interface Constraint extends Comparable<Constraint> {
	
	public boolean equals(Constraint constraint);
	
	public Constraint subst(Map<String, Element> values);
	
	public boolean check(JCas cas, Map<String, Type> parameters);
	
	public boolean match(JCas cas, Map<String, Annotation> values);
	
}

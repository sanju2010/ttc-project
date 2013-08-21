package uima.sandbox.catcher.resources;

import java.util.Map;

import org.apache.uima.cas.Type;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;

public interface Element extends Comparable<Element> {
	
	public boolean equals(Element term);
	
	public Element subst(Map<String, Element> values);
	
	public boolean check(JCas cas, Map<String, Type> variables, Type type);
	
	public Object match(JCas cas, Map<String, Annotation> values);
	
}

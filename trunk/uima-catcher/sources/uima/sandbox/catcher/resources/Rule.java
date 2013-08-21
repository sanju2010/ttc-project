package uima.sandbox.catcher.resources;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.uima.cas.CASException;
import org.apache.uima.cas.Type;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;

import uima.sandbox.catcher.resources.Structure;


public class Rule {

	private String id;
	
	public String id() {
		return this.id;
	}
	
	private Map<String, String> parameters;
		
	public Map<String, String> parameters() {
		return this.parameters;
	}
	
	private Constraint constraint;
		
	public Constraint constraint() {
		return this.constraint;
	}
	
	public Rule(String id, Map<String, String> parameters, Constraint constraint) {
		this.id = id;
		this.parameters = parameters;
		this.constraint = constraint;
	}

	public boolean check(JCas cas) {
		try {
			Map<String, Type> parameters = new HashMap<String, Type>();
			for (String name : this.parameters.keySet()) {
				String ty = this.parameters.get(name);
				Type type = cas.getRequiredType(ty);
				parameters.put(name, type);
			}
			return this.constraint.check(cas, parameters);
		} catch (CASException e) {
			return false;
		}
	}
	
	private List<List<Annotation>> results;
	
	private void set(List<List<Annotation>> results) {
		this.results = results;
	}
	
	public List<List<Annotation>> get() {
		return this.results;
	}
	
	public boolean match(JCas cas) throws Exception {
		Structure struct = new Structure();
		boolean result = struct.process(cas,this.parameters(),this.constraint());
		this.set(struct.release());
		return result;
	}

	public boolean check(JCas cas, List<Annotation> annotations) {
		try {
			int index = 0;
			Map<String, Type> parameters = new HashMap<String, Type>();
			for (String name : this.parameters().keySet()) {
				Annotation annotation = annotations.get(index);
				parameters.put(name, annotation.getType());
				index++;
			}
			return this.constraint().check(cas, parameters);
		} catch (Exception e) {
			return false;
		}
	}
	
	public boolean match(JCas cas, List<Annotation> annotations) {
		try {
			int index = 0;
			Map<String, Annotation> values = new HashMap<String, Annotation>();
			for (String name : this.parameters().keySet()) {
				Annotation annotation = annotations.get(index);
				values.put(name, annotation);
				index++;
			}
			boolean match = this.constraint().match(cas, values);
			ArrayList<List<Annotation>> list = new ArrayList<List<Annotation>>();
			list.add(annotations);
			this.set(list);
			return match;
		} catch (Exception e) {
			return false;
		}
	}
	
	public boolean check(JCas cas, Annotation first, Annotation second) {
		List<Annotation> annotations = new ArrayList<Annotation>();
		annotations.add(first);
		annotations.add(second);
		return this.check(cas, annotations);
	}
	
	public boolean match(JCas cas, Annotation first, Annotation second) {
		List<Annotation> annotations = new ArrayList<Annotation>();
		annotations.add(first);
		annotations.add(second);
		return this.match(cas, annotations);
	}
	
}

package uima.sandbox.catcher.resources;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.apache.uima.cas.CASException;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.text.AnnotationIndex;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;

public class Structure {
	
	private List<FSIterator<Annotation>> indexes;
	
	private List<FSIterator<Annotation>> indexes() {
		return this.indexes;
	}
	
	private List<String> names;
		
	private List<String> names() {
		return this.names;
	}
	
	private List<Map<String, Annotation>> values;
	
	private void execute(JCas cas, Constraint constraint) throws Exception {
		Map<String, Annotation> values = this.next();
		try {
			if (constraint.match(cas, values)) {
				Map<String, Annotation> result = new HashMap<String, Annotation>();
				for (String name : values.keySet()) {
					if (this.names().contains(name)) {
						Annotation a = values.get(name);
						result.put(name,a);
					}
				}
				this.values.add(result);
			}
		} catch (Exception e) {
			throw e;
		}
	}
	
	private void compute(JCas cas, Constraint constraint) throws Exception {
		try {
			while (true) { 
				this.execute(cas, constraint); 
			}
		} catch (InterruptedException e) { 
			// ignore
		}
	}
	
	private Map<String, Annotation> get() throws InterruptedException {
		try {
			Map<String, Annotation> values = new HashMap<String, Annotation>();
			for (int i = 0; i < this.indexes().size(); i++) {
				String name = this.names.get(i);
				FSIterator<Annotation> index = this.indexes().get(i);
				Annotation a = index.get();
				values.put(name,a);
			}
			return values;
		} catch (NoSuchElementException e) {
			throw new InterruptedException(e.getMessage());
		}
	}
	
	private boolean done = false;
	
	private void move(int i) throws InterruptedException {
		if (i >= 0) {
			FSIterator<Annotation> index = this.indexes().get(i);
			index.moveToNext();
			if (index.isValid()) {
			} else {
				if (this.done) {
					index.moveToFirst();
					this.move(i-1);
					this.done = false;
				} else {
					index.moveToLast();
					this.done = true;
				}
			}
		} else {
			throw new InterruptedException();
		}
	}
	
	private Map<String, Annotation> next() throws InterruptedException {
		Map<String, Annotation> next = get();
		this.move(this.indexes().size()-1);
		return next;
	}
		
	private List<List<Annotation>> results;
	
	public List<List<Annotation>> release() {
		return this.results;
	}

	private void set(JCas cas, Map<String, String> parameters) throws CASException {
		this.names = new ArrayList<String>();
		this.results = new ArrayList<List<Annotation>>();
		this.values = new ArrayList<Map<String, Annotation>>();
		this.indexes = new ArrayList<FSIterator<Annotation>>();
		for (String parameter : parameters.keySet()) {
			Type type = cas.getRequiredType(parameters.get(parameter));
			 AnnotationIndex<Annotation> index = cas.getAnnotationIndex(type);
			this.indexes.add(index.iterator().copy());
			this.names.add(parameter);
		}
	}
	
	public boolean process(JCas cas, Map<String, String> parameters, Constraint constraint) throws Exception {
		this.set(cas, parameters);		
		this.compute(cas, constraint);
		if (this.values.size() > 0) {
			for (Map<String, Annotation> values : this.values) {
				List<Annotation> result = new ArrayList<Annotation>();
				for (String name : values.keySet()) {
					Annotation a = values.get(name);
					result.add(a);
				}
				this.release().add(result);
			}
			return true;
		} else {
			return false;
		}
	}
	
}

package uima.sandbox.catcher.resources.element;

import java.util.Map;

import org.apache.uima.cas.ArrayFS;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;

import uima.sandbox.catcher.resources.Element;

public class SizeFeature extends Feature {

	public SizeFeature(Element feature) {
		super(feature,"size");
	}

	@Override
	public Object match(JCas cas, Map<java.lang.String, Annotation> values) {
		Object object = this.term.match(cas, values);
		if (object instanceof ArrayFS) {
			ArrayFS annotation = (ArrayFS) object;
			return annotation.size();
		} else {
			return null;
		}
	}
	
}

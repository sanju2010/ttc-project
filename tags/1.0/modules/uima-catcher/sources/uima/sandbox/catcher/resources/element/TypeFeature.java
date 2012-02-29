package uima.sandbox.catcher.resources.element;

import java.util.Map;

import org.apache.uima.cas.text.AnnotationFS;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;

import uima.sandbox.catcher.resources.Element;

public class TypeFeature extends Feature {
	
	public TypeFeature(Element feature) {
		super(feature,"type");
	}

	@Override
	public Object match(JCas cas, Map<java.lang.String, Annotation> values) {
		Object object = this.term.match(cas, values);
		if (object instanceof AnnotationFS) {
			AnnotationFS annotation = (AnnotationFS) object;
			return annotation.getType();
		} else {
			return null;
		}
	}
	
}

package uima.sandbox.catcher.resources.element;

import java.util.Map;

import org.apache.uima.cas.CAS;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.text.AnnotationFS;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;

import uima.sandbox.catcher.resources.Element;

public class CoveredTextFeature extends Feature {
	
	public CoveredTextFeature(Element feature) {
		super(feature,"coveredText");
	}

	@Override
	public boolean check(JCas cas, Map<java.lang.String, Type> variables, Type type) {
		// System.out.println("Check: " + this + " vs " + type.getName() + " == " + CAS.TYPE_NAME_STRING);
		Type string = cas.getTypeSystem().getType(CAS.TYPE_NAME_STRING);
		if (type.getName().equals(string.getName())) {
			Type ty = cas.getTypeSystem().getType(CAS.TYPE_NAME_ANNOTATION);
			return this.term.check(cas, variables, ty);
		} else {
			return false;
		}
	}
	
	@Override
	public Object match(JCas cas, Map<java.lang.String, Annotation> values) {
		Object object = this.term.match(cas, values);
		if (object instanceof AnnotationFS) {
			AnnotationFS annotation = (AnnotationFS) object;
			// System.out.println("match " + this + "\t\t" + annotation.getCoveredText());
			return annotation.getCoveredText();
		} else {
			// System.out.println("match " + this + "\t\t" + null);
			return null;
		}
	}
	
}

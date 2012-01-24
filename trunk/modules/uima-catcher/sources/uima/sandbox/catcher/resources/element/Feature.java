package uima.sandbox.catcher.resources.element;

import java.util.Iterator;
import java.util.Map;

import org.apache.uima.cas.CAS;
import org.apache.uima.cas.FeatureStructure;
import org.apache.uima.cas.Type;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;

import uima.sandbox.catcher.resources.Element;

public class Feature implements Element {

	protected Element term;
	
	public Element term() {
		return this.term;
	}
	
	private java.lang.String name;
	
	public java.lang.String name() {
		return this.name;
	}
	
	public Feature(Element term,java.lang.String name) {
		this.term = term;
		this.name = name;
	}

	@Override
	public java.lang.String toString() {
		return this.term.toString() + ":" + this.name;
	}
	
	@Override
	public int compareTo(Element term) {
		if (term instanceof Variable) {
			return 1;
		} else if (term instanceof Integer) {
			return 1;
		} else if (term instanceof String) {
			return 1;
		} else if (term instanceof Feature) {
			Feature feature = (Feature) term;
			int diff = this.name.compareTo(feature.name());
			if (diff == 0) {
				return this.term.compareTo(feature.term());
			} else {
				return diff;
			}
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
		Element term = this.term.subst(values);
		return new Feature(term, this.name);
	}

	@Override
	public boolean check(JCas cas, Map<java.lang.String, Type> variables, Type type) {
		Iterator<Type> iterator = cas.getTypeSystem().getTypeIterator();
		while (iterator.hasNext()) {
			Type dom = iterator.next();
			if (!dom.isPrimitive() && !type.isArray()) {
				// System.out.println(this.term + " vs " + dom.getName());
				if (this.term.check(cas, variables, dom)) {
					org.apache.uima.cas.Feature feat = dom.getFeatureByBaseName(this.name);
					if (feat != null) {
						Type cod = feat.getRange();	
						if (cas.getTypeSystem().subsumes(cod, type)) {
							return true;
						}
					} 
				}				
			}
		}
		return false;
	}
	
	@Override
	public Object match(JCas cas, Map<java.lang.String, Annotation> values) {
		Object object = this.term.match(cas, values);
		if (object instanceof FeatureStructure) {
			FeatureStructure fs = (FeatureStructure) object;
			org.apache.uima.cas.Type type = fs.getType();
			org.apache.uima.cas.Feature feat = type.getFeatureByBaseName(this.name);
			if (feat == null) {
				// System.out.println("match " + this + "\t\t" + null);
				return null;
			} else {
				java.lang.String name = feat.getRange().getName();
				if (name.equals(CAS.TYPE_NAME_INTEGER)) {
					// System.out.println("match " + this + "\t\t" + fs.getIntValue(feat));
					return fs.getIntValue(feat);
				} else if (name.equals(CAS.TYPE_NAME_STRING)) {
					// System.out.println("match " + this + "\t\t" + fs.getStringValue(feat));
					return fs.getStringValue(feat);
				} else {
					// System.out.println("match " + this + "\t\t" + null);
					return null;
				}
			}
		} else {
			System.out.println("match " + this + "\t\t" + null);
			return null;
		}
	}
	
}

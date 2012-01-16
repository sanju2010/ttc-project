package uima.sandbox.catcher.resources.element;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.uima.cas.CASException;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.text.AnnotationFS;
import org.apache.uima.cas.text.AnnotationIndex;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.cas.FSArray;
import org.apache.uima.jcas.tcas.Annotation;

import uima.sandbox.catcher.resources.Element;

public class Sub implements Element {

	private Element term;
	
	public Element term() {
		return this.term;
	}
	
	private java.lang.String type;
	
	public java.lang.String type() {
		return this.type;
	}
	
	public Sub(java.lang.String type, Element term) {
		this.term = term;
		this.type = type;
	}

	@Override
	public int compareTo(Element term) {
		if (term instanceof Sub) {
			Sub sub = (Sub) term;
			int diff = this.type.compareTo(sub.type());
			if (diff == 0) {
				return this.term.compareTo(sub.term());
			} else {
				return diff;
			}
		} else {
			return 1;
		}
	}

	@Override
	public boolean equals(Element term) {
		return this.compareTo(term) == 0;
	}

	@Override
	public Element subst(Map<java.lang.String, Element> values) {
		Element term = this.term.subst(values);
		return new Sub(this.type, term);
	}

	@Override
	public boolean check(JCas cas, Map<java.lang.String, Type> variables, Type type) {
		// System.out.println("check: " + this + " vs " + type.getName());
		if (type.isArray()) {
			Type ty = type.getComponentType();
			Type tau = cas.getTypeSystem().getType(this.type);
			if (tau != null && cas.getTypeSystem().subsumes(ty, tau)) {
				// System.out.println("checked: " + tau.getName() + " < " + ty.getName());
				return this.term.check(cas, variables, ty);
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	@Override
	public Object match(JCas cas, Map<java.lang.String, Annotation> values) {
		try {
			Object object = this.term.match(cas, values);
			if (object instanceof AnnotationFS) {
				AnnotationFS annotation = (AnnotationFS) object;
				Type type = cas.getRequiredType(this.type);
				AnnotationIndex<Annotation> index = cas.getAnnotationIndex(type);
				FSIterator<Annotation> iterator = index.subiterator(annotation);
				List<AnnotationFS> list = new ArrayList<AnnotationFS>();
				while (iterator.hasNext()) {
					AnnotationFS a = iterator.next();
					list.add(a);
				}
				int length = list.size();
				// System.out.println("match " + this + "\t\tsize = " + length);
				AnnotationFS[] subannotations = new AnnotationFS[length];
				list.toArray(subannotations);
				FSArray array = new FSArray(cas, length);
				array.copyFromArray(subannotations, 0, 0, length);
				// java.lang.String str = "{";
				// for (AnnotationFS a : subannotations) {
					// str += a.getCoveredText() + ", ";
				// }
				// str += "}";
				// System.out.println("match " + this + "\t\t" + str);
				return array;
			} else {
				// System.out.println("match " + this + "\t\t" + null);
				return null;
			}
		} catch (CASException e) {
			// System.out.println("match " + this + "\t\t" + null);
			return null;
		}
	}

	@Override
	public java.lang.String toString() {
		return "subiterator(" + this.type +", " + this.term.toString() + ")";
	}
	
}

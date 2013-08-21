package uima.sandbox.matcher.constraints;

import org.apache.uima.cas.ConstraintFactory;
import org.apache.uima.cas.FSMatchConstraint;
import org.apache.uima.cas.FSTypeConstraint;
import org.apache.uima.cas.Type;
import org.apache.uima.jcas.JCas;

public class TypeConstraint implements Constraint {
	
	private String name;
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
	protected Type getType(JCas cas) {
		return cas.getTypeSystem().getType(this.name);
	}
	
	@Override
	public FSMatchConstraint getConstraint(JCas cas) {
		ConstraintFactory factory = cas.getConstraintFactory();
		FSTypeConstraint constraint = factory.createTypeConstraint();
		constraint.add(this.name);
		return constraint;
	}
	
	public String toString() {
		return "type = " + this.name;
	}
	
	public TypeConstraint clone() {
		try {
			return (TypeConstraint) super.clone();
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}
	
}

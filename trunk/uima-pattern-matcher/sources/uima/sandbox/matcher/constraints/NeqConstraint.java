package uima.sandbox.matcher.constraints;

import org.apache.uima.cas.ConstraintFactory;
import org.apache.uima.cas.FSMatchConstraint;
import org.apache.uima.cas.FSStringConstraint;
import org.apache.uima.cas.Feature;
import org.apache.uima.cas.FeaturePath;
import org.apache.uima.cas.Type;
import org.apache.uima.jcas.JCas;

public class NeqConstraint implements Constraint {
	
	private String type;
	
	public void setType(String type) {
		this.type = type;
	}

	protected Type getType(JCas cas) {
		return cas.getTypeSystem().getType(this.type);
	}
	
	private String name;
	
	private String value;
		
	public NeqConstraint(String name,String value) {
		this.name = name;
		this.value = value;
	}
	
	@Override
	public FSMatchConstraint getConstraint(JCas cas) {
		if (this.name.equals("coveredText")) {
			CoveredTextConstraint constraint = new CoveredTextConstraint();
			constraint.equals(this.value);
			return new NotConstraint(constraint);
		} else {
			Type type = cas.getTypeSystem().getType(this.type);
			ConstraintFactory factory = cas.getConstraintFactory();
			FSStringConstraint constraint = factory.createStringConstraint();
			constraint.equals(this.value);
			FeaturePath path = cas.createFeaturePath();
			if (type == null) {
				throw new NullPointerException(this.type + ":" + this.name + " " + this.value);
			}
			Feature feature = type.getFeatureByBaseName(this.name);
			path.addFeature(feature);
			return new NotConstraint(factory.embedConstraint(path,constraint));
		}
	}
	
	public String toString() {
		return this.name + "=" + this.value;
	}

	public NeqConstraint clone() {
		try {
			return (NeqConstraint) super.clone();
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}
	
}

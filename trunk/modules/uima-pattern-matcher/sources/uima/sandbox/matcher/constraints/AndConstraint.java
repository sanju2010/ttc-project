package uima.sandbox.matcher.constraints;

import java.util.ArrayList;
import java.util.List;

import org.apache.uima.cas.FSMatchConstraint;
import org.apache.uima.cas.Type;
import org.apache.uima.jcas.JCas;

public class AndConstraint implements Constraint {
	
	private String type;
	
	public void setType(String type) {
		this.type = type;
	}

	protected Type getType(JCas cas) {
		return cas.getTypeSystem().getType(this.type);
	}
	
	private List<Constraint> constraints;
	
	private void setConstraints() {
		this.constraints = new ArrayList<Constraint>();
	}
	
	public List<Constraint> getConstraints() {
		return this.constraints;
	}
		
	public AndConstraint() {
		this.setConstraints();
	}
	
	@Override
	public FSMatchConstraint getConstraint(JCas cas) {
		FSMatchConstraint match = null;
		for (Constraint constraint : this.getConstraints()) {
			FSMatchConstraint cst = constraint.getConstraint(cas);
			if (match == null) {
				match = cst;
			} else {
				match = cas.getConstraintFactory().and(match,cst);
			}
		}
		return match;
	}
	
	public String toString() {
		StringBuilder builder = new StringBuilder();
		int size = this.getConstraints().size();
		for (int index = 0; index < size; index++) {
			Constraint constraint = this.getConstraints().get(index);
			builder.append(constraint.toString());
			if (index < size - 1) {
				builder.append(",");
			}
		}
		return builder.toString();
	} 
	
	public AndConstraint clone() {
		try {
			return (AndConstraint) super.clone();
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}
	
}
package uima.sandbox.matcher.constraints;

import org.apache.uima.cas.FSMatchConstraint;
import org.apache.uima.cas.FeatureStructure;

public class NotConstraint implements FSMatchConstraint {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8391314330948221429L;
	
	private FSMatchConstraint constraint;
	
	public NotConstraint(FSMatchConstraint constraint) {
		this.constraint = constraint;
	}
	
	@Override
	public boolean match(FeatureStructure fs) {
		return !this.constraint.match(fs);
	}

}

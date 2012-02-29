package uima.sandbox.matcher.constraints;

import org.apache.uima.cas.FSMatchConstraint;
import org.apache.uima.cas.FeatureStructure;
import org.apache.uima.jcas.tcas.Annotation;

public class CoveredTextConstraint implements FSMatchConstraint {

	private static final long serialVersionUID = -4313090906799864342L;
	
	private String coveredText;

	public void equals(String coveredText) {
		this.coveredText = coveredText;
	}

	@Override
	public boolean match(FeatureStructure fs) {
		if (this.coveredText == null || fs == null) {
			return false;
		} else if (fs instanceof Annotation) { 
			Annotation annotation = (Annotation) fs; 
			String coveredText = annotation.getCoveredText(); 
			return coveredText.equals(this.coveredText);
		} else {
			return false;
		}
	}
	
	public String toString() {
		return "coveredText = " + this.coveredText;
	}

} 

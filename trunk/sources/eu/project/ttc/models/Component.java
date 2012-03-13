package eu.project.ttc.models;

import eu.project.ttc.types.TermComponentAnnotation;

public class Component {

	private String category;
	private String lemma;
	private String stem;

	public int length() {
		return this.lemma.length();
	}
	
	public void release(TermComponentAnnotation annotation) {
		annotation.setCategory(this.category);
		annotation.setLemma(this.lemma);
		annotation.setStem(this.stem);
	}
	
	public void update(TermComponentAnnotation annotation) {
		this.category = annotation.getCategory();
		this.lemma = annotation.getLemma();
		this.stem = annotation.getStem();
	}
	
}

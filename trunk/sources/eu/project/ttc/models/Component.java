package eu.project.ttc.models;

import eu.project.ttc.types.TermComponentAnnotation;

public class Component {

	private String category;
	private String lemma;
	private String stem;
	private int begin;
	private int end;
	
	public void release(TermComponentAnnotation annotation, int offset) {
		annotation.setBegin(offset + this.begin);
		annotation.setEnd(offset + this.end);
		annotation.setCategory(this.category);
		annotation.setLemma(this.lemma);
		annotation.setStem(this.stem);
	}
	
	public void update(TermComponentAnnotation annotation,int offset) {
		this.category = annotation.getCategory();
		this.lemma = annotation.getLemma();
		this.stem = annotation.getStem();
		this.begin = annotation.getBegin() - offset;
		this.end = annotation.getEnd() - offset;
	}
	
}

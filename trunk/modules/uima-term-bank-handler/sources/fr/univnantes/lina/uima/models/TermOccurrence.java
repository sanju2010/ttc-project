package fr.univnantes.lina.uima.models;

import java.io.Serializable;

public class TermOccurrence implements Comparable<TermOccurrence>, Serializable {
	
	private static final long serialVersionUID = 876513076663548395L;
	
	private String coveredText;
	
	public String text() {
		return this.coveredText;
	}
	
	private String identifier;
	
	public String identifier() {
		return this.identifier;
	}
	
	private Integer begin;
	
	public int begin() {
		return this.begin.intValue();
	}
	
	private Integer end;
	
	public int end() {
		return this.end.intValue();
	}
	
	public TermOccurrence(String coveredText,String identifier,Integer begin,Integer end) {
		this.coveredText = coveredText;
		this.identifier = identifier;
		this.begin = begin;
		this.end = end;
	}

	public String toString() {
		return this.coveredText;
	}
	
	@Override
	public int compareTo(TermOccurrence occurrence) {
		int diff = this.coveredText.compareTo(occurrence.coveredText);
		if (diff == 0) {
			diff = this.identifier.compareTo(occurrence.identifier);
			if (diff == 0) {
				diff = this.begin.compareTo(occurrence.begin);
				if (diff == 0) {
					return this.end.compareTo(occurrence.end);
				} else {
					return diff;
				}
			} else {
				return diff;
			}
		} else {
			return diff;
		}
	}
}
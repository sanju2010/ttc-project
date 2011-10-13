package fr.univnantes.lina.uima.models;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class Term implements Comparable<Term>, Serializable {
	
	private static final long serialVersionUID = 4152965011835414401L;
	
	private String category;
	
	public String category() {
		return this.category;
	}
	
	private String lemma;
	
	public String lemma() {
		return this.lemma;
	}
	
	private String complexity;
	
	public String complexity() {
		return this.complexity;
	}
	
	private String language;
	
	public String language() {
		return this.language;
	}
	
	private Set<TermOccurrence> occurrences;

	public Set<TermOccurrence> occurrences() {
		return this.occurrences;
	}
	
	public Term(String category,String lemma,String complexity,String language) {
		this.category = category;
		this.lemma = lemma;
		this.complexity = complexity;
		this.language = language;
		this.occurrences = new HashSet<TermOccurrence>();
	}
	
	public void add(TermOccurrence occurrence) {
		this.occurrences.add(occurrence);
	}
	public int size() {
		return this.occurrences.size();
	}
	
	@Override
	public int compareTo(Term term) {
		return this.lemma.compareTo(term.lemma);
		/*
		int diff = this.language.compareTo(term.language);
		if (diff == 0) {
			diff = this.complexity.compareTo(term.complexity);
			if (diff == 0) {
				diff = this.lemma.compareTo(term.lemma);
				if (diff == 0) {
					return this.category.compareTo(term.category);
				} else {
					return diff;
				}
			} else {
				return diff;
			}
		} else {
			return diff;
		}
		*/
	}
	
	@Override
	public boolean equals(Object object) {
		if (object instanceof Term) {
			Term term = (Term) object;
			return this.compareTo(term) == 0;
		} else {
			return false;
		}
		
	}
	
	public String toString() {
		return this.lemma;
	}
	
}

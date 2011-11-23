package eu.project.ttc.models;

import java.util.HashMap;
import java.util.Map;

public class CrossTable {
	
	private Map<String, Integer> scores;
	private Map<String, Integer> coScores;
	private int total;
	
	private void addScores(Double value,String term,Map<String,Integer> scores) {
		Integer score = scores.get(term);
		if (score == null) {
			score = new Integer(value.intValue());
		} else {
			score = new Integer(score.intValue() + value.intValue());
		}
		scores.put(term, score);

	}

	private void set() {
		this.scores = new HashMap<String, Integer>();
		this.coScores = new HashMap<String, Integer>();
		this.total = 0;
		for (String term : this.index.getTermContexts().keySet()) {
			TermContext context = this.index.getTermContexts().get(term);
			for (String coTerm : context.getCoOccurrences().keySet()) {
				Double score = context.getCoOccurrences().get(coTerm);
				if (score != null) {
					this.total += score.intValue();
					this.addScores(score, term, this.scores);
					this.addScores(score, coTerm, this.coScores);
				}
			}
		}
	}
	
	private TermContextIndex index;

	public void setIndex(TermContextIndex index) {
		this.index = index;
		this.set();
	}
	
	private String term;
	
	public void setTerm(String term) {
		this.term = term;
	}
	
	private String coTerm;
	
	public void setCoTerm(String coTerm) {
		this.coTerm = coTerm;
	}
	
	private int a;
	
	private void setA() {
		Double score = this.index.getTermContexts().get(this.term).getCoOccurrences().get(this.coTerm);
		if (score == null) {
			throw new NullPointerException(this.index.getLanguage() + ": term " + this.term + "[" + this.coTerm + "]");
		} else {
			this.a = score.intValue();
		}
		// FIXME
		/*
		Double coScore = this.index.getTermContexts().get(this.coTerm).getCoOccurrences().get(this.term);
		if (coScore == null) {
			throw new NullPointerException(this.index.getLanguage() + ": co-term " + this.coTerm + "[" + this.term + "]");
		}
		if (score.equals(coScore)) {
			this.a = score.intValue();
		} else {
			throw new Exception("Wrong value: " + this.term + "[" + this.coTerm + "] == " + score + " != " + coScore + " == " + this.coTerm + "[" + this.term + "]");
		}
		*/
	}
	
	public int getA() {
		return this.a;
	}
	
	private int b;
	
	private void setB() {
		Integer occ = this.scores.get(this.term);
		if (occ == null) {
			throw new NullPointerException("index of term: " + this.term);
		} else {
			this.b = occ.intValue() - this.a;
		}
		/*
		TermContext context = this.index.getTermContexts().get(this.term);
		if (context == null) {
			throw new NullPointerException("No co-term context for computing the cross-table of " + this.term + " " + this.coTerm);
		} else {
			for (String key : context.getCoOccurrences().keySet()) ;{
				Double coOcc = context.getCoOccurrences().get(key);
				this.b += coOcc.doubleValue();
			}
			this.b -= this.a;
		}
		*/
	}
	
	public int getB() {
		return this.b;
	}
	
	private int c;
	
	private void setC() {
		Integer occ = this.coScores.get(this.coTerm);
		if (occ == null) {
			// throw new NullPointerException("index of co-term: " + this.coTerm);
			this.c = 0;
		} else {
			this.c = occ.intValue() - this.a;
		}
		/*
		this.c = 0;
		/* TODO
		for (String term : this.index.getTermContexts().keySet()) {
			TermContext context = this.index.getTermContexts().get(term);
			Double coOcc = context.getCoOccurrences().get(this.coTerm);
			if (coOcc != null) {
				this.c += coOcc.intValue();
			}
		}
		/*
		TermContext context = this.index.getTermContexts().get(this.coTerm);
		if (context == null) {
			throw new NullPointerException("No co-term context for computing the cross-table of " + this.term + " " + this.coTerm);
		} else {
			for (String key : context.getCoOccurrences().keySet()) {
				Double coOcc = context.getCoOccurrences().get(key);
				this.c += coOcc.doubleValue();
			}
			this.c -= this.a;
		}
		*/
	}
	
	public int getC() {
		return this.c;
	}
		
	private int d;

	private void setD() {
		this.d = this.total;
		this.d -= this.a;
		this.d -= this.b;
		this.d -= this.c;
	}
	
	public int getD() {
		return this.d;
	}
	
	public void compute() {
		this.setA();
		this.setB();
		this.setC();
		this.setD();
	}
	
}

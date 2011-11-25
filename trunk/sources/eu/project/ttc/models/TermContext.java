package eu.project.ttc.models;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class TermContext /* implements Comparable<TermContext> */ {

	private TermContextComparator comparator;
	
	private void setComparator() {
		this.comparator = new TermContextComparator();
	}
	
	private TermContextComparator getComparator() {
		return this.comparator;
	}
	
	private Map<String,Double> coOccurrences;

	private void setCoOccurrences() {
		this.coOccurrences = new HashMap<String,Double>();
	}

	public Map<String,Double> getCoOccurrences() {
		return coOccurrences;
	}
	
	public TermContext() {
		this.setComparator();
		this.setCoOccurrences();
	}
		
	public static int MAX_MODE = 0;
	
	public static int MIN_MODE = 1;
	
	public static int ADD_MODE = 2;
	
	public static int DEL_MODE = 3;
	
	public void setCoOccurrences(String term,Double coOccurrences,int mode) {
		Double coOcc = this.getCoOccurrences().get(term);
		if (coOcc == null) {
			this.getCoOccurrences().put(term, coOccurrences);
		} else {
			if (mode == DEL_MODE) {
				this.getCoOccurrences().put(term, coOccurrences);
			} else if (mode == ADD_MODE) {
				this.getCoOccurrences().put(term, new Double(coOccurrences.doubleValue() + coOcc.doubleValue()));
			} else if (mode == MAX_MODE && coOccurrences.doubleValue() > coOcc.doubleValue()) {
				this.getCoOccurrences().put(term, coOccurrences);	
			} else if (mode == MIN_MODE && coOccurrences.doubleValue() < coOcc.doubleValue()) {
				this.getCoOccurrences().put(term, coOccurrences);
			} 
		}
	}

	public Map<String, Double> sort() {
		Map<String, Double> occurrences = new TreeMap<String, Double>(this.getComparator());
		occurrences.putAll(this.getCoOccurrences());
		return occurrences;
	}
		
	/*
	public String toString(String term,int depth) {
		StringBuilder builder = new StringBuilder();
		int index = 0;
		Map<String, Double> occurrences = this.getSortedCoOccurrences();
		for (String key: occurrences.keySet()) {
			index++;
			if (index <= depth) {
				builder.append(term);
				builder.append("::");
				builder.append("null");
				builder.append("::en-fr::");
				builder.append(key);
				builder.append("::");
				builder.append("null");
				builder.append('\n');
			} else {
				break;
			}
		}
		return builder.toString();
	}
	*/
	
	public String toString() {
		StringBuilder builder = new StringBuilder();
		int index = 0;
		Map<String, Double> occurrences = this.sort();
		int size = occurrences.size();
		for (String key: occurrences.keySet()) {
			index++;
			builder.append(key);
			builder.append('#');
			double value = occurrences.get(key).doubleValue();
			builder.append(value);
			if (index < size) {
				builder.append(':');				
			}
		}
		return builder.toString();
	}
	
	/*
	@Override
	public int compareTo(TermContext termContext) {
		for (String key : this.getCoOccurrences().keySet()) {
			Double score = this.getCoOccurrences().get(key);
			Double termScore = termContext.getCoOccurrences().get(key);
			if (termScore == null) {
				return 1;
			} else {
				long value = Math.round(score.doubleValue() * 10000000000.0);
				long termValue = Math.round(termScore.doubleValue() * 10000000000.0);
				int diff = new Long(value).compareTo(new Long(termValue));
				if (diff != 0) {
					return diff;
				}
			}
		}
		return 0;
	}
	*/
	
	private class TermContextComparator implements Comparator<String> {

		@Override
		public int compare(String sourceKey,String targetKey) {
			Double sourceValue = getCoOccurrences().get(sourceKey);
			Double targetValue = getCoOccurrences().get(targetKey);
			double source = sourceValue == null ? 0.0 : sourceValue.doubleValue();
			double target = targetValue == null ? 0.0 : targetValue.doubleValue();
			if (source < target) {
				return 1;
			} else if (source > target) {
				return -1;
			} else {
				return sourceKey.compareTo(targetKey);
			}
		}
		
	}
		
}

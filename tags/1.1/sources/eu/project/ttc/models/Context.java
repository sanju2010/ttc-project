package eu.project.ttc.models;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

public class Context {

	private ContextComparator comparator;
	
	private void setComparator() {
		this.comparator = new ContextComparator();
	}
	
	private ContextComparator getComparator() {
		return this.comparator;
	}
	
	private Map<String,Double> coOccurrences;

	private void setCoOccurrences() {
		this.coOccurrences = new ConcurrentHashMap<String,Double>();
	}

	public Map<String,Double> getCoOccurrences() {
		return coOccurrences;
	}
	
	public Context() {
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
	
	public void fromString(String text) {
		String[] pairs = text.split("\n");
		for (String pair : pairs) {
			String[] keyValue = pair.split("\t");
			String key = keyValue[0];
			String value = keyValue[1];
			this.setCoOccurrences(key, Double.valueOf(value), Context.ADD_MODE);
		}
	}
	
	public String toString() {
		StringBuilder builder = new StringBuilder();
		int index = 0;
		Map<String, Double> occurrences = this.sort();
		int size = occurrences.size();
		for (String key: occurrences.keySet()) {
			index++;
			builder.append(key);
			builder.append('\t');
			double value = occurrences.get(key).doubleValue();
			builder.append(value);
			if (index < size) {
				builder.append('\n');				
			}
		}
		return builder.toString();
	}
	
	private class ContextComparator implements Comparator<String> {

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

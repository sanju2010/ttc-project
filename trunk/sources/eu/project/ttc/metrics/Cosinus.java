package eu.project.ttc.metrics;

import java.util.Map;

public class Cosinus implements SimilarityDistance {
	
	@Override
	public double getValue(Map<String, Double> source, Map<String, Double> target) {
		double fstSum = 0.0;
		double sndSum = 0.0;
		double sum = 0.0;
		for (String key : source.keySet()) {
			double sourceValue = source.get(key) == null ? 0.0 : source.get(key).doubleValue();
			fstSum += sourceValue * sourceValue;
		}
		for (String key : target.keySet()) {
			double targetValue = target.get(key) == null ? 0.0 : target.get(key).doubleValue();
			fstSum += targetValue * targetValue;
		}
		for (String key : source.keySet()) {
			double sourceValue = source.get(key) == null ? 0.0 : source.get(key).doubleValue();
			double targetValue = target.get(key) == null ? 0.0 : target.get(key).doubleValue();
			sum += sourceValue * targetValue;
		}
		// FIXME
		// double prod = Math.sqrt(fstSum) * Math.sqrt(sndSum);
		double prod = Math.sqrt(fstSum * sndSum);
		if (prod == 0.0) {
			return 0.0;
		} else {
			return sum / prod;
		}
	}
	
	/*
	
	@Override
	public double getValue(Map<String,Double> first,Map<String, Double> second) {
		double fst = getProduct(first,first);
		if (Double.compare(fst,0.0) == 0) {
			return 0.0;
		} else {
			double snd = getProduct(second,second);
			if (Double.compare(snd,0.0) == 0) {
				return 0.0;
			} else {
				// FIXME
				// return getProduct(first,second) / Math.sqrt(fst * snd);
				return getProduct(first,second) / (Math.sqrt(fst) * Math.sqrt(snd));
			}
		}
	}
	
	private double getProduct(Map<String,Double> first,Map<String, Double> second) {
		double product = 0;
		for(final String item  : first.keySet()) {
			if (second.containsKey(item)) {
				product += first.get(item) *  second.get(item);
			}
		}
		return product;
	}

	*/
}

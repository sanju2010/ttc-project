package eu.project.ttc.metrics;

import java.util.Map;

public class Cosine implements SimilarityDistance {
	
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
			sndSum += targetValue * targetValue;
		}
		for (String key : source.keySet()) {
			double sourceValue = source.get(key) == null ? 0.0 : source.get(key).doubleValue();
			double targetValue = target.get(key) == null ? 0.0 : target.get(key).doubleValue();
			sum += sourceValue * targetValue;
		}
		for (String key : target.keySet()) {
			double sourceValue = source.get(key) == null ? 0.0 : source.get(key).doubleValue();
			double targetValue = target.get(key) == null ? 0.0 : target.get(key).doubleValue();
			sum += sourceValue * targetValue;
		}
		// double prod = Math.sqrt(fstSum) * Math.sqrt(sndSum);
		double prod = Math.sqrt(fstSum * sndSum);
		if (prod == 0.0) {
			return 0.0;
		} else {
			return sum / prod;
		}
	}
	
}

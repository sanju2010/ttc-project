package fr.univnantes.lina.metrics;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Jaccard implements SimilarityDistance {
	
	@Override
	public double getValue(Map<String, Double> source, Map<String, Double> target) {
		double infSum = 0;
		double supSum = 0;
		Set<String> keys = new HashSet<String>();
		keys.addAll(source.keySet());
		keys.addAll(target.keySet());
		for (String key : keys) {
			double sourceValue = source.get(key) == null ? 0 : source.get(key).doubleValue();
			double targetValue = target.get(key) == null ? 0 : target.get(key).doubleValue();
			if (sourceValue < targetValue) {
				infSum += sourceValue;
				supSum += targetValue;
			} else {
				infSum += targetValue;
				supSum += sourceValue;
			}
		}
		if (supSum == 0) {
			return 0;
		} else {
			return infSum / supSum;
		}
	}

}

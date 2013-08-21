package eu.project.ttc.metrics;

import java.util.Map;

public interface SimilarityDistance {

	public double getValue(Map<String,Double> first,Map<String,Double> second);
	
}

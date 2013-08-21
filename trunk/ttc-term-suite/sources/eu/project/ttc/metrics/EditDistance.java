package eu.project.ttc.metrics;

public interface EditDistance {

	public double normalize(int distance, String source, String target);
	
	public int compute(String source, String target);
	
	public boolean isFailFast();
	
	public void setFailThreshold(double threshold);
}

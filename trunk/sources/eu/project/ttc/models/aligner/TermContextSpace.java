package eu.project.ttc.models.aligner;

import java.io.OutputStream;
import java.util.Map;
import java.util.Set;

import org.apache.uima.resource.SharedResourceObject;

public interface TermContextSpace extends SharedResourceObject {
	
	public void addIndex(String anIndexID) throws Exception;
	
	public void delIndex(String anIndexID) throws Exception;
	
	public TermContextIndex getIndex(String anIndexID) throws Exception;

	public void doFilter(String indexID,Set<String> terms) throws Exception;

	public void doShrink(String indexID,Set<String> terms) throws Exception;
	
	public void setAssociationRate(String associationRateClassName) throws Exception;

	public void doNormalize(String sourceID,String targetID) throws Exception;
	
	public void setSimilarityDistance(String similarityDistanceClassName) throws Exception;
	
	public void doAlign(String sourceID,String targetID,String resultID) throws Exception;

	public void doCheck(String indexID, Map<String, Set<String>> translations, OutputStream outputStream) throws Exception;

	public void doTransfer(String sourceID, String targetID, String resultID, Map<String, Set<String>> translations) throws Exception;

}